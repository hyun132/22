package com.iium.iium_medioz.util.adapter.map

import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.Documents
import kotlinx.android.synthetic.main.item_search.view.*

class HospitalAdapter() : RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {

    private var list = mutableListOf<AddressDocument>()
    private var listener: ((String, String) -> Unit)? = null
    lateinit var key: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    fun submitList(list: List<AddressDocument>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    fun submitVariable(key: String, latitude: Double, longitude: Double) {
        this.key = key
        this.latitude = latitude
        this.longitude = longitude
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, null, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(
            list[position].address_name!!,
            "${list[position].region_1depth_name} ${list[position].region_2depth_name} ${list[position].region_3depth_name} ",
            list[position].x!!,
            list[position].y!!
        )
    }

    fun setOnItemClickListener(listener: ((String, String) -> Unit)?) {
        this.listener = listener
    }

    fun findIndexes(word: String, document: String): MutableList<Int> {
        val indexList = mutableListOf<Int>()
        var index: Int = document.indexOf(word)

        while (index != -1) {
            indexList.add(index.toByte().toInt())
            index = document.indexOf(word, index + word.length)

        }
        return indexList
    }

    // 좌표로 거리구하기
    fun getDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): String {
        val myLoc = Location(LocationManager.NETWORK_PROVIDER)
        val targetLoc = Location(LocationManager.NETWORK_PROVIDER)
        myLoc.latitude = lat1
        myLoc.longitude = lng1

        targetLoc.latitude = lat2
        targetLoc.longitude = lng2

        return String.format("%.1f", myLoc.distanceTo(targetLoc) / 1000)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(text_ad: String, text_pl: String, x: String, y: String) {
            var builder = SpannableStringBuilder(text_ad)

            for (i in 0 ..findIndexes(key, text_ad).size -1) {
                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    findIndexes(key, text_ad)[i],
                    findIndexes(key, text_ad)[i] + key.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
                builder.setSpan(
                    ForegroundColorSpan(R.color.red_temp),
                    findIndexes(key, text_ad)[i],
                    findIndexes(key, text_ad)[i] + key.length,
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE
                )
            }

            itemView.tv_address_name.text = builder

            builder = SpannableStringBuilder(text_pl)

            for (i in 0 ..findIndexes(key, text_pl).size -1) {
                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    findIndexes(key, text_pl)[i],
                    findIndexes(key, text_pl)[i] + key.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                )
                builder.setSpan(
                    ForegroundColorSpan(R.color.red_temp),
                    findIndexes(key, text_pl)[i],
                    findIndexes(key, text_pl)[i] + key.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            itemView.tv_place_name.text = builder

            itemView.tv_distance.text =
                getDistance(latitude, longitude, y.toDouble(), x.toDouble()) + "km"

            itemView.setOnClickListener {
                listener?.invoke(x, y)
            }
        }
    }
}