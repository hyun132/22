package com.iium.iium_medioz.view.login.sign

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySignUpBinding
import com.iium.iium_medioz.util.`object`.Constant.LOGIN_PHONE
import com.iium.iium_medioz.util.`object`.Constant.LOGIN_SEX
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.log.LLog.TAG
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySignUpBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
        changeImg()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    private fun changeImg() {

        mBinding.radioGroup.setOnCheckedChangeListener { group, i ->
            when(i) {
                R.id.radio_man -> tv_test_gender.text = "남자"
                R.id.radio_girl -> tv_test_gender.text = "여자"
            }
            when(i) {
                R.id.radio_girl -> mBinding.imgGirl.setImageResource(R.drawable.gril_change)
                else -> mBinding.imgGirl.setImageResource(R.drawable.gril_default)
            }
            when(i) {
                R.id.radio_man -> mBinding.imgMan.setImageResource(R.drawable.man_change)
                else -> mBinding.imgMan.setImageResource(R.drawable.man_default)
            }
        }
    }

    fun onOkClick(v: View?) {
        val phone = intent.getStringExtra(LOGIN_PHONE)
        Log.d(TAG,"핸드폰 번호 -> $phone")
        val gender = mBinding.tvTestGender.text.toString()
        val intent = Intent(this, SignUpProfileActivity::class.java)
        intent.putExtra(LOGIN_SEX, gender)
        intent.putExtra(LOGIN_PHONE, phone)
        startActivity(intent)
    }
}