package com.yuke.homework.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.yuke.homework.R
import com.yuke.homework.model.CommentBean
import com.yuke.homework.model.TweetBean
import kotlinx.android.synthetic.main.layout_tweet_content.view.*

/**
 * 放内容的Adapter
 *
 * @param mContext Context
 */
class TweetAdapter(private val mContext: Context, val loadMoreData: () -> Unit) :
    RecyclerView.Adapter<TweetAdapter.TweetHolder>() {

    private val mTweetList: ArrayList<TweetBean.TweetBO> by lazy {
        ArrayList<TweetBean.TweetBO>()
    }

    /**
     * 刷新表格
     *
     * @param data 新数据
     */
    fun setNewData(data: ArrayList<TweetBean.TweetBO>) {
        if (data != mTweetList) {
            mTweetList.clear()
            mTweetList.addAll(data)
            notifyDataSetChanged()
        }
    }

    /**
     * 在尾部增加数据，刷新表格
     *
     * @param data 新增数据
     */
    fun addData(data: ArrayList<TweetBean.TweetBO>) {
        mTweetList.addAll(data)
        notifyDataSetChanged()
    }

    fun isEmptyList(): Boolean {
        return mTweetList.isEmpty()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.layout_tweet_content, parent, false)
        return TweetHolder(view)
    }

    override fun getItemCount(): Int {
        return mTweetList.size
    }

    override fun onBindViewHolder(holder: TweetHolder, position: Int) {
        if (position < 0 || position > mTweetList.size - 1) {
            return
        }
        holder.bindView(mTweetList[position])
        if (position == mTweetList.size - 1) {
            //最后一条数据了，需要加载更多
            loadMoreData()
        }
    }

    inner class TweetHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * 数据绑定
         *
         * @param tweet bean对象
         */
        fun bindView(tweet: TweetBean.TweetBO) {
            Log.v("zwp", tweet.toString())
            tweet.sender?.avatar?.let {
                Glide.with(mContext)
                    .load(it)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(10)))
                    .error(R.drawable.ic_image_error)
                    .placeholder(R.drawable.ic_avatar_default)
                    .into(itemView.iv_avatar)
            }

            tweet.sender?.username?.let {
                itemView.tv_username.text = it
            }

            tweet.content?.let {
                itemView.tv_text_content.visibility = View.VISIBLE
                itemView.tv_text_content.text = it
            } ?: apply {
                itemView.tv_text_content.visibility = View.GONE
            }

            tweet.images?.let {
                if (it.size > 0) {
                    //加载图片
                    itemView.list_image.visibility = View.VISIBLE
                    bindImageList(it)
                } else {
                    itemView.list_image.visibility = View.GONE
                }
            } ?: apply {
                itemView.list_image.visibility = View.GONE
            }

            tweet.comments?.let {
                if (it.size > 0) {
                    //显示评论
                    itemView.list_comment.visibility = View.VISIBLE
                    bindCommentList(it)
                } else {
                    itemView.list_comment.visibility = View.GONE
                }
            } ?: apply {
                itemView.list_comment.visibility = View.GONE
            }
        }

        private fun bindImageList(images: ArrayList<TweetBean.Image>) {
            itemView.list_image.layoutManager = GridLayoutManager(mContext, 3)
            itemView.list_image.adapter = ImageAdapter(mContext, images)
            itemView.list_image.isNestedScrollingEnabled = false
        }

        private fun bindCommentList(comments:ArrayList<CommentBean>){
            itemView.list_comment.adapter = CommentAdapter(mContext, comments)
            itemView.list_comment.isNestedScrollingEnabled = false
        }
    }
}