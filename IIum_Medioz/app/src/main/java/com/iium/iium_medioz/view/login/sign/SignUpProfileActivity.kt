package com.iium.iium_medioz.view.login.sign

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySignUpProfileBinding
import com.iium.iium_medioz.model.rest.login.LoginResult
import com.iium.iium_medioz.util.`object`.Constant.ALBUM_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.LOGIN_PHONE
import com.iium.iium_medioz.util.`object`.Constant.LOGIN_SEX
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.file.FileUtil
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList

class SignUpProfileActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySignUpProfileBinding
    private lateinit var apiServices: APIService
    private val fileUtil = FileUtil()
    private var files4: MutableList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_profile)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    fun onProfile(v: View?) {
        requestPermission()
    }

    private fun requestPermission() {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ONE_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlbum()
            } else {
                Toast.makeText(this, "사진권한 동의를 해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent,ALBUM_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( resultCode == Activity.RESULT_OK) {
            if (requestCode == ALBUM_REQUEST_CODE) {
                try {
                    val img = data?.data
                    mBinding.imgPake.setImageURI(img)
                    val imgPath = img.let {
                        fileUtil.getPath(this, it!!)
                    }

                    files4.add(Uri.parse(imgPath))
                    if (imgPath != null) {
                        Log.e("image", imgPath)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun onOkClick(v: View) {
        val nickname = mBinding.etNickname.text.toString()
        when {
            nickname.isEmpty() -> {
                mBinding.etNickname.error = "미입력"
            }
            else -> signAPI()
        }
    }

    private fun signAPI() {
        val sex = intent.getStringExtra(LOGIN_SEX)
        val phone = intent.getStringExtra(LOGIN_PHONE)
        val nickname = mBinding.etNickname.text.toString()
        val profile: MutableList<MultipartBody.Part?> =  ArrayList()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        for (uri:Uri in files4) {
            uri.path?.let { Log.i("profile", it) }
            profile.add(prepareFilePart("profile", uri))
        }
        requestHashMap["phone"] = phone!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sex"] = sex!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["name"] = nickname.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.e("회원가입 API")
        apiServices.getSignUp(profile,requestHashMap).enqueue(object :
            Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getSignUp API SUCCESS -> $result")
                    moveMain()
                }
                else {
                    Log.d(TAG,"getSignUp API ERROR -> ${response.errorBody()}")
                    ErrorDialog()
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                Log.d(TAG,"getSignUp Fail -> $t")
                ErrorDialog()
            }
        })


    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath)
        val requestFile: RequestBody = file
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveSignup()
    }
}