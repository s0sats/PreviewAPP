package com.namoadigital.prj001.ui.act020;

import android.content.Context;

import com.namoadigital.prj001.model.TProduct_Serial;

import java.util.ArrayList;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_View {

    void showPD();

    void setRecordInfo(long record_page);

    void loadProductSerialList(ArrayList<TProduct_Serial> prod_serial_list);

    void showQtyExceededMsg(long record_count);

    void callAct006(Context context);

}
