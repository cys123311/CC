package com.sprout.ui.main.home.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.luck.picture.lib.tools.ToastUtils
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentSubRecommendBinding
import com.sprout.ui.main.home.HomeFraViewModel
import com.sprout.ui.main.home.adapter.SubRecAdapter
import com.sprout.ui.main.home.bean.LZTrendsData

class SubRecommendFragment(var command: Int, var channelId: Int) :
    BaseFragment<HomeFraViewModel, FragmentSubRecommendBinding>(),
    OnItemChildClickListener {

    //列表适配器
    val subRecAdapter: SubRecAdapter by lazy { SubRecAdapter() }
    var subRecList:MutableList<LZTrendsData> = mutableListOf()

    override fun initView() {
        vm.getTrendsList(command, 1, 10, channelId, true)

        v.recyclerSubRecommend.apply {
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)

            subRecAdapter.setNewInstance(subRecList)
            adapter = subRecAdapter
        }
    }

    override fun initVM() {
        vm.submitTrends.observe(this, Observer {
            subRecList.clear()
            subRecList.addAll(it)
            subRecAdapter.notifyDataSetChanged()
        })
    }

    override fun initData() {

    }

    override fun initClick() {

        subRecAdapter.addChildClickViewIds(R.id.line_sub_rec)
        subRecAdapter.setOnItemChildClickListener(this)
    }

    override fun lazyLoadData() {

    }

    //适配器条目监听
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id){
            R.id.line_sub_rec ->{
                //条目监听
                ToastUtils.s(mContext,subRecList[position].nickname)
            }
        }
    }
}