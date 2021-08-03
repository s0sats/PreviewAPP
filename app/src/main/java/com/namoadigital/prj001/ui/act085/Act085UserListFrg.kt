package com.namoadigital.prj001.ui.act085

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act085_User_Adapter
import com.namoadigital.prj001.databinding.Act085UserListFrgBinding
import com.namoadigital.prj001.model.Act085UserModel
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.util.Constant
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
    lateinit var iFrgToolbarInteraction: IFrgToolbarInteraction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userListModel = it.getSerializable(ARG_USER_LIST) as Act085UserModel
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
        }
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
        setToolbarTitle()
        setRecyclerView()
        setRecordsCountLabel()
        binding.act085MketFilter.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
            override fun reportTextChange(s: String) {}
            //
            override fun reportTextChange(s: String, b: Boolean) {
                if (mAdapter != null) {
                    mAdapter.filter.filter(binding.act085MketFilter.getText().toString().trim { it <= ' ' })
                }
            }
        })
    }

    private fun setToolbarTitle() {
        iFrgToolbarInteraction.updateToolbarTitle (hmAux_Trans["act085_user_list_ttl"]?:"")
    }

    private fun setRecordsCountLabel() {
        binding.act085TvRecords.text =
            """ ${hmAux_Trans["showing_lbl"]} ${userListModel.usersCount} ${hmAux_Trans["records_lbl"]} """
    }

    private fun setRecyclerView() {
        binding.act085RvUserList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            if(userListModel.users.size == 1){
                onUserSelected(userListModel.users[0])
            }
        }
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
        fun newInstance(param1: Act085UserModel, hmAuxTrans: HMAux) =
            Act085UserListFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER_LIST, param1)
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "records_found_lbl",
                "records_display_limit_lbl",
                "records_lbl",
                "showing_lbl",
                "alert_qty_records_exceeded_ttl",
                "alert_qty_records_exceeded_msg",
                "alert_qty_records_founded",
                "act085_user_list_ttl"
            )
        }
    }
}