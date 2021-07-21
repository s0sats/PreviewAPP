package com.namoadigital.prj001.ui.act085

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act085_User_Adapter
import com.namoadigital.prj001.databinding.Act085UserListFrgBinding
import com.namoadigital.prj001.model.Act085UserModel
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER_LIST = "arg_user_list"

/**
 * A simple [Fragment] subclass.
 * Use the [Act085UserListFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act085UserListFrg : BaseFragment() {

    private val mAdapter by lazy {
        Act085_User_Adapter(userListModel.users, onUserSelected)
    }

    private lateinit var userListModel: Act085UserModel
    private lateinit var binding: Act085UserListFrgBinding
    var onUserSelected: (user: TUserWorkgroupObj)  -> Unit = { user: TUserWorkgroupObj -> }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userListModel = it.getSerializable(ARG_USER_LIST) as Act085UserModel
        }
        iniSetup()
    }

    private fun iniSetup() {

        val mResource_Code = ToolBox_Inf.getResourceCode(
            context,
            Constant.APP_MODULE,
            Constant.ACT085
        )

        val transList: MutableList<String> = ArrayList()
        transList.add("act005_title")
        transList.add("records_found_lbl")
        transList.add("records_display_limit_lbl")
        transList.add("records_lbl")
        transList.add("showing_lbl")
        transList.add("alert_qty_records_exceeded_ttl")
        transList.add("alert_qty_records_exceeded_msg")
        transList.add("alert_qty_records_founded")
        transList.add("alert_user_nfc_bloqued_ttl")
        transList.add("alert_user_nfc_bloqued_msg")

        hmAux_Trans = ToolBox_Inf.setLanguage(
            context,
            Constant.APP_MODULE,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = Act085UserListFrgBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.act005RvUserList.layoutManager = LinearLayoutManager(context)
        binding.act005RvUserList.adapter = mAdapter
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment Act085UserListFrg.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Act085UserModel) =
            Act085UserListFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER_LIST, param1)
                }
            }
    }
}