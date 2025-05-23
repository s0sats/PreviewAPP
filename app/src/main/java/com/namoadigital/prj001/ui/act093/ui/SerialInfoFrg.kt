package com.namoadigital.prj001.ui.act093.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.databinding.FragmentSerialInfoBinding
import com.namoadigital.prj001.extensions.formatForDisplay
import com.namoadigital.prj001.extensions.serial.showMeasureSuffixAndDate
import com.namoadigital.prj001.extensions.toStringConsiderDecimal
import com.namoadigital.prj001.ui.act093.adapter.Act093Adapter
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf


/**
 * A simple [Fragment] subclass.
 * Use the [SerialInfoFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class SerialInfoFrg : BaseFragment() {
    private val binding: FragmentSerialInfoBinding by lazy {
        FragmentSerialInfoBinding.inflate(layoutInflater)
    }

    var serialInfo: Act093State.SerialInfo? = null

    private var list: List<DeviceTpModel> = mutableListOf()
    private var _mFrgListener: ItemCheckListFragmentInteraction? = null
    private val mFrgListener get() = _mFrgListener!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans =
                HMAux.getHmAuxFromHashMap(it.getSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    fun initSerialInfoHeader() {
        with(binding) {
            serialInfo?.let {
                if (it.lastUpdateSerial.isNullOrEmpty()) {
                    lastUpdateSerial.visibility = View.GONE
                } else {
                    lastUpdateSerial.apply {
                        text =
                            "${hmAux_Trans["last_update_serial_lbl"]}: ${it.lastUpdateSerial}"
                        visibility = View.VISIBLE
                    }
                }

                if (it.last_cycle_value != null) {
                    titleCycle.text = hmAux_Trans["last_cycle_lbl"]
                    titleCycle.visibility = View.VISIBLE
                } else {
                    titleCycle.visibility = View.GONE
                }

                if (it.last_measure_date != null) {
                    titleMeasure.text = hmAux_Trans["last_measure_lbl"]
                    titleMeasure.visibility = View.VISIBLE
                } else {
                    titleMeasure.visibility = View.GONE
                }

                if (!it.last_measure_date.isNullOrEmpty()) {
                    //
                    val formattedValue = it.last_measure_value?.toStringConsiderDecimal()?.let{value ->
                        "($value ${it.value_suffix.formatForDisplay()})"
                    }?: ""
                    measureValue.text = "${it.last_measure_date} $formattedValue"

                    linearLayout6.visibility = View.VISIBLE
                } else {
                    measureValue.visibility = View.GONE
                    linearLayout6.visibility = View.GONE
                }

                linearLayout5.visibility =
                    if (it.last_cycle_value == null
                        || it.last_cycle_value == 0.0f
                    ) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }


                val cycleFormatted = if (it.last_cycle_value != null) {
                    if (!it.last_cycle_date.isNullOrEmpty()) {
                        "${it.last_cycle_date}  (${
                            ToolBox_Inf.convertDoubleToBigDecimalString(
                                it.last_cycle_value.toDouble(),
                                true
                            )
                        } ${it.value_suffix.formatForDisplay()} )"
                    } else {
                        "${
                            ToolBox_Inf.convertDoubleToBigDecimalString(
                                it.last_cycle_value.toDouble(),
                                true
                            )
                        } ${it.value_suffix.formatForDisplay()}"
                    }
                } else {
                    null
                }

                if (cycleFormatted.isNullOrEmpty()) {
                    cycleValue.visibility = View.GONE
                } else {
                    cycleValue.apply {
                        text = cycleFormatted
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun refreshProgressLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                progressLoading.visibility = View.VISIBLE
            } else {
                progressLoading.visibility = View.GONE
                initSerialInfoHeader()
            }
        }
    }

    private fun initRecyclerView() {
        if (list.isNotEmpty()) {

            val mAdapter = Act093Adapter(
                list,
                hmAux_Trans,
                ::onItemSelected,
            )

            binding.recyclerViewList.apply {
                adapter = mAdapter
                visibility = View.VISIBLE
                layoutManager = LinearLayoutManager(context)
            }

        } else {
            binding.recyclerViewList.apply {
                visibility = View.GONE
            }
        }

    }

    fun onItemSelected(
        position: Int,
        item: DeviceTpModel
    ) {
        mFrgListener.itemCheckSelected(position, item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemCheckListFragmentInteraction) {
            _mFrgListener = context
        } else {
            throw RuntimeException("${context.toString()} must implement FrgFFInteraction")
        }

    }

    override fun onDetach() {
        super.onDetach()
        _mFrgListener = null
    }

    fun setItemsList(list: List<DeviceTpModel>) {
        this.list = list
        initRecyclerView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SerialInfoFragment.
         */
        //
        @JvmStatic
        fun newInstance(
            hmAux_Trans: HMAux
        ) =
            SerialInfoFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ConstantBaseApp.MAIN_HMAUX_TRANS_KEY, hmAux_Trans)
                }
            }
    }
}