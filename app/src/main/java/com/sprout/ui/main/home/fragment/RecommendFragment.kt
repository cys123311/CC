package com.sprout.ui.main.home.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sprout.R
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentRecommendBinding
import com.sprout.ui.main.home.HomeFraViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import java.util.ArrayList

//推荐 频道
class RecommendFragment :
    BaseFragment<HomeFraViewModel, FragmentRecommendBinding>() {

    val fragment = ArrayList<Fragment>()
    val tabList = ArrayList<String>()

    override fun initView() {
        vm.getChannel(true)
    }

    override fun initVM() {
        vm.channel.observe(this, Observer {
            initTab(it) //加载tab数据
        })
    }

    //加载tab数据
    private fun initTab(it: List<LZChannelBean>) {
        for (i in it.indices){
            //tab 标题 获取
            tabList.add(it[i].name)
            //通过 tab id 获取 相关列表数据
            v.mTabRec.addTab(v.mTabRec.newTab().setText(it[i].name))
        }
    }

    override fun initData(){

    }

    override fun initClick() {

    }

    override fun lazyLoadData() {

    }


}