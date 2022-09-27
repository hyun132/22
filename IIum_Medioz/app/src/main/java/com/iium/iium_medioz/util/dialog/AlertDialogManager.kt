package com.iium.iium_medioz.util.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.view.View


class AlertDialogManager {

    private var context: Activity? = null
    private var currentDialog: CustomDialog? = null
    private var rentGuideDialog: Dialog? = null

    @SuppressLint("NotConstructor")
    fun AlertDialogManager(context: Activity?) {
        this.context = context
    }

    fun showDialog(text: CharSequence?, positive: View.OnClickListener?): CustomDialog? {
        if (context!!.isFinishing) {
            return null
        }
        if (currentDialog != null && currentDialog!!.getDialog()!!.isShowing) {
            return null
        }
        currentDialog = CustomDialog()
        currentDialog!!.text(text)
            ?.positive(positive!!)
            ?.cancelable(false)
            ?.show()
        return currentDialog
    }

    fun showDialog(
        text: CharSequence?,
        positive: View.OnClickListener?,
        negative: View.OnClickListener?
    ): CustomDialog? {
        if (context!!.isFinishing) {
            return null
        }
        if (currentDialog != null && currentDialog!!.getDialog()!!.isShowing) {
            return null
        }
        currentDialog = CustomDialog()
        currentDialog!!.text(text)
            ?.positive(positive!!)
            ?.negative(negative!!)
            ?.cancelable(false)
            ?.show()
        return currentDialog
    }

    fun showDialog(
        text: CharSequence?,
        positiveText: String?,
        positive: View.OnClickListener?,
        negativeText: String?,
        negative: View.OnClickListener?
    ): CustomDialog? {
        if (context!!.isFinishing) {
            return null
        }
        if (currentDialog != null && currentDialog!!.getDialog()!!.isShowing) {
            return null
        }
        currentDialog = CustomDialog()
        currentDialog!!.text(text)
            ?.positive(positiveText!!, positive!!)
            ?.negative(negativeText!!, negative!!)
            ?.cancelable(false)
            ?.show()
        return currentDialog
    }

    fun showHtmlDialog(
        html: String?,
        positive: View.OnClickListener?,
        negative: View.OnClickListener?
    ): CustomDialog? {
        if (context!!.isFinishing) {
            return null
        }
        if (currentDialog != null && currentDialog!!.getDialog()!!.isShowing) {
            return null
        }
        currentDialog = CustomDialog()
        currentDialog!!.htmlText(html)
            ?.positive(positive!!)
            ?.negative(negative!!)
            ?.cancelable(false)
            ?.show()
        return currentDialog
    }

    fun dismiss() {
        if (currentDialog != null) {
            currentDialog!!.dismiss()
        }
    }

}