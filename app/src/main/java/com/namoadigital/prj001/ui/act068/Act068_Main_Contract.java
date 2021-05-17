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

       void callAct076();

       void showResult(boolean ticketResult);

       void setSync(int qty);

       void addResultList(ArrayList<HMAux> resultList);

       void callAct070(Bundle buildAct070Bundle);

       void callAct083(Bundle bundle);
   }

   interface I_Presenter{
       void getSync();

       void getPendencies();

       void getMD_Products();

       void onBackPressedClicked();

       void executeSerialSearch(String product_id, String serial_id, String tracking, boolean forceExactSearch);

       void extractSearchResult(String result);

       void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page);

       boolean hasItensToSend();

       void executeWSTicketSave();

       void processSaveReturn(String jsonResult);

       boolean verifyProductForForm();

       void executeWSTicketDownload();

       void defineWsToCall();

       void processWS_SaveReturn(String result);

       void executeTicketSearch(String contract_id, String client_id, String ticket_id);

       void updateTabPreference(String sTag);

       void processSearchByTicketTab(HMAux hmAux);

       void setFragTicketSearchParamsIntoBundle(Bundle bundle, HMAux hmAuxValues);
   }
}
