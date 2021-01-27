package com.sprout.ui.main.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZThemeBean
import com.sprout.ui.main.home.fragment.CityFragment
import com.sprout.ui.main.home.fragment.ConcernFragment
import com.sprout.ui.main.home.fragment.RecommendFragment

class HomeFraViewModel :BaseViewModel() {

    val tabList = arrayListOf<String>("同城","关注","推荐")
    var fragments = arrayListOf<Fragment>()

    fun fragmentInit(){
        fragments.add(CityFragment())
        fragments.add(ConcernFragment())
        fragments.add(RecommendFragment())
    }
}