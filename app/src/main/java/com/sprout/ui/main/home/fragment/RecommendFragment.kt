package com.sprout.ui.main.home.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sprout.api.ext.bindReCommendViewPager2
import com.sprout.api.ext.bindViewPager2
import com.sprout.api.ext.init
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentRecommendBinding
import com.sprout.ui.main.home.HomeFraViewModel
import com.sprout.ui.main.home.bean.LZChannelBean


//推荐 频道
class RecommendFragment :
    BaseFragment<HomeFraViewModel, FragmentRecommendBinding>() {

    val tabList : MutableList<String> = mutableListOf()
    val fragmentList = arrayListOf<Fragment>()

    override fun initView() {
        vm.getChannelsTab(true)
    }

    override fun initVM() {
        //加载Fragment数据
        vm.channels.observe(this, Observer {
            for(i in it.indices){
                val fragment = SubRecommendFragment(3,it[i].id)
                tabList.add(it[i].name)
                fragmentList.add(fragment)
            }
            //初始化viewpager2设置适配器
            v.vp2Recommend.init(this,fragmentList)

            //初始化fragment指示器
            v.tabHomeRecommend.bindReCommendViewPager2(v.vp2Recommend,tabList)
        })
    }

    override fun initData(){

    }

    override fun initClick() {

    }

    override fun lazyLoadData() {

    }
}