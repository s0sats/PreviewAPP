package com.namoadigital.prj001.ui.act068;

import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;

import java.util.ArrayList;

public interface Act068_Main_Contract {

   interface I_View{

       void setPendenciesQty(int qty);

       void setProduct(ArrayList<MD_Product> list);

       void showMsg(String title, String msg);

       void showPD(String title, String msg);

       void setWsProcess(String wsProcess);

       void callAct072(Bundle bundle);

       void callAct005();

       void callAct076(Bundle bundle);

       void showResult(ArrayList<HMAux> resultList);
   }

   interface I_Presenter{
       void getPendencies();

       void getMD_Products();

       void onBackPressedClicked();

       void checkPendenciesFlow(int pendencies_qty);

       void executeSerialSearch(String product_id, String serial_id, String tracking);

       void extractSearchResult(String result);

       void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page);

       boolean hasItensToSend();

       void executeWSTicketSave();

       void processSaveReturn(String jsonResult);

       boolean verifyProductForForm();
   }
}
