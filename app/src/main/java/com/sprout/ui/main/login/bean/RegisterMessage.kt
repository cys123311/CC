package com.sprout.ui.main.login.bean

data class RegisterMessage(
    val token: String?,
    val code:Int,
    val userInfo: UserInfo?
)

data class UserInfo(
    val avatar: String,
    val birthday: Int,
    val gender: Int,
    val nickname: String?,
    val uid: String,
    val username: String,
    val age :String?,
    val sex: String?
)