package com.yuke.homework.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.yuke.homework.R
import com.yuke.homework.dp2px
import com.yuke.homework.setStatusTextColorDark
import com.yuke.homework.view.adapter.TweetAdapter
import com.yuke.homework.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    var isLoadAll = false

    private val mAdapter: TweetAdapter by lazy {
        TweetAdapter(this) {
            if (!isLoadAll) {
                viewModel.addTweetData()
            }
        }
    }

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
            MainViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createObserver()
        viewModel.loadUserData()
        setStatusTextColorDark(this)
        list_content.adapter = mAdapter
        list_content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE ->
                        if (Glide.with(this@MainActivity).isPaused) {
                            Glide.with(this@MainActivity).resumeRequests()
                        }
                    RecyclerView.SCROLL_STATE_DRAGGING ->
                        if (!Glide.with(this@MainActivity).isPaused) {
                            Glide.with(this@MainActivity).pauseRequests()
                        }
                }
            }
        })
        appbar_layout.addOnOffsetChangedListener(this)
        refresh_layout.setOnRefreshListener {
            viewModel.loadAllTweetsData()
        }
        refresh_layout.autoRefresh()
    }

    /**
     * 创建LiveData观察者
     */
    private fun createObserver() {
        viewModel.mAllTweetsData.observe(this, Observer {
            if (it.isSuccess) {
                //数据加载成功
                it.tweets?.let { list ->
                    if (list.size > 0) {
                        //第一次加载成功
                        viewModel.addTweetData()
                    }
                }
            }
            refresh_layout.isRefreshing = false
        })

        viewModel.mUserData.observe(this, Observer {
            if (it.isSuccess) {
                //数据加载成功
                Glide.with(this).load(it.profile).placeholder(R.drawable.ic_image_default)
                    .error(R.drawable.ic_image_error).into(iv_background)
                Glide.with(this).load(it.avatar).placeholder(R.drawable.ic_avatar_default)
                    .error(R.drawable.ic_image_error).into(iv_user_avatar)
                tv_username.text = it.username
            }
        })

        viewModel.mCurrentTweetData.observe(this, Observer {
            it?.tweets?.let { list ->
                if (list.size > 0) {
                    if (mAdapter.isEmptyList()) {
                        mAdapter.setNewData(list)
                    } else {
                        mAdapter.addData(list)
                    }
                }
            }
        })

        viewModel.isLoadAllData.observe(this, Observer {
            isLoadAll = it
        })
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        refresh_layout.isEnabled = verticalOffset >= 0
        abs(verticalOffset).toFloat().div(dp2px(100F)).let {
            if (it >= 1) {
                toolbar.alpha = 1F
            } else {
                toolbar.alpha = it
            }
        }
    }
}
