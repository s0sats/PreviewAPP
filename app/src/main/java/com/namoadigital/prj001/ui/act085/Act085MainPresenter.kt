package com.namoadigital.prj001.ui.act085

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.receiver.WBR_User_Search
import com.namoadigital.prj001.service.WS_User_Search
import com.namoadigital.prj001.util.Constant
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.dao.EV_UserDao
import com.namoadigital.prj001.model.TUser_Search_Rec
import com.namoadigital.prj001.model.TWorkgroupObj
import com.namoadigital.prj001.model.T_Workgroup_Member_Edit_Env
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_Edit
import com.namoadigital.prj001.receiver.WBR_Workgroup_Member_List
import com.namoadigital.prj001.service.WS_Workgroup_Member_Edit
import com.namoadigital.prj001.service.WS_Workgroup_Member_List
import com.namoadigital.prj001.ui.act085.Act085Main.Companion.USER_LIST_FRG_TAG
import com.namoadigital.prj001.ui.act085.Act085Main.Companion.USER_SEARCH_FRG_TAG
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act085MainPresenter(
    private val context: Context,
    private val mView: Act085MainContract.I_View,
    private val bundle: Bundle,
    private val mModule_Code: String,
    private val mResource_Code: String,

    ) : Act085MainContract.I_Presenter {

    private val hmAuxTrans: HMAux by lazy {
        loadTranslation()
    }

    private fun loadTranslation(): HMAux {
        val transList: MutableList<String> = mutableListOf(
            "act085_title",
            "dialog_user_search_ttl",
            "dialog_user_search_start",
            "workgroup_edit_ttl",
            "workgroup_edit_start",
            "workgroup_member_list_ttl",
            "workgroup_member_list_start",
            "alert_workgroup_list_not_found_tll",
            "alert_workgroup_list_not_found_msg",
            "alert_leave_without_save_ttl",
            "alert_leave_without_save_confirm",
            "alert_leave_remove_workgroup_ttl",
            "alert_leave_remove_workgroup_confirm"

        )
        transList.addAll(Act085UserSearchFrg.getFragTranslationsVars())
        transList.addAll(Act085UserListFrg.getFragTranslationsVars())
        transList.addAll(Act085WorkgroupRemoveListFrg.getFragTranslationsVars())
        transList.addAll(Act085WorkgroupAddListFrg.getFragTranslationsVars())
        //
        return ToolBox_Inf.setLanguage(
            context,
            mModule_Code,
            mResource_Code,
            ToolBox_Con.getPreference_Translate_Code(context),
            transList
        )
    }

    override fun getTranslation(): HMAux {
        return hmAuxTrans
    }

    //region Act085WorkgroupRemoveListFrg.onWorkgroupRemoveInteract
    override fun executeWorkgroupEditService(
        userCode: Int,
        action: Int,
        workgroupCode: ArrayList<Int>,
        limit: Int,
        dateExpire: String?,
        expireReturn: Int
    ) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Workgroup_Member_Edit::class.java.name)
            //
            mView.showPD(
                hmAuxTrans["workgroup_edit_ttl"] ?: "",
                hmAuxTrans["workgroup_edit_start"] ?: "",
            )
            //
            val mIntent = Intent(context, WBR_Workgroup_Member_Edit::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.USER_CODE, userCode)
                        putInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.ACTIVE, action)
                        putIntegerArrayList(
                            T_Workgroup_Member_Edit_Env.WorkgroupSetData.GROUP_CODE,
                            workgroupCode
                        )
                        putInt(T_Workgroup_Member_Edit_Env.WorkgroupSetData.LIMIT, limit)
                        putString(
                            T_Workgroup_Member_Edit_Env.WorkgroupSetData.DATE_EXPIRE,
                            dateExpire
                        )
                        putInt(
                            T_Workgroup_Member_Edit_Env.WorkgroupSetData.EXPIRE_RETURN,
                            expireReturn
                        )
                    }
                )
            }
            //
            context.sendBroadcast(mIntent)
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun executeWorkgroupMemberListService(userCode: Int) {
        if(ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Workgroup_Member_List::class.java.name)
            //
            mView.showPD(
                hmAuxTrans["workgroup_member_list_ttl"] ?: "",
                hmAuxTrans["workgroup_member_list_start"] ?: "",
            )
            //
            val mIntent = Intent(context, WBR_Workgroup_Member_List::class.java).apply {
                putExtras(
                    Bundle().apply {
                        putInt(EV_UserDao.USER_CODE, userCode)
                    }
                )
            }
            //
            context.sendBroadcast(mIntent)
        }else{
            ToolBox_Inf.showNoConnectionDialog(context)
        }
    }

    override fun processWgMemberListReturn(mLink: String?) {
        val file = File(
            ConstantBaseApp.TICKET_JSON_PATH,
            mLink?: ConstantBaseApp.MD_WORKGROUP_MEMBER_LIST_JSON_FILE
        )
        //
        if(file.exists()){
            var wgMemberList = emptyList<TWorkgroupObj>()
            val gson = GsonBuilder().serializeNulls().create()
            try{
                wgMemberList = gson.fromJson<java.util.ArrayList<TWorkgroupObj>>(
                    ToolBox.jsonFromOracle(
                        ToolBox_Inf.getContents(file)
                    ),
                    object : TypeToken<java.util.ArrayList<TWorkgroupObj?>?>() {}.type
                )
            } catch (e: Exception){
                ToolBox_Inf.registerException(javaClass.name,e)
            }
            //
            deleteWgJsonFile(file)
            //
            mView.updateWorkgroupMemberList(wgMemberList)
            //
            mView.updateLinkeWorkgroupListIntoFrag(getOnlyLinkedWorkgroup(wgMemberList))
        } else{
            mView.showAlert(
                hmAuxTrans["alert_workgroup_list_not_found_tll"]!!,
                hmAuxTrans["alert_workgroup_list_not_found_msg"]!!
            )
        }
    }

    /**
     * LUCHE - 21/07/2021
     * Fun que filtar apenas a lista de WG vinculados ao usr
     */
    private fun getOnlyLinkedWorkgroup(
        wgMemberList: List<TWorkgroupObj>
    ) = wgMemberList.filter {
            it.active == 1
    }

    override fun getUnlinkedWgList(
        workgroupMemberList: List<TWorkgroupObj>
    ) = workgroupMemberList.filter{
        it.active == 0
    }

    private fun deleteWgJsonFile(file: File) {
        file.delete()
    }
    //endregion

    override fun onBackPressedClick(fm: FragmentManager, errorOnWorkgroupServices: Boolean) {
        val visibleFrg = fm.fragments.filter {
            it.isVisible
        }
        //
        val lastTest = fm.fragments.last()
        //
        if (visibleFrg.size == 1) {
            val fragment = visibleFrg[0]
            when (fragment) {
                is Act085WorkgroupAddListFrg -> {
                    confirmLeaveWorkgroupAddFrg(fm, fragment.hasUnsavedDate())
                }
                is Act085WorkgroupRemoveListFrg -> {
//                    fm.popBackStack(USER_SEARCH_FRG_TAG, 0)
                    if(errorOnWorkgroupServices){
                        fm.popBackStack(USER_LIST_FRG_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    }else{
                        mView.showAlert(
                            hmAuxTrans["alert_leave_remove_workgroup_ttl"] ?: "",
                            hmAuxTrans["alert_leave_remove_workgroup_confirm"] ?: "",
                            DialogInterface.OnClickListener { _, _ ->
                                fm.popBackStack(USER_SEARCH_FRG_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            },
                            0
                        )
                    }
                }
                is Act085UserSearchFrg -> {
                    mView.callAct005()
                }
                else -> fm.popBackStack()
            }
        } else {
            mView.callAct005()
        }
    }

    override fun executeWsUserSearch(
        user_code_sql: String,
        email_p: String,
        erp_code: String,
        user_name: String
    ) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_User_Search::class.java.getName())
            if(hmAuxTrans.hasConsistentValue("dialog_user_search_ttl")
                && hmAuxTrans.hasConsistentValue("dialog_user_search_start")){
                    mView.showPD(hmAuxTrans["dialog_user_search_ttl"]!!, hmAuxTrans["dialog_user_search_start"]!!)
            } else {
                mView.showPD("", "")
            }
            val mIntent = Intent(context, WBR_User_Search::class.java)
            val bundle = Bundle()
            bundle.putString(Constant.WS_PROFILE_CHECK_FIELD, "1")
            bundle.putString(Constant.WS_USER_NAME_FIELD, user_name)
            bundle.putString(Constant.WS_USER_EMAIL_FIELD, email_p)
            bundle.putString(Constant.WS_USER_CODE_FIELD, user_code_sql)
            bundle.putString(Constant.WS_ERP_CODE_FIELD, erp_code)
            mIntent.putExtras(bundle)
            //
            context.sendBroadcast(mIntent)
        } else {
            ToolBox_Inf.showNoConnectionDialog(context)
        }

    }

    override fun extractUserSearchResult(mLink: String?) {
        //
        val gson = GsonBuilder().serializeNulls().create()
        //
        val result = gson.fromJson(
            mLink,
            TUser_Search_Rec::class.java
        )

        mView.callAct085UserListFrg(result.getAct085UserModel())

    }

    /**
     * Fun que verifica se ha dados alterados antes de trocar de frag.
     * Caso possua, exibe msg de confirmacao, se não volta sem msg
     */
    private fun confirmLeaveWorkgroupAddFrg(fm: FragmentManager, hasUnsavedDate: Boolean) {
        if(hasUnsavedDate) {
            mView.showAlert(
                hmAuxTrans["alert_leave_without_save_ttl"] ?: "",
                hmAuxTrans["alert_leave_without_save_confirm"] ?: "",
                DialogInterface.OnClickListener { _, _ ->
                    fm.popBackStackImmediate()
                },
                1
            )
        }else{
            fm.popBackStackImmediate()
        }
    }
}