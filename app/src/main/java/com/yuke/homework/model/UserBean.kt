package com.yuke.homework.model

import com.google.gson.annotations.SerializedName
import com.yuke.homework.base.BaseBean

data class UserBean(
    @SerializedName("profile-image")
    var profile: String?,
    var avatar: String?,
    var nick: String?,
    var username: String?
) : BaseBean()