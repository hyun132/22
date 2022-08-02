package com.iium.iium_medioz.view.main.bottom.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.kimcore.inko.Inko
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataDetyailBinding
import com.iium.iium_medioz.databinding.ActivityDataUploadBinding
import com.iium.iium_medioz.model.upload.CreateMedical
import com.iium.iium_medioz.model.upload.KeywordEntity
import com.iium.iium_medioz.model.upload.NormalModel
import com.iium.iium_medioz.model.upload.VideoModel
import com.iium.iium_medioz.util.`object`.Constant.DEFAULT_CODE_TRUE
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SECOND_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SEND_CODE_FALSE
import com.iium.iium_medioz.util.`object`.Constant.THRID_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.adapter.upload.MultiImageAdapter
import com.iium.iium_medioz.util.adapter.upload.NormalImgAdapter
import com.iium.iium_medioz.util.adapter.upload.VideoRecyclerAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.databaseReference
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.dialog.keyWordDialog
import com.iium.iium_medioz.util.file.FileUtil
import com.iium.iium_medioz.util.keyword.KeywordChecker
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.MainActivity
import com.iium.iium_medioz.view.main.bottom.data.keyword.KeywordAdapter
import com.iium.iium_medioz.view.main.bottom.data.keyword.KeywordDialog
import com.iium.iium_medioz.view.main.bottom.data.keyword.KeywordListener
import com.iium.iium_medioz.view.term.FirstDialog
import com.iium.iium_medioz.viewmodel.calendar.CalendarViewModel
import com.iium.iium_medioz.viewmodel.data.KeyWordViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.utf8Size
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.NullPointerException
import java.time.LocalDate
import java.util.HashMap
import kotlin.concurrent.thread

@AndroidEntryPoint
class DataUploadActivity : BaseActivity(), KeywordListener {

    private lateinit var mBinding : ActivityDataUploadBinding
    private lateinit var apiServices: APIService

    private var files4 :MutableList<Uri> = ArrayList()      // 텍스트 이미지
    private var files5 :MutableList<Uri> = ArrayList()      // 일반 이미지
    private var files6 :MutableList<Uri> = ArrayList()      // 비디오

    private val textAdapter = MultiImageAdapter(files4, this)
    private val normalAdapter = NormalImgAdapter(files5, this)
    private val videoAdapter = VideoRecyclerAdapter(files6, this)

    private val fileUtil = FileUtil()

    private lateinit var map: Map<String, String>// 서버에 있는 키워드를 가져와서 저장할 변수
    private val viewModel by viewModels<KeyWordViewModel>()

    private lateinit var keywordAdapter: KeywordAdapter
    private var registeredKeywordList = arrayListOf<KeywordEntity>()
    private val inko = Inko()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_upload)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
        initSecond()
        initThird()
        initKey()
        initEvent()
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

    private fun initKey() {
        mBinding.rvKeyword.layoutManager = LinearLayoutManager(this)
        keywordAdapter = KeywordAdapter()
        mBinding.rvKeyword.adapter = keywordAdapter
    }

    private fun initEvent() {
        databaseReference
            .child("keywords")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    map = p0.value as Map<String, String>
                }
            })

        mBinding.etKeyword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isNotEmpty()) {
                    mBinding.btnSubscribe.isEnabled = true
                    mBinding.btnSubscribe.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorupdate))
                } else {
                    mBinding.btnSubscribe.isEnabled = false
                    mBinding.btnSubscribe.setTextColor(ContextCompat.getColor(applicationContext, R.color.colorBlackDisabled2))
                }
            }
        })

        keywordAdapter.setItemClickListener(object : KeywordAdapter.ItemClickListener{
            override fun onClick(keyword: String) {
                unSubscribe(keyword)
            }
        })

        mBinding.btnSubscribe.setOnClickListener {
            val keyword = mBinding.etKeyword.text.toString()
            try {
                KeywordChecker.check(keyword, registeredKeywordList)
            } catch (e: Exception) {
                showToastMessage(e.message.toString())
            }
        }

        viewModel.searchResult.observe(this) {
            val keyword = mBinding.etKeyword.text.toString()

            if(it){
                subscribe(keyword)
            } else {
                KeywordDialog(this, this).createDialog(keyword)
            }
        }

        viewModel.problem.observe(this) {
            showToastMessage(resources.getString(R.string.database_error))
        }
    }

    private fun subscribe(keyword: String) {
        showProgress()
        var num = "1" // 기본값 1
        if (map.containsKey(keyword)) {
            num = (map.getValue(keyword).toInt() + 1).toString() // 구독자 수 +1
        }

        databaseReference.child("keywords").child(keyword).setValue(num)
        viewModel.writeKeyword(keyword)
    }

    private fun unSubscribe(keyword: String) {
        showProgress()
    }

    override fun keyWordListener(keyword: String) {
        subscribe(keyword)
        mBinding.etKeyword.text = null
        val num = map.getValue(keyword).toInt() - 1 // 구독자 수 -1
        databaseReference.child("keywords").child(keyword).setValue(num.toString())
        viewModel.deleteKeyword(keyword)

    }

    private fun showProgress() {
        this.window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mBinding.lottieViewSheep.visibility = View.VISIBLE
        mBinding.lottieViewSheep.playAnimation()
    }

    private fun hideProgress() {
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        mBinding.lottieViewSheep.visibility = View.GONE
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
        val et_keyword = mBinding.etKeyword.text.toString()
        when {
            et_title.isEmpty() -> {
                mBinding.etTitle.error = "미입력"
                Toast.makeText(this,"제목을 입력해 주세요",Toast.LENGTH_SHORT).show().toString()
            }

            et_keyword.isEmpty() -> {
                mBinding.etKeyword.error = "미입력"
                Toast.makeText(this,"키워드를 입력해 주세요",Toast.LENGTH_SHORT).show().toString()

            }
            else -> {
                callCreateAPI()
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
        val keyword =mBinding.etKeyword.text.toString()
        val timestamp = mBinding.tvTodayData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        val sendcode = SEND_CODE_FALSE
        val defaultcode = DEFAULT_CODE_TRUE
        val sensitivity = ""

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] = timestamp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] = sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] = defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] = sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("데이터 업로드_첫번째 API")
        apiServices.getCreate(prefs.myaccesstoken,textimg,requestHashMap).enqueue(object :
            Callback<CreateMedical> {
            override fun onResponse(call: Call<CreateMedical>, response: Response<CreateMedical>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getCreate API SUCCESS -> $result")
//                    Thread{
//                        try {
//                            normalAPI()
//                        }
//                        catch (e: Exception) {
//                            LLog.e(e.toString())
//                        }
//                    }.start()
                }
                else {
                    Log.d(TAG,"getCreate API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }

            }

            override fun onFailure(call: Call<CreateMedical>, t: Throwable) {
                Log.d(TAG,"getCreate Fail -> $t")
                serverDialog()
            }
        })

    }

    private fun otherAPI() {
        val textimg : MutableList<MultipartBody.Part?> = ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let {
                Log.i("textImg", it)
                Log.d(TAG,"사진 업로드 테스트 : ${it.length}")
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
        val keyword =mBinding.etKeyword.text.toString()
        val timestamp = mBinding.tvTodayData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()
        val sendcode = SEND_CODE_FALSE
        val defaultcode = DEFAULT_CODE_TRUE
        val sensitivity = ""

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] = timestamp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] = sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] = defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] = sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("데이터 업로드_두번째 API")
        apiServices.getCreate(prefs.newaccesstoken,textimg, requestHashMap).enqueue(object :
            Callback<CreateMedical> {
            override fun onResponse(call: Call<CreateMedical>, response: Response<CreateMedical>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"getCreate Second API SUCCESS -> $result")
                    Thread{
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
                }
            }

            override fun onFailure(call: Call<CreateMedical>, t: Throwable) {
                Log.d(TAG,"getCreate Second Fail -> $t")
                serverDialog()
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

    private fun videoFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath.toString())
        val requestFile: RequestBody = file
            .asRequestBody("video/*".toMediaTypeOrNull())
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

}