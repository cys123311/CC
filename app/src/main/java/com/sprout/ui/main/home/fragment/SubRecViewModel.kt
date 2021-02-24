package com.sprout.ui.main.home.fragment

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZTrendsData

class SubRecViewModel:BaseViewModel() {

    //获取动态数据 列表
    var submitTrend: MutableLiveData<List<LZTrendsData>> = MutableLiveData()

    fun getTrendList(command:Int,page:Int,size :Int,channelid :Int,isShowLoading: Boolean) {
        launch({ httpUtil.getTrendsList(command,
            page,size,channelid) }, submitTrend, isShowLoading)
    }
}