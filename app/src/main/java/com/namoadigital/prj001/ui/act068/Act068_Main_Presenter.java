package com.namoadigital.prj001.ui.act068;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.model.DataPackage;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Download;
import com.namoadigital.prj001.receiver.WBR_Sync;
import com.namoadigital.prj001.receiver.WBR_TK_Ticket_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.service.WS_TK_Ticket_Download;
import com.namoadigital.prj001.service.WS_Sync;
import com.namoadigital.prj001.service.WS_TK_Ticket_Save;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act068_001;
import com.namoadigital.prj001.sql.Sql_Act068_002;
import com.namoadigital.prj001.sql.Sql_Act069_002;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_008;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Act068_Main_Presenter implements Act068_Main_Contract.I_Presenter {

    private Context context;
    private Act068_Main_Contract.I_View mView;
    private HMAux hmAux_Trans;
    private MD_Product_SerialDao serialDao;
    private MD_ProductDao productDao;
    private TK_TicketDao ticketDao;
    private MD_Product mdProduct;
    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;

    public Act068_Main_Presenter(Context context, Act068_Main_Contract.I_View mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        initDaos();
    }

    private void initDaos() {
        productDao = new MD_ProductDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        serialDao = new MD_Product_SerialDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
        ticketDao = new TK_TicketDao(context,ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM);
    }

    @Override
    public void getMD_Products() {
        ArrayList<MD_Product> productList = (ArrayList<MD_Product>) productDao.query(
            new MD_Product_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
        mView.setProduct(productList);
    }

    @Override
    public void getSync() {
        //
        List<TK_Ticket> tickets = ticketDao.query(
                new Sql_Act068_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = tickets.size();
        //
        mView.setSync(qty);

    }

    @Override
    public void getPendencies() {
        int qty = 0;
        HMAux auxPendencies = ticketDao.getByStringHM(
            new TK_Ticket_Sql_008(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
        if( auxPendencies != null
            && auxPendencies.hasConsistentValue(TK_Ticket_Sql_008.PENDENCIES_QTY)
        ){
            qty = ToolBox_Inf.convertStringToInt(auxPendencies.get(TK_Ticket_Sql_008.PENDENCIES_QTY));
        }
        //
        mView.setPendenciesQty(qty);
    }

    @Override
    public void checkPendenciesFlow(int pendencies_qty) {
        if(pendencies_qty > 0){
            mView.callAct076(new Bundle());
        } else{
            mView.showMsg(
                hmAux_Trans.get("alert_no_pendencies_ttl"),
                hmAux_Trans.get("alert_no_pendencies_msg")
            );
        }
    }

    @Override
    public boolean hasItensToSend() {
        int qtyToSend =
            ToolBox_Inf.convertStringToInt(
                ToolBox_Inf.handleTicketUpdateRequired(context, ToolBox_Con.getPreference_Customer_Code(context))
            );
        //
        return qtyToSend > 0;
    }

    @Override
    public void executeSerialSearch(String product_id, String serial_id, String tracking) {
        mdProduct = searchProduct(product_id);
        mProduct_id = product_id;
        mSerial_id = serial_id;
        mTracking = tracking;

        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Search.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_serial_search_ttl"),
                hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct!= null ?  String.valueOf(mdProduct.getProduct_code()) : null );
            //bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            //LUCHE - 21/01/2020
            //Enquanto não houver o processo de criaçãode ticket via App, não haverá lista offline.
            //Por hora, será exibida msg de que esta offline e para ele buscar nos itens pendentes.
            //offlineSerialSearch();
            mView.showMsg(
                hmAux_Trans.get("alert_no_conection_ttl"),//Title é recurso sys.(usado no metodo showNoConnectionDialog)
                hmAux_Trans.get("alert_no_connection_try_pendencies_msg")

            );
        }
    }

    @Override
    public void executeWSTicketSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_TK_Ticket_Save.class.getName());
            //
            mView.showPD(
                hmAux_Trans.get("dialog_ticket_save_ttl"),
                hmAux_Trans.get("dialog_ticket_save_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Save.class);
            Bundle bundle = new Bundle();
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void extractSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
            result,
            TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page());
    }

    @Override
    public void defineSearchResultFlow(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
            mView.showMsg(
                hmAux_Trans.get("alert_no_serial_found_ttl"),
                hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {
            Bundle bundle = getSerialListActBundle(
                serial_list,
                record_count,
                record_page,
                mdProduct,
                mProduct_id,
                mSerial_id,
                mTracking
            );
            //
            mView.callAct072(bundle);
        }
    }

    private Bundle getSerialListActBundle(ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page, MD_Product mdProduct, String mProduct_id, String mSerial_id, String mTracking) {
        ArrayList<MD_Product_Serial> results = processEqualCheck(serial_list,mdProduct,mProduct_id,mSerial_id,mTracking);

        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");

        if (results.size() != 0) {
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
        } else {
            if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(mSerial_id)) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            } else {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            }
        }
        //
        bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, mSerial_id);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
        //
        bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, mProduct_id); //mdProduct != null ? mdProduct.getProduct_id() : "");
        bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, mSerial_id != null ? mSerial_id : "");
        bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, mTracking != null ? mTracking : "");
        //
        return bundle;
    }

    private ArrayList<MD_Product_Serial> processEqualCheck(ArrayList<MD_Product_Serial> serial_list,MD_Product mdProduct, String mProduct_id, String mSerial_id, String mTracking) {
        ArrayList<MD_Product_Serial> results = new ArrayList<>();

        if (mdProduct == null) {
            return results;
        } else {

            for (MD_Product_Serial psAux : serial_list) {
                String res = "";

                if (!mdProduct.getProduct_id().equalsIgnoreCase(psAux.getProduct_id())) {
                    continue;
                } else {
                    res += "1";
                }
                //
                if (mSerial_id.isEmpty()) {
                    res += "0";
                } else {
                    if (mSerial_id.equalsIgnoreCase(psAux.getSerial_id())) {
                        res += "1";
                    } else {
                        res += "0";
                    }
                }
                //
                if (mTracking.isEmpty()) {
                    res += "1";
                } else {
                    int mSize = psAux.getTracking_list().size();

                    if (mSize == 0) {
                        res += "0";
                    } else {
                        for (int i = 0; i < mSize; i++) {
                            if (mTracking.equalsIgnoreCase(psAux.getTracking_list().get(i).getTracking())) {
                                res += "1";
                                break;
                            }
                        }
                    }
                }

                if (res.equalsIgnoreCase("111")) {
                    results.add(psAux);
                }
            }

            return results;
        }
    }

    @Override
    public void processSaveReturn(String jsonRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn> checkinReturns = null;
        ArrayList<HMAux> resultList = new ArrayList<>();
        //
        if (jsonRet != null && !jsonRet.isEmpty()) {
            try {
                checkinReturns = gson.fromJson(
                    jsonRet,
                    new TypeToken<ArrayList<WS_TK_Ticket_Save.TicketSaveActReturn>>() {
                    }.getType());

            } catch (Exception e) {
                ToolBox_Inf.registerException(getClass().getName(), e);
            }
            //
            if (checkinReturns != null && checkinReturns.size() > 0) {
                boolean ticketResult = true;
                int ticketNextIdx = 0;
                HMAux auxResult = new HMAux();
                //
                for (WS_TK_Ticket_Save.TicketSaveActReturn actReturn : checkinReturns) {
                    String ticketCode = actReturn.getPrefix() + "." + actReturn.getCode();
                    //
                    if (!auxResult.containsKey(ticketCode)
                        || (auxResult.containsKey(ticketCode)
                        &&  !ConstantBaseApp.MAIN_RESULT_OK.equalsIgnoreCase(actReturn.getRetStatus())
                    )
                    ) {
                        //Se erro, verifica se erro de processamento qual erro foi e pega msg
                        auxResult.put(ticketCode, getResultSaveMsgFormmated(actReturn));
                    }
                }
                //For no resumido por ticket montando msg a ser exibida
                for (Map.Entry<String, String> item : auxResult.entrySet()) {
                    HMAux hmAux = new HMAux();
                    //
                    //Monta HmAux
                    hmAux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("ticket_lbl"));
                    hmAux.put(Generic_Results_Adapter.LABEL_ITEM_1, item.getKey());
                    hmAux.put(Generic_Results_Adapter.VALUE_ITEM_1, item.getValue());
                    resultList.add(hmAux);
                }
                //
                mView.showResult(resultList);
            } else {
                mView.showMsg(
                    hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                    hmAux_Trans.get("alert_none_ticket_returned_msg")
                );
            }
        } else {
            mView.showMsg(
                hmAux_Trans.get("alert_none_ticket_returned_ttl"),
                hmAux_Trans.get("alert_none_ticket_returned_msg")
            );
        }
    }

    @Override
    public boolean verifyProductForForm() {
        if(ToolBox_Inf.hasFormProductOutdate(context)){
            if (ToolBox_Con.isOnline(context)) {
                mView.setWsProcess(WS_Sync.class.getName());
                //
                mView.showPD(
                        hmAux_Trans.get("progress_sync_ttl"),
                        hmAux_Trans.get("progress_sync_msg")
                );
                //
                ArrayList<String> data_package = new ArrayList<>();
                data_package.add(DataPackage.DATA_PACKAGE_CHECKLIST);
                //
                Intent mIntent = new Intent(context, WBR_Sync.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.GS_SESSION_APP, ToolBox_Con.getPreference_Session_App(context));
                bundle.putStringArrayList(Constant.GS_DATA_PACKAGE, data_package);
                bundle.putLong(Constant.GS_PRODUCT_CODE, 0);
                bundle.putInt(Constant.GC_STATUS_JUMP, 1);
                bundle.putInt(Constant.GC_STATUS, 1);
                //
                mIntent.putExtras(bundle);
                //
                context.sendBroadcast(mIntent);
                return true;
            }
            return false;
        }else{
            return false;
        }
    }

    @Override
    public void executeWSTicketDownload() {
        if(ToolBox_Con.isOnline(context)){
            mView.setWsProcess(WS_TK_Ticket_Download.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_download_ticket_ttl"),
                    hmAux_Trans.get("dialog_download_ticket_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_TK_Ticket_Download.class);
            Bundle bundle = new Bundle();
            bundle.putString(TK_TicketDao.TICKET_PREFIX, getTicketConcatList());
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);

        }else{
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private String getTicketConcatList() {
        ArrayList<HMAux> auxTickets = getTicketToSync();
        String ticketPKList = "";
        for (HMAux aux : auxTickets) {
            if(aux.hasConsistentValue(Sql_Act069_002.TICKET_PK)){
                ticketPKList += ConstantBaseApp.MAIN_CONCAT_STRING + aux.get(Sql_Act069_002.TICKET_PK);
            }
        }
        //
        return ticketPKList.contains(ConstantBaseApp.MAIN_CONCAT_STRING) ? ticketPKList.substring(ConstantBaseApp.MAIN_CONCAT_STRING.length()) : "";
    }

    private ArrayList<HMAux> getTicketToSync() {
        ArrayList<HMAux> auxTickets = new ArrayList<>();
        //
        auxTickets = (ArrayList<HMAux>) ticketDao.query_HM(
                new Sql_Act068_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        return auxTickets;
    }

    private String getResultSaveMsgFormmated(WS_TK_Ticket_Save.TicketSaveActReturn actReturn) {
        if (actReturn.getRetStatus().equals(ConstantBaseApp.MAIN_RESULT_OK)) {
            return actReturn.getRetStatus();
        } else {
            return actReturn.isProcessError() ? actReturn.getProcessStatus() + "\n" + actReturn.getProcessMsg() : actReturn.getRetStatus() + "\n" + actReturn.getRetMsg();
        }
    }

    private MD_Product searchProduct(String product_id) {
       MD_Product md_product = productDao.getByString(
           new MD_Product_Sql_003(
               ToolBox_Con.getPreference_Customer_Code(context),
               "",
               product_id
           ).toSqlQuery()
       );
       //
       return md_product;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005();
    }
}
