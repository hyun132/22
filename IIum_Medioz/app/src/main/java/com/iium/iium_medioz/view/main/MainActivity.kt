package com.iium.iium_medioz.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.view.main.bottom.data.DataFragment
import com.iium.iium_medioz.view.main.bottom.feel.FeelFragment
import com.iium.iium_medioz.view.main.bottom.home.HomeFragment
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview


@FlowPreview
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMainBinding
    private lateinit var apiServices: APIService
    private var doubleBackToExit = false

    private val fragmentOne by lazy { HomeFragment() }
    private val fragmentTwo by lazy { DataFragment() }
    private val fragmentThree by lazy { FeelFragment() }
    private val fragmentFour by lazy { HospitalFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction().replace(R.id.fl_container, HomeFragment()).commit()     //초기 fragment 세팅
        mBinding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener{
                when(it.itemId) {
                    R.id.nav_home -> {
                        changeFragment(fragmentOne)
                    }
                    R.id.nav_data -> {
                        changeFragment(fragmentTwo)
                    }
                    R.id.nav_band -> {
                        changeFragment(fragmentThree)
                    }
                    R.id.nav_Insurance -> {
                        changeFragment(fragmentFour)
                    }
                }
                true

            }
            selectedItemId = R.id.nav_home
        }
//        transaction.addToBackStack(null)
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//        transaction.commit()
        return
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    fun onUploadClick(v: View) {
        moveUpload()
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }

    private fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

    companion object {
        const val EXTRA_EDIT = "edit"

        fun newIntent(context: Context, edit: Boolean = false): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_EDIT, edit)
            }
        }
    }

}