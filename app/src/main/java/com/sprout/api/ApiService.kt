package com.sprout.api

import com.sprout.api.response.BaseResult
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.addition.bean.LZSubmitTrends
import com.sprout.ui.main.addition.bean.LZChannelBean
import com.sprout.ui.main.addition.bean.LZThemeBean
import com.sprout.ui.main.login.bean.RegisterMessage
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    //获取品牌数据 sprout.cdwan.cn/api/tag/brand?page=1&size=10
    @GET("tag/brand")
    suspend fun getLabelBrandList(@Query("page") page:Int,@Query("size") size:Int):BaseResult<BrandData>

    //获取商品数据 sprout.cdwan.cn/api/tag/goods?page=1&size=10
    @GET("tag/goods")
    suspend fun getLabelGoodsList(@Query("page") page:Int,@Query("size") size:Int):BaseResult<GoodData>

    //发布动态 api/trends/submitTrends
    @POST("api/trends/submitTrends")
    suspend fun submitTrends(@Body trends: RequestBody) :BaseResult<LZSubmitTrends>

    //频道数据 sprout.cdwan.cn/api/channel/channel
    @GET("api/channel/channel")
    suspend fun getChannel(): BaseResult<List<LZChannelBean>>

    //主题数据 sprout.cdwan.cn/api/theme/getTheme 主题数据
    @GET("api/theme/getTheme")
    suspend fun getTheme(): BaseResult<List<LZThemeBean>>


    /**
     * 登录
     *
     */
    @POST("api/auth/login")
    @FormUrlEncoded
    suspend fun login(@Field("username")userName :String, @Field("password")userPsw:String):BaseResult<RegisterMessage>


    /**
     * 注册
     */
    @POST("api/auth/register")
    @FormUrlEncoded
    suspend fun register(@Field("username")userName :String,
                         @Field("password")userPsw:String,
                         @Field("imei")imei:String,
                         @Field("lng")lng:String,
                         @Field("lat")lat:String):
            BaseResult<RegisterMessage>
}