package com.sprout.ui.main.home.fragment

import android.content.ContentValues
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentSubRecommendBinding
import com.sprout.ui.adapter.ImageAdapter2
import com.sprout.ui.bean.Distribution
import com.sprout.ui.bean.Poi
import com.sprout.ui.main.home.adapter.SubRecAdapter
import com.sprout.ui.main.home.adapter.TrendsAdapter
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.utils.GetDistance
import com.sprout.utils.MyMmkv
import com.sprout.utils.XPopupUtils
import com.sprout.widget.ListDecoration
import com.sprout.widget.clicks
import org.jetbrains.anko.find
import java.math.BigDecimal
import java.util.ArrayList

class SubRecommendFragment(var command: Int, var channelId: Int) :
    BaseFragment<SubRecViewModel, FragmentSubRecommendBinding>(),
    OnItemChildClickListener {

    //获取手机当前地址
    var lat = MyMmkv.getFloat("lat")
    var lon = MyMmkv.getFloat("lon")

    //页码
    var page :Int = 1

    //列表适配器
    val trendsAdapter: TrendsAdapter by lazy { TrendsAdapter() }
    var subRecList:MutableList<LZTrendsData> = mutableListOf()

    override fun initView() {
        vm.getTrendList(command, 1, 20, channelId, true)

        v.recyclerSubRecommend.apply {
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(ListDecoration())
            trendsAdapter.setNewInstance(subRecList)
            adapter = trendsAdapter
        }

        v.mSmart.setRefreshHeader(WaterDropHeader(mContext))
        v.mSmart.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                //加载更多
                page++
                vm.getTrendList(command, page, 20, channelId, true)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                //初始化
                subRecList.clear()
                vm.getTrendList(command, page, 20, channelId, true)
            }
        })
    }

    override fun initVM() {
        vm.submitTrend.observe(this, Observer {
            //初始化数据列表
            for (i in it.indices){
                if (lat.isFinite() && !lon.isFinite() &&
                    lat!= 0f && lon != 0f||
                    it[i].lat.isFinite() && it[i].lng.isFinite() &&
                    it[i].lat!= 0f && it[i].lng != 0f) {
                    val distance =
                        GetDistance.getDistance(
                            Distribution(lon,lat),
                            Distribution(it[i].lng,it[i].lat)
                        )
                    val bg = BigDecimal(distance)
                    val km: Double = bg.setScale(1, BigDecimal.ROUND_HALF_UP).toDouble()
                    it[i].distance = km.toString()+"km"
                }else{
                    it[i].distance = "10km"
                }
            }
            subRecList.addAll(it)
            trendsAdapter.notifyDataSetChanged()

            v.mSmart.finishRefresh()
            v.mSmart.finishLoadMore()
        })
    }

    override fun initData() {

    }

    override fun initClick() {

        trendsAdapter.addChildClickViewIds(R.id.img_city)
        trendsAdapter.setOnItemChildClickListener(this)
    }

    override fun lazyLoadData() {

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id){
            R.id.img_city ->{
                //数据为图片
                if (subRecList[position].type==1){
                    //图片为多张
                    if (subRecList[position].res.size>1){
                        val list = ArrayList<String>()
                        for (i in subRecList[position].res.indices){
                            list.add(subRecList[position].res[i].url)
                        }
                        playImage(list)
                    }else{
                        //单张图片
                        XPopup.Builder(context)
                            .asImageViewer(view.findViewById(R.id.img_city),
                                subRecList[position].url, XPopupUtils.ImageLoader())
                            .show()
                    }

                }else if(subRecList[position].type==2) {
                    if(!subRecList[position].url.endsWith(".mp4"))return
                    //数据为视频
                    playVideo(subRecList[position].url,subRecList[position].title)
                }
            }
        }
    }

    lateinit var popupVideo : PopupWindow
    private fun playVideo(url: String, title: String) {
        val popupView: View = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_video_pop, null)
        //设置popu
        popupVideo = PopupWindow(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val mGs = popupView.find<StandardGSYVideoPlayer>(R.id.mGs)
        mGs.setUp(url, false,title)


//设置返回键
        mGs.backButton.clicks {
            popupVideo.dismiss()
            GSYVideoManager.releaseAllVideos() //释放所有视频
            mGs.setVideoAllCallBack(null)
        }
//设置全屏按键功能
        mGs.fullscreenButton.setOnClickListener {
            mGs.startWindowFullscreen(context, false, true)
            popupVideo.dismiss()
        }
//防止错位设置

        mGs.playTag = ContentValues.TAG
//是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        mGs.isAutoFullWithSize = true
//音频焦点冲突时是否释放
        mGs.isReleaseWhenLossAudio = false
//全屏动画
        mGs.isShowFullAnimation = true
//小屏时不触摸滑动
        mGs.setIsTouchWiget(false)

        //设置旋转
        val orientationUtils = OrientationUtils(mContext, mGs)
        //设置全屏按键功能
        mGs.fullscreenButton.setOnClickListener(View.OnClickListener {
            orientationUtils.resolveByClick() //此方法是切换屏幕旋转，例如现在是竖屏，调用后变横屏，反正一样，设置这个之前，先把Activity禁止横竖屏切换，并且竖屏模式，不然此方法无效
        })

        //增加封面
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(mContext).setDefaultRequestOptions(
            RequestOptions()
                .frame(1000000)
                .centerCrop()
                .error(R.mipmap.ic_video_error)
        ).load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
            .into(imageView)

        mGs.thumbImageView = imageView

        //找到视图
        popupVideo.contentView = popupView
        popupVideo.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        popupVideo.showAtLocation(v.fragmentSub, Gravity.CENTER, 0, 0) //开启弹窗

        popupVideo.setOnDismissListener {
            //关闭弹窗后 同时 关闭播放器
            GSYVideoManager.releaseAllVideos() //释放所有视频
            mGs.setVideoAllCallBack(null)
        }
    }

    lateinit var popupImage : PopupWindow
    private fun playImage(list: ArrayList<String>) {
        val popupView: View = LayoutInflater.from(mContext)
            .inflate(R.layout.layout_image_pop, null)
        //设置popu
        popupImage = PopupWindow(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        val vp2 = popupView.findViewById<ViewPager2>(R.id.mVp_label)
        val mImg = popupView.findViewById<ImageView>(R.id.mImg_label)
        mImg.clicks {
            popupImage.dismiss()
        }

        //初始化 适配器
        vp2.adapter = ImageAdapter2(list, vp2)

        //找到视图
        popupImage.contentView = popupView
        popupImage.isClippingEnabled = false

        //在按钮的下方弹出  无偏移 第一种方式
        popupImage.showAtLocation(v.fragmentSub, Gravity.CENTER, 0, 0) //开启弹窗
    }
}