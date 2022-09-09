package com.iium.iium_medioz.util.adapter.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.AddressActivity

class AddressListAdapter(private val datalist: List<AddressDocument>, val context: Context)
    : RecyclerView.Adapter<AddressListAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.search_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position],context)    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val name = itemView?.findViewById<TextView>(R.id.resultText)
        val cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_address_search)

        @SuppressLint("SetTextI18n")
        fun bind(itemData: AddressDocument, context: Context){
            name?.text = "${itemData.region_1depth_name} ${itemData.region_2depth_name} ${itemData.region_3depth_name} ${itemData.region_4depth_name}"

            cl?.setOnClickListener {
                val intent = Intent(context, AddressActivity::class.java)
                intent.putExtra(Constant.KAKAO_MAPX, itemData.x.toString())
                intent.putExtra(Constant.KAKAO_MAPY, itemData.y.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }

    }
}