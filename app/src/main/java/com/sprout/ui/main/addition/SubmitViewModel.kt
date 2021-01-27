package com.sprout.ui.main.addition

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.addition.bean.LZSubmitTrends

class SubmitViewModel :BaseViewModel() {

    //发布动态
    var submit: MutableLiveData<LZSubmitTrends> = MutableLiveData()

    fun submitTrends(isShowLoading: Boolean) {
        launch({ httpUtil.submitTrends() }, submit, isShowLoading)
    }

}