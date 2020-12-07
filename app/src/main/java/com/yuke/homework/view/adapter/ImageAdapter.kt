package com.yuke.homework.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yuke.homework.R
import com.yuke.homework.model.TweetBean
import kotlinx.android.synthetic.main.layout_image.view.*

/**
 * 图片适配器
 */
class ImageAdapter(val mContext: Context, val images: ArrayList<TweetBean.Image>) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_image, parent, false)
        return ImageHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindView(images[position])
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(image: TweetBean.Image) {
            Glide.with(mContext).load(image.url).error(R.drawable.ic_image_error)
                .placeholder(R.drawable.ic_image_default)
                .into(itemView.iv_image)
        }
    }
}