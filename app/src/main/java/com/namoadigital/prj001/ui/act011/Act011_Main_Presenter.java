package com.namoadigital.prj001.ui.act011;

import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_Presenter {

    void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String form_data, String product_desc, String product_id, String formtype_desc, String formcode_desc, String serial_id, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code);

    void saveData(GE_Custom_Form_Data formData, boolean bMsg);

    void checkData(GE_Custom_Form_Data formData , ArrayList<GE_File> geFiles );

    void checkSignature(GE_Custom_Form_Data formData, int signature, int opc , ArrayList<GE_File> geFiles);

    void onBackPressedClicked();

    void deleteFormLocal(GE_Custom_Form_Local formLocal);

}
