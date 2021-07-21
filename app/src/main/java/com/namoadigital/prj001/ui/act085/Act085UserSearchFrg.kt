package com.namoadigital.prj001.ui.act085

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.databinding.Act085UserSearchFrgBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

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
    var usernameFormField = ""
    var emailFormField = ""
    var userCodeFormField = ""
    var erpCodeFormField = ""
    val hmAux_Trans_Frag by lazy {
        val transListFrag = ArrayList<String>()
        //
        transListFrag.add("user_name_nick_hint")
        transListFrag.add("email_hint")
        transListFrag.add("user_code_hint")
        transListFrag.add("erp_code_hint")
        transListFrag.add("btn_user_work_group_seacrh")
        transListFrag.add("alert_invalid_input_ttl")
        transListFrag.add("alert_invalid_input_msg")
        transListFrag.add("progress_user_work_group_seacrh_ttl")
        transListFrag.add("progress_user_work_group_seacrh_msg")
        //
        val mResource_Code_Frag = ToolBox_Inf.getResourceCode(
            context,
            ConstantBaseApp.APP_MODULE,
            ConstantBaseApp.FRG_USER_WORK_GROUP_SEARCH
        )
        //
        ToolBox_Inf.setLanguage(
            context,
            ConstantBaseApp.APP_MODULE,
            mResource_Code_Frag,
            ToolBox_Con.getPreference_Translate_Code(context),
            transListFrag
        )
    }

    var executeWsSearchUser: (name: String,
                              email: String,
                              userCode: String,
                              erpCode: String) -> Unit = { name: String, email: String, userCode: String, erpCode: String -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        setMkeditTextHint()
        //
        setActions()
    }

    private fun setActions() {
        binding.act085UserSearchFrgBtnSearch.setOnClickListener {
            usernameFormField = binding.act085UserSearchFrgMketUserName.text.toString()
            emailFormField = binding.act085UserSearchFrgMketUserEmail.text.toString()
            userCodeFormField = binding.act085UserSearchFrgMketUserCode.text.toString()
            erpCodeFormField = binding.act085UserSearchFrgMketUserErpCode.text.toString()
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
                    hmAux_Trans_Frag["alert_invalid_input_ttl"],
                    hmAux_Trans_Frag["alert_invalid_input_msg"],
                    null,
                    0
                )
            }
        }
    }

    private fun setMkeditTextHint() {
        binding.act085UserSearchFrgMketUserName.hint = hmAux_Trans_Frag.get("user_name_nick_hint")
        binding.act085UserSearchFrgMketUserEmail.hint = hmAux_Trans_Frag.get("email_hint")
        binding.act085UserSearchFrgMketUserCode.hint =hmAux_Trans_Frag.get("user_code_hint")
        binding.act085UserSearchFrgMketUserErpCode.hint = hmAux_Trans_Frag.get("erp_code_hint")
        binding.act085UserSearchFrgBtnSearch.text = hmAux_Trans_Frag.get("btn_user_work_group_seacrh")
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
        fun newInstance() =
            Act085UserSearchFrg().apply {
                arguments = Bundle().apply {

                }
            }
    }
}