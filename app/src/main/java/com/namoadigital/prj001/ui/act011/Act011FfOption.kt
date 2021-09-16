package com.namoadigital.prj001.ui.act011

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.adapter.Act011FfOptionsAdapter
import com.namoadigital.prj001.databinding.Act011FfOptionsBinding
import com.namoadigital.prj001.model.Act011FfOptionsViewObject
import com.namoadigital.prj001.model.Act011FormTab
import com.namoadigital.prj001.util.Constant
import java.io.File


class Act011FfOption : Fragment() {
    private val mAdapter by lazy {
        Act011FfOptionsAdapter(mObjectView.tabs, mObjectView.tabSelected, mListener)
    }
    private lateinit var mObjectView: Act011FfOptionsViewObject
    private lateinit var hmAux_Trans: HMAux
    private lateinit var binding: Act011FfOptionsBinding
    private lateinit var mListener: ICustom_Form_FF_Options_ll


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = Act011FfOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        setAction()
        //
    }

    private fun iniVars() {
        with(binding) {
            rvFormTabs.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(
                    DividerItemDecoration(getContext(), LinearLayoutManager(context).getOrientation()))
                if(adapter == null){
                    adapter = mAdapter
                }
            }
        }
    }

    private fun setAction() {

        binding.ibDelete.apply {
            this.setOnClickListener {
                mListener.delete()
            }
        }
        //
        binding.ibSave.apply {
            this.setOnClickListener {
                mListener.save()
            }
        }
        //
        binding.ibFinalize.apply {
            this.setOnClickListener {
                mListener.check()
            }
        }
        //
        binding.ibSignature.apply {
            this.setOnClickListener {
                mListener.autograph()
            }
        }
        //
        binding.ibInfo.apply {
            this.setOnClickListener {
                mListener.info()
            }
        }
        //
        binding.ibNserv.apply {
            this.setOnClickListener {
                mListener.nserv()
            }
        }
        //
        binding.tvFormAutoAnswer.apply {
            this.setOnClickListener {
                mListener.auto()
            }
        }
    }

    private fun setupViews() {
        with(mObjectView) {
            binding.tvFormDesc.text = mObjectView.formDesc

            if (isSOForm) {
                binding.ibNserv.visibility = View.VISIBLE
            } else {
                binding.ibNserv.visibility = View.GONE
            }

            val sFile = File(ConstantBase.CACHE_PATH_PHOTO + "/" + signature)
            if (sFile.exists()) {
                binding.ibSignature.visibility = View.VISIBLE
            } else {
                binding.ibSignature.visibility = View.GONE
            }

            when (formStatus.toUpperCase()) {
                Constant.SYS_STATUS_DONE, Constant.SYS_STATUS_DELETED, Constant.SYS_STATUS_IGNORED, Constant.SYS_STATUS_CANCELLED -> {
                    binding.apply {
                        ibDelete.setVisibility(View.GONE)
                        ibSave.setVisibility(View.GONE)
                        ibFinalize.setVisibility(View.GONE)
                        tvFormAutoAnswer.setVisibility(View.GONE)
                    }
                }
                Constant.SYS_STATUS_WAITING_SYNC -> {
                    binding.apply {
                        if (!isTicketForm) {
                            ibDelete.setVisibility(View.VISIBLE)
                        } else {
                            ibDelete.setVisibility(View.GONE)
                        }
                        ibSave.setVisibility(View.GONE)
                        ibFinalize.setVisibility(View.GONE)
                        tvFormAutoAnswer.setVisibility(View.GONE)
                    }
                }
                else -> {
                    binding.apply {
                        ibSave.setVisibility(View.VISIBLE)
                        ibFinalize.setVisibility(View.VISIBLE)
                        ibDelete.setVisibility(View.VISIBLE)
                        if (automatic) {
                            tvFormAutoAnswer.text = hmAux_Trans.get("drawer_automatic_lbl")
                            tvFormAutoAnswer.setVisibility(View.VISIBLE)
                            vDivider.setVisibility(View.GONE)
                        } else {
                            tvFormAutoAnswer.setVisibility(View.GONE)
                            vDivider.setVisibility(View.VISIBLE)
                        }
                    }
                }
            }
        }
    }

    interface ICustom_Form_FF_Options_ll {
        fun info()
        fun delete()
        fun save()
        fun check()
        fun autograph()
        fun auto()
        fun nserv()
        fun onTabSelected(page: Int)
    }

    fun setFragmentsArgs(
        mObjectView: Act011FfOptionsViewObject,
        hmAux_Trans: HMAux,
        mListener: ICustom_Form_FF_Options_ll
    ) {
        this.hmAux_Trans = hmAux_Trans
        this.mObjectView = mObjectView
        this.mListener = mListener
        setupViews()
        iniVars()
    }

    fun updateTabList(tabs: List<Act011FormTab>, tabSelected: Int) {
        mObjectView.tabs.clear()
        mObjectView.tabs.addAll(tabs)
        mObjectView.tabSelected = tabSelected
        mAdapter.tabSelected = tabSelected
        mAdapter.notifyDataSetChanged()
    }

    fun updateTabList(tab: Act011FormTab, tabSelected: Int) {
        val indexOld = tab.page - 1
        mObjectView.tabs.set(indexOld, tab)
        mObjectView.tabSelected = tabSelected
        mAdapter.tabSelected = tabSelected
        mAdapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment Act085UserListFrg.
         */
        //
        fun getFragTranslationsVars(): List<String> {
            return listOf(
                "drawer_automatic_lbl"
            )
        }
    }

}