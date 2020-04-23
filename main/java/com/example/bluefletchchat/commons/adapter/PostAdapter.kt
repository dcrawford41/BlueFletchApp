package com.example.bluefletchassignment

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bluefletchchat.api.FeedItem


class PostAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>
    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val loadingItem = object: ViewType {
        override fun getViewType() = AdapterConstants.LOADING
    }



    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.POSTS, PostsDelegateAdapter())
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int {
            return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType)!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))?.onBindViewHolder(holder,
            this.items[position])

    }

    override fun getItemViewType(position: Int): Int {
        return this.items.get(position).getViewType()
    }

    fun addPosts(posts: List<FeedItem>, limit: Int) {
        val initPosition = items.size - 1
        items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        items.addAll(posts)
        items.add(loadingItem)
        notifyItemRangeInserted(initPosition, items.size + 1)
        if (limit == items.size) {
            items.remove(loadingItem)
            notifyItemRemoved(initPosition)
        }
    }

    fun clearAndAddPosts(posts: List<FeedItem>) {
        items.clear()
        items.addAll(posts)
        items.add(loadingItem)
    }

    fun getPosts(): List<FeedItem> {
        return items
            .filter { it.getViewType() == AdapterConstants.POSTS }
            .map { it as FeedItem }
    }





    private fun getLastPosition() = if (items.lastIndex == -1) 0 else items.lastIndex
}