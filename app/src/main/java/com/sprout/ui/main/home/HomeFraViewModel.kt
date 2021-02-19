package com.sprout.ui.main.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.ui.main.home.fragment.CityFragment
import com.sprout.ui.main.home.fragment.ConcernFragment
import com.sprout.ui.main.home.fragment.RecommendFragment

class HomeFraViewModel :BaseViewModel() {

    //首页 顶部导航 同城 关注 推荐
    val tabList = arrayListOf<String>("同城","关注","推荐")
    var fragments = arrayListOf<Fragment>()

    fun fragmentInit(){
        if(fragments.size==0){
            fragments.add(CityFragment())
            fragments.add(ConcernFragment())
            fragments.add(RecommendFragment())
        }
    }

    //推荐 顶部导航
    var channels:MutableLiveData<List<LZChannelBean>> = MutableLiveData()

    fun getChannelsTab(isShowLoading: Boolean) {
        launch({ httpUtil.getChannelTab() }, channels, isShowLoading)
    }



    //获取动态数据 列表
    var submitTrends: MutableLiveData<List<LZTrendsData>> = MutableLiveData()

    fun getTrendsList(command:Int,page:Int,size :Int,channelid :Int,isShowLoading: Boolean) {
        launch({ httpUtil.getTrendsList(command,
            page,size,channelid) }, submitTrends, isShowLoading)
    }
}