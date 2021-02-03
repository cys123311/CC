package com.sprout.ui.main.addition

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData

class AdditionViewModel :BaseViewModel() {

    var bpage = 0
    var gpage = 0
    var size = 10

    var labelbrandList: MutableLiveData<BrandData> = MutableLiveData()
    var labelgoodList: MutableLiveData<GoodData> = MutableLiveData()

    fun getLabelBrandList(isShowLoading: Boolean) {
        launch({ httpUtil.getLabelBrandList(bpage,size) }, labelbrandList, isShowLoading)
    }

    fun getLabelGoodsList(isShowLoading: Boolean) {
        launch({ httpUtil.getLabelGoodsList(gpage,size) }, labelgoodList, isShowLoading)
    }
}