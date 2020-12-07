package com.yuke.homework.model

import com.yuke.homework.base.BaseBean

data class CommentBean(
    var content: String?,
    var sender: UserBean?
) : BaseBean()