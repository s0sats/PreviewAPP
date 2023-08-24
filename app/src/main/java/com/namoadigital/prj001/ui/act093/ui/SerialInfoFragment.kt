package com.namoadigital.prj001.ui.act093.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namoadigital.prj001.databinding.FragmentSerialInfoBinding
import com.namoadigital.prj001.extensions.formatForDisplay
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

    private lateinit var serialInfo: Act093State.SerialInfo

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

    private fun onUpdateHeader() {
        with(binding) {

            if (serialInfo.iconColor.isNullOrEmpty()) {
                iconSerialColor.visibility = android.view.View.GONE
            } else {
                iconSerialColor.apply {
                    setColorFilter(android.graphics.Color.parseColor(serialInfo.iconColor))
                    visibility = android.view.View.VISIBLE
                }
            }

            if (serialInfo.serialId.isNullOrEmpty()) {
                serialId.visibility = android.view.View.GONE
            } else {
                serialId.apply {
                    text = serialInfo.serialId
                    visibility = android.view.View.VISIBLE
                }
            }

            if (serialInfo.product.isNullOrEmpty()) {
                productId.visibility = android.view.View.GONE
            } else {
                productId.apply {
                    text = serialInfo.product
                    visibility = android.view.View.VISIBLE
                }
            }

            if (serialInfo.model.isNullOrEmpty()) {
                brandModel.visibility = android.view.View.GONE
            } else {
                brandModel.apply {
                    text = serialInfo.model
                    visibility = android.view.View.VISIBLE
                }
            }

            infoAndTrackingLayout.visibility =
                if (serialInfo.trackings.isNullOrEmpty() && serialInfo.infoAdd.isNullOrEmpty()) android.view.View.GONE else android.view.View.VISIBLE

            if (serialInfo.infoAdd.isNullOrEmpty()) {
                infosAddText.visibility = android.view.View.GONE
            } else {
                infosAddText.apply {
                    visibility = android.view.View.VISIBLE
                    text = serialInfo.infoAdd
                }
            }

            if (serialInfo.trackings.isNullOrEmpty()) {
                trackingsText.visibility = android.view.View.GONE
            } else {
                trackingsText.apply {
                    text = serialInfo.trackings
                    visibility = android.view.View.VISIBLE
                }
            }


            val measureFormatted = if (serialInfo.last_measure_value != null) {
                if (!serialInfo.last_measure_date.isNullOrEmpty()) {
                    "${ToolBox_Inf.convertDoubleToBigDecimalString(serialInfo.last_measure_value!!, true)} ${serialInfo.value_suffix.formatForDisplay()} (${serialInfo.last_measure_date})"
                } else {
                    "${ToolBox_Inf.convertDoubleToBigDecimalString(serialInfo.last_measure_value!!, true)} ${serialInfo.value_suffix.formatForDisplay()}"
                }
            } else {
                null
            }

            if (measureFormatted.isNullOrEmpty()) {
                measureValue.visibility = android.view.View.GONE
            } else {
                measureValue.apply {
                    text = measureFormatted
                    visibility = android.view.View.VISIBLE
                }
            }

            linearLayout6.visibility = if (measureFormatted.isNullOrEmpty()) {
                android.view.View.GONE
            } else {
                android.view.View.VISIBLE
            }

            linearLayout5.visibility =
                if (serialInfo.last_cycle_value == null
                    || serialInfo.last_cycle_value == 0.0f) {
                    android.view.View.GONE
                } else {
                    android.view.View.VISIBLE
                }


            val cycleFormatted = if (serialInfo.last_cycle_value != null) {
                if (!serialInfo.last_cycle_date.isNullOrEmpty()) {
                    "${ToolBox_Inf.convertDoubleToBigDecimalString(serialInfo.last_cycle_value!!.toDouble(), true)} ${serialInfo.value_suffix.formatForDisplay()}  (${serialInfo.last_cycle_date})"
                } else {
                    "${ToolBox_Inf.convertDoubleToBigDecimalString(serialInfo.last_cycle_value!!.toDouble(), true)} ${serialInfo.value_suffix.formatForDisplay()}"
                }
            } else {
                null
            }

            if (cycleFormatted.isNullOrEmpty()) {
                cycleValue.visibility = android.view.View.GONE
            } else {
                cycleValue.apply {
                    text = cycleFormatted
                    visibility = android.view.View.VISIBLE
                }
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