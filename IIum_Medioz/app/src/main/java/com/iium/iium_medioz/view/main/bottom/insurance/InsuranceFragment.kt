package com.iium.iium_medioz.view.main.bottom.insurance

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentInsuranceBinding
import com.iium.iium_medioz.model.document.DocumentList
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.util.adapter.document.DocumentAdapter
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import com.iium.iium_medioz.viewmodel.main.insurance.InsuranceViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsuranceFragment : BaseFragment(),LifecycleObserver {

    private lateinit var mBinding : FragmentInsuranceBinding
    private var readapter: DocumentAdapter?=null
    private lateinit var callback: OnBackPressedCallback
    private val viewModel : InsuranceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_insurance, container, false)
        mBinding.fragment = this
        return mBinding.root
    }


    fun onTestClick(v: View) {
        val intent = Intent(activity, HospitalActivity::class.java)
        startActivity(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.documentList.observe(viewLifecycleOwner){
            setAdapter(it)
        }
        viewModel.mutableErrorMessage.observe(viewLifecycleOwner){
            ErrorDialog()
        }
    }

    private fun setAdapter(documentList: List<DocumentList>) {
        val adapter = DocumentAdapter(documentList, context!!)
        val rv = mBinding.documentRe
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        readapter?.notifyDataSetChanged()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this@InsuranceFragment)
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        activity?.lifecycle?.addObserver(this@InsuranceFragment)

    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

}