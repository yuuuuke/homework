package com.yuke.homework.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuke.homework.R
import com.yuke.homework.model.CommentBean
import kotlinx.android.synthetic.main.layout_comment.view.*

class CommentAdapter(private val mContext: Context, private val comments: ArrayList<CommentBean>) :
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment, parent, false)
        return CommentHolder(view)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.bindView(comments[position])
    }


    inner class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(comment: CommentBean) {
            itemView.tv_name.text = "${comment.content?.trim() ?: ""}:"
            itemView.tv_comment.text = comment.content ?: ""
        }
    }
}