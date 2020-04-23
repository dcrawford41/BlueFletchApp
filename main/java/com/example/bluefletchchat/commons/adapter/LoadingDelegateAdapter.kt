package com.example.bluefletchassignment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluefletchchat.R

class LoadingDelegateAdapter: ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class LoadingViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(
        parent.inflate(R.layout.post_item_loading)
    )
}
