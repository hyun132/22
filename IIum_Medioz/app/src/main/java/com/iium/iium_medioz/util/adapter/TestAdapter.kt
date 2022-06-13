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
import com.iium.iium_medioz.model.recycler.*
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DATA_TEXTIMG
import com.iium.iium_medioz.view.main.bottom.data.DataDetyailActivity


class TestAdapter (private val datalist : List<DataList>, val context: Context)
    : RecyclerView.Adapter<TestAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_test_item, parent, false)
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
        val cl_body = itemView?.findViewById<ConstraintLayout>(R.id.cl_data_body)

        fun bind(itemData: DataList, context: Context){
            title?.text = itemData.title.toString()
            keyword?.text = itemData.keyword.toString()
            timestamp?.text = itemData.timestamp.toString()

            val textlist = itemData.DataList?.map {  it ->
                it.textImg.map {
                    it.filename
                }
            }

            cl_body?.setOnClickListener {
                val intent = Intent(context, DataDetyailActivity::class.java)
                intent.putExtra(Constant.DATA_TITLE, itemData.title.toString())
                intent.putExtra(Constant.DATA_KEYWORD, itemData.keyword.toString())
                intent.putExtra(Constant.DATA_TIMESTAMP, itemData.timestamp.toString())
                intent.putExtra(DATA_TEXTIMG, textlist.toString())

                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }

    }
}