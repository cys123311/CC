package com.sprout.ui.main.addition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sprout.R
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityAdditionBinding
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.utils.GlideEngine
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.activity_addition.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * 动态页面图片的编辑
 */
class AdditionActivity :
    BaseActivity<AdditionViewModel,ActivityAdditionBinding>(){

    private val CODE_TAG = 99
    private var imgList:MutableList<String> = mutableListOf()
    var fragments:MutableList<ImageFragment> = mutableListOf()

    //当前界面tag相关数据
    var imgArray:MutableList<ImgData> = mutableListOf()

    /**
     * 初始化界面
     */
    override fun initView() {
        imgList = mutableListOf()
        v.txtTag.clicks {
            val intent = Intent(mContext,TagsActivity::class.java)
            startActivityForResult(intent,CODE_TAG)
        }
        v.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        // 跳转到 动态数据的提交 发布数据
        v.txtNext.clicks {
            val intent = Intent(mContext,SubmitMoreActivity::class.java)
            intent.putExtra("data",decodeImgs())
            startActivity(intent)
        }

        //打开相册选取图片
        openPhoto()
    }

    override fun initClick() {

    }

    override fun initData() {

    }

    override fun initVM() {

    }

    private fun openPhoto(){
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
            .maxSelectNum(9)
            .imageSpanCount(3)
            .selectionMode(PictureConfig.MULTIPLE)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    /**
     * json结构原生的封装
     */
    private fun decodeImgs():String{
        var imgs = JSONArray()
        for(i in 0 until imgArray.size){
            var item = imgArray[i]
            var imgJson = JSONObject()  //图片的json结构
            imgJson.put("path",item.path)
            var tags = JSONArray()
            for(j in 0 until item.tags.size){
                var tag = item.tags[j]
                var tagJson = JSONObject()
                tagJson.put("x",tag.x)
                tagJson.put("y",tag.y)
                tagJson.put("type",tag.type)
                tagJson.put("name",tag.name)
                tagJson.put("lng",tag.lng)
                tagJson.put("lat",tag.lat)
                tags.put(tagJson)
            }
            imgJson.put("tags",tags)
            imgs.put(imgJson)
        }
        return imgs.toString()
    }

    /**
     * 打开activity后回传
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PictureConfig.CHOOSE_REQUEST -> {
                // onResult Callback
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.size == 0) return
                //获取本地图片的选择地址，上传到服务器
                //头像的压缩和二次采样
                //把选中的图片插入到列表
                for(i in 0 until selectList.size){
                    imgList.add(selectList[i].path) //保留图片的绝对路径
                    //图片数据的初始化
                    val imgData = ImgData(selectList[i].path, mutableListOf())
                    imgArray.add(imgData)
                    val fragment = ImageFragment.instance(i, selectList[i].path, mutableListOf())
                    fragments.add(fragment)
                }
                v.viewPager.adapter?.notifyDataSetChanged()
            }
            //处理TAG设置返回
            CODE_TAG -> {
                if(resultCode == 1){
                    val id = data!!.getIntExtra("id",0)
                    val name = data.getStringExtra("name")
                    val pos = viewPager.currentItem
                    fragments[pos].addTagsToView(1,id,name!!)
                }else if(resultCode == 2){
                    val id = data!!.getIntExtra("id",0)
                    val name = data.getStringExtra("name")
                    fragments[viewPager.currentItem].addTagsToView(2,id,name!!)
                }
            }
            else -> {
            }
        }
    }
}