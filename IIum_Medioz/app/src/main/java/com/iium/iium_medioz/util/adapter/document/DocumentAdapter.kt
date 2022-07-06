package com.iium.iium_medioz.util.adapter.document

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.document.DocumentList

class DocumentAdapter (private val datalist : List<DocumentList>, val context: Context)
    : RecyclerView.Adapter<DocumentAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_document, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position], context)
    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val title = itemView?.findViewById<TextView>(R.id.tv_map_title)
        val address = itemView?.findViewById<TextView>(R.id.tv_do_address)
        val call = itemView?.findViewById<TextView>(R.id.tv_do_call)
        val timestamp = itemView?.findViewById<TextView>(R.id.tv_do_timestamp)
        val cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_do)
        val thumbnailImageView = itemView?.findViewById<ImageView>(R.id.tv_do_img)


        fun bind(itemData: DocumentList, context: Context){
            title?.text = itemData.doname.toString()
            address?.text = itemData.address.toString()
            call?.text = itemData.call.toString()
            timestamp?.text = itemData.timestamp.toString()

            Glide.with(thumbnailImageView!!.context)
                .load(itemData.imgUrl)
                .transform(CenterCrop(), RoundedCorners(dpToPx(thumbnailImageView.context,10)))
                .into(thumbnailImageView)
        }

    }

    private fun dpToPx (context : Context, dp : Int) : Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }

}