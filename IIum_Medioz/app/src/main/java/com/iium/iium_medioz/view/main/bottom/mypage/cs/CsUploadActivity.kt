package com.iium.iium_medioz.view.main.bottom.mypage.cs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCsBinding
import com.iium.iium_medioz.databinding.ActivityCsUploadBinding
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.adapter.cs.CounSelingAdapter
import com.iium.iium_medioz.util.adapter.cs.CsAlbumAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.file.FileUtil
import com.iium.iium_medioz.util.log.LLog.TAG
import java.lang.NullPointerException
import java.time.LocalDate

class CsUploadActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCsUploadBinding
    private lateinit var apiServices: APIService
    private var files4 :MutableList<Uri> = ArrayList()
    private val csAlbumadapter = CsAlbumAdapter(files4, this)
    private val fileUtil = FileUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cs_upload)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvCsTime.text = onlyDate.toString()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.reCsAlbum.layoutManager = layoutManager
        mBinding.reCsAlbum.adapter = csAlbumadapter
        mBinding.reCsAlbum.setHasFixedSize(true)

    }

    fun onBackPressed(v: View) {
        moveCs()
    }

    fun onCsAlbumClick(v: View) {
        getAlbumPermission()
    }

    private fun getAlbumPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getAlbum()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"사진 접근 권한 동의를 해주세요", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),ONE_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 200)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == 200 ) {
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count > 5) {
                    Toast.makeText(this,"사진은 5장까지 선택 가능합니다.",Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    for( i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        val imgPath = imageUri.let {
                            fileUtil.getPath(this, it!!)
                        }
                        files4.add(Uri.parse(imgPath))
                        Log.d(TAG,"upload TextIMG -> $imgPath")
                    }
                }
                catch (e : NullPointerException) {
                    e.printStackTrace()
                }
            }
            else {
                val img = data?.data
                img.let {
                    val imageUri : Uri? = img
                    fileUtil.getPath(this, it!!)
                    if(imageUri != null) {
                        files4.add(Uri.parse(img.toString()))
                        Log.d(TAG,"upload TextIMG One -> $img")
                    }
                }
            }
            csAlbumadapter.notifyDataSetChanged()
        }
    }

    fun onCsSendClick(v: View) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveCs()
    }
}