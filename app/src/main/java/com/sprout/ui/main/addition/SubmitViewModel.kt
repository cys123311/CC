package com.sprout.ui.main.addition

import androidx.lifecycle.MutableLiveData
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.addition.bean.LZChannelBean
import com.sprout.ui.main.addition.bean.LZSubmitTrends
import com.sprout.ui.main.addition.bean.LZThemeBean
import okhttp3.MediaType
import okhttp3.RequestBody

class SubmitViewModel : BaseViewModel() {

    //发布动态  提交数据
    var submit: MutableLiveData<LZSubmitTrends> = MutableLiveData()

    fun submitTrends(json: String, isShowLoading: Boolean) {
        // json 类型 转换成 RequestBody 类型 进行提交数据
        val body = RequestBody.create(MediaType.parse
            ("application/json;charset=utf-8"),json)
        // https://stackoverflow.com/questions/32821102/retrofit-500-internal-server-error
        val friendsList: MutableList<RequestBody> = mutableListOf()
        friendsList.add(body)

        launch({ httpUtil.submitTrends(friendsList) },submit,isShowLoading)
    }

    //获取频道
    val channel :MutableLiveData<List<LZChannelBean>> = MutableLiveData()

    fun getChannel(isShowLoading: Boolean){
        launch({httpUtil.getChannel()},channel,isShowLoading)
    }

    //主题数据
    val theme: MutableLiveData<List<LZThemeBean>> = MutableLiveData()

    fun getTheme(isShowLoading: Boolean) {
        launch({ httpUtil.getTheme() }, theme, isShowLoading)
    }
}