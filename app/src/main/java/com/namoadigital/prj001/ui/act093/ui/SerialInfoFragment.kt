package com.namoadigital.prj001.ui.act093.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.FragmentSerialInfoBinding
import com.namoadigital.prj001.extensions.formatForDisplay
import com.namoadigital.prj001.ui.act093.adapter.Act093Adapter
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.act093.util.Act093State
import com.namoadigital.prj001.util.ToolBox_Inf


/**
 * A simple [Fragment] subclass.
 * Use the [SerialInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SerialInfoFragment : Fragment() {
    private val binding: FragmentSerialInfoBinding by lazy{
        FragmentSerialInfoBinding.inflate(layoutInflater)
    }

    private val state: Act093State.SerialInfo by lazy{
        Act093State.SerialInfo()
    }

    private lateinit var list: List<DeviceTpModel>
    private lateinit var hmAux_Trans: HMAux

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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

    fun initSerialInfoHeader(){
        with(binding) {

            if (state.lastUpdateSerial.isNullOrEmpty()) {
                lastUpdateSerial.visibility = View.GONE
            } else {
                lastUpdateSerial.apply {
                    text = "${hmAux_Trans["last_update_serial_lbl"]}: ${state.lastUpdateSerial}"
                    visibility = View.VISIBLE
                }
            }

            if (state.last_cycle_value != null) {
                titleCycle.text = hmAux_Trans["last_cycle_lbl"]
                titleCycle.visibility = View.VISIBLE
            } else {
                titleCycle.visibility = View.GONE
            }

            if (state.last_measure_value != null) {
                titleMeasure.text = hmAux_Trans["last_measure_lbl"]
                titleMeasure.visibility = View.VISIBLE
            } else {
                titleMeasure.visibility = View.GONE
            }
            val measureFormatted = if (state.last_measure_value != null) {
                if (!state.last_measure_date.isNullOrEmpty()) {
                    "${
                        ToolBox_Inf.convertDoubleToBigDecimalString(
                            state.last_measure_value!!,
                            true
                        )
                    } ${state.value_suffix.formatForDisplay()} (${state.last_measure_date})"
                } else {
                    "${
                        ToolBox_Inf.convertDoubleToBigDecimalString(
                            state.last_measure_value!!,
                            true
                        )
                    } ${state.value_suffix.formatForDisplay()}"
                }
            } else {
                null
            }

            if (measureFormatted.isNullOrEmpty()) {
                measureValue.visibility = View.GONE
            } else {
                measureValue.apply {
                    text = measureFormatted
                    visibility = View.VISIBLE
                }
            }

            linearLayout6.visibility = if (measureFormatted.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }

            linearLayout5.visibility =
                if (state.last_cycle_value == null
                    || state.last_cycle_value == 0.0f
                ) {
                    View.GONE
                } else {
                    View.VISIBLE
                }


            val cycleFormatted = if (state.last_cycle_value != null) {
                if (!state.last_cycle_date.isNullOrEmpty()) {
                    "${
                        ToolBox_Inf.convertDoubleToBigDecimalString(
                            state.last_cycle_value!!.toDouble(),
                            true
                        )
                    } ${state.value_suffix.formatForDisplay()}  (${state.last_cycle_date})"
                } else {
                    "${
                        ToolBox_Inf.convertDoubleToBigDecimalString(
                            state.last_cycle_value!!.toDouble(),
                            true
                        )
                    } ${state.value_suffix.formatForDisplay()}"
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

    private fun initRecyclerView(

    ) {
        if (list.isNotEmpty()) {

            val mAdapter = Act093Adapter(
                list,
                hmAux_Trans
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SerialInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SerialInfoFragment().apply {
                arguments = Bundle().apply {


                }
            }
    }
}