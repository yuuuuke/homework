package com.yuke.homework.model

import com.yuke.homework.base.BaseBean

data class TweetBean(
    var tweets: ArrayList<TweetBO>?
) : BaseBean() {

    data class TweetBO(
        var content: String?,
        var images: ArrayList<Image>?,
        var sender: UserBean?,
        var comments: ArrayList<CommentBean>?
    ) {
        /**
         * 判断是否是无效数据
         */
        fun isJunkData(): Boolean {
            sender?.let {
                content?.let {
                    return false
                }
                images?.let {
                    return it.size <= 0
                }
            }
            return true
        }
    }

    data class Image(var url: String?)
}