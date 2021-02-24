package com.sprout.ui.main.addition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sprout.base.BaseViewModel
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.addition.bean.LZThemeBean
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

class SubmitViewModel : BaseViewModel() {

    var state:MutableLiveData<Int> = MutableLiveData()

    /**
     * 提交动态数据
     */
    fun submitTrends(json:String){
        val body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),json)
        viewModelScope.launch {
            var result = httpUtil.submitTrends(body)
            if(result.errorCode == 0){
                state.postValue(0)
            }else if(result.errorCode == 603){
                state.postValue(-2)  //token无效
            }else{
                state.postValue(-1)
            }
        }
    }

    //获取频道
    val channel :MutableLiveData<List<LZChannelBean>> = MutableLiveData()

    fun getChannel(isShowLoading: Boolean){
        launch({httpUtil.getChannelTab()},channel,isShowLoading)
    }

    //主题数据
    val theme: MutableLiveData<List<LZThemeBean>> = MutableLiveData()

    fun getTheme(isShowLoading: Boolean) {
        launch({ httpUtil.getTheme() }, theme, isShowLoading)
    }
}