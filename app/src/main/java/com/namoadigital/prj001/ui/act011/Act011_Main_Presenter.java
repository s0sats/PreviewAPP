package com.namoadigital.prj001.ui.act011;

import com.namoadigital.prj001.model.GE_Custom_Form_Data;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_Presenter {

    void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String form_data);

    void saveData(GE_Custom_Form_Data formData);

    void checkData(GE_Custom_Form_Data formData);

    void onBackPressedClicked();

}
