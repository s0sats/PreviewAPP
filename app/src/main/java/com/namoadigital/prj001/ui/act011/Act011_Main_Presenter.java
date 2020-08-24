package com.namoadigital.prj001.ui.act011;

import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;

import java.util.ArrayList;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_Presenter {

    void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String form_data, String product_desc, String product_id, String formtype_desc, String formcode_desc, String serial_id, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code);

    void saveData(GE_Custom_Form_Data formData, boolean bMsg);

    void checkData(GE_Custom_Form_Data formData, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location);

    MD_Schedule_Exec getMdScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec);

    void checkSignature(GE_Custom_Form_Data formData, int signature, int opc, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location);

    void onBackPressedClicked();

    void deleteFormLocal(GE_Custom_Form_Local formLocal);

    boolean checkNFormExists(GE_Custom_Form_Local formLocal);

    MD_Product_Serial getSerialInfo(long customer_code, long product_code, String serial_id, GE_Custom_Form_Local formLocal);

    boolean isInProcessing(GE_Custom_Form_Local formLocal);

    void processWS_SaveReturn(String json);

    boolean hasGpsPendecy(long customer_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data);

    void cancelScheduleAndForm(GE_Custom_Form_Local customFormLocal, MD_Schedule_Exec scheduleExec);

    void validateGPSResource(GE_Custom_Form_Local formLocal);
}
