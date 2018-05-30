package com.namoadigital.prj001.ui.act030;

import android.content.Context;
import android.os.Bundle;

import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act030_Main_View {

    void showPD();

    void setRecordInfo(long record_size, long record_page);

    void loadProductSerialList(ArrayList<MD_Product_Serial> prod_serial_list);

    void showQtyExceededMsg(long record_page ,long record_count);

    void showNewSerialMsg();

    void callAct005(Context context);

    void callAct031(Context context, Bundle bundle);

    void setWs_process(String ws_process);

    void setProductInfoToDrawer(MD_Product md_product);

    void setTProductSerial(MD_Product_Serial serial);

    void setProduto(ArrayList<MD_Product> list);

}
