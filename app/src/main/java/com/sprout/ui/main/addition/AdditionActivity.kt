package com.sprout.ui.main.addition

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.iknow.android.features.select.VideoSelectActivity
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.tools.ScreenUtils
import com.luck.picture.lib.tools.ToastUtils
import com.sprout.R
import com.sprout.api.URLConstant
import com.sprout.api.ext.navigationTo
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivityAdditionBinding
import com.sprout.ui.main.addition.adapter.BrandAdapter
import com.sprout.ui.main.addition.adapter.GoodAdapter
import com.sprout.ui.main.addition.adapter.RecentTagsAdapter
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.ui.main.addition.bean.RecentTags
import com.sprout.ui.main.login.RegisterActivity
import com.sprout.utils.MyMmkv
import com.sprout.utils.PicSelectUtils
import com.sprout.widget.clicks
import com.sprout.widget.glide.BlurTransformation
import org.jetbrains.anko.alert
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * 动态页面图片的编辑
 */
class AdditionActivity :
    BaseActivity<AdditionViewModel, ActivityAdditionBinding>() ,
    OnItemChildClickListener {

    var token = MyMmkv.getString(URLConstant.token)

    //用于 选择 图片资源、视频资源 完成选择
    var photoOrVideo : Boolean = false

    //用于 跳转到视频编辑页面 请求码
    var CODE_VIDEO : Int = 100
    var PAGE_TYPE :Int = URLConstant.TYPE_IMG //获取 类型 1图片 2视频
    var RETURN_HOME : Int = 0  //发布成功 关闭本页面 回到主页面

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


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus){
            if (token.isNullOrEmpty()){
                /**
                 * 打开一个君如登录页的弹框
                 */
                alert("请先登录"){
                    positiveButton("稍后登录"){
                        finish()
                    }
                    negativeButton("立即登录"){
                        MyMmkv.setValue("long2", true)
                        navigationTo<RegisterActivity>()
                    }
                }.show()
            }
            // 待获取 windel 窗口后 打开相册选取图片、视频
            if(imgArray.size>0)return
            openChangeAlert()
        }
    }

    override fun initView() {
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
//        recentTags.reverse() //反转list
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

    /**
     * 打开一个选中图片或者视频的弹框
     */
    private fun openChangeAlert(){

        Glide.with(this).load(R.mipmap.ic_addition_back)
            .apply(RequestOptions.bitmapTransform(
                BlurTransformation(this, 25, 3)))
            .into(v.ivAddBack)

        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.layout_change_pop, null)

        //声明 频道 pop 统一设置pop 宽高
        val pWAddition = PopupWindow(
            (ScreenUtils.getScreenWidth(this) * 0.7).toInt(),
            (ScreenUtils.getScreenHeight(this) * 0.2).toInt()
        )

        //获取控件id
        val tvPhoto = view.findViewById<TextView>(R.id.tv_photo)
        val tvVideo = view.findViewById<TextView>(R.id.tv_video)

        tvPhoto.clicks {
            //图库 页面逻辑 处理
            PAGE_TYPE = URLConstant.TYPE_IMG
            openPhoto()
            pWAddition.dismiss()
        }
        tvVideo.clicks {
            //视频
            PAGE_TYPE = URLConstant.TYPE_VIDEO
            openVideo()
            pWAddition.dismiss()
        }

        //找到视图
        pWAddition.contentView = view
        pWAddition.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWAddition.showAtLocation(v.groupView, Gravity.CENTER, 0, 0) //开启弹窗
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
                    //下一步 图片
                    val intent = Intent(mContext,SubmitMoreActivity::class.java)
                    intent.putExtra("img_data",decodeImgs())
                    startActivityForResult(intent,RETURN_HOME)
                }
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.init_brand ->{
                //初始化 最近标签 数据
                val item = brandList[position]
                recentTags.add(0,RecentTags(0,item.name,item.sort,item.url+""))
                setTagAdapter()

                val id = brandList[position].id
                val name = brandList[position].name
                fragments[mViewPager.currentItem].addTagsToView(1,id,name)
            }
            R.id.init_good ->{
                //初始化最近标签 数据
                val item = goodList[position]
                recentTags.add(0,RecentTags(1,item.name,item.sort,item.url+""))
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
            PictureConfig.CHOOSE_REQUEST->{//图库返回
                val selectList = PictureSelector.obtainMultipleResult(data)
                if (selectList.size === 0) {
                    photoOrVideo = false  //选择 打开相册 为true 打开视频编辑默认为false
                    v.ivAddBack.visibility = View.VISIBLE  //背景图片显示
                    v.lineAddition.visibility = View.GONE //主布局隐藏
                    return
                }else{
                    photoOrVideo = true  //选择 打开相册 为true 打开视频编辑默认为false
                    v.ivAddBack.visibility = View.GONE  //背景图片隐藏
                    v.lineAddition.visibility = View.VISIBLE //主布局显示
                }
                when(PAGE_TYPE){
                    URLConstant.TYPE_IMG -> { //图片1 视频2
                        for(i in selectList.indices){
                            imgList.add(selectList[i].path)//保留图片的绝对路径
                            val imgData = ImgData(selectList[i].path, mutableListOf())  //图片数据的初始化
                            imgArray.add(imgData)
                            val fragment = ImageFragment.instance(i,selectList[i].path, imgData.tags)
                            fragments.add(fragment)
                        }
                        v.txtLabelBannerIndex.text = "${1}/${imgList.size}"
                        mViewPager.adapter?.notifyDataSetChanged()
                    }
                    URLConstant.TYPE_VIDEO ->{

                    }
                }

            }
            CODE_VIDEO->{ //视频编辑返回
                //视频编辑完成 返回 视频路径 携带数据 跳转到发布页
                if (data!=null && data.hasExtra("newVideoPath")){
                    val intent = Intent(this, SubmitMoreActivity::class.java)
                    intent.putExtra("video_data", data.getStringExtra("newVideoPath"))
                    startActivityForResult(intent,RETURN_HOME)
                }else{
                    ToastUtils.s(this,"没有接收到视频压缩处理的数据")
                }
            }
            RETURN_HOME->{
                if (resultCode==0){
                    finish() //关闭本页面
                }
            }
        }
    }

    //初始化视频
    private fun openVideo(){
        startActivityForResult(
            Intent(this,VideoSelectActivity::class.java),CODE_VIDEO)
    }

    //初始化图片
    private fun openPhoto() {
        PicSelectUtils.openPhoto(9,this)
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
        val imgs = JSONArray()
        for(i in 0 until imgArray.size){
            val item = imgArray[i]
            val imgJson = JSONObject()  //图片的json结构
            imgJson.put("path",item.path)
            val tags = JSONArray()
            for(j in 0 until item.tags.size){
                val tag = item.tags[j]
                val tagJson = JSONObject()
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