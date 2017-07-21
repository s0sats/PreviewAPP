package com.namoadigital.prj001.ui.act031;

import com.namoadigital.prj001.model.MD_Product;

/**
 * Created by neomatrix on 03/07/17.
 */

public interface Act031_Main_View {
    void fieldFocus();

    void showPD(String title , String msg);

    void showAlertDialog(String title, String msg);

    void setWs_process(String ws_process);

    void setProductValues(MD_Product md_product);
}
