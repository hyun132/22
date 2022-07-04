package com.iium.iium_medioz.util.adapter.band

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
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.view.main.bottom.data.search.SearchDetailActivity

class BandSearchAdapter (private val searchlist : List<DataList>, private val context: Context)
    : RecyclerView.Adapter<BandSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BandSearchAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.view_search_item, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: BandSearchAdapter.ViewHolder, position: Int) {
        holder.bind(searchlist[position], context)

    }

    override fun getItemCount(): Int {
        return searchlist.count()
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val search_title = itemView?.findViewById<TextView>(R.id.tv_search_title)
        val search_keyword = itemView?.findViewById<TextView>(R.id.tv_search_keyword)
        val search_data = itemView?.findViewById<TextView>(R.id.tv_search_data)
        val search_cl = itemView?.findViewById<ConstraintLayout>(R.id.cl_search_body)

        fun bind(list: DataList?, context: Context) {
            search_title?.text = list?.title.toString()
            search_keyword?.text = list?.keyword.toString()
            search_data?.text = list?.timestamp.toString()

            val textlist = list?.DataList?.map {  it ->
                it.textImg.map {
                    it.filename
                }
            }

            val normallist = list?.DataList?.map { it ->
                it.Img.map {
                    it.filename
                }
            }

//            val video = list?.DataList?.map { it ->
//                it.video.map {
//                    it.filename
//                }
//            }

            search_cl?.setOnClickListener {
                val intent = Intent(context, SearchDetailActivity::class.java)
                intent.putExtra(Constant.SEARCH_TITLE, list?.title.toString())
                intent.putExtra(Constant.SEARCH_KEYWORD, list?.keyword.toString())
                intent.putExtra(Constant.SEARCH_TIME_STAMP, list?.timestamp.toString())
                intent.putExtra(Constant.SEARCH_TEXTIMG, textlist.toString())
                intent.putExtra(Constant.SEARCH_NORMAL, normallist.toString())
//                intent.putExtra(SEARCH_VIDEO, video.toString())
                intent.putExtra(Constant.SEARCH_ID, list?.id.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }
}