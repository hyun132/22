package com.iium.iium_medioz.util.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.iium.iium_medioz.R
import com.iium.iium_medioz.view.main.MainActivity
import com.iium.iium_medioz.view.main.bottom.data.SaveActivity
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BasePerMissionActivity <B: ViewDataBinding>(@LayoutRes private val layoutResId: Int) : AppCompatActivity() {
    protected lateinit var binding: B
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    open fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    internal fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    internal fun moveSave() {
        val intent = Intent(this, SaveActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    @SuppressLint("ResourceType")
    internal fun serverDialog() {
        val myLayout = layoutInflater.inflate(R.layout.one_button_dialog, null)
        val build = AlertDialog.Builder(this).apply {
            setView(myLayout)
        }
        val textView : TextView = myLayout.findViewById(R.id.popTv)
        textView.text = getString(R.string.server_check)
        val dialog = build.create()
        dialog.show()

        myLayout.ok_btn.setOnClickListener {
            dialog.dismiss()
        }

    }
}