package com.sprout.ui.main.home.fragment

import com.sprout.api.ext.bindReCommendViewPager2
import com.sprout.api.ext.init
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentRecommendBinding


//推荐 频道
class RecommendFragment :
    BaseFragment<RecommendFraViewModel, FragmentRecommendBinding>() {

    override fun initView() {

        //加载Fragment数据
        vm.fragmentInit()

        //初始化viewpager2设置适配器
        v.vp2Recommend.init(this,vm.fragments)

        //初始化fragment指示器
        v.tabHomeRecommend.bindReCommendViewPager2(v.vp2Recommend,vm.tapTitles)

    }

    override fun initVM() {

    }

    override fun initData(){

    }

    override fun initClick() {

    }

    override fun lazyLoadData() {

    }
}