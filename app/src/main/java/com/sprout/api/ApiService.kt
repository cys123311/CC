package com.sprout.api

import com.sprout.api.response.BaseResult
import com.sprout.ui.main.addition.bean.*
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZTrendsData
import com.sprout.ui.main.login.bean.RegisterMessage
import com.sprout.utils.MyMmkv
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

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

    //获取品牌数据 sprout.cdwan.cn/api/tag/brand?page=1&size=10
    @GET("tag/brand")
    suspend fun getLabelBrandList(@Query("page") page:Int,@Query("size") size:Int):BaseResult<BrandData>

    //获取商品数据 sprout.cdwan.cn/api/tag/goods?page=1&size=10
    @GET("tag/goods")
    suspend fun getLabelGoodsList(@Query("page") page:Int,@Query("size") size:Int):BaseResult<GoodData>

    //频道数据 sprout.cdwan.cn/api/channel/channel
    @GET("api/channel/channel")
    suspend fun getChannelTab(): BaseResult<List<LZChannelBean>>

    //主题数据 sprout.cdwan.cn/api/theme/getTheme 主题数据
    @GET("api/theme/getTheme")
    suspend fun getTheme(): BaseResult<List<LZThemeBean>>

    //发布动态 api/trends/submitTrends
    //注解中@Body标签不能和@FormUrlEncoded .@Multipart标签同时使用。
    @Headers("Content-Type: application/json")
    @JvmSuppressWildcards
    @POST("api/trends/submitTrends")
    suspend fun submitTrends(@Body trends: List<RequestBody>) :BaseResult<LZSubmitTrends>

    /**
     * 获取动态数据
     */
    @GET("api/trends/trendsList")
    suspend fun getTrendsList(@Query("command") command:Int,
                              @Query("page") page:Int,
                              @Query("size") size: Int,
                              @Query("channelid") channelid:Int
    ):BaseResult<List<LZTrendsData>>



}