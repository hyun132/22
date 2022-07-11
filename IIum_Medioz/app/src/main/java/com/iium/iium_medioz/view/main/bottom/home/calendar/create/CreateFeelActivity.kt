package com.iium.iium_medioz.view.main.bottom.home.calendar.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCalendarBinding
import com.iium.iium_medioz.databinding.ActivityCreateFeelBinding
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.BaseActivity.Companion.setWindowFlag

class CreateFeelActivity : BaseActivity() {

    private lateinit var mBinding : ActivityCreateFeelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_feel)
        mBinding.activity = this
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }
}