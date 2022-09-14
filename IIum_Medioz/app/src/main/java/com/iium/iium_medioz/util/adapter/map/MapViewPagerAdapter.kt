package com.iium.iium_medioz.util.adapter.map

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.map.AddressDocument

class MapViewPagerAdapter(
    val itemClickListener : (AddressDocument) -> Unit
) : ListAdapter<AddressDocument, MapViewPagerAdapter.HouseViewHolder>(diffUtil) {

    inner class HouseViewHolder(
        private val view : View
    ) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind (houseModel: AddressDocument) {
            val titleTextView = view.findViewById<TextView>(R.id.tv_map_title)
            val addressTextView = view.findViewById<TextView>(R.id.tv_map_address)
            val callTextView = view.findViewById<TextView>(R.id.tv_map_call)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)

            titleTextView.text = houseModel.place_name
            addressTextView.text = houseModel.address_name
            callTextView.text = houseModel.call

            Glide.with(thumbnailImageView.context)
                .load(houseModel.imgURL)
                .into(thumbnailImageView)

            view.setOnClickListener { itemClickListener(houseModel) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HouseViewHolder(inflater.inflate(R.layout.view_item_map_view_pager, parent, false))
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<AddressDocument>() {
            override fun areItemsTheSame(oldItem: AddressDocument, newItem: AddressDocument)
                    = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AddressDocument, newItem: AddressDocument)
                    = oldItem == newItem
        }
    }
}