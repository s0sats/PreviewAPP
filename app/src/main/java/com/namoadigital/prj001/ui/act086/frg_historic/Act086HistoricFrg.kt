package com.namoadigital.prj001.ui.act086.frg_historic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act086HistoricAlertAdapter
import com.namoadigital.prj001.databinding.Act086HistoricFrgBinding
import com.namoadigital.prj001.model.Act086HistoricAlert
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [Act086HistoricFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act086HistoricFrg : BaseFragment() {
    private val binding: Act086HistoricFrgBinding by lazy{
        Act086HistoricFrgBinding.inflate(layoutInflater)
    }
    private val alertAdapter: Act086HistoricAlertAdapter by lazy{
        Act086HistoricAlertAdapter(alertList as ArrayList<Act086HistoricAlert>)
    }
    private var alertList = mutableListOf<Act086HistoricAlert>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVars()
        initRecycle()
    }


    private fun initVars() {
        getFakeAlertList(5)
    }

    private fun initRecycle() {
        binding.act086HistoricFrgRvAlertHistoric.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alertAdapter
        }
    }

    private fun getFakeAlertList(qty: Int) {
        for( i in 0 .. qty){
            alertList.add(
                Act086HistoricAlert(
                    ToolBox_Inf.millisecondsToString(
                        ToolBox_Inf.dateToMilliseconds(
                            ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
                        ),
                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                    )
                   ,
                    "Medição $i",
                    "Comentario do item $i"
                )
            )
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param hmAux_Trans: A traduções
         * @return A new instance of fragment Act086HistoricFrg.
         */
        @JvmStatic

        fun newInstance(hmAux_Trans: HMAux) =
            Act086HistoricFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                }
            }
    }
}