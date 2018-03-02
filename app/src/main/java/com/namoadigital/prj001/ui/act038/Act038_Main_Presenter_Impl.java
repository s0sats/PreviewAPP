package com.namoadigital.prj001.ui.act038;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.namoa_digital.namoa_library.ctls.MkDateTime;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.MD_DepartmentDao;
import com.namoadigital.prj001.dao.MD_UserDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_User;
import com.namoadigital.prj001.receiver.WBR_AP_Search;
import com.namoadigital.prj001.sql.GE_Custom_Form_Ap_Sql_005;
import com.namoadigital.prj001.sql.MD_Department_Sql_001;
import com.namoadigital.prj001.sql.MD_Department_Sql_002;
import com.namoadigital.prj001.sql.MD_User_Sql_001;
import com.namoadigital.prj001.sql.MD_User_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act038_Main_Presenter_Impl implements Act038_Main_Presenter {

    private Context context;
    private Act038_Main_View mView;
    private HMAux hmAux_Trans;

    private GE_Custom_Form_Ap mGe_custom_form_ap;
    private GE_Custom_Form_ApDao mGe_custom_form_apDao;


    public Act038_Main_Presenter_Impl(Context context, Act038_Main_View mView, HMAux hmAux_Trans, GE_Custom_Form_ApDao mGe_custom_form_apDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mGe_custom_form_apDao = mGe_custom_form_apDao;
    }

    @Override
    public void getloadAP(String mCustomer_Code, String mCustom_Form_Type, String mCustom_Form_Code, String mCustom_Form_Version, String mCustom_Form_Data, String mAp_Code) {
        //
        mGe_custom_form_ap = mGe_custom_form_apDao.getByString(
                new GE_Custom_Form_Ap_Sql_005(
                        mCustomer_Code,
                        mCustom_Form_Type,
                        mCustom_Form_Code,
                        mCustom_Form_Version,
                        mCustom_Form_Data,
                        mAp_Code,
                        GE_Custom_Form_Ap_Sql_005.RETURN_SQL_OBJ
                ).toSqlQuery()
        );
        //
        mView.loadAP(mGe_custom_form_ap);
    }

    @Override
    public void loadSSStatus(String ap_status) {
        ArrayList<HMAux> statusList = new ArrayList<>();
        if(ap_status.equals(Constant.SYS_STATUS_EDIT)) {
            if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_AP,Constant.PROFILE_MENU_AP_PARAM_EDIT)){
                statusList = ToolBox_Inf.statusList(
                        hmAux_Trans
                );
            }else{
                statusList = ToolBox_Inf.statusList(
                        hmAux_Trans,
                        Constant.SYS_STATUS_CANCELLED
                );
            }
        }else{
            if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_AP,Constant.PROFILE_MENU_AP_PARAM_EDIT)){
                statusList = ToolBox_Inf.statusList(
                        hmAux_Trans,
                        Constant.SYS_STATUS_EDIT
                );
            }else{
                statusList = ToolBox_Inf.statusList(
                        hmAux_Trans,
                        Constant.SYS_STATUS_EDIT,
                        Constant.SYS_STATUS_CANCELLED
                );
            }
        }
        //
        mView.loadSSStatus(statusList);
    }

    @Override
    public void loadSSUsers() {
        MD_UserDao md_userDao = new MD_UserDao(context);
        //
        ArrayList<HMAux> userList = (ArrayList<HMAux>) md_userDao.query_HM(
                new MD_User_Sql_001(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        //
        mView.loadSSUsers(userList);
    }

    @Override
    public void loadSSDepartments() {
        MD_DepartmentDao departmentDao = new MD_DepartmentDao(context);
        //
        ArrayList<HMAux> departmentList = (ArrayList<HMAux>) departmentDao.query_HM(
                new MD_Department_Sql_001(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context))
                ).toSqlQuery()
        );
        //
        mView.loadSSDepartment(departmentList);
    }

    @Override
    public MD_User loadUser(String customer_code, String user_code) {
        MD_UserDao md_userDao = new MD_UserDao(context);
        return md_userDao.getByString(
                new MD_User_Sql_002(
                        customer_code,
                        user_code
                ).toSqlQuery()
        );
    }

    @Override
    public MD_Department loadDepartment(String customer_code, String department_code) {
        MD_DepartmentDao md_departmentDao = new MD_DepartmentDao(context);
        return md_departmentDao.getByString(
                new MD_Department_Sql_002(
                        customer_code,
                        department_code
                ).toSqlQuery()
        );
    }

    @Override
    public void applyUserProfile(ArrayList<View> editable_views_list) {
        int status_change = ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_AP, Constant.PROFILE_MENU_AP_PARAM_CHANGE_STATUS) ? 1 : 0;
        int edit = ToolBox_Inf.profileExists(context, Constant.PROFILE_MENU_AP, Constant.PROFILE_MENU_AP_PARAM_EDIT) ? 2 : 0;
        int profile_level = status_change + edit;
        //
        for (View view : editable_views_list) {
            switch (profile_level) {
                case 1:
                    if (view instanceof SearchableSpinner) {
                        if (view.getId() == R.id.act038_content_ss_status) {
                            ((SearchableSpinner) view).setmEnabled(true);
                        } else {
                            ((SearchableSpinner) view).setmEnabled(false);
                        }
                    } else if (view instanceof MkDateTime) {
                        ((MkDateTime) view).setmEnabled(false);
                    } else {
                        view.setEnabled(false);
                    }
                    break;
                case 2:
                case 3:
                    if (view instanceof SearchableSpinner) {
                        ((SearchableSpinner) view).setmEnabled(true);
                    } else if (view instanceof MkDateTime) {
                        ((MkDateTime) view).setmEnabled(true);
                    } else {
                        view.setEnabled(true);
                    }
                    break;
                default:
                    if (view instanceof SearchableSpinner) {
                        ((SearchableSpinner) view).setmEnabled(false);
                    } else if (view instanceof MkDateTime) {
                        ((MkDateTime) view).setmEnabled(false);
                    } else {
                        view.setEnabled(false);
                    }
                    break;
            }
        }
        //
        if (profile_level == 1 || profile_level == 2 || profile_level == 3) {
            mView.showBtnSave(true);
        } else {
            mView.showBtnSave(false);
        }
    }

    @Override
    public void executeApSyncWs() {
        mView.showPD(
                hmAux_Trans.get("progress_sync_ap_ttl"),
                hmAux_Trans.get("progress_sync_ap_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_AP_Search.class);
        Bundle bundle = new Bundle();
        bundle.putInt(GE_Custom_Form_ApDao.SYNC_REQUIRED,1);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }
}
