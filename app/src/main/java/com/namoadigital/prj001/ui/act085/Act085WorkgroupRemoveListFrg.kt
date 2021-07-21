package com.namoadigital.prj001.ui.act085

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act085WorkgroupRemoveAdapter
import com.namoadigital.prj001.databinding.Act085WorkgroupRemoveListFrgBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.util.Constant
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [Act085WorkgroupRemoveListFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act085WorkgroupRemoveListFrg : BaseFragment() {
    private val binding: Act085WorkgroupRemoveListFrgBinding by lazy{
        Act085WorkgroupRemoveListFrgBinding.inflate(layoutInflater)
    }
    private lateinit var hmAux_Trans: String
    private lateinit var userWgObj: TUserWorkgroupObj
    private var linkedWg = emptyList<TWorkgroupObj>()
    private val mAdapter: Act085WorkgroupRemoveAdapter by lazy {
        Act085WorkgroupRemoveAdapter(
            linkedWg,
            this::onRemoveItemClick
        )
    }
    private var mFragListner : onWorkgroupRemoveInteract? = null

    interface onWorkgroupRemoveInteract{
        fun callWorkgroupEditService(userCode:Int, action: Int, workgroupCode: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            userWgObj = it.getSerializable(ARG_USER_WG_OBJ) as TUserWorkgroupObj
            linkedWg = it.getSerializable(ARG_WG_LIST_OBJ) as ArrayList<TWorkgroupObj>
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
        //
        setLabels()
        initVars()
        initRecycler()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is onWorkgroupRemoveInteract){
            mFragListner = context
        } else{
            throw Exception("onWorkgroupRemoveInteract Not Implemented")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mFragListner = null
    }

    private fun setLabels() {
        with(binding){
            act085WorkgroupRemoveListFrgTvListTtl.text = hmAux_Trans["linked_workgroup_lbl"]
            act085WorkgroupRemoveListFrgBtnAddInWg.text = hmAux_Trans["btn_add_in_wrokgroup"]
        }
    }

    private fun initVars() {
        with(binding) {
            act085WorkgroupRemoveListFrgTvUsrName.text = userWgObj.userName
            act085WorkgroupRemoveListFrgTvUsrNick.text = userWgObj.userNick
            act085WorkgroupRemoveListFrgTvUsrName.visibility = View.VISIBLE
        }
    }

    private fun initRecycler() {
        with(binding) {
            act085WorkgroupRemoveListFrgRvWg.layoutManager = LinearLayoutManager(context)
            act085WorkgroupRemoveListFrgRvWg.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            //
            act085WorkgroupRemoveListFrgRvWg.adapter = mAdapter
        }
    }

    private fun onRemoveItemClick(action: Int, workgroupObj: TWorkgroupObj) {
        ToolBox.alertMSG_YES_NO(
            context,
            hmAux_Trans["remove_workgroup_link_ttl"],
            hmAux_Trans["remove_workgroup_link_confirm"],
            DialogInterface.OnClickListener { _, _ ->
                mFragListner?.callWorkgroupEditService( userWgObj.userCode, action,workgroupObj.groupCode)
            },
            1
        )
    }

    fun updateLinkedWorkgroupList(updatedLinkedWgList: ArrayList<TWorkgroupObj>){
        arguments?.apply {
            putSerializable(ARG_WG_LIST_OBJ, updatedLinkedWgList)
        }
        //
        mAdapter.source = updatedLinkedWgList
        mAdapter.notifyDataSetChanged()
        //
        ToolBox.toastMSG(
            context,
            hmAux_Trans["workgroup_list_successfully_update"]
        )
    }

    companion object {
        private const val ARG_USER_WG_OBJ = "ARG_USER_WG_OBJ"
        private const val ARG_WG_LIST_OBJ = "ARG_WG_LIST_OBJ"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param hmAuxTrans traducoes.
         * @param userWgObj objUserSelecionado.
         * @param linkedWgList Lista com Workgroups vinculadas ao usr.
         * @return A new instance of fragment Act085WorkgroupRemoveListFrg.
         */
        @JvmStatic
        fun newInstance(hmAuxTrans: HMAux, userWgObj: TUserWorkgroupObj, linkedWgList: ArrayList<TWorkgroupObj>) =
            Act085WorkgroupRemoveListFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                    putSerializable(ARG_USER_WG_OBJ, userWgObj)
                    putSerializable(ARG_WG_LIST_OBJ, linkedWgList)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "linked_workgroup_lbl",
                "btn_add_in_wrokgroup",
                "remove_workgroup_link_ttl",
                "remove_workgroup_link_confirm",
                "workgroup_list_successfully_update"
            )
        }
    }
}