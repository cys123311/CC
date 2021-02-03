package com.sprout.api

class URLConstant {
    companion object {

        const val token = "token"

        private const val BASE_URL_DEBUG: String = "http://sprout.cdwan.cn/"
        private const val BASE_URL_RELEASE: String = "http://sprout.cdwan.cn/"

        val BASE_URL: String = if (com.sprout.App.DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE
    }
}