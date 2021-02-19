package com.sprout.ui.main.home

import android.content.Intent
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.sprout.R
import com.sprout.api.ext.bindViewPager2
import com.sprout.api.ext.init
import com.sprout.api.ext.navigationTo
import com.sprout.base.BaseFragment
import com.sprout.databinding.FragmentHomeBinding
import com.sprout.ui.main.home.fragment.CityFragment
import com.sprout.ui.main.home.fragment.ConcernFragment
import com.sprout.ui.main.home.fragment.RecommendFragment
import com.sprout.ui.main.music.MusicActivity
import com.sprout.widget.clicks


class HomeFragment : BaseFragment<HomeFraViewModel,FragmentHomeBinding>() {

    override fun initView() {
        //初始化fragmentList
        vm.fragmentInit()
        //初始化viewpager2
        v.vp2Home.init(this, fragments = vm.fragments).offscreenPageLimit =
            vm.fragments.size

        //初始化指示器
        v.magicIndicatorHome.bindViewPager2(v.vp2Home, mStringList = vm.tabList)

    }

    override fun initVM() {

    }

    override fun initData() {

    }

    override fun initClick() {
        v.waterDrop.clicks{navigationTo<MusicActivity>() }
    }

    override fun lazyLoadData() {

    }

}