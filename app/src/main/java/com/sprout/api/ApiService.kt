package com.sprout.api

import com.sprout.api.response.BaseResult
import com.sprout.ui.main.addition.bean.BrandData
import com.sprout.ui.main.addition.bean.GoodData
import com.sprout.ui.main.home.bean.LZChannelBean
import com.sprout.ui.main.home.bean.LZThemeBean
import retrofit2.http.*


interface ApiService {

    //主题数据 sprout.cdwan.cn/api/theme/getTheme 主题数据
    @GET("api/theme/getTheme")
    suspend fun getTheme(): BaseResult<LZThemeBean>

    //频道分类数据 sprout.cdwan.cn/api/channel/channel
    @GET("api/channel/channel")
    suspend fun getChannel(): BaseResult<List<LZChannelBean>>

    //获取品牌数据 sprout.cdwan.cn/api/tag/brand?page=1&size=10
    @GET("tag/brand")
    suspend fun getBrand(@Query("page") page:Int,@Query("size") size:Int):BaseResult<BrandData>

    //获取商品数据 sprout.cdwan.cn/api/tag/goods?page=1&size=10
    @GET("tag/good")
    suspend fun getGood(@Query("page") page:Int,@Query("size") size:Int):BaseResult<GoodData>

}