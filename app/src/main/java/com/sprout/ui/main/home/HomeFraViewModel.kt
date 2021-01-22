package com.sprout.ui.main.home

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZThemeBean

class HomeFraViewModel :BaseViewModel() {

    //主题数据 sprout.cdwan.cn/api/theme/getTheme 主题数据
    var theme: MutableLiveData<LZThemeBean> = MutableLiveData()

    fun getTheme(isShowLoading: Boolean) {
        launch({ httpUtil.getTheme() }, theme, isShowLoading)
    }

    //频道分类数据 sprout.cdwan.cn/api/channel/channel
    var channel: MutableLiveData<List<LZChannelBean>> = MutableLiveData()

    fun getChannel(isShowLoading: Boolean) {
        launch({ httpUtil.getChannel() }, channel, isShowLoading)
    }
}