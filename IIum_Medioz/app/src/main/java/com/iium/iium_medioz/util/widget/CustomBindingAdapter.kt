package com.iium.iium_medioz.util.widget

import android.graphics.Bitmap
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.google.android.exoplayer2.ui.PlayerView
import com.iium.iium_medioz.R
import java.text.DecimalFormat


@BindingAdapter("bind_dowload_count", "bind_security_accident_count")
fun bindWelcomeSubtitle(view: TextView, downloadCount: Int, securityAccidentCount: Int) {
    val formatter = DecimalFormat("#,##0")

    val colorTagStart = "<font color=\"#3182f7\">"
    val colorTagEnd = "</font>"

    val downloadCountText = colorTagStart + formatter.format(downloadCount) + "만" + colorTagEnd
    val securityAccidentCountText =
        colorTagStart + formatter.format(securityAccidentCount) + "건" + colorTagEnd

    val subTitle =
        view.resources.getString(R.string.welcome_toss_is_safe) + "<br>" + view.resources.getString(
            R.string.welcome_boast,
            downloadCountText,
            securityAccidentCountText
        )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        view.text = Html.fromHtml(subTitle, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        view.text = Html.fromHtml(subTitle)
    }
}

/**
 * 추가
 */
@BindingAdapter("backgroundSrc")
fun bindBackgroundImg(layout: ViewGroup, background: Int) {
    layout.setBackgroundResource(background)
}

/**
 * 추가
 */
@BindingAdapter("bindImgResource")
fun bindImgResource(layout: ImageView, rsc: Int?) {
    if (rsc != null) {
        layout.setImageResource(rsc)
    }
}

/**
 * 추가
 */
@BindingAdapter("bindImgResource")
fun bindImgResource(view: ImageView, rsc: Bitmap?) {
    if (rsc != null) {
        view.setImageBitmap(rsc)
    }
}

/**
 * 추가
 */
@BindingAdapter("bindImgVisibility")
fun bindImgVisibility(view: ImageView, rsc: Bitmap?) {
    view.visibility = if (rsc == null) {
        View.GONE
    } else View.VISIBLE
}

/**
 * 추가
 */
@BindingAdapter("bindImgVisibility")
fun bindImgVisibility(view: PlayerView, rsc: Bitmap?) {
    view.visibility = if (rsc != null) {
        View.GONE
    } else View.VISIBLE
}

/**
 * 추가
 */
@BindingAdapter("textByForce")
fun setText(view: TextView, text: CharSequence) {
    if (TextUtils.isEmpty(text)) {
        return
    }
    view.text = text
}

//@InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
//fun getTextString(view: TextView): String? {
//    return view.text.toString()
//}