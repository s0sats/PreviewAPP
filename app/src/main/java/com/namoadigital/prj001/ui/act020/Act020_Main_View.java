package com.namoadigital.prj001.ui.act020;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.TProduct_Serial;

import java.util.ArrayList;

/**
 * Created by d.luche on 17/05/2017.
 */

public interface Act020_Main_View {

    void showPD(String progress_type);

    void setRecordInfo(long record_size, long record_page);

    void loadProductSerialList(ArrayList<TProduct_Serial> prod_serial_list);

    void showQtyExceededMsg(long record_page ,long record_count);

    void callAct006(Context context);

    void callAct009(Context context, Bundle bundle);

    void callAct011(Context context, Bundle bundle);

}
