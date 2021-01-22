package com.sprout.ui.main.addition

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.home.bean.LZThemeBean

class TagsActViewModel :BaseViewModel() {

    var bpage = 0
    var gpage = 0
    var size = 10

    var brandList: MutableLiveData<BrandData> = MutableLiveData()
    var goodList: MutableLiveData<GoodData> = MutableLiveData()

    fun getBrandList(isShowLoading: Boolean) {
        launch({ httpUtil.getBrand(bpage,size) }, brandList, isShowLoading)
    }

    fun getGoodList(isShowLoading: Boolean) {
        launch({ httpUtil.getGood(gpage,size) }, goodList, isShowLoading)
    }
}