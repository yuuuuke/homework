package com.yuke.homework.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.yuke.homework.model.TweetBean
import com.yuke.homework.model.UserBean
import com.yuke.homework.network.NetService
import com.yuke.homework.network.ServiceCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val TAG = "MainViewModel"

    val mAllTweetsData: MutableLiveData<TweetBean> = MutableLiveData()

    val mUserData: MutableLiveData<UserBean> = MutableLiveData()

    val mCurrentTweetData: MutableLiveData<TweetBean> = MutableLiveData()

    val isLoadAllData:MutableLiveData<Boolean> = MutableLiveData()

    fun loadAllTweetsData() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    ServiceCreator.create<NetService>().getUserTweets()
                }
            }.onSuccess {
                //请求成功
                deleteJunkData(it)
                val tweets = TweetBean(it)
                tweets.isSuccess = true
                mAllTweetsData.postValue(tweets)
            }.onFailure {
                //请求失败
                Log.e(TAG, "加载推文数据出错" + it.message)
                val empty = TweetBean(null)
                empty.isSuccess = false
                mAllTweetsData.postValue(empty)
            }
        }
    }

    fun loadUserData() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    ServiceCreator.create<NetService>().getUserInfo()
                }
            }.onSuccess {
                //请求成功
                it.isSuccess = true
                mUserData.postValue(it)
            }.onFailure {
                //请求失败
                Log.e(TAG, "加载用户数据出错" + it.message)
                val empty = UserBean(null, null, null, null)
                empty.isSuccess = false
                mUserData.postValue(empty)
            }
        }
    }

    /**
     * 每次添加五条，异步
     */
    fun addTweetData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentTweets: ArrayList<TweetBean.TweetBO> =
                    mCurrentTweetData.value?.tweets ?: ArrayList()
                val allTweets: ArrayList<TweetBean.TweetBO> =
                    mAllTweetsData.value?.tweets ?: ArrayList()
                val allTweetSize = allTweets.size
                val currentTweetSize = currentTweets.size
                if (allTweetSize > currentTweetSize) {
                    //添加数据
                    if (allTweetSize - currentTweetSize >= 5) {
                        for (position: Int in currentTweetSize until currentTweetSize + 5) {
                            currentTweets.add(allTweets[position])
                        }
                    } else {
                        //不足五条了
                        for (position: Int in currentTweetSize until allTweetSize) {
                            currentTweets.add(allTweets[position])
                        }
                    }
                    mCurrentTweetData.postValue(TweetBean(currentTweets))
                } else {
                    //所有数据都已经添加了
                    isLoadAllData.postValue(true)
                }
            }
        }
    }


    /**
     * 去除无用数据
     */
    private fun deleteJunkData(list: ArrayList<TweetBean.TweetBO>) {
        if (list.size > 0) {
            for (position: Int in list.size - 1 downTo 0) {
                list[position].let {
                    if (it.isJunkData()) {
                        list.remove(it)
                    }
                }
            }
        }
    }
}