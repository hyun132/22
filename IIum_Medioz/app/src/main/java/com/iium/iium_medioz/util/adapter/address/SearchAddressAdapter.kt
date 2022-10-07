package com.iium.iium_medioz.util.adapter.address

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
import com.iium.iium_medioz.model.map.Items
import com.iium.iium_medioz.model.map.NaverSearchModel
import com.iium.iium_medioz.model.recycler.DataList
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity

class SearchAddressAdapter (private val datalist : List<Items>, val context: Context)
    : RecyclerView.Adapter<SearchAddressAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_search_address, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position],context)    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val title = itemView?.findViewById<TextView>(R.id.tv_search_title_detail)
        val address = itemView?.findViewById<TextView>(R.id.tv_search_address_detail)
        val phone = itemView?.findViewById<TextView>(R.id.tv_search_phone_detail)
        val cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_search)

        fun bind(itemData: Items, context: Context){
            val title_detail_first = itemData.title?.replace("<b>","")
            val title_substring = title_detail_first?.replace("</b>","")

            title?.text = title_substring.toString()
            address?.text = itemData.address.toString()
            phone?.text = itemData.telephone.toString()

            cl?.setOnClickListener {
                val intent = Intent(context, HospitalActivity::class.java)
                intent.putExtra(Constant.NAVER_MAPX, itemData.mapx)
                intent.putExtra(Constant.NAVER_MAPY, itemData.mapy)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }

    }
}