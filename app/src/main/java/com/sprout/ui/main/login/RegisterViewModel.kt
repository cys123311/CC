package com.sprout.ui.main.login

import androidx.lifecycle.MutableLiveData
import com.sprout.R
import com.sprout.api.response.BaseResult
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.login.bean.RegisterMessage

class RegisterViewModel:BaseViewModel() {

    var registerInfo: MutableLiveData<RegisterMessage> = MutableLiveData()

    fun login(userName: String, userPsw: String) {
        launch({ httpUtil.login(userName, userPsw) },registerInfo )
    }

    fun register(userName: String, userPsw: String, imei: String, lng: String, lat: String) {
        launch({ httpUtil.register(userName, userPsw, imei, lng, lat) }, registerInfo)
    }
}