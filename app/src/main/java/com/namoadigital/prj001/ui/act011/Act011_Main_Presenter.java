package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.namoa_digital.namoa_library.ctls.CustomFF;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.Act011FormTab;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.GeOs;
import com.namoadigital.prj001.model.InspectionCell;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MeMeasureTp;
import com.namoadigital.prj001.model.TK_Ticket_Form;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public interface Act011_Main_Presenter {

    void setData(
            String customer_code,
            String formtype_code,
            String form_code,
            String formversion_code,
            String product_code,
            String form_data,
            String product_desc,
            String product_id,
            String formcode_desc,
            String serial_id,
            Integer so_prefix,
            Integer so_code,
            String so_site_code,
            Integer so_operation_code,
            Integer mTicket_prefix,
            Integer mTicket_code,
            Integer mTicket_seq,
            Integer mTicket_seq_tmp,
            Integer mStep_code,
            Integer mCustomFormDataPartition,
            Integer mCustomFormDataVersion
    );

    void saveData(GE_Custom_Form_Data formData, boolean bMsg);

    void checkData(GE_Custom_Form_Data formData, GeOs geOs, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location, int require_signature);

    boolean isaTicketFlowForm();

    boolean isIndependentForm(GE_Custom_Form_Local customFormLocal, Integer mSo_Prefix, Integer mSo_Code);

    MD_Schedule_Exec getMdScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec);

    void checkSignature(GE_Custom_Form_Data formData, GeOs geOs, int signature, int opc, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location);

    void onBackPressedClicked();

    void deleteFormLocal(GE_Custom_Form_Local formLocal);

    boolean checkNFormExists(GE_Custom_Form_Local formLocal);

    MD_Product_Serial getSerialInfo(long customer_code, long product_code, String serial_id, GE_Custom_Form_Local formLocal);

    boolean isInProcessing(GE_Custom_Form_Local formLocal);

    void processWS_SaveReturn(String json);

    boolean hasGpsPendecy(long customer_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data);

    void cancelScheduleAndForm(GE_Custom_Form_Local customFormLocal, MD_Schedule_Exec scheduleExec);

    void validateGPSResource(GE_Custom_Form_Local formLocal);

    //
    boolean isFormCreateByTicket(GE_Custom_Form_Local customFormLocal);

    void resetTicketCtrlFormDataIfNeeds(GE_Custom_Form_Local formLocal);

    boolean setForceSentByForm(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, int custom_form_data);

    Integer getSeqTmpForFormOffHand(Context context, Integer mTicket_prefix, Integer mTicket_code, Integer mStep_code);

    String getDialogTicketInfo(Integer ticket_prefix, Integer ticket_code, Integer step_code);

    void checkAppExecutionDecrementUpdateNeeds(Integer mSo_prefix, Integer mSo_code, GE_Custom_Form_Data formData);

    void checkOriginDoneFlow(Bundle act083Bundle);

    Bitmap getProductIconBitmap(String custom_product_icon_name);

    boolean hasAnyInvalidField(ArrayList<Act011FormTab> tabs);

    void resetScheduleExecIfNeeds(GE_Custom_Form_Local formLocal);

    void deleteGeOsFormIfNeeds(GE_Custom_Form_Local formLocal);

    void updateGeOsItems(GeOs geOs, int missingJustifyCounter, String dateStart, String dateEnd);

    void addGeOsDeviceItemPhotosIntoFiles(GE_Custom_Form_Local formLocal, ArrayList<GE_File> geFiles, String sDate);

    InspectionCell setAlreadyOkItem(String itemPk, String date_start);

    int getMissingForecastAnswers(GeOs geOs);

    MeMeasureTp getMeasureTp(long customerCode, int measureTpCode);

    TK_Ticket_Form getTkTicketForm(int mTicket_prefix,
                                   int mTicket_code,
                                   int mTicket_seq_tmp,
                                   int mStep_code);
    CustomFF checkNumberOrMeasureCtrl(HMAux cf, MD_Product_Serial serialInfo);

    @Nullable
    String getLastMeasureInfo(MeMeasureTp measureTp, MD_Product_Serial serialInfo, int decimalFromContent);

    ArrayList<HMAux> getSerialClassList();

    void saveSerialClass(long preference_customer_code, int productCode, String serialId, GE_Custom_Form_Local formLocal, SearchableSpinner ssSerialClass);

    boolean isFormTicketKanban(Integer mTicket_prefix, Integer mTicket_code);

    boolean checkIfFormIsNew(
            String customer_code,
            String formtype_code,
            String form_code,
            String formversion_code,
            String product_code,
            String form_data,
            String product_desc,
            String product_id,
            String formcode_desc,
            String serial_id,
            Integer so_prefix,
            Integer so_code,
            String so_site_code,
            Integer so_operation_code,
            Integer mTicket_prefix,
            Integer mTicket_code,
            Integer mTicket_seq,
            Integer mTicket_seq_tmp,
            Integer mStep_code
    );


    int hasPassedDay();

    List<String> getSiteEmailList(int site_code);

    void executeStructureUpdate();

    boolean isTripInUpdateRequired();

    void executeTripUpdate();
    boolean isUserOnSyncRequiredTrip();

    boolean hasSerialStructurePending();

    void clearTicketDownloadSingleton();

    void setTicketDownloadSingleton(int mTkPrefix, int mTkCode);
}
