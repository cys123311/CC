package com.sprout.ui.main.home.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sprout.R
import com.sprout.api.ext.navigationTo
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentCityBinding
import com.sprout.ui.bean.Distribution
import com.sprout.ui.bean.Poi
import com.sprout.ui.main.addition.AdditionActivity
import com.sprout.ui.main.home.HomeFraViewModel
import com.sprout.ui.main.home.adapter.TrendsAdapter
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.utils.GetDistance
import com.sprout.utils.GlideEngine
import com.sprout.utils.MyMmkv
import com.sprout.utils.XPopupUtils
import com.sprout.widget.ListDecoration
import com.sprout.widget.clicks
import java.math.BigDecimal

class CityFragment :
    BaseFragment<HomeFraViewModel,FragmentCityBinding>(),
    OnItemChildClickListener {

    //获取手机当前地址
    var lat = MyMmkv.getFloat("lat")
    var lon = MyMmkv.getFloat("lon")

    //页码
    var page :Int = 1

    //创建适配器
    val trendsAdapter: TrendsAdapter by lazy { TrendsAdapter() }
    //数据源
    val cityList : MutableList<LZTrendsData> = mutableListOf()

    override fun initView() {
        GlideEngine.createGlideEngine()
            .loadImage(mContext,R.drawable.iv_img_city,v.ivImgCity)
        //command 1同城 2关注 3推荐
        vm.getTrendsList(1,page,20,0,true)

        //初始化 适配器
        v.mRecCity.apply {
            //设置瀑布流布局管理器
            layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
            //添加 自定义线 使条目间隔等比
            addItemDecoration(ListDecoration())
            trendsAdapter.setNewInstance(cityList)
            //绑定适配器
            adapter=trendsAdapter
        }

        trendsAdapter.setOnItemClickListener { adapter, view, position ->
            //图片列表 item条目的点击
            //当前点击的是加号  数组下标从0开始 故不需要加1 进行判断是否为加号
            XPopup.Builder(context)
                .asImageViewer(view.findViewById(R.id.img_city), cityList[position].url, XPopupUtils.ImageLoader())
                .show()
        }

        //http://www.cocoachina.com/articles/32556 WaveSwipeHeader
        v.mSmart.setRefreshHeader(WaterDropHeader(mContext))
        v.mSmart.setOnRefreshLoadMoreListener(object :OnRefreshLoadMoreListener{
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                //加载更多
                //command 1同城 2关注 3推荐
                page++
                vm.getTrendsList(1,page,20,0,true)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                //初始化
                //command 1同城 2关注 3推荐
                cityList.clear()
                vm.getTrendsList(1,1,20,0,true)
            }
        })
    }

    override fun initVM() {

        vm.submitTrends.observe(this, Observer {
            //初始化数据列表
            for (i in it.indices){
                if (lat.isFinite() && !lon.isFinite() &&
                        lat!= 0f && lon != 0f||
                        it[i].lat.isFinite() && it[i].lng.isFinite() &&
                        it[i].lat!= 0f && it[i].lng != 0f) {
                    val distance =
                        GetDistance.getDistance(
                            //东京116度  北纬40度
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
            cityList.addAll(it)
            trendsAdapter.notifyDataSetChanged()

            v.mSmart.finishRefresh()
            v.mSmart.finishLoadMore()
        })
    }

    override fun initData() {

    }

    override fun initClick() {

        v.ivImgCity.clicks {
            //调用发布页
            navigationTo<AdditionActivity>()
        }
        v.ivCityAdd.clicks {
            //调用签到页
            //navigationTo<AdditionActivity>()
        }


        trendsAdapter.addChildClickViewIds(R.id.img_city)
        trendsAdapter.setOnItemChildClickListener(this)
    }

    override fun lazyLoadData() {

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id){
            R.id.img_city ->{
                //条目图片 监听
                XPopup.Builder(context)
                    .asImageViewer(view.findViewById(R.id.img_city),
                        cityList[position].url, XPopupUtils.ImageLoader())
                    .show()
            }
        }
    }
}