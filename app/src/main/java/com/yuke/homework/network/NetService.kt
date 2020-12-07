package com.yuke.homework.network

import com.yuke.homework.model.TweetBean
import com.yuke.homework.model.UserBean
import retrofit2.http.GET

interface NetService {
    @GET("user/jsmith")
    suspend fun getUserInfo():UserBean

    @GET("user/jsmith/tweets")
    suspend fun getUserTweets():ArrayList<TweetBean.TweetBO>
}