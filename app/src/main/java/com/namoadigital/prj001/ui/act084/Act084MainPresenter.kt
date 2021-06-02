package com.namoadigital.prj001.ui.act084

import android.content.Context
import android.os.Bundle
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.*
import com.namoadigital.prj001.sql.*
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.*

class Act084MainPresenter(
        private val context: Context,
        private val mView: Act084Main,
        private val bundle: Bundle,
        private val mModule_Code: String,
        private val mResource_Code: String,
        private val ticketDao: TK_TicketDao,
        private val scheduleDao: MD_Schedule_ExecDao,
        private val formApDao: GE_Custom_Form_ApDao,
        private val formLocalDao: GE_Custom_Form_LocalDao
) : Act084MainContract.I_Presenter {
    private lateinit var myActionFilterParam: MyActionFilterParam
    private var launch : Job? = null
    private var _myActionsList = mutableListOf<MyActionsBase>()
    val myActionsList : MutableList<MyActionsBase>
        get() {
            return _myActionsList
        }
    val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }
    private val originFlow = ConstantBaseApp.ACT084
    private var initialTabToLoad : Int = 1
    private var initialTextFilter : String? = null
    private var initialNcFilter : Boolean = false
    private var _lastSelectedActionPk : String? = null
    private var _lastSelectedActionType : String? = null
    val lastSelectedActionPk :String?
        get() = _lastSelectedActionPk
    val lastSelectedActionType :String?
        get() = _lastSelectedActionType


    init {
        recoverIntentsInfo()
        setViewFiltersParam()
        generateMyActionList(initialTabToLoad,initialNcFilter)
    }

    private fun recoverIntentsInfo() {
        myActionFilterParam =
                (bundle.getSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM) as MyActionFilterParam?)
                ?: MyActionFilterParam()
       // originFlow = bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, ConstantBaseApp.ACT005)
        initialTextFilter = myActionFilterParam.paramTextFilter
        initialTabToLoad = myActionFilterParam.paramItemSelectedTab ?: 1
        _lastSelectedActionPk = myActionFilterParam.paramItemSelectedPk
        _lastSelectedActionType = myActionFilterParam.paramItemSelectedType
        initialNcFilter = myActionFilterParam.paramNcFilter?: false
    }

    private fun setViewFiltersParam() {
        mView.setViewFiltersParam(
                initialTextFilter,
                initialTabToLoad,
                initialNcFilter
        )
    }

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf()
        transList.add("act084_title")
        transList.add("filter_hint")
        transList.add("tab_done_lbl")
        transList.add("tab_discard_lbl")
        transList.add("no_record_lbl")
        transList.add("form_lbl")
        transList.add("other_steps_available_lbl")
        transList.add("alert_schedule_status_prevents_to_open_ttl")
        transList.add("alert_schedule_status_prevents_to_open_msg")
        transList.add("alert_schedule_form_not_found_ttl")
        transList.add("alert_schedule_form_not_found_msg")
        transList.add("alert_schedule_ticket_not_found_ttl")
        transList.add("alert_schedule_ticket_not_found_msg")
        //
        return ToolBox_Inf.setLanguage(
                context,
                mModule_Code,
                mResource_Code,
                ToolBox_Con.getPreference_Translate_Code(context),
                transList
        )
    }

    override fun updateMyActionList(userFocusFilter: Int, ncFilterOn: Boolean) {
        generateMyActionList(userFocusFilter,ncFilterOn)
    }

    private fun generateMyActionList(tabDone: Int, ncFilterOn: Boolean) {
        _myActionsList.clear()
        //Cancela a coroutine em execução caso ainda exista.
        launch?.let {
            if(it.isActive){
                it.cancel()
            }
        }
        //
        launch = CoroutineScope(Dispatchers.IO).launch {
            /*
            * Como somente agendamento de form e form possuem nc, somente busca ticket e form ap se
            * filtro ncFilterOn desativado
            * */
            if(!ncFilterOn) {
                _myActionsList.addAll(
                        getLocalTickets(tabDone).map {
                            val lastTicketSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_TICKET)
                            TK_Ticket.toMyActionsObj(context, it, lastTicketSelected)
                        }
                )
            }
            //
            _myActionsList.addAll(
                    getSchedules(tabDone, ncFilterOn).map {
                        val lastScheduleSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_SCHEDULE)
                        it.toMyActionsObj(context,lastScheduleSelected)
                    }
            )
            //
            if(!ncFilterOn) {
                _myActionsList.addAll(
                        getFormAp(tabDone).map {
                            val lastFormApSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_FORM_AP)
                            it.toMyActionsObj(context, lastFormApSelected)
                        }
                )
            }

            myActionsList.addAll(
                    getLocalForms(tabDone,ncFilterOn).map {
                        val lastFormSelected = getLastSelectedPk(MyActions.MY_ACTION_TYPE_FORM)
                        GE_Custom_Form_Local.toMyActionsObj(context, it,lastFormSelected)
                    }
            )
            //
            _myActionsList.sortBy {
                when (it) {
                    is MyActions -> it.orderBy
                    is MyActionsFormButton -> it.orderBy
                    else -> "190001010000"
                }
            }
            //
            withContext(Dispatchers.Main) {
                mView.changeProgressBarVisility(false)
                mView.iniRecycler()
            }
        }
    }

    /**
     * Fun que retrona pk do item navegado caso seja do mesmo tipo da action
     * passada. Caso contario null.
     * Usado para passar a pk somente para a lista de acton do memso tipo da navegada
     */
    private fun getLastSelectedPk(myActionType: String) =
            if (_lastSelectedActionType == myActionType) {
                _lastSelectedActionPk
            } else {
                null
            }

    private fun getLocalTickets(tabDone: Int): MutableList<HMAux> {
        return ticketDao.query_HM(
                SqlAct084_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        tabDone,
                        hmAuxTrans["other_steps_available_lbl"]
                ).toSqlQuery()
        )
    }

    private fun getSchedules(tabDone: Int, ncFilterOn: Boolean): MutableList<MD_Schedule_Exec> {
        return scheduleDao.query(
                SqlAct084_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ncFilterOn,
                        tabDone
                ).toSqlQuery()
        )
    }

    private fun getFormAp(tabDone: Int):  MutableList<GE_Custom_Form_Ap> {
        return formApDao.query(
                SqlAct084_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        tabDone
                ).toSqlQuery()
        )
    }

    private fun getLocalForms(tabDone: Int, ncFilterOn: Boolean): MutableList<HMAux> {
        val lbl = hmAuxTrans["form_lbl"] ?: "FORMULARIO"

        return formLocalDao.query_HM(
                SqlAct084_004(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        lbl,
                        ncFilterOn,
                        tabDone
                ).toSqlQuery()
        )
    }

    override fun processActionClick(myAction: MyActions) {
        when(myAction.actionType){
            MyActions.MY_ACTION_TYPE_TICKET -> processLocalTicketClick(myAction)
            MyActions.MY_ACTION_TYPE_SCHEDULE -> checkScheduleFlow(myAction)
            MyActions.MY_ACTION_TYPE_FORM_AP -> processFormApClick(myAction)
            MyActions.MY_ACTION_TYPE_FORM -> processFormClick(myAction)
        }
    }

    private fun processLocalTicketClick(myAction: MyActions) {
        mView.callAct070(
                getLocalTicket(
                        myAction
                )
        )
    }

    private fun getLocalTicket(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        //
        return ticketBundle(myAction, splippedPk[0].toInt(), splippedPk[1].toInt())
    }

    private fun checkScheduleFlow(myAction: MyActions) {
        val scheduleExec = getScheduleFromMyAction(myAction)
        //
        if(scheduleExec != null) {
            when (scheduleExec.schedule_type) {
                ConstantBaseApp.MD_SCHEDULE_TYPE_FORM -> processScheduleTypeForm(myAction, scheduleExec)
                else -> processScheduleTypeTicket(myAction,scheduleExec)
            }
        }
    }

    private fun processScheduleTypeForm(myAction: MyActions, scheduleExec: MD_Schedule_Exec) {
        if (isStatusPossibleToOpen(scheduleExec)) {
            prepareOpenForm(myAction,scheduleExec)
        } else {
            mView.showMsg(
                    ttl = hmAuxTrans["alert_schedule_status_prevents_to_open_ttl"],
                    msg = hmAuxTrans["alert_schedule_status_prevents_to_open_msg"]
            )
        }
    }

    private fun prepareOpenForm(myAction: MyActions, scheduleExec: MD_Schedule_Exec) {
        val formLocal = getFormLocalInfo(scheduleExec)
        //
        if(formLocal != null){
            //Seta dados da action selecionado no filterParam
            setSeletedActionInfosIntoFilterParam(myAction)
            mView.callAct011(getFormBundle(formLocal))
        }else{
            mView.showMsg(
                    ttl = hmAuxTrans["alert_schedule_form_not_found_ttl"],
                    msg = hmAuxTrans["alert_schedule_form_not_found_msg"]
            )
        }

    }

    private fun getFormLocalInfo(scheduleExec: MD_Schedule_Exec): GE_Custom_Form_Local? {
        return formLocalDao.getByString(
                MD_Schedule_Exec_Sql_006(
                        scheduleExec.customer_code.toString(),
                        scheduleExec.schedule_prefix.toString(),
                        scheduleExec.schedule_code.toString(),
                        scheduleExec.schedule_exec.toString()
                ).toSqlQuery()
        )
    }

    private fun getTicketBySchedule(schedule_prefix: Int, schedule_code: Int, schedule_exec: Int): TK_Ticket? {
        return ticketDao.getByString(
                TK_Ticket_Sql_009(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
    }

    private fun isStatusPossibleToOpen(scheduleExec: MD_Schedule_Exec): Boolean {
        return (scheduleExec.status != null
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_CANCELLED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_REJECTED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_IGNORED
                && scheduleExec.status != ConstantBaseApp.SYS_STATUS_NOT_EXECUTED)
    }

    private fun processScheduleTypeTicket(myAction: MyActions, scheduleExec: MD_Schedule_Exec) {
        if (isStatusPossibleToOpen(scheduleExec)) {
            prepareOpenTicket(myAction, scheduleExec)
        } else {
            mView.showMsg(
                    ttl = hmAuxTrans["alert_schedule_status_prevents_to_open_ttl"],
                    msg = hmAuxTrans["alert_schedule_status_prevents_to_open_msg"]
            )
        }
    }

    private fun prepareOpenTicket(myAction: MyActions, scheduleExec: MD_Schedule_Exec) {
        val scheduleTicket = getTicketBySchedule(scheduleExec.schedule_prefix, scheduleExec.schedule_code, scheduleExec.schedule_exec)
        if(scheduleTicket != null && TK_Ticket.isValidTkTicket(scheduleTicket)){
            //Seta dados da action selecionado no filterParam
            setSeletedActionInfosIntoFilterParam(myAction)
            //
            mView.callAct070(
                    ticketBundle(myAction, scheduleTicket.ticket_prefix, scheduleTicket.ticket_code)
            )
        }else{
            mView.showMsg(
                    ttl = hmAuxTrans["alert_schedule_ticket_not_found_ttl"],
                    msg = hmAuxTrans["alert_schedule_ticket_not_found_msg"]
            )
        }
    }

    private fun getScheduleFromMyAction(myAction: MyActions):  MD_Schedule_Exec? {
        val splippedPk = myAction.getSplippedPk()
        //
        val schedule_prefix = splippedPk.get(0).toInt()
        val schedule_code = splippedPk.get(1).toInt()
        val schedule_exec = splippedPk.get(2).toInt()
        //
        val scheduleExec: MD_Schedule_Exec? = scheduleDao.getByString(
                MD_Schedule_Exec_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        schedule_prefix,
                        schedule_code,
                        schedule_exec
                ).toSqlQuery()
        )
        //
        return scheduleExec
    }

    private fun processFormApClick(myAction: MyActions) {
        mView.callAct038(getFormApBundle(myAction))
    }

    private fun processFormClick(myAction: MyActions) {
        mView.callAct011(getFormBundle(myAction))
    }

    private fun getFormApBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT084)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(GE_Custom_Form_ApDao.CUSTOMER_CODE, ToolBox_Con.getPreference_Customer_Code(context).toString())
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_TYPE, splippedPk[0])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_CODE, splippedPk[1])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_VERSION, splippedPk[2])
        bundle.putString(GE_Custom_Form_ApDao.CUSTOM_FORM_DATA, splippedPk[3])
        bundle.putString(GE_Custom_Form_ApDao.AP_CODE, splippedPk[4])
        return bundle
    }

    private fun getFormBundle(myAction: MyActions): Bundle {
        val splippedPk = myAction.getSplippedPk()
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction)
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT084)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        bundle.putString(MD_ProductDao.PRODUCT_CODE, myAction.productCode.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, myAction.productDesc)
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, myAction.serialId)
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, splippedPk[0])
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, myAction.customFormTypeDesc)
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, splippedPk[1])
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, splippedPk[2])
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, myAction.customFormDesc)
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, splippedPk[3])
        bundle.putString(Constant.ACT017_SCHEDULED_SITE, myAction.siteCode.toString())
        return bundle
    }

    private fun getFormBundle(formLocal: GE_Custom_Form_Local): Bundle {
        val bundle = Bundle()
        //
        bundle.putString(Constant.MAIN_REQUESTING_ACT, Constant.ACT084)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(MD_ProductDao.PRODUCT_CODE, formLocal.custom_product_code.toString())
        bundle.putString(MD_ProductDao.PRODUCT_DESC, formLocal.custom_product_desc)
        bundle.putString(MD_Product_SerialDao.SERIAL_ID, formLocal.serial_id)
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE, formLocal.custom_form_type.toString())
        bundle.putString(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE_DESC, formLocal.custom_form_type_desc)
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_CODE, formLocal.custom_form_code.toString())
        bundle.putString(GE_Custom_FormDao.CUSTOM_FORM_VERSION, formLocal.custom_form_version.toString())
        bundle.putString(Constant.ACT010_CUSTOM_FORM_CODE_DESC, formLocal.custom_form_desc)
        bundle.putString(GE_Custom_Form_LocalDao.CUSTOM_FORM_DATA, formLocal.custom_form_data.toString())
        //
        return bundle
    }

    private fun ticketBundle(myAction: MyActions,ticketPrefix: Int, ticketCode: Int): Bundle {
        val bundle = Bundle()
        //Seta dados da action selecionado no filterParam
        setSeletedActionInfosIntoFilterParam(myAction)
        //
        bundle.putString(ConstantBaseApp.MAIN_REQUESTING_ACT, ConstantBaseApp.ACT084)
        bundle.putInt(TK_TicketDao.TICKET_PREFIX, ticketPrefix)
        bundle.putInt(TK_TicketDao.TICKET_CODE, ticketCode)
        bundle.putSerializable(MyActionFilterParam.MY_ACTION_FILTER_PARAM, myActionFilterParam)
        bundle.putString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, originFlow)
        return bundle
    }


    fun setSeletedActionInfosIntoFilterParam(myAction: MyActions){
        if(myActionFilterParam != null){
            myActionFilterParam.setSelectedItemParams(
                    mView.getMketFilter(),
                    mView.getCurrentTab(),
                    myAction.actionType,
                    myAction.processPk,
                    mView.getNcFilterStatus()
            )
            //
            myActionFilterParam
        }
    }
}