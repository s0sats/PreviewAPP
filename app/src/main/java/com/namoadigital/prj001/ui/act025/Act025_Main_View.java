package com.namoadigital.prj001.ui.act025;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.TProduct_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act025_Main_View {
    void showPD();

    void setRecordInfo(long record_size, long record_page);

    void loadProductSerialList(ArrayList<TProduct_Serial> prod_serial_list);

    void showQtyExceededMsg(long record_page ,long record_count);

    void callAct021(Context context);

    void callAct023(Context context, Bundle bundle);
//
//    void callAct009(Context context, Bundle bundle);
//
//    void callAct011(Context context, Bundle bundle);

    void setWs_process(String ws_process);
}
