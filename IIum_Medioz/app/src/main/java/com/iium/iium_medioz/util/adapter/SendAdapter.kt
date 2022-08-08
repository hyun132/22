package com.iium.iium_medioz.util.adapter

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
import com.iium.iium_medioz.model.send.SendList
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_DEFAULT
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_ID
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_SEND_CODE
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_SENSITIVITY
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_VIDEO
import com.iium.iium_medioz.view.main.bottom.data.send.SendDetailActivity

class SendAdapter(private val datalist: List<SendList>, val context: Context)
    : RecyclerView.Adapter<SendAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_send_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position], context)
    }

    override fun getItemCount(): Int {
        return datalist.count()
    }

    inner class ViewHolder (itemView: View? ) : RecyclerView.ViewHolder(itemView!!){

        val title = itemView?.findViewById<TextView>(R.id.tv_list_title)
        val keyword = itemView?.findViewById<TextView>(R.id.tv_data_keyword)
        val timestamp = itemView?.findViewById<TextView>(R.id.tv_data_data)
        val cl_body = itemView?.findViewById<ConstraintLayout>(R.id.cl_data_nor_list)

        fun bind(itemData: SendList, context: Context){
            title?.text = itemData.title.toString()
            keyword?.text = itemData.keyword.toString()
            timestamp?.text = itemData.timestamp.toString()

            cl_body?.setOnClickListener {
                val intent = Intent(context, SendDetailActivity::class.java)
                intent.putExtra(SEND_DETAIL_TITLE, itemData.title.toString())
                intent.putExtra(SEND_DETAIL_KEYWORD, itemData.keyword.toString())
                intent.putExtra(SEND_DETAIL_TIME_STAMP, itemData.timestamp.toString())
                intent.putExtra(SEND_DETAIL_TEXTIMG, itemData.textlist.toString())
                intent.putExtra(SEND_DETAIL_NORMAL, itemData.normallist.toString())
                intent.putExtra(SEND_DETAIL_VIDEO, itemData.videolist.toString())
                intent.putExtra(SEND_DETAIL_DEFAULT, itemData.defaultcode.toString())
                intent.putExtra(SEND_DETAIL_SENSITIVITY, itemData.sensitivity.toString())
                intent.putExtra(SEND_DETAIL_SEND_CODE, itemData.sendcode.toString())
                intent.putExtra(SEND_DETAIL_ID, itemData.id.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }

    }
}