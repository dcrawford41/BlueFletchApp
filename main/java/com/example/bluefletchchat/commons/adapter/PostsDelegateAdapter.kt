package com.example.bluefletchassignment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluefletchchat.R
import com.example.bluefletchchat.api.FeedItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.post_item.view.*

class PostsDelegateAdapter(): ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return PostsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as PostsViewHolder
        holder.bind(item as FeedItem)
    }

    inner class PostsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        parent.inflate(R.layout.post_item)
    ) {
        private val profileImg = itemView.img_thumbnail
        private val message = itemView.post_message
        private val username = itemView.username
        private val time = itemView.time
        private val updatedAt = itemView.edit_time
        private val comments = itemView.comments

        fun bind(item: FeedItem) {
            message.text = item.text
            Picasso.get().load(item.user.profilePic).into(profileImg)
            time.text = item.createdAt
            username.text = item.username
            if (!item.updatedAt.equals(item.createdAt)) {
                updatedAt.text = "Edited at: " + item.updatedAt
            }
            comments.text = "Comments"
        }

    }


}