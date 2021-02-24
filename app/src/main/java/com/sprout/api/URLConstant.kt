package com.sprout.api

class URLConstant {
    companion object {

        const val token = "token"

        //oss 上传
//        const val bucketName = "2002aaa"
//        const val ossPoint = "http://oss-cn-beijing.aliyuncs.com"
//        const val key = "LTAI4G68osGeb6ck3m4ogpKA"           //appkey
//        const val secret = "MNIJ56CV0j5E1zS667tVlZxrbbGbxo"  //密码

        const val bucketName = "sprout-app"
        const val ossPoint = "http://oss-cn-beijing.aliyuncs.com"
        const val key = "LTAI4GH6Gy8tFbbXJ3vatsAn" //appkey
        const val secret = "YjWyqsTnHX8336jRZ1vg7FSWaojPf8" //密码

//        AccessKey ID
//        LTAI4GH6Gy8tFbbXJ3vatsAn
//        AccessKey Secret
//        YjWyqsTnHX8336jRZ1vg7FSWaojPf8

        const val HEAD_WIDTH = 1000 // 宽高 图片二次采样
        const val HEAD_HEIGHT = 1000

        const val TYPE_IMG = 1     //发布 1图片 2视频
        const val TYPE_VIDEO = 2

        private const val BASE_URL_DEBUG: String = "http://sprout.cdwan.cn/"
        private const val BASE_URL_RELEASE: String = "http://sprout.cdwan.cn/"

        val BASE_URL: String = if (com.sprout.App.DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE
    }
}