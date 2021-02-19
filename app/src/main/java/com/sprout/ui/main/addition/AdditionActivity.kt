package com.sprout.ui.main.addition

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sprout.R
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityAdditionBinding
import com.sprout.ui.main.addition.adapter.BrandAdapter
import com.sprout.ui.main.addition.adapter.GoodAdapter
import com.sprout.ui.main.addition.adapter.RecentTagsAdapter
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.ui.main.addition.bean.RecentTags
import com.sprout.utils.CacheUtil
import com.sprout.utils.GlideEngine
import org.json.JSONArray
import org.json.JSONObject

/**
 * 动态页面图片的编辑
 */
class AdditionActivity :
    BaseActivity<AdditionViewModel, ActivityAdditionBinding>() ,
    OnItemChildClickListener {

    //记录上一个初始化的tag
    lateinit var lastTagText: TextView
    //搜索TYPE 类型
    var searchType = 0
    //搜索 需要存储的原始数据集合 用于搜索
    val brandList2:MutableList<BrandData.Data> = arrayListOf()
    val goodsList2:MutableList<GoodData.Data> = arrayListOf()

    //初始化控件
    private lateinit var mViewPager: ViewPager
    //bannerAdapter 数据源
    private var imgList : MutableList<String> = mutableListOf()
    var fragments : MutableList<ImageFragment> = mutableListOf()

    //当前界面tag相关数据
    var imgArray :MutableList<ImgData> = mutableListOf()

    //type brand标签 适配器
    var brandList: MutableList<BrandData.Data> = mutableListOf()
    val brandAdapter  : BrandAdapter by lazy { BrandAdapter() }

    //type 商品标签 适配器
    var goodList: MutableList<GoodData.Data> = mutableListOf()
    val goodAdapter  : GoodAdapter by lazy { GoodAdapter() }

    //最近标签adapter
    val recentTagsAdapter: RecentTagsAdapter by lazy { RecentTagsAdapter() }
    //最近标签 数据集合
    var recentTags : MutableList<RecentTags> = mutableListOf()

    override fun initView() {
        openPhoto()

        //请求数据 brand 用于显示
        vm.getLabelBrandList(true)

        mViewPager = v.bannerLabel

        //设置type 数据列表 管理器
        v.recyclerLabelChannel.layoutManager = LinearLayoutManager(mContext)

        //初始化bannerAdapter
        mViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager){

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }

        //动态更换顶部页码
        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                v.txtLabelBannerIndex.text = "${position + 1}/${imgList.size}"
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        //记录第一个变黑字体变大的tab
        lastTagText = v.txtTagBrand
        //绑定点击事件
        v.onClick = Proxy()
        //标签集合设置为横向
        v.recyclerRecentTag.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        //最近标签 渲染数据
        recentTagsAdapter.setNewInstance(recentTags)
        //最近标签 适配器绑定
        v.recyclerRecentTag.adapter = recentTagsAdapter

        //加载最近标签页的adapter
        setTagAdapter()

        //搜索
        v.editLabelInputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

                when (searchType) {
                    0 -> {

                        if (s!!.isEmpty()) {
                            brandAdapter.setList(brandList2)
                            brandAdapter.notifyDataSetChanged()
                        }

                        brandAdapter.setList(brandList2.dropLastWhile {
                            !it.name.contains(s.toString())
                        })
                        brandAdapter.notifyDataSetChanged()

                    }
                    1 -> {

                        if (s!!.isEmpty()) {
                            goodAdapter.setList(goodsList2)
                            goodAdapter.notifyDataSetChanged()
                        }

                        goodAdapter.setList(goodsList2.dropLastWhile {
                            !it.name.contains(s.toString())
                        })
                        goodAdapter.notifyDataSetChanged()

                    }
                }
            }
        })

    }

    override fun initClick() {
        brandAdapter.addChildClickViewIds(R.id.init_brand)
        goodAdapter.addChildClickViewIds(R.id.init_good)
        brandAdapter.setOnItemChildClickListener(this)
        goodAdapter.setOnItemChildClickListener(this)
    }

    override fun initData() {

    }

    override fun initVM() {
        vm.labelbrandList.observe(this, Observer {
            brandList.clear()
            brandList.addAll(it.data)
            brandAdapter.setNewInstance(brandList.toMutableList())
            v.recyclerLabelChannel.adapter = brandAdapter
        })
        vm.labelgoodList.observe(this, Observer {
            goodList.clear()
            goodList.addAll(it.data)
            goodAdapter.setNewInstance(goodList.toMutableList())
            v.recyclerLabelChannel.adapter = goodAdapter
        })
    }

    inner class Proxy {
        fun txtTag(view: View) {

            when (view.id) {
                R.id.txt_tag_brand -> {
                    setTagTheme(v.txtTagBrand)
                    vm.getLabelBrandList(true)
                    searchType = 0
                }
                R.id.txt_tag_goods -> {
                    setTagTheme(v.txtTagGoods)
                    vm.getLabelGoodsList(true)
                    searchType = 1
                }
                R.id.txt_tag_location -> {
                    //地点
                    setTagTheme(v.txtTagLocation)
                }
                R.id.txt_tag_user -> {
                    //用户
                    setTagTheme(v.txtTagUser)
                }
                R.id.txt_label_next -> {
                    //下一步
                    val intent = Intent(mContext,SubmitMoreActivity::class.java)
                    intent.putExtra("data",decodeImgs())
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.init_brand ->{
                //初始化 最近标签 数据
                val item = brandList[position]
                recentTags.add(RecentTags(0,item.name,item.sort,item.url+""))
                setTagAdapter()

                val id = brandList[position].id
                val name = brandList[position].name
                fragments[mViewPager.currentItem].addTagsToView(1,id,name)
            }
            R.id.init_good ->{
                //初始化最近标签 数据
                val item = goodList[position]
                recentTags.add(RecentTags(1,item.name,item.sort,item.url+""))
                setTagAdapter()

                val id = goodList[position].id
                val name = goodList[position].name
                fragments[mViewPager.currentItem].addTagsToView(2,id,name)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            PictureConfig.CHOOSE_REQUEST->{
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.size === 0) return
                //获取本地图片的选择地址，上传到服务器
                //头像的压缩和二次采样
                //把选中的图片插入到列表
                for(i in selectList.indices){
                    imgList.add(selectList[i].path)
                    val imgData = ImgData(selectList[i].path, mutableListOf())
                    imgArray.add(imgData)
                    val fragment = ImageFragment.instance(i,selectList[i].path, mutableListOf())
                    fragments.add(fragment)
                }
                v.txtLabelBannerIndex.text = "${1}/${imgList.size}"
                mViewPager.adapter?.notifyDataSetChanged()
            }
        }
    }


    //初始化图片
    private fun openPhoto() {
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
            .maxSelectNum(9)
            .imageSpanCount(3)
            .selectionMode(PictureConfig.MULTIPLE)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    //根据点击事件动态更换4个Tag的状态
    private fun setTagTheme(txtTag: TextView) {
        if (lastTagText != txtTag) {
            txtTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            txtTag.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            txtTag.setTextColor(resources.getColor(R.color.black))
            lastTagText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
            lastTagText.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            lastTagText.setTextColor(resources.getColor(R.color.colorA))
            lastTagText = txtTag
        }
    }


    //存储最近标签 展示
    private fun setTagAdapter() {
        recentTagsAdapter.notifyDataSetChanged()
    }

    /**
     * json结构原生的封装
     */
    private fun decodeImgs(): String? {
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
}