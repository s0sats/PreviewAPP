package com.namoadigital.prj001.view.frag.frg_main_home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.databinding.FrgMainHomeBinding
import com.namoadigital.prj001.model.MdTag

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Frg_Main_Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Frg_Main_Home : BaseFragment(), Frg_Main_Home_Contract.View {
    // TODO: Rename and change types of parameters
    private var param1: String? = null

    private var _binding: FrgMainHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            hmAux_Trans = it.getSerializable(ARG_PARAM2) as HMAux?
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FrgMainHomeBinding.inflate(inflater, container, false)
        //
        initializeLayoutVisibility()
        //
        setLabels()
        //
        setActions()
        //
        val view = binding.root
        return view
    }

    private fun initializeLayoutVisibility() {
        TODO("Not yet implemented")
    }

    private fun setLabels() {
        _binding?.tvCalendar?.text = hmAux_Trans.get("")
        _binding?.tvListPlaceholder?.text = hmAux_Trans.get("")
        _binding?.tvMessenger?.text = hmAux_Trans.get("")
        _binding?.tvSearch?.text = hmAux_Trans.get("")
        _binding?.tvListTagLbl?.text = hmAux_Trans.get("")
    }

    private fun setActions() {
        _binding?.llCalendar?.setOnClickListener {

        }
        //
        _binding?.llSearch?.setOnClickListener {

        }
        //
        _binding?.llMessenger?.setOnClickListener {

        }
        //
        _binding?.fabSearchBySerial?.setOnClickListener {

        }
        //
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Frg_Main_Home.
         */
        //
        @JvmStatic
        fun newInstance(param1: String, hmAux_Trans: HMAux) =
                Frg_Main_Home().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putSerializable(ARG_PARAM2, hmAux_Trans)
                    }
                }
    }

    override fun showMsg(ttl: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun showAlert(ttl: String, msg: String, listenerOk: DialogInterface.OnClickListener?, showNegative: Boolean) {
        TODO("Not yet implemented")
    }

    override fun setWsProcess(wsProcess: String) {
        TODO("Not yet implemented")
    }

    override fun showPD(ttl: String, msg: String) {
        TODO("Not yet implemented")
    }

    override fun loadMenuV3(tags: MdTag) {
        TODO("Not yet implemented")
    }
}