package com.namoadigital.prj001.ui.act011

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.InspectionFormAdapter
import com.namoadigital.prj001.databinding.Act011InspectionListFragmentBinding
import com.namoadigital.prj001.model.AcessoryFormView
import com.namoadigital.prj001.model.InspectionCell
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.view.frag.frg_main_home.FrgMainHome
import java.util.*

private const val ARG_VIEW_OBJECT = "ARG_VIEW_OBJECT"
private const val MAIN_HMAUX_TRANS_KEY = "MAIN_HMAUX_TRANS_KEY"
class InspectionListFragment : BaseFragment() {

    private val mAdapter by lazy {
        InspectionFormAdapter(acessoryFormView.inspections, onInspectionSelected)
    }
    private lateinit var acessoryFormView: AcessoryFormView
    private var _binding: Act011InspectionListFragmentBinding? = null
    private val binding get() = _binding!!
    var onInspectionSelected: (inspection: InspectionCell)  -> Unit = { inspection: InspectionCell -> }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            acessoryFormView  = (it.getSerializable(ARG_VIEW_OBJECT) as AcessoryFormView?)!!
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = Act011InspectionListFragmentBinding.inflate(inflater, container, false)
        //
        setLabels()
        //
        setInspectionList()
        //
        val view = binding.root
        return view
    }

    private fun setInspectionList() {
        binding.rvInspections.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun setLabels() {
        binding.edtInspectionFilter.hint = hmAux_Trans.get("inspection_filter_list_hint")
    }

    companion object {
        @JvmStatic
        fun newInstance(viewObject: AcessoryFormView,hmAux_Trans: HMAux?) =
            FrgMainHome().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_VIEW_OBJECT, viewObject)
                    putSerializable(MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "alert_filter_applied_msg",
                "inspection_filter_list_hint"
            )
        }
    }
}