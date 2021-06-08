package com.namoadigital.prj001.ui.act020;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_View {

    void showPD(String title, String msg);

    void setRecordInfo(long record_size, long record_page);

    void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list);

    void showQtyExceededMsg(long record_page ,long record_count);

    void callAct006(Context context);

    void callAct009(Context context, Bundle bundle);

    void callAct008(Context context, Bundle bundle);

    void callAct011(Context context, Bundle bundle);

    void setWs_process(String ws_process);
    //
    boolean isSerial_creation();

    boolean isScheduleFlow();

    void callAct017(Context context);

    void callAct013(Context context);

    void callAct081(Context context);

    boolean hasTk_ticket_is_form_off_hand();

    boolean isOffHandForm();

    void callAct083(Context context);

    Bundle getAct083Bundle();

    //void closeDrawer();

}
