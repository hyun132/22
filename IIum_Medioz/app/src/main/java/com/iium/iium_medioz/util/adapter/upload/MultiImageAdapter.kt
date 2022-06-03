package com.iium.iium_medioz.util.adapter.upload

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.R
import com.iium.iium_medioz.util.activity.GlideApp
import com.iium.iium_medioz.util.log.LLog.TAG

class MultiImageAdapter(private val items: MutableList<Uri> = ArrayList(), val context: Context) :
    RecyclerView.Adapter<MultiImageAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(items[position],context)
        try {
            GlideApp
                .with(context)
                .load(item.toString())
                .into(holder.image)
            Log.d(TAG,"Glide -> $item")
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.view_photo_item,parent,false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var image = v.findViewById<ImageView>(R.id.image)

        fun bind(listener: Uri, item: Context) {
        }
    }
}