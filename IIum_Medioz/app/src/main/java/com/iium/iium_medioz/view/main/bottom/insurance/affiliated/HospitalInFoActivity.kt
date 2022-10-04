package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityHospitalInFoBinding
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_WEBSITE
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity


class HospitalInFoActivity : BaseActivity() {

    private lateinit var mBinding : ActivityHospitalInFoBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_in_fo)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            initView()
            inStatusBar()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun initView() {
        val name = intent.getStringExtra(DOCUMENT_NAME)
        val address = intent.getStringExtra(DOCUMENT_ADDRESS)
        val call = intent.getStringExtra(DOCUMENT_CALL)
        val image = intent.getStringExtra(DOCUMENT_IMGURL)
        val web = intent.getStringExtra(DOCUMENT_WEBSITE)
        val image_banner = mBinding.ivBannerImage

        mBinding.tvInfoPlaceName.text = name
        mBinding.tvInfoAddressName.text = address
        mBinding.tvInfoCall.text = call
        mBinding.tvInfoWeb.text = web
        Glide.with(image_banner.context)
            .load(image)
            .transform(CenterCrop())
            .into(image_banner)

        val call_text =  mBinding.tvInfoCall.text.toString()
        val tel = "tel:${call_text}"

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(tel))
                startActivity(intent)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@HospitalInFoActivity,"전화 연결 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        mBinding.tvInfoCall.setOnClickListener {
            TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("[설정] 에서 권한을 열어줘야 전화 연결이 가능합니다.")
                .setPermissions(Manifest.permission.CALL_PHONE)
                .check()
        }

        mBinding.tvInfoWeb.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(web))
            startActivity(intent)
        }
    }

    fun onSendClick(v: View) {

    }

}