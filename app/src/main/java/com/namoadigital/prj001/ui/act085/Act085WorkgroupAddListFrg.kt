package com.namoadigital.prj001.ui.act085

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.BaseFragment
import com.namoadigital.prj001.adapter.Act085WorkgroupAddAdapter
import com.namoadigital.prj001.databinding.Act085WorkgroupAddListFrgBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.util.Constant
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [Act085WorkgroupAddListFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class Act085WorkgroupAddListFrg : BaseFragment() {
    private val binding: Act085WorkgroupAddListFrgBinding by lazy{
        Act085WorkgroupAddListFrgBinding.inflate(layoutInflater)
    }
    private lateinit var userWgObj: TUserWorkgroupObj
    private var unlinkedWgList = emptyList<TWorkgroupObj>()
    private val mAdapter: Act085WorkgroupAddAdapter by lazy {
        Act085WorkgroupAddAdapter(
            unlinkedWgList,
            this::onItemClick
        )
    }
    private var mFragListner : onWorkgroupAddInteract? = null
    private var iFrgToolbarInteraction: IFrgToolbarInteraction? = null

    interface onWorkgroupAddInteract{
        fun onAddWorkgroupSave(
            userCode: Int,
            action: Int,
            workgroupCode: ArrayList<Int>,
            period: Int,
            expireDate: String?,
            expireReturn: Int
        )
    }

    private fun onItemClick(position: Int, workgroupObj: TWorkgroupObj, isChecked: Boolean) {
        val wgIdx = unlinkedWgList.indexOf(workgroupObj)
        if(wgIdx > -1 && wgIdx <= unlinkedWgList.lastIndex){
            unlinkedWgList[wgIdx].createUsrWgLink = isChecked
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hmAux_Trans = HMAux.getHmAuxFromHashMap(it.getSerializable(Constant.MAIN_HMAUX_TRANS_KEY) as HashMap<String?, String?>)
            userWgObj = it.getSerializable(Act085Main.ARG_USER_WG_OBJ) as TUserWorkgroupObj
            unlinkedWgList = it.getSerializable(Act085Main.ARG_WG_LIST_OBJ) as ArrayList<TWorkgroupObj>
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
        setLabels()
        setToolbarTitle()
        initVars()
        initRecycler()
        applyEmptyListLayoutIfCase()
        initAction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is onWorkgroupAddInteract){
            mFragListner = context
        } else{
            throw Exception("onWorkgroupAddInteract Not Implemented")
        }
        if(context is IFrgToolbarInteraction){
            iFrgToolbarInteraction = context
        }else{
            throw Exception("IFrgToolbarInteraction Not Implemented")
        }

    }

    override fun onDetach() {
        super.onDetach()
        mFragListner = null
        iFrgToolbarInteraction = null
        unlinkedWgList.forEach {
            it.createUsrWgLink = false
        }
    }

    private fun setLabels() {
        with(binding){
            act085WorkgroupAddListFrgMketSearch.hint = hmAux_Trans["search_hint"]
            act085WorkgroupAddListFrgTvPeriod.text = hmAux_Trans["periodo_lbl"]
            act085WorkgroupAddListFrgRdoNoLimit.text = hmAux_Trans["rdo_no_expire_date"]
            act085WorkgroupAddListFrgRdoUntil.text = hmAux_Trans["rdo_until_expire_date"]
            act085WorkgroupAddListFrgTvDateLbl.text = hmAux_Trans["date_lbl"]
            act085WorkgroupAddListFrgTvTimeLbl.text = hmAux_Trans["hour_lbl"]
            act085WorkgroupAddListFrgChkExpireReturn.text = hmAux_Trans["chk_deactivate_other_workgroup_until_expire_date"]
            act085WorkgroupAddListFrgBtnCancel.text = hmAux_Trans["btn_cancel"]
            act085WorkgroupAddListFrgBtnSave.text = hmAux_Trans["btn_save"]
            act085WorkgroupAddListTvEmptyList.text = hmAux_Trans["unlinked_wg_empty_list"]
            act085WorkgroupAddListFrgTvDateVal.setmLabel("")
        }
    }

    private fun setToolbarTitle() {
        iFrgToolbarInteraction?.updateToolbarTitle (hmAux_Trans["act085_add_workgroup_ttl"]?:"")
    }

    private fun initVars() {
        with(binding) {
            act085WorkgroupAddListFrgTvUsrName.text = userWgObj.userName
            act085WorkgroupAddListFrgTvUsrNick.text = userWgObj.userNick
            act085WorkgroupAddListFrgClUntilDate.visibility = View.GONE
            act085WorkgroupAddListFrgMketSearch.apply {
                setmBARCODE(false)
                setmOCR(false)
                setmNFC(false)
            }
        }
    }

    private fun initRecycler() {
        with(binding) {
            act085WorkgroupAddListFrgRvWg.layoutManager = LinearLayoutManager(context)
            act085WorkgroupAddListFrgRvWg.adapter = mAdapter
        }
    }

    /**
     * Fun que seta visibilidade inicial da tabela baseada na existencia ou não de itens na lista
     */
    private fun applyEmptyListLayoutIfCase() {
        with(binding) {
            if (!unlinkedWgList.isNullOrEmpty()) {
                act085WorkgroupAddListTvEmptyList.visibility = View.GONE
                act085WorkgroupAddListFrgMketSearch.visibility = View.VISIBLE
                act085WorkgroupAddListFrgCvPeriod.visibility = View.VISIBLE
                act085WorkgroupAddListFrgCvPeriod.visibility = View.VISIBLE
                act085WorkgroupAddListFrgClBtn.visibility = View.VISIBLE
                //act085WorkgroupAddListFrgTvRecords.visibility = View.VISIBLE
                act085WorkgroupAddListFrgTvRecords.text = getRecordInfoLabel(unlinkedWgList.size)
            } else{
                act085WorkgroupAddListTvEmptyList.visibility = View.VISIBLE
                act085WorkgroupAddListFrgMketSearch.visibility = View.GONE
                act085WorkgroupAddListFrgCvPeriod.visibility = View.GONE
                act085WorkgroupAddListFrgCvPeriod.visibility = View.GONE
                act085WorkgroupAddListFrgClBtn.visibility = View.GONE
                act085WorkgroupAddListFrgTvRecords.visibility = View.GONE

            }
        }
    }

    private fun getRecordInfoLabel(size: Int): String {
        return "$size ${hmAux_Trans["workgroup_found_lbl"]}"
    }

    private fun initAction() {
        with(binding) {
            act085WorkgroupAddListFrgMketSearch.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText {
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    act085WorkgroupAddListFrgRvWg.stopScroll()
                    applyTextFilter(text)
                }
            })

            act085WorkgroupAddListFrgRgPeriod.setOnCheckedChangeListener { _, checkedId ->
                configDateComponentForRdoOption(checkedId == act085WorkgroupAddListFrgRdoUntil.id)
            }
            //
            act085WorkgroupAddListFrgBtnSave.setOnClickListener {
                if(isValidSave()){
                    showAlert(
                        hmAux_Trans["alert_save_edition_ttl"],
                        hmAux_Trans["alert_save_edition_confirm"],
                        DialogInterface.OnClickListener { _, _ ->
                            prepareDataToSave()
                        },
                        1
                    )
                }else{
                    showAlert(
                        hmAux_Trans["alert_invalid_save_ttl"],
                        hmAux_Trans["alert_invalid_save_msg"]
                    )
                }
            }
            //
            act085WorkgroupAddListFrgBtnCancel.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    /**
     * Fun que prepara os dados para chamada do ws
     */
    private fun prepareDataToSave() {
        var wgToLinkList = unlinkedWgList.filter {
            it.createUsrWgLink
        }.map {
            it.groupCode
        } as ArrayList<Int>
        //
        var period = 0
        var expireDate: String? = null
        var expireReturn = 0
        with(binding) {
            if (act085WorkgroupAddListFrgRdoUntil.isChecked) {
                period = 1
                expireDate = act085WorkgroupAddListFrgTvDateVal.getmValue()
                expireReturn = if (act085WorkgroupAddListFrgChkExpireReturn.isChecked) 1 else 0
            }
        }
        //Chama interface que chamará o WS
        mFragListner?.onAddWorkgroupSave(
            userCode = userWgObj.userCode,
            action = 1,
            workgroupCode = wgToLinkList,
            period = period,
            expireDate = expireDate,
            expireReturn = expireReturn
        )
    }

    private fun showAlert(ttl: String?, msg: String?, positiveListener: DialogInterface.OnClickListener? = null, negativeOption: Int = 0){
        if(negativeOption == 0) {
            ToolBox.alertMSG(
                context,
                ttl,
                msg,
                positiveListener,
                negativeOption
            )
        }else{
            ToolBox.alertMSG_YES_NO(
                context,
                ttl,
                msg,
                positiveListener,
                negativeOption
            )
        }
    }

    /**
     * Fun que define comportamento da view de dateHour
     */
    private fun configDateComponentForRdoOption(dateFieldRequired: Boolean) {
        with(binding) {
            act085WorkgroupAddListFrgTvDateVal.setmRequired(dateFieldRequired)
            act085WorkgroupAddListFrgTvDateVal.setmHighlightWhenInvalid(dateFieldRequired)
            act085WorkgroupAddListFrgTvDateVal.setmLabel("")
            //
            if(dateFieldRequired){
                act085WorkgroupAddListFrgTvDateVal.setmValue(
                    getTodayLastMinute()
                )
                act085WorkgroupAddListFrgClUntilDate.visibility = View.VISIBLE
            }else{
                act085WorkgroupAddListFrgTvDateVal.setmValue(null)
                act085WorkgroupAddListFrgTvDateVal.isValid
                act085WorkgroupAddListFrgClUntilDate.visibility = View.GONE
            }
        }
    }

    /**
     * Fun que retorna o ultimo minuto do dia de hoje
     */
    private fun getTodayLastMinute(): String{
       return "${ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z").substring(0,10)} 23:59:59 ${ToolBox.getDeviceGMT(false)}"
    }

    private fun applyTextFilter(text: String?){
        mAdapter.filter.filter(text)
    }
    /**
     * LUCHE - 21/07/2021
     * <p></p>
     * Fun que valida se os dados necessarios para o save foram preenchidos.
     *  Ao menos um wg marcado(find{}) e se selecionou data de expiração, data preenchida
     */
    private fun isValidSave(): Boolean {
        unlinkedWgList.find {
            it.createUsrWgLink
        } ?: return false
        //
        if( binding.act085WorkgroupAddListFrgRdoUntil.isChecked
            && !binding.act085WorkgroupAddListFrgTvDateVal.isValid
        ){
            return false
        }
        //
        return true
    }

    /**
     * Fun que verifica se existem dados alterados  na tela
     */
    fun hasUnsavedDate() : Boolean{
       //Busca item com checkbox marcado e se encontrar retorna true
       unlinkedWgList.find {
            it.createUsrWgLink
        }?.let {
            return true
        }
        //Se nenhum item, verifica se teve alteração na data ou no checkbox
        return binding.act085WorkgroupAddListFrgTvDateVal.hasChanged()
               && binding.act085WorkgroupAddListFrgChkExpireReturn.isChecked
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param hmAuxTrans traducoes.
         * @param userWgObj objUserSelecionado.
         * @param unlinkedWgList Lista com Workgroups NAO vinculadas ao usr.
         * @return A new instance of fragment Act085WorkgroupAddListFrg.
         */
        @JvmStatic
        fun newInstance(hmAuxTrans: HMAux, userWgObj: TUserWorkgroupObj, unlinkedWgList: ArrayList<TWorkgroupObj>) =
            Act085WorkgroupAddListFrg().apply {
                arguments = Bundle().apply {
                    putSerializable(Constant.MAIN_HMAUX_TRANS_KEY, hmAuxTrans)
                    putSerializable(Act085Main.ARG_USER_WG_OBJ, userWgObj)
                    putSerializable(Act085Main.ARG_WG_LIST_OBJ, unlinkedWgList)
                }
            }

        fun getFragTranslationsVars() : List<String>{
            return listOf(
                "search_hint",
                "periodo_lbl",
                "rdo_no_expire_date",
                "rdo_until_expire_date",
                "date_lbl",
                "hour_lbl",
                "chk_deactivate_other_workgroup_until_expire_date",
                "btn_cancel",
                "btn_save",
                "alert_invalid_save_ttl",
                "alert_invalid_save_msg",
                "alert_save_edition_ttl",
                "alert_save_edition_confirm",
                "unlinked_wg_empty_list",
                "workgroup_found_lbl",
                "act085_add_workgroup_ttl"
            )
        }
    }
}