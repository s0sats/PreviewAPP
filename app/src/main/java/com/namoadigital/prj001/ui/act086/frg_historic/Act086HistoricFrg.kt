package com.namoadigital.prj001.ui.act086.frg_historic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.adapter.Act086HistoricAlertAdapter
import com.namoadigital.prj001.dao.GeOsDeviceItemDao
import com.namoadigital.prj001.databinding.Act086HistoricFrgBinding
import com.namoadigital.prj001.model.Act086HistoricAlert
import com.namoadigital.prj001.model.GeOsDeviceItem
import com.namoadigital.prj001.model.GeOsDeviceItemHist
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
class Act086HistoricFrg : BaseFragment(), Act086HistoricFrgContract.IView {
    private val binding: Act086HistoricFrgBinding by lazy{
        Act086HistoricFrgBinding.inflate(layoutInflater)
    }
    private val alertAdapter: Act086HistoricAlertAdapter by lazy{
        Act086HistoricAlertAdapter(alertList as ArrayList<Act086HistoricAlert>)
    }
    private var alertList = mutableListOf<Act086HistoricAlert>()
    private lateinit var itemHist: ArrayList<GeOsDeviceItemHist>
    private var itemCheckStatus: String? = null
    private var nextCycleMeasure: Float? = null
    private var nextCycleMeasureDate: String? = null
    private var nextCycleLimitDate: String? = null
    private var measureValueSufix: String? = null
    private var verificationInstruction: String? = null
    private var restrictionDecimal: Int? = null
    private val mPresenter: Act086HistoricFrgContract.IPresenter by lazy{
            Act086HistoricFrgPresenter(
                requireContext(),
                this,
                hmAux_Trans
            )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            itemHist = it.getSerializable(GeOsDeviceItemHist::javaClass.name) as ArrayList<GeOsDeviceItemHist>
            itemCheckStatus = it.getString(GeOsDeviceItemDao.ITEM_CHECK_STATUS)
            nextCycleMeasure = it.getFloat(GeOsDeviceItemDao.NEXT_CYCLE_MEASURE)
            nextCycleMeasureDate = it.getString(GeOsDeviceItemDao.NEXT_CYCLE_MEASURE_DATE)
            nextCycleLimitDate =  it.getString(GeOsDeviceItemDao.NEXT_CYCLE_LIMIT_DATE)
            measureValueSufix = it.getString(GeOsDeviceItemDao.VALUE_SUFIX)
            verificationInstruction = it.getString(GeOsDeviceItemDao.VERIFICATION_INSTRUCTION)
            restrictionDecimal = it.getInt(GeOsDeviceItemDao.RESTRICTION_DECIMAL)
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
        setLabels()
        setTypeAlertInfo()
        setNextCycleInfo()
        setInstructionInfo()
        setLastFixedInfo()
        setAlertListInfo()
    }

    private fun setLabels() {
        with(binding){
            act086HistoricFrgTvAlertTypeTtl.text = hmAux_Trans["alert_type_ttl"]
            act086HistoricFrgTvNextVerifyTtl.text = hmAux_Trans["next_cycle_ttl"]
            act086HistoricFrgTvMeasureLbl.text = hmAux_Trans["measure_lbl"]
            act086HistoricFrgTvDeadlineLbl.text = hmAux_Trans["limit_date_lbl"]
            act086HistoricFrgTvInstructionTtl.text = hmAux_Trans["verification_instruction_ttl"]
            act086HistoricFrgTvLastAdjustTtl.text = hmAux_Trans["last_fix_ttl"]
            act086HistoricFrgTvAdjustLbl.text = hmAux_Trans["fixed_lbl"]
            act086HistoricFrgTvLastMeasureLbl.text = hmAux_Trans["last_measure_lbl"]
            act086HistoricFrgTvMaterialLbl.text = hmAux_Trans["material_applied_lbl"]
            act086HistoricFrgTvAlertHistoricTtl.text = hmAux_Trans["alert_historic_ttl"]
        }
    }


    private fun setTypeAlertInfo() {
        with(binding){
            var visibility = View.GONE
            var textVal: String? = null
            //Só exibir tipo de alerta itemCheckStatus for um "alerta"
            when(itemCheckStatus){
                GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT ,
                GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT ,
                GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED ,
                GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED -> {
                    visibility = View.VISIBLE
                    textVal = hmAux_Trans[itemCheckStatus]
                }
                else -> {
                    visibility =  View.GONE
                    textVal = null
                }
            }
            //
            act086HistoricFrgClAlertType.visibility = visibility
            act086HistoricFrgTvAlertTypeVal.text = textVal
        }
    }

    private fun setNextCycleInfo() {
        with(binding){
            if((nextCycleMeasure != null && nextCycleMeasureDate != null)
               || nextCycleLimitDate != null
            ){
                act086HistoricFrgClNextCycle.visibility = View.VISIBLE
                configNextMeasureInfo()
                configLimitDateInfo()
            }else{
                act086HistoricFrgClNextCycle.visibility = View.GONE
            }
        }
    }

    private fun configNextMeasureInfo() {
        with(binding) {
            if (nextCycleMeasure != null && nextCycleMeasureDate != null) {
                act086HistoricFrgTvMeasureLbl.visibility = View.VISIBLE
                act086HistoricFrgTvMeasureVal.apply {
                    visibility = View.VISIBLE
                    text = getFormattedMeasureInfo()
                    setTextColor(getDateColor(nextCycleMeasureDate))
                }

            } else {
                act086HistoricFrgTvMeasureLbl.visibility = View.GONE
                act086HistoricFrgTvMeasureVal.visibility = View.GONE
            }
        }
    }

    private fun configLimitDateInfo() {
        with(binding) {
            if (nextCycleLimitDate != null) {
                act086HistoricFrgTvDeadlineLbl.visibility = View.VISIBLE
                act086HistoricFrgTvDeadlineVal.apply {
                    visibility = View.VISIBLE
                    text = getFormattedLimitDate(nextCycleLimitDate!!)
                    setTextColor(getDateColor(nextCycleLimitDate))
                }

            } else {
                act086HistoricFrgTvDeadlineLbl.visibility = View.GONE
                act086HistoricFrgTvDeadlineVal.visibility = View.GONE
            }
        }
    }

    private fun getDateColor(date: String?): Int {
        var textColor = R.color.namoa_font_color_black222
        date?.let {
           ToolBox_Inf.getDateDiferenceInMilliseconds(
                it,
                ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
            ).let { dateDiff ->
               if(dateDiff < 0){
                   textColor = R.color.namoa_os_form_problem_red
               }
           }
        }
        //
        return ContextCompat.getColor(requireContext(), textColor)
    }

    private fun getFormattedMeasureInfo(): String {
        return "$nextCycleMeasure $measureValueSufix (${ToolBox_Inf.millisecondsToString(
                                                        ToolBox_Inf.dateToMilliseconds(nextCycleMeasureDate),
                                                        ToolBox_Inf.nlsDateFormat(context)
                                                    )})"
    }

    private fun getFormattedLimitDate(nextCycleLimitDate: String): String {
        return ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(nextCycleLimitDate),
                ToolBox_Inf.nlsDateFormat(context)
        )
    }

    private fun setInstructionInfo() {
        with(binding){
            if(verificationInstruction != null){
                act086HistoricFrgClInstruction.visibility = View.VISIBLE
                act086HistoricFrgTvInstructionVal.text = verificationInstruction
            }else{
                act086HistoricFrgClInstruction.visibility = View.GONE
                act086HistoricFrgTvInstructionVal.text = null
            }
        }
    }

    private fun setLastFixedInfo() {
        val lastFixed = itemHist.find {
            it.exec_type.equals(GeOsDeviceItem.EXEC_TYPE_FIXED,true)
        }
        //
        with(binding) {
            if(lastFixed != null) {
                act086HistoricFrgClLastAdjust.visibility = View.VISIBLE
                act086HistoricFrgTvAdjustDate.text = ToolBox_Inf.millisecondsToString(
                                                        ToolBox_Inf.dateToMilliseconds(lastFixed.exec_date),
                                                        ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                                    ).replace(" ", "\n")

                act086HistoricFrgTvLastMeasureVal.text = mPresenter.getFormattedLastMeasureInfo(lastFixed,measureValueSufix,restrictionDecimal)
                act086HistoricFrgTvMaterialVal.text = if(lastFixed.exec_material == 1) hmAux_Trans["YES"] else hmAux_Trans["NO"]
                act086HistoricFrgTvComment.apply {
                    visibility = if(lastFixed.exec_comment.isNullOrEmpty()) View.GONE else  View.VISIBLE
                    text = lastFixed.exec_comment
                }
            }else{
                act086HistoricFrgClLastAdjust.visibility = View.GONE
            }
        }
    }

    private fun initRecycle() {
        binding.act086HistoricFrgRvAlertHistoric.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alertAdapter
        }
    }

    private fun setAlertListInfo() {
        alertList.clear()
        //Filtra itens que são alerta
        val toAlertList = mPresenter.getAlertList(itemHist,measureValueSufix,restrictionDecimal)
        //
        if(toAlertList.isNotEmpty()) {
            alertList.addAll(toAlertList)
        }else{
            binding.act086HistoricFrgClAlertHistoric.visibility = View.GONE
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

        fun newInstance(
            hmAux_Trans: HMAux,
            item_check_status: String,
            next_cycle_measure: Float? = null,
            next_cycle_measure_date: String? = null,
            next_cycle_limit_date: String? = null,
            measure_value_sufix: String? = null,
            verification_instruction: String? = null,
            restriction_decimal: Int? = null,
            itemHist: ArrayList<GeOsDeviceItemHist>
        ) =
            Act086HistoricFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                    putString(GeOsDeviceItemDao.ITEM_CHECK_STATUS,item_check_status)
                    next_cycle_measure?.let{
                        putFloat(GeOsDeviceItemDao.NEXT_CYCLE_MEASURE, it)
                    }
                    putString(GeOsDeviceItemDao.NEXT_CYCLE_MEASURE_DATE,next_cycle_measure_date)
                    putString(GeOsDeviceItemDao.NEXT_CYCLE_LIMIT_DATE,next_cycle_limit_date)
                    putString(GeOsDeviceItemDao.VALUE_SUFIX,measure_value_sufix)
                    putString(GeOsDeviceItemDao.VERIFICATION_INSTRUCTION,verification_instruction)
                    restriction_decimal?.let{
                        putInt(GeOsDeviceItemDao.RESTRICTION_DECIMAL, it)
                    }
                    putSerializable(GeOsDeviceItemHist::javaClass.name, itemHist)
                }
            }

        fun getFragTranslationsVars() = listOf<String>(
                "alert_type_ttl",
                "next_cycle_ttl",
                "measure_lbl",
                "limit_date_lbl",
                "verification_instruction_ttl",
                "last_fix_ttl",
                "fixed_lbl",
                "last_measure_lbl",
                "material_applied_lbl",
                "alert_historic_ttl",
                "material_requested_lbl",
                "still_with_problem_lbl"
        )
    }
}