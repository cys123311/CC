package com.sprout.ui.main.addition

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.sdk.android.oss.ClientConfiguration
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSSClient
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.google.gson.Gson
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.sprout.R
import com.sprout.api.URLConstant
import com.sprout.base.BaseActivity
import com.sprout.databinding.ActivitySubmitMoreBinding
import com.sprout.ui.main.addition.adapter.ChannelAdapter
import com.sprout.ui.main.addition.adapter.LocationAdapter
import com.sprout.ui.main.addition.adapter.SubmitImgAdapter
import com.sprout.ui.main.addition.adapter.ThemeAdapter
import com.sprout.ui.main.addition.bean.ImgData
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.addition.bean.LZThemeBean
import com.sprout.ui.main.addition.bean.LocationInfo
import com.sprout.utils.PicSelectUtils
import com.sprout.utils.PwUtils
import com.sprout.utils.ToastUtil
import com.sprout.widget.clicks
import org.json.JSONArray
import org.json.JSONObject

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


    var max_img = 9

    //图片 适配器 060411
    val submitAdapter: SubmitImgAdapter by lazy { SubmitImgAdapter() }

    //本地数据集合
    var submitList: MutableList<ImgData> = mutableListOf()
    //声明 oss 图片上传之后 图片路径
    lateinit var ossClient : OSSClient
    var imgs: MutableList<String> = mutableListOf()
    //用于 记录 图片数量
    var imgCount = 0


    //声明 频道 pop 统一设置pop 宽高
    var pWChannel = PwUtils.getPwXY()
    //声明 pop 频道适配器
    val channelAdapter: ChannelAdapter by lazy { ChannelAdapter() }
    //声明 pop 频道 数据源集合
    val channelList: MutableList<LZChannelBean> = mutableListOf()


    //声明 主题 pop  统一设置pop 宽高
    var pWTheme = PwUtils.getPwXY()
    //声明 pop 主题适配器
    val themeAdapter: ThemeAdapter by lazy { ThemeAdapter() }
    //声明 pop 主题 数据源集合
    val themeList: MutableList<LZThemeBean> = mutableListOf()


    //定位
    lateinit var mLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption

    //声明 定位 pop  统一设置pop 宽高
    var pWLocation = PwUtils.getPwXY()

    //声明 定位适配器
    val locationAdapter: LocationAdapter by lazy { LocationAdapter() }

    //声明定位集合
    val locationList: MutableList<LocationInfo> = mutableListOf()


    override fun initView() {
        //初始化 OSS
        initOss()
        //初始化 高德定位
        initLocation()

        //图片 列表初始化
        v.recyclerReleaseImg.apply {
            layoutManager = GridLayoutManager(mContext, 3)
            submitAdapter.setNewInstance(submitList)
            adapter = submitAdapter
        }

        submitAdapter.setOnItemClickListener { adapter, view, position ->
            //图片列表 item条目的点击
            //当前点击的是加号  数组下标从0开始 故不需要加1 进行判断是否为加号
            if (position == submitList.size - 1 &&
                submitList[submitList.size - 1].path.isNullOrEmpty()
            ) {
                openPhoto()
            } else if (!submitList[position].path.isNullOrEmpty()) {
                val lineSubmitDelete = view.findViewById<ImageView>(R.id.line_submit_delete)
                lineSubmitDelete.visibility =
                    if (lineSubmitDelete.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    override fun initData() {
        if (intent.hasExtra("data")) {
            val json = intent.getStringExtra("data")
            if (json!!.isNotEmpty()) {
                val jsonArr = JSONArray(json)
                for (i in 0 until jsonArr.length()) {
                    val imgData =
                        Gson().fromJson<ImgData>(jsonArr.getString(i), ImgData::class.java)
                    submitList.add(imgData)
                }
                //处理加号 添加＋号
                if (submitList.size < max_img) {
                    val imgData = ImgData(null, mutableListOf())
                    submitList.add(imgData)
                }
                submitAdapter.notifyDataSetChanged()
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
            //二次采样 上传服务器 获取服务器地址 进行组装数据 上传到接口 发布
            getOssUrl()

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
            if(locationList.size>0)obtainLocation(locationList)
            else ToastUtil.showToast(mContext,"获取位置信息失败")
        }

        //适配器 条目监听声明   图片列表、频道、主题、地址
        submitAdapter.addChildClickViewIds(R.id.line_submit_delete)
        submitAdapter.setOnItemChildClickListener(this)
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
            Log.e("222", it.toString())
            when (it.errno) {
                0 -> {
                    //发布成功 关闭本页面r
                    finish()
                }
                602 -> {
                    //token 过期 未登录
                    ToastUtil.showToast(mContext, it.errmsg)
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
     *初始化OSS
     */
    private fun initOss() {
        val credentialProvider =
            OSSStsTokenCredentialProvider(URLConstant.key, URLConstant.secret, "")
        // 配置类如果不设置，会有默认配置。
        val conf = ClientConfiguration()
        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒。
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒。
        conf.maxConcurrentRequest = 5 // 最大并发请求数，默认5个。
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次。
        ossClient = OSSClient(applicationContext, URLConstant.ossPoint, credentialProvider)
    }

    /**
     * 获取 Oss 上传之后图片路径
     */
    private fun getOssUrl() {
        for (i in submitList.indices) {
            if(!submitList[i].path.isNullOrEmpty()){
                //记录图片数量
                imgCount++

                //bitmp 二次采样 后 返回流 故为空 看不到具体值
//                val scaleBitmp = BitmapUtils.getScaleBitmap(
//                    submitList[i].path,
//                    URLConstant.HEAD_WIDTH, URLConstant.HEAD_HEIGHT
//                )
//                val apricot= BitmapUtils.getBytesByBitmap(scaleBitmp)
//                val path = Base64.encodeToString(apricot, Base64.DEFAULT)
                submitList[i].path?.let { getOssLoadURL(it) }
            }
        }
//        val content = getSubmitJson()
//        vm.submitTrends(content, true)
    }

    //获取 Oss 上传之后图片路径
    fun getOssLoadURL(path: String) {
        val fileName = path.substring(path.lastIndexOf("/") + 1, path.length)
        val put = PutObjectRequest(URLConstant.bucketName, fileName, path)

        put.progressCallback = OSSProgressCallback<PutObjectRequest> { request, currentSize, totalSize ->
                // 进度百分比的计算
                // int p = (int) (currentSize/totalSize*100);
                if (currentSize == totalSize) {
                    //本地地址完成
                    val headUrl = request.uploadFilePath
                    Log.i("111", "本地地址完成$headUrl")
                }
            }
        ossClient.asyncPutObject(put,
            object : OSSCompletedCallback<PutObjectRequest, PutObjectResult> {

                override fun onSuccess(request: PutObjectRequest, result: PutObjectResult) {
                    Log.d("PutObject", "UploadSuccess")
                    Log.d("ETag", result.eTag)
                    Log.d("RequestId", result.requestId)
                    //成功的回调中读取相关的上传文件的信息  生成一个url地址
                    val url = ossClient.presignPublicObjectURL(request.bucketName, request.objectKey)
                    imgs.add(url)
                    if (imgs.size == imgCount){
                        val content = getSubmitJson()
                        vm.submitTrends(content, true)
                    }
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    // 请求异常。  // 本地异常，如网络异常等。
                    if (clientException!=null){
                        clientException.printStackTrace()
                        Log.e("111",clientException.toString())
                    }

                    if (serviceException != null) {
                        // 服务异常。
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                    }
                }
            })
    }

    /**
     * 组装提交的内容
     */
    fun getSubmitJson(): String {
        val title = v.editReleaseTitle.text.toString()        //标题
        val mood = v.editReleaseContent.text.toString()       //内容 心情
        val theme = v.textView5.text.toString()          //主题 内容
        val address = v.textView6.text.toString()        //地址
        val json: JSONObject = JSONObject()
        json.put("type", type)                //类型 图片为1 视频为2
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
                    img.put("url", imgs[i]) //oss 上传之后 图片路径
                    val tags = JSONArray() //标签
                    img.put("tags", tags)
                    for (j in 0 until submitList[i].tags.size) {
                        val tagItem = submitList[i].tags[j]
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
                    val imgData = ImgData(selectList[i].path, mutableListOf())
                    val index = submitList.size - 1
                    submitList.add(index, imgData)
                }
                //处理加号 消失
                if (submitList.size > max_img &&
                    submitList[submitList.size - 1].path.isNullOrEmpty()
                ) {
                    submitList.removeAt(submitList.size - 1)
                }
                submitAdapter.notifyDataSetChanged()
            }

            else -> {
            }
        }
    }

    /*当前还能插入的图片数量*/
    private fun openPhoto() {
        //当前还能插入的图片数量
        val num = max_img - submitList.size + 1
        PicSelectUtils.openPhoto(num, this)
    }


    /**
     * 关于定位  初始化 定位
     */
    private fun initLocation() {
        //初始化定位
        mLocationClient =
            AMapLocationClient(mContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置是否只定位一次,默认为false
        mLocationOption.isOnceLocation = true
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //启动定位
        mLocationClient.startLocation()
    }

    /**
     * 高德地图检索周边地址
     */
    @SuppressLint("HardwareIds")
    override fun onLocationChanged(p0: AMapLocation) {
        lat = p0.latitude //获取纬度
        lng = p0.longitude //获取经度
        //根据经纬度检索
        val query: PoiSearch.Query = PoiSearch.Query("", "", "")
        query.pageSize = 20 //检索个数
        val search = PoiSearch(mContext, query)
        search.bound = PoiSearch.SearchBound(LatLonPoint(lat, lng), 10000)
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
        locationList.addAll(list)
        v.textView6.text = list[0].address2
    }

    /**
     *  统一设置 pop 布局
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

    /**
     * 展示位置 pop
     */
    private fun obtainLocation(list: MutableList<LocationInfo>) {

        val popupView = getPWView(R.mipmap.great, locationAdapter)

        //渲染数据
        locationAdapter.setNewInstance(list)

        //开启阴影
        PwUtils.openShadow(window)

        //找到视图
        pWLocation.contentView = popupView
        pWLocation.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWLocation.showAtLocation(v.groupView, Gravity.BOTTOM, 0, 0) //开启弹窗
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
        PwUtils.openShadow(window)

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
        PwUtils.openShadow(window)

        //找到视图
        pWTheme.contentView = popupView
        pWTheme.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        pWTheme.showAtLocation(v.groupView, Gravity.BOTTOM, 0, 0) //开启弹窗
    }

    /**
     * 适配器条目监听
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.line_submit_delete -> {
                //点击 删除标记隐藏 再次点击 显示  //局部刷新
                submitList.removeAt(position)

                //处理加号  判断是否需要 添加＋号
                if (submitList.size < max_img &&
                    !submitList[submitList.size - 1].path.isNullOrEmpty()
                ) {

                    val imgData = ImgData(null, mutableListOf())
                    submitList.add(imgData)
                }
                submitAdapter.notifyItemRemoved(position)
            }
            R.id.line_channel -> {
                //频道Id 赋值
                channelId = channelList[position].id

                //频道列表 条目监听
                v.textView4.text = channelList[position].name
                //关闭主题pop 恢复窗口透明度
                pWChannel.dismiss()
                PwUtils.closeShadow(window)
            }
            R.id.line_theme -> {
                //主题Id 赋值
                themeId = themeList[position].id

                //主题列表 条目监听
                v.textView5.text = themeList[position].name
                //关闭主题pop 恢复窗口透明度
                pWTheme.dismiss()
                PwUtils.closeShadow(window)
            }
            R.id.line_location -> {
                //地点列表 条目监听
                v.textView6.text = locationList[position].address2
                //关闭地址pop 恢复窗口透明度
                pWLocation.dismiss()
                PwUtils.closeShadow(window)
            }
        }
    }
}