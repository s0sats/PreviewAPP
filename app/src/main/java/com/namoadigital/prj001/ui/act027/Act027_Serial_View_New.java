package com.namoadigital.prj001.ui.act027;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;

import java.util.ArrayList;

/**
 * Created by neomatrix on 12/07/17.
 */

public interface Act027_Serial_View_New {

    void setProductValues(MD_Product md_product);

    void setSerialValues(HMAux md_product_serial);

    void showPD(String title, String msg);

    void showAlertDialog(String title, String msg);

    void showSingleResultMsg(String ttl, String msg);

    void showSerialResults(ArrayList<HMAux> returnList);

}
