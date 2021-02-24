package com.sprout.ui.main.home.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.lxj.xpopup.XPopup
import com.scwang.smartrefresh.header.WaterDropHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentSubRecommendBinding
import com.sprout.ui.bean.Distribution
import com.sprout.ui.bean.Poi
import com.sprout.ui.main.home.adapter.SubRecAdapter
import com.sprout.ui.main.home.adapter.TrendsAdapter
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.utils.GetDistance
import com.sprout.utils.MyMmkv
import com.sprout.utils.XPopupUtils
import com.sprout.widget.ListDecoration
import java.math.BigDecimal

class SubRecommendFragment(var command: Int, var channelId: Int) :
    BaseFragment<SubRecViewModel, FragmentSubRecommendBinding>(),
    OnItemChildClickListener {

    //获取手机当前地址
    var lat = MyMmkv.getFloat("lat")
    var lon = MyMmkv.getFloat("lon")

    //页码
    var page :Int = 1

    //列表适配器
    val trendsAdapter: SubRecAdapter by lazy { SubRecAdapter() }
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

    //适配器条目监听
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id){
            R.id.img_city ->{
                //条目图片 监听
                XPopup.Builder(context)
                    .asImageViewer(view.findViewById(R.id.img_city),
                        subRecList[position].url, XPopupUtils.ImageLoader())
                    .show()
            }
        }
    }
}