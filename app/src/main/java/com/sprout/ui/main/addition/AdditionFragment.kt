package com.sprout.ui.main.addition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sprout.R
import com.sprout.utils.GlideEngine
import com.sprout.widget.clicks
import kotlinx.android.synthetic.main.fragment_addition.*
import kotlinx.android.synthetic.main.fragment_addition.view.*

/**
 * 动态页面图片的编辑
 */
class AdditionFragment : Fragment(){

    private val CODE_TAG = 99
    private var imgList:MutableList<String> = mutableListOf()
    var fragments:MutableList<ImageFragment> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addition, container, false)
        initView(view)
        return view
    }

    /**
     * 初始化界面
     */
    private fun initView(v: View) {
        imgList = mutableListOf()
        txt_tag.clicks {
            val intent = Intent(activity,TagsActivity::class.java)
            startActivityForResult(intent,CODE_TAG)
        }
        viewPager.adapter = object : FragmentPagerAdapter(childFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        //打开相册选取图片
        openPhoto()
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
                    val fragment = ImageFragment.instance(i, selectList[i].path)
                    fragments.add(fragment)
                }
                viewPager.adapter?.notifyDataSetChanged()
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