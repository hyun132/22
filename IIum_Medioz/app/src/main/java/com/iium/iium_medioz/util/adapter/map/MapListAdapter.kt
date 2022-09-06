package com.iium.iium_medioz.util.adapter.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.map.AddressDocument


class MapListAdapter(val itemClickListener : (AddressDocument) -> Unit) : ListAdapter<AddressDocument, MapListAdapter.HouseViewHolder>(diffUtil) {

    inner class HouseViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind (houseModel: AddressDocument) {
            val titleTextView = view.findViewById<TextView>(R.id.tv_map_title)
            val addressTextView = view.findViewById<TextView>(R.id.tv_map_address)
            val callTextView = view.findViewById<TextView>(R.id.tv_map_call)
            val thumbnailImageView = view.findViewById<ImageView>(R.id.thumbnailImageView)
            val btn = view.findViewById<AppCompatButton>(R.id.btn_map_ok)

            titleTextView.text = houseModel.address_name
            addressTextView.text = "${houseModel.region_1depth_name} ${houseModel.region_2depth_name} ${houseModel.region_3depth_name} ${houseModel.region_4depth_name}"
            callTextView.text = houseModel.call

            Glide.with(thumbnailImageView.context)
                .load(houseModel.imgURL)
                .transform(CenterCrop(), RoundedCorners(dpToPx(thumbnailImageView.context,30)))
                .into(thumbnailImageView)

            btn.setOnClickListener { itemClickListener(houseModel) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HouseViewHolder(inflater.inflate(R.layout.view_item_map_list, parent, false))
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    private fun dpToPx (context : Context, dp : Int) : Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
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