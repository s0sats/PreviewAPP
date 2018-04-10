package com.namoadigital.prj001.ui.act038;

import android.view.View;

import com.namoadigital.prj001.model.GE_Custom_Form_Ap;
import com.namoadigital.prj001.model.MD_Department;
import com.namoadigital.prj001.model.MD_User;

import java.util.ArrayList;

/**
 * Created by d.luche on 31/08/2017.
 */

public interface Act038_Main_Presenter {

    void getloadAP(
            String mCustomer_Code,
            String mCustom_Form_Type,
            String mCustom_Form_Code,
            String mCustom_Form_Version,
            String mCustom_Form_Data,
            String mAp_Code
    );

    GE_Custom_Form_Ap getAp(
            String mCustomer_Code,
            String mCustom_Form_Type,
            String mCustom_Form_Code,
            String mCustom_Form_Version,
            String mCustom_Form_Data,
            String mAp_Code
    );

    void loadSSStatus(String ap_status);

    void loadSSUsers();

    void loadSSDepartments();

    MD_User loadUser(String customer_code, String user_code);

    MD_Department loadDepartment(String customer_code, String department_code);

    void applyUserProfile(ArrayList<View> editable_views_list, String status);

    void executeApSyncWs();

    void executeWsApSave(GE_Custom_Form_Ap ap);

    boolean detectApSyncRequired(
            String mCustomer_Code,
            String mCustom_Form_Type,
            String mCustom_Form_Code,
            String mCustom_Form_Version,
            String mCustom_Form_Data,
            String mAp_Code
    );

    boolean chatFlow(GE_Custom_Form_Ap ap, boolean checkRoom);

}
