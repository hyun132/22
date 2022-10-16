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
import com.iium.iium_medioz.model.rest.base.DataList
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_ID
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEARCH_VIDEO
import com.iium.iium_medioz.util.common.setOnDebounceClickListener
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.data.search.SearchDetailActivity

class SearchAdapter(private val searchlist : List<DataList>, private val context: Context)
    : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_search_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        LLog.d("holder : $holder context :$context")
        holder.bind(searchlist[position], context)

    }

    override fun getItemCount(): Int {
        return searchlist.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val search_title = itemView?.findViewById<TextView>(R.id.tv_search_title)
        val search_keyword = itemView?.findViewById<TextView>(R.id.tv_search_keyword)
        val search_data = itemView?.findViewById<TextView>(R.id.tv_search_data)
        val search_cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_search_body)

        fun bind(list: DataList?, context: Context) {
            search_title?.text = list?.title.toString()
            search_keyword?.text = list?.keyword.toString()
            search_data?.text = list?.timestamp.toString()

            val textlist = list?.DataList?.map {  it ->
                if (!it.textImg.isNullOrEmpty()){
                    it.textImg.map {
                        it.filename
                    }
                }
            }

            val normallist = list?.DataList?.map { it ->
                if (!it.Img.isNullOrEmpty()){
                    it.Img.map {
                        it.filename
                    }
                }
            }

//            val video = list?.DataList?.map { it ->
//                it.video.map {
//                    it.filename
//                }
//            }

            search_cl.setOnDebounceClickListener {
                val intent = Intent(context, SearchDetailActivity::class.java)
                intent.putExtra(SEARCH_TITLE, list?.title.toString())
                intent.putExtra(SEARCH_KEYWORD, list?.keyword.toString())
                intent.putExtra(SEARCH_TIME_STAMP, list?.timestamp.toString())
                intent.putExtra(SEARCH_TEXTIMG, textlist.toString())
                intent.putExtra(SEARCH_NORMAL, normallist.toString())
//                intent.putExtra(SEARCH_VIDEO, video.toString())
                intent.putExtra(SEARCH_ID, list?.id.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}