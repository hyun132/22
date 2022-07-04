package com.iium.iium_medioz.util.adapter.band

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.band.BandModeling

class BandRecyclerAdapter(private val counseling : List<BandModeling>,val context: Context)
    : RecyclerView.Adapter<BandRecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BandRecyclerAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_band_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: BandRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(counseling[position],context)
    }

    override fun getItemCount(): Int {
        return counseling.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val band_title = itemView?.findViewById<TextView>(R.id.tv_band_title)
        val band_nickname = itemView?.findViewById<TextView>(R.id.tv_band_nick_name)
        val band_keyword = itemView?.findViewById<TextView>(R.id.tv_band_keyword)
        val band_timestamp = itemView?.findViewById<TextView>(R.id.tv_band_data)
        val band_inquiry = itemView?.findViewById<TextView>(R.id.tv_band_inquiry)
        val band_good = itemView?.findViewById<TextView>(R.id.tv_band_good)
        val band_text = itemView?.findViewById<TextView>(R.id.tv_band_text)

        val band_cl_detail = itemView?.findViewById<ConstraintLayout>(R.id.cl_band_list)

        fun bind(band: BandModeling, context: Context){
            band_title?.text = band.band_text.toString()
            band_nickname?.text = band.band_nickname.toString()
            band_keyword?.text = band.band_keyword.toString()
            band_timestamp?.text = band.band_timestamp.toString()
            band_inquiry?.text = band.band_inquiry.toString()
            band_good?.text = band.band_good.toString()
            band_text?.text = band.band_text.toString()

            band_cl_detail?.setOnClickListener {

            }
        }
    }

}