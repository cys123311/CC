package com.sprout.ui.main.addition

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.ToastUtils
import com.sprout.R
import com.sprout.api.IItemClick
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivitySubmitMoreBinding
import com.sprout.ui.main.addition.adapter.ChannelAdapter
import com.sprout.ui.main.addition.adapter.LocationAdapter
import com.sprout.ui.main.addition.adapter.SubmitImgAdapter
import com.sprout.ui.main.addition.adapter.ThemeAdapter
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.ui.main.addition.bean.LZThemeBean
import com.sprout.ui.main.addition.bean.LocationInfo
import com.sprout.ui.main.addition.bean.LZChannelBean
import com.sprout.utils.GlideEngine
import com.sprout.utils.ToastUtil
import com.sprout.widget.clicks
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

/**
 * 动态数据的提交
 */
class SubmitMoreActivity :
    BaseActivity<SubmitViewModel, ActivitySubmitMoreBinding>(),
    OnItemChildClickListener, AMapLocationListener,
    PoiSearch.OnPoiSearchListener {

    //发布 类型 1图片 2视频
    val type = 1

    //主题Id
    var themeId: Int = 0

    //频道Id
    var channelId: Int = 0

    //经纬度
    var lat: Double = 0.0  //经度
    var lng: Double = 0.0  //纬度

    var imgs: MutableList<ImgData> = mutableListOf()
    var max_img = 9

    //图片 适配器 060411
    lateinit var imgAdapter: SubmitImgAdapter


    //声明 频道 pop 统一设置pop 宽高
    var pWChannel = getPwXY()

    //声明 pop 频道适配器
    val channelAdapter: ChannelAdapter by lazy { ChannelAdapter() }

    //声明 pop 频道 数据源集合
    val channelList: MutableList<LZChannelBean> = mutableListOf()


    //声明 主题 pop  统一设置pop 宽高
    var pWTheme = getPwXY()

    //声明 pop 主题适配器
    val themeAdapter: ThemeAdapter by lazy { ThemeAdapter() }

    //声明 pop 主题 数据源集合
    val themeList: MutableList<LZThemeBean> = mutableListOf()


    //定位
    lateinit var mLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption

    //声明 定位 pop  统一设置pop 宽高
    var pWLocation = getPwXY()

    //声明 定位适配器
    val locationAdapter: LocationAdapter by lazy { LocationAdapter() }

    //声明定位集合
    val locationList: MutableList<LocationInfo> = mutableListOf()


    override fun initView() {
        imgAdapter = SubmitImgAdapter(this, imgs)
        v.recyclerReleaseImg.layoutManager = GridLayoutManager(this, 3)
        v.recyclerReleaseImg.adapter = imgAdapter
        imgAdapter.clickEvt = SubmitClickEvt()
        /**
         * item条目的点击
         */
        imgAdapter.itemClick {
            //当前点击的是加号
            if (it == imgs.size + 1) {
                openPhoto()
            }
            ToastUtil.showToast(mContext, "111")
            Log.e("imgAdapter", "1")
        }
    }

    override fun initData() {
        if (intent.hasExtra("data")) {
            var json = intent.getStringExtra("data")
            if (json!!.isNotEmpty()) {
                val jsonArr = JSONArray(json)
                for (i in 0 until jsonArr.length()) {
                    val imgData =
                        Gson().fromJson<ImgData>(jsonArr.getString(i), ImgData::class.java)
                    imgs.add(imgData)
                }
                //处理加号
                if (imgs.size < max_img) {
                    val imgData = ImgData(null, mutableListOf())
                    imgs.add(imgData)
                }
            }
        }
    }

    override fun initClick() {
        v.imageReleaseClose.clicks {
            //关闭当前页面 返回上一层页面
            finish()
        }
        v.txtSaveRelease.clicks {
            //保存数据为草稿
        }
        v.txtReleaseBtn.clicks {
            //发布数据
            val content = getSubmitJson()
            vm.submitTrends(content, true)
        }
        v.btnReleaseChannel.clicks {
            //选择频道
            vm.getChannel(true)
        }
        v.btnReleaseAppendTheme.clicks {
            //获取主题 请求数据
            vm.getTheme(true)
        }
        v.btnReleaseAddress.clicks {
            //获取 位置 定位
            location()
        }

        //适配器 条目监听声明  频道、主题、地址
        channelAdapter.addChildClickViewIds(R.id.line_channel)
        channelAdapter.setOnItemChildClickListener(this)
        themeAdapter.addChildClickViewIds(R.id.line_theme)
        themeAdapter.setOnItemChildClickListener(this)
        locationAdapter.addChildClickViewIds(R.id.line_location)
        locationAdapter.setOnItemChildClickListener(this)
    }

    override fun initVM() {
        //发布 是否成功
        vm.submit.observe(this, Observer {
            Log.e("222",it.toString())
            when (it.errno) {
                200 -> {
                    //发布成功 关闭本页面
                    finish()
                }
                -1 -> {
                    //发布失败吐司提示原因
                    ToastUtil.showToast(mContext,it.errmsg)
                }
            }
        })

        //频道 数据展示
        vm.channel.observe(this, Observer {
            //设置pop
            obtainChannel(it)
        })

        //主题 数据展示
        vm.theme.observe(this, Observer {
            //设置pop
            obtainTheme(it)
        })
    }


    /**
     * 组装提交的内容
     */
    fun getSubmitJson(): String {
        val title = v.editReleaseTitle.toString() //标题
        val mood = v.editReleaseContent.toString()  //内容 心情
        val theme = v.textView5.toString()   //主题 内容
        val address = v.textView6.toString()       //地址
        val json: JSONObject = JSONObject()
        json.put("type", type)              //类型 图片为1 视频为2
        json.put("title", title)              //标题
        json.put("mood", mood)                //内容 情绪
        json.put("address", address)          //地址
        json.put("themeid", themeId)          //主题id
        json.put("channelid", channelId)      //频道id
        json.put("lng", lng)                  //经度
        json.put("lat", lat)                  //维度
        val res = JSONArray()
        when (type) {
            1 -> { //图片
                for (i in 0 until imgs.size) {
                    val img = JSONObject()
                    img.put("url", imgs[i].path)
                    val tags = JSONArray()
                    img.put("tags", tags)
                    for (j in 0 until imgs[i].tags.size) {
                        val tagItem = imgs[i].tags[j]
                        val tag = JSONObject()
                        tag.put("type", tagItem.type)
                        tag.put("id", tagItem.id)
                        tag.put("name", tagItem.name)
                        tag.put("x", tagItem.x)
                        tag.put("y", tagItem.y)
                        tag.put("lng", tagItem.lng)
                        tag.put("lat", tagItem.lat)
                        tags.put(tag)
                    }
                    json.put("res", res)
                }
            }
            2 -> {

            }
        }

        return json.toString()
    }

    //TODO item条目的点击
    inner class ItemClick : IItemClick<ImgData> {
        override fun itemClick(data: ImgData) {
            //当前点击的是加号
            if (data.path.isNullOrEmpty()) {
                openPhoto()
            }
        }
    }

    //点击 删除图标 删除本条数据 刷新适配器 删除点击
    inner class SubmitClickEvt {
        fun clickDelete(data: ImgData) {
            imgs.remove(data)
            imgAdapter.notifyDataSetChanged()
        }
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
                for (i in 0 until selectList.size) {
                    var imgData = ImgData(selectList.get(i).path, mutableListOf())
                    var index = imgs.size - 1
                    imgs.add(index, imgData)
                }
                if (imgs.size > max_img) {
//                    imgs.removeLast()
                }
                imgAdapter.notifyDataSetChanged()
            }

            else -> {
            }
        }
    }

    /*当前还能插入的图片数量*/
    private fun openPhoto() {
        //当前还能插入的图片数量
        val num = max_img - imgs.size + 1
        PictureSelector.create(this)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
            .maxSelectNum(num)
            .imageSpanCount(3)
            .selectionMode(PictureConfig.MULTIPLE)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }


    /**
     * 适配器条目监听
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.line_channel -> {
                //频道Id 赋值
                channelId = channelList[position].id

                //频道列表 条目监听
                v.textView4.text = channelList[position].name
                //关闭主题pop 恢复窗口透明度
                pWChannel.dismiss()
                initPwNo()
            }
            R.id.line_theme -> {
                //主题Id 赋值
                themeId = themeList[position].id

                //主题列表 条目监听
                v.textView5.text = themeList[position].name
                //关闭主题pop 恢复窗口透明度
                pWTheme.dismiss()
                initPwNo()
            }
            R.id.line_location -> {
                //地点列表 条目监听
                v.textView6.text = locationList[position].address2
                //关闭地址pop 恢复窗口透明度
                pWLocation.dismiss()
                initPwNo()
            }
        }
    }

    /**
     * 获取频道
     */
    private fun obtainChannel(it: List<LZChannelBean>) {

        //获取pop布局
        val popupView = getPWView(R.mipmap.channel_head, channelAdapter)

        //请求到数据 添加到 集合中
        channelList.clear()
        channelList.addAll(it)
        //渲染数据
        channelAdapter.setNewInstance(channelList)

        //开启阴影
        initPwYes()

        //找到视图
        pWChannel.contentView = popupView
        pWChannel.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWChannel.showAtLocation(v.groupView, Gravity.BOTTOM, 0, 0) //开启弹窗
    }


    /**
     * 获取主题
     */
    private fun obtainTheme(it: List<LZThemeBean>) {
        //获取布局  实参 pop头部 图片  与 数据源 适配器
        val popupView = getPWView(R.mipmap.theme, themeAdapter)

        //请求到数据 添加到 集合中
        themeList.clear()
        themeList.addAll(it)
        themeAdapter.setNewInstance(themeList)

        //开启阴影
        initPwYes()

        //找到视图
        pWTheme.contentView = popupView
        pWTheme.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWTheme.showAtLocation(v.groupView, Gravity.BOTTOM, 0, 0) //开启弹窗
    }


    /**
     * 关于定位
     */
    private fun location() {
        //初始化定位
        mLocationClient =
            AMapLocationClient(mContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true)
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    @SuppressLint("HardwareIds")
    override fun onLocationChanged(p0: AMapLocation) {
        lat = p0.latitude //获取纬度
        lng = p0.longitude //获取经度
        //获取经纬度
        setSearchApi(lat, lng)
    }

    /**
     * 高德地图检索周边地址
     */
    fun setSearchApi(wei: Double, jing: Double) {
        val query: PoiSearch.Query = PoiSearch.Query("", "", "")
        query.pageSize = 20 //检索个数
        val search = PoiSearch(mContext, query)
        search.bound = PoiSearch.SearchBound(LatLonPoint(wei, jing), 10000)
        search.setOnPoiSearchListener(this)
        search.searchPOIAsyn()
    }

    override fun onPoiItemSearched(p0: PoiItem?, p1: Int) {

    }

    override fun onPoiSearched(result: PoiResult?, postion: Int) {
        val list: MutableList<LocationInfo> = mutableListOf()
        val query = result!!.query
        val pois = result.pois

        for (poi in pois) {
            val name = poi.cityName
            val city: String = poi.adName //海淀区
            val area: String = poi.businessArea //清河
            val snippet: String = poi.snippet //街道地址
            val detail: String = poi.title  //详细地址

            val point = poi.latLonPoint //经纬度

            val info = LocationInfo(detail, city + area + snippet, point.latitude, point.longitude)

            list.add(info)
        }

        //展示 位置
        obtainLocation(list)
    }

    /**
     * 展示位置 pop
     */
    private fun obtainLocation(list: MutableList<LocationInfo>) {

        val popupView = getPWView(R.mipmap.great, locationAdapter)

        //请求到数据 添加到 集合中
        locationList.clear()
        locationList.addAll(list)
        //渲染数据
        locationAdapter.setNewInstance(locationList)

        //开启阴影
        initPwYes()

        //找到视图
        pWLocation.contentView = popupView
        pWLocation.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWLocation.showAtLocation(v.groupView, Gravity.BOTTOM, 0, 0) //开启弹窗
    }


    /**
     *     统一设置 pop 布局
     */
    private fun getPWView(head: Int, mAdapter: BaseQuickAdapter<*, *>): View {
        val popupView: View = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_obtain_pop, null)

        //获取控件id
        val mHead = popupView.findViewById<ImageView>(R.id.mIv_obtain)
        val mRecObtain = popupView.findViewById<RecyclerView>(R.id.mRec_obtain)
        mHead.setImageResource(head)//设置布局头部图片
        //适配器 初始化
        mRecObtain.apply {
            //设置布局管理器
            layoutManager = LinearLayoutManager(mContext)
            //绑定适配器
            adapter = mAdapter
        }
        return popupView
    }

    //统一设置 pop布局 宽高
    private fun getPwXY(): PopupWindow {
        return PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, 600)
    }

    //开启阴影
    private fun initPwYes() {
        val attributes = window.attributes
        attributes.alpha = 0.8f
        window.attributes = attributes
    }

    //关闭阴影
    private fun initPwNo() {
        //关闭阴影
        val attributes = window.attributes
        attributes.alpha = 1f
        window.attributes = attributes
    }
}
