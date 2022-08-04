package com.iium.iium_medioz.util.keyword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.databinding.ItemKeywordBinding

class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.KeywordsViewHolder>() {
    private lateinit var itemClickListener: ItemClickListener
    private val items = ArrayList<KeywordEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : KeywordsViewHolder {
        val binding = ItemKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeywordsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: KeywordsViewHolder, position: Int) {
        holder.bind(items[position].keyword)
    }

    fun setList(list: List<KeywordEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class KeywordsViewHolder(private val binding: ItemKeywordBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(keyword: String){
            binding.keyword = keyword
            binding.ivDelete.setOnClickListener {
                itemClickListener.onClick(keyword)
            }
        }
    }

    interface ItemClickListener {
        fun onClick(keyword: String)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}