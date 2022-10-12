package com.iium.iium_medioz.view.main.bottom.data.tablayout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentAllBinding
import com.iium.iium_medioz.model.recycler.*
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.TestAdapter
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.data.DataViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllFragment : BaseFragment() {

    private lateinit var mBinding: FragmentAllBinding
    private var readapter: TestAdapter? = null
    private lateinit var callback: OnBackPressedCallback
    private val viewModel: DataViewModel by sharedViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, container, false)
        mBinding.fragment = this
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.myDataList.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.initAllView()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAdapter(datalist: List<DataList>) {
        if (datalist.isEmpty()) {
            mBinding.medicalRecyclerView.visibility = View.GONE
            mBinding.tvDataNot.visibility = View.VISIBLE
        } else {
            mBinding.medicalRecyclerView.visibility = View.VISIBLE
            mBinding.tvDataNot.visibility = View.GONE
        }
        val adapter = TestAdapter(datalist, context!!)
        val rv = mBinding.medicalRecyclerView
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
                    ?.remove(this@AllFragment)
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }
}