package com.iium.iium_medioz.view.main.bottom.data.pdf

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityMakePdfactivityBinding
import com.iium.iium_medioz.databinding.ActivitySendBinding
import com.iium.iium_medioz.model.pdf.RPdfGeneratorModel
import com.iium.iium_medioz.model.pdf.RTransaction
import com.iium.iium_medioz.model.pdf.RTransactionType
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.pdf.RPdfGenerator
import com.iium.iium_medioz.util.pdf.RPermissionHelper
import com.itextpdf.svg.converter.SvgConverter.createPdf
import kotlinx.android.synthetic.main.activity_make_pdfactivity.*

class MakePDFActivity : BaseActivity() {

    private lateinit var mBinding : ActivityMakePdfactivityBinding
    private lateinit var apiServices: APIService
    private lateinit var dummyInfo: RPdfGeneratorModel
    private var isGenerating = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make_pdfactivity)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initialSetup()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun initialSetup() {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        createPdf(false)

        mBinding.createPdfButton.setOnClickListener {

            createPdf(true)
        }

        dummyInfo = dummyModel()

    }

    private fun createPdf(download: Boolean) {

        val permissionHelper = RPermissionHelper(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        permissionHelper.denied {
            if (it) {
                Log.d("Permission check", "Permission denied by system")
                permissionHelper.openAppDetailsActivity()
            } else {
                Log.d("Permission check", "Permission denied")
            }
        }

        permissionHelper.requestAll {
            Log.d("Permission check", "All permission granted")

            if (!isGenerating && download) {
                isGenerating = true
                RPdfGenerator.generatePdf(this, dummyInfo)

                val handler = Handler()
                val runnable = Runnable {
                    isGenerating = false
                }
                handler.postDelayed(runnable, 2000)
            }
        }

        permissionHelper.requestIndividual {
            Log.d("Permission check", "Individual Permission Granted")
        }
    }

    private fun dummyModel(): RPdfGeneratorModel {
        val list = dummyTransactions()
        val header = "Medioz"
        val dummy = RPdfGeneratorModel(list, header)
        return dummy
    }

    private fun dummyTransactions(): List<RTransaction> {

        val list = arrayListOf<RTransaction>()

        val i1 = RTransaction()
        i1.pdf_index = "1"
        i1.pdf_name = "Kim-Young-won"
        i1.pdf_address = "DaeJeon"
        i1.pdf_gender = "Man"
        i1.pdf_data = "2022.06.27"
        i1.pdf_sortation = "sortation"
        i1.transType = RTransactionType.plus
        list.add(i1)

        val i2 = RTransaction()
        i1.pdf_index = "seoul"
        i1.pdf_name = "Hong"
        i1.pdf_address = "man"
        i1.pdf_gender = "2022.02.01"
        i1.pdf_data = "sss"
        i1.pdf_sortation = "10 Sep, 20"
        i1.transType = RTransactionType.plus
        list.add(i2)

        val i3 = RTransaction()
        i1.pdf_index = "서울 세브란스 병원"
        i1.pdf_name = "홍길동"
        i1.pdf_address = "남자"
        i1.pdf_gender = "2022.02.01"
        i1.pdf_data = "입원"
        i1.pdf_sortation = "10 Sep, 20"
        i1.transType = RTransactionType.plus
        list.add(i3)

        val i4 = RTransaction()
        i1.pdf_index = "서울 세브란스 병원"
        i1.pdf_name = "홍길동"
        i1.pdf_address = "남자"
        i1.pdf_gender = "2022.02.01"
        i1.pdf_data = "입원"
        i1.pdf_sortation = "10 Sep, 20"
        i4.transType = RTransactionType.plus
        list.add(i4)

//        list.add(i1)
//        list.add(i2)
//        list.add(i3)
//        list.add(i4)
//
//        list.add(i1)
//        list.add(i2)
//        list.add(i3)
//        list.add(i4)

        return list
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}