package com.iium.iium_medioz.util.adapter.map

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
import com.iium.iium_medioz.model.map.KaKaoDocuments
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.AddressActivity

class KaKaoLocalAdapter (private val datalist : List<KaKaoDocuments>, val context: Context)
    : RecyclerView.Adapter<KaKaoLocalAdapter.ViewHolder>(){
    private var list = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_item_kakao_search, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position],context)    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!) {

        val title = itemView?.findViewById<TextView>(R.id.tv_search_title_detail)
        val name = itemView?.findViewById<TextView>(R.id.tv_search_address_detail)
        val phone = itemView?.findViewById<TextView>(R.id.tv_search_phone_detail)
        private val cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_search)

        fun bind(itemData: KaKaoDocuments, context: Context) {
            title?.text = itemData.address_name.toString()

            cl?.setOnClickListener {
                val intent = Intent(context, AddressActivity::class.java)
                intent.putExtra(Constant.KAKAO_MAPX, itemData.x)
                intent.putExtra(Constant.KAKAO_MAPY, itemData.y)
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}