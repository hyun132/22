package com.iium.iium_medioz.util.adapter.map

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
import com.iium.iium_medioz.model.map.MapModel

class MapViewPagerAdapter(
    val itemClickListener : (MapModel) -> Unit
) : ListAdapter<MapModel, MapViewPagerAdapter.HouseViewHolder>(diffUtil) {

    inner class HouseViewHolder(
        private val view : View
    ) : RecyclerView.ViewHolder(view) {

        fun bind (houseModel: MapModel) {
            val titleTextView = view.findViewById<TextView>(R.id.tv_map_title)
            val addressTextView = view.findViewById<TextView>(R.id.tv_map_address)
            val callTextView = view.findViewById<TextView>(R.id.tv_map_call)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)

            titleTextView.text = houseModel.name
            addressTextView.text = houseModel.address
            callTextView.text = houseModel.call

            Glide.with(thumbnailImageView.context)
                .load(houseModel.imgUrl)
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
        val diffUtil = object : DiffUtil.ItemCallback<MapModel>() {
            override fun areItemsTheSame(oldItem: MapModel, newItem: MapModel)
                    = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MapModel, newItem: MapModel)
                    = oldItem == newItem
        }
    }
}