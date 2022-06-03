package com.iium.iium_medioz.view.login.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySignUpProfileBinding
import com.iium.iium_medioz.databinding.ActivitySignUpPwBinding
import com.iium.iium_medioz.util.base.BaseActivity

class SignUpPwActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySignUpPwBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_pw)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
    }
}