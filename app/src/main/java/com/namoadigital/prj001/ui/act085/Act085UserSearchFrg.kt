package com.namoadigital.prj001.ui.act085

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.databinding.Act085UserSearchFrgBinding
import com.namoadigital.prj001.util.Constant
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Act085UserSearchFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act085UserSearchFrg : BaseFragment() {

    private lateinit var binding: Act085UserSearchFrgBinding

    var executeWsSearchUser: (name: String,
                              email: String,
                              userCode: String,
                              erpCode: String) -> Unit = { name: String, email: String, userCode: String, erpCode: String -> }
    var addControlStaIntoAct: (controlStaList : List<MKEditTextNM>) -> Unit = {}
    var removeControlStaIntoAct: (controlStaList : List<MKEditTextNM>) -> Unit = {}

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
        // Inflate the layout for this fragment
        binding = Act085UserSearchFrgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        setStaControlsIntoList()
        setMkeditTextHint()
        //
        setActions()
    }

    private fun setStaControlsIntoList() {
        if(controls_sta.isEmpty()) {
            controls_sta = arrayListOf(
                binding.act085UserSearchFrgMketUserName,
                binding.act085UserSearchFrgMketUserEmail,
                binding.act085UserSearchFrgMketUserCode,
                binding.act085UserSearchFrgMketUserErpCode
            )
            addControlStaIntoAct(controls_sta)
        }
    }

    private fun setActions() {
        binding.act085UserSearchFrgBtnSearch.setOnClickListener {
            //
            val usernameFormField = binding.act085UserSearchFrgMketUserName.text.toString()
            val emailFormField = binding.act085UserSearchFrgMketUserEmail.text.toString()
            val userCodeFormField = binding.act085UserSearchFrgMketUserCode.text.toString()
            val erpCodeFormField = binding.act085UserSearchFrgMketUserErpCode.text.toString()
            //
            if(isValidSearch(
                    usernameFormField,
                    emailFormField,
                    userCodeFormField,
                    erpCodeFormField
                )
            ){
                executeWsSearchUser(
                    usernameFormField,
                    emailFormField,
                    userCodeFormField,
                    erpCodeFormField
                )
            }else{
                ToolBox.alertMSG(
                    context,
                    hmAux_Trans["alert_invalid_input_ttl"],
                    hmAux_Trans["alert_invalid_input_msg"],
                    null,
                    0
                )
            }
        }
    }

    private fun setMkeditTextHint() {
        binding.act085UserSearchFrgMketUserName.hint = hmAux_Trans.get("user_name_nick_hint")
        binding.act085UserSearchFrgMketUserEmail.hint = hmAux_Trans.get("email_hint")
        binding.act085UserSearchFrgMketUserCode.hint =hmAux_Trans.get("user_code_hint")
        binding.act085UserSearchFrgMketUserErpCode.hint = hmAux_Trans.get("erp_code_hint")
        binding.act085UserSearchFrgBtnSearch.text = hmAux_Trans.get("btn_user_work_group_seacrh")
    }

    private fun isValidSearch(
        name: String,
        email: String,
        userCode: String,
        erpCode: String
    ): Boolean {
        return !name.isEmpty() || !email.isEmpty() || !userCode.isEmpty() || !erpCode.isEmpty()
    }

    interface OnUserSearchInteract{
        fun executeWsSearchUser(wsProcess : String)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeControlStaIntoAct(controls_sta)
        controls_sta.clear()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Act085UserSearchFrg.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            hmAuxTrans: HMAux
        ) =
            Act085UserSearchFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "user_name_nick_hint",
                "email_hint",
                "user_code_hint",
                "erp_code_hint",
                "btn_user_work_group_seacrh",
                "alert_invalid_input_ttl",
                "alert_invalid_input_msg",
                "progress_user_work_group_seacrh_ttl",
                "progress_user_work_group_seacrh_msg"
            )
        }
    }
}