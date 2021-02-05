package com.sprout.ui.main.home.fragment

import android.util.SparseArray
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sprout.BR
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentCityBinding
import com.sprout.ui.main.home.HomeFraViewModel
import com.sprout.ui.main.home.adapter.CityTrendsAdpater
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.widget.ListDecoration

class CityFragment :
    BaseFragment<HomeFraViewModel,FragmentCityBinding>() {

    //创建适配器
    val cityTrendsAdapter: CityTrendsAdpater by lazy { CityTrendsAdpater() }

    override fun initView() {
        //command 1同城 2关注 3推荐
        vm.getTrendsList(1,1,10,0,true)

        //初始化 适配器
        v.mRecCity.apply {
            //设置瀑布流布局管理器
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            //添加 自定义线 使条目间隔等比
            addItemDecoration(ListDecoration())
            //绑定适配器
            adapter=cityTrendsAdapter
        }
    }

    override fun initVM() {
        vm.submitTrends.observe(this, Observer {
            //初始化数据列表
            cityTrendsAdapter.setNewInstance(it.toMutableList())
            cityTrendsAdapter.notifyDataSetChanged()
        })
    }

    override fun initData() {

    }

    override fun initClick() {

    }

    override fun lazyLoadData() {

    }
}