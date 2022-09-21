package com.iium.iium_medioz.view.main.bottom.data

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataUploadBinding
import com.iium.iium_medioz.model.upload.CreateMedical
import com.iium.iium_medioz.util.`object`.Constant.DEFAULT_CODE_TRUE
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SECOND_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SEND_CODE_FALSE
import com.iium.iium_medioz.util.`object`.Constant.THRID_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.adapter.upload.MultiImageAdapter
import com.iium.iium_medioz.util.adapter.upload.NormalImgAdapter
import com.iium.iium_medioz.util.adapter.upload.VideoRecyclerAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.common.setOnDebounceClickListener
import com.iium.iium_medioz.util.feel.show
import com.iium.iium_medioz.util.file.FileUtil
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import kotlin.concurrent.thread


class DataUploadActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDataUploadBinding
    private lateinit var apiServices: APIService

    private var files4 :MutableList<Uri> = ArrayList()      // 텍스트 이미지
    private var files5 :MutableList<Uri> = ArrayList()      // 일반 이미지
    private var files6 :MutableList<Uri> = ArrayList()      // 비디오

    private val textAdapter = MultiImageAdapter(files4, this)
    private val normalAdapter = NormalImgAdapter(files5, this)
    private val videoAdapter = VideoRecyclerAdapter(files6, this)

    private val fileUtil = FileUtil()
    private val listArray = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_upload)
        apiServices = ApiUtils.apiService
        mBinding.activity = this
        mBinding.lifecycleOwner = this
        inStatusBar()
        runOnUiThread {
            initView()
            initSecond()
            initThird()
            initKey()
        }
    }

    private fun initKey() {
        mBinding.btnSubscribe.setOnClickListener {
            createKeyWord()
        }
    }

    private fun createKeyWord() {
        val textView = TextView(applicationContext)
        val listView = mBinding.llKeyword
        val typeface = resources.getFont(R.font.noto_sans_kr_bold)

        textView.text = mBinding.etKeyword.text.toString()
        textView.textSize = 15f
        textView.typeface = typeface
        textView.id = ViewCompat.generateViewId()

        val param : LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        param.marginStart = 20
        textView.layoutParams = param
        listArray.add(textView.text.toString())

        if (listArray.count() == 6) {
            Toast.makeText(this,"키워드는 5개만 등록할 수 있습니다.",Toast.LENGTH_SHORT).show()
            listArray.clear()
            mBinding.tvRegisteredKeyword.text = listArray.count().toString()
            listView.removeAllViews()
        } else {
            listView.addView(textView)
            mBinding.clTextDelete.setOnClickListener {
                if(listArray.size == 0) {
                    Toast.makeText(this,"키워드를 등록해주세요",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    listArray.removeLastOrNull()
                    listView.removeViewAt(listArray.size -0)
                    mBinding.tvRegisteredKeyword.text = listArray.count().toString()
                }
            }
            mBinding.tvRegisteredKeyword.text = listArray.count().toString()
            mBinding.etKeyword.text = null
        }

    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun initView() {
        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvTodayData.text = onlyDate.toString()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.textRe.layoutManager = layoutManager
        mBinding.textRe.adapter = textAdapter
        mBinding.textRe.setHasFixedSize(true)
        mBinding.textRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

    }

    private fun initSecond() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.normalRe.layoutManager = layoutManager
        mBinding.normalRe.adapter = normalAdapter
        mBinding.normalRe.setHasFixedSize(true)
        mBinding.normalRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    private fun initThird() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.videoRe.layoutManager = layoutManager
        mBinding.videoRe.adapter = videoAdapter
        mBinding.videoRe.setHasFixedSize(true)
        mBinding.videoRe.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    fun onTextClick(v: View?) {
        getTextPerMission()
    }

    fun onNormalClick(v: View?) {
        getImgPerMission()
    }

    fun onVideoClick(v: View?) {
        geVideoPerMission()
    }

    /////////////////// 앨범 권한 설정 ///////////////////
    private fun getTextPerMission() {
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


    private fun getImgPerMission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getNormal()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"사진 접근 권한 동의를 해주세요", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),SECOND_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun geVideoPerMission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getVideo()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"사진 접근 권한 동의를 해주세요", Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),THRID_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ONE_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlbum()
            }
            SECOND_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getNormal()
            }
            THRID_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getVideo()
            }
            else {
                Toast.makeText(this, "사진권한 동의를 해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    private fun getAlbum() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 200)
    }

    private fun getNormal() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 300)
    }

    private fun getVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "video/*"
        startActivityForResult(intent, 400)
    }

    /////////////////// 앨범 사진 가져오기(limit : 5개) ///////////////////
    @Deprecated("Deprecated in Java")
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
            textAdapter.notifyDataSetChanged()
        }

        if(resultCode == RESULT_OK && requestCode == 300 ) {
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
                        files5.add(Uri.parse(imgPath))
                        Log.d(TAG,"upload IMG -> $imgPath")
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
                        files5.add(Uri.parse(img.toString()))
                        Log.d(TAG,"upload IMG One -> $img")
                    }
                }
            }
            normalAdapter.notifyDataSetChanged()
        }

        if(resultCode == RESULT_OK && requestCode == 400 ) {
            if(data?.clipData != null) {
                val count = data.clipData!!.itemCount
                if(count > 3) {
                    Toast.makeText(this,"영상은 3장까지 선택 가능합니다.",Toast.LENGTH_SHORT).show()
                    return
                }
                try {
                    for( i in 0 until count) {
                        val videoUri = data.clipData!!.getItemAt(i).uri
                        val videoPath = videoUri.let {
                            fileUtil.getPath(this, it!!)
                        }
                        files6.add(Uri.parse(videoPath))
                        Log.d(TAG,"upload video -> $videoPath")
                    }
                }
                catch (e : NullPointerException) {
                    e.printStackTrace()
                }
            }
            else {
                val video = data?.data
                video.let {
                    val videoUri : Uri? = video
                    fileUtil.getPath(this, it!!)
                    if(videoUri != null) {
                        files6.add(Uri.parse(video.toString()))
                        Log.d(TAG,"upload video One -> $video")
                    }
                }
            }
            videoAdapter.notifyDataSetChanged()
        }
    }

    fun onDataSendClick(v: View?) {
        val et_title = mBinding.etTitle.text.toString()
        when {
            et_title.isEmpty() -> {
                mBinding.etTitle.error = "미입력"
                Toast.makeText(this,"제목을 입력해 주세요",Toast.LENGTH_SHORT).show().toString()
            }
            else -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
                dlg.setTitle("데이터 등록") //제목
                dlg.setMessage("데이터를 등록 하시겠습니까?")
                dlg.setPositiveButton("확인") { dialog, which ->
                    callCreateAPI()
                    dialog.dismiss()
                }
                dlg.setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
                dlg.show()
            }
        }
    }


    /////////////////// API 호출 ///////////////////
    private fun callCreateAPI() {
        val textimg : MutableList<MultipartBody.Part?> = ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let {
                Log.i("textImg", it)
            }
            textimg.add(prepareFilePart("textImg", uri))
            Log.e("textImg", uri.toString())
        }

        for (imguri:Uri in files5) {
            imguri.path?.let {
                Log.i("Img", it)
            }
            textimg.add(prepareFilePart("Img", imguri))
            Log.e("Img", imguri.toString())
        }

        for (videouri:Uri in files6) {
            videouri.path?.let {
                Log.i("video", it)
            }
            textimg.add(prepareFilePart("video", videouri))
            Log.e("video", videouri.toString())
        }

        val title = mBinding.etTitle.text.toString()

        val keyword = listArray.toString()
        val timestamp = mBinding.tvTodayData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        val sendcode = SEND_CODE_FALSE
        val defaultcode = DEFAULT_CODE_TRUE
        val sensitivity = ""

        val picksize = files4.size
        val pick = 0
        val pickkk = when(picksize) {
            1 -> pick + 4
            2 -> pick + 8
            3 -> pick + 12
            4 -> pick + 16
            5 -> pick + 20
            else -> pick + 0
        }
        val pickscore = pickkk.toString()

        val videosize = files6.size
        val video = 0
        val videooo = when(videosize) {
            1 -> video + 4
            2 -> video + 8
            3 -> video + 12
            4 -> video + 16
            5 -> video + 20
            else -> video + 0
        }
        val videoscore = videooo.toString()

        val keyscore = 0
        val key = listArray.count()
        val keyy = when(key) {
            1 -> keyscore + 4
            2 -> keyscore + 8
            3 -> keyscore + 12
            4 -> keyscore + 16
            5 -> keyscore + 20
            else -> keyscore + 0
        }
        val keywordscore = keyy.toString()

        val pickk : Int = pickscore.toInt()
        val vidd : Int = videoscore.toInt()
        val kkk : Int = keywordscore.toInt()

        val all = pickk + vidd + kkk
        val allscore : String = all.toString()

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] = timestamp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] = sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] = defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] = sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["pickscore"] = pickscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["videoscore"] = videoscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keywordscore"] = keywordscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["allscore"] = allscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("데이터 업로드_두번째 API")
        apiServices.getCreate(prefs.newaccesstoken,textimg, requestHashMap).enqueue(object :
            Callback<CreateMedical> {
            override fun onResponse(call: Call<CreateMedical>, response: Response<CreateMedical>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getCreate Second API SUCCESS -> $result")
                    Thread {
                        try {
                            moveSave()
                        }
                        catch (e: Exception) {
                            LLog.e(e.toString())
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"getCreate Second API ERROR -> ${response.errorBody()}")
                    ErrorDialog()
                }
            }

            override fun onFailure(call: Call<CreateMedical>, t: Throwable) {
                Log.d(TAG,"getCreate Second Fail -> $t")
                ErrorDialog()
            }
        })
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath.toString())
        val requestFile: RequestBody = file
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        textAdapter.notifyDataSetChanged()
        normalAdapter.notifyDataSetChanged()
        videoAdapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}