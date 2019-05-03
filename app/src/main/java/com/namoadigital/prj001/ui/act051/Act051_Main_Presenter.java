package com.namoadigital.prj001.ui.act051;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.IO_Blind_MoveDao;
import com.namoadigital.prj001.dao.IO_Inbound_ItemDao;
import com.namoadigital.prj001.dao.IO_MoveDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.model.IO_Serial_Process_Record;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.T_IO_Serial_Process_Response;
import com.namoadigital.prj001.receiver.WBR_IO_Serial_Process_Search;
import com.namoadigital.prj001.service.WS_IO_Serial_Process_Search;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_009;
import com.namoadigital.prj001.sql.IO_Move_Order_Item_Sql_010;
import com.namoadigital.prj001.sql.MD_Product_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_003;
import com.namoadigital.prj001.sql.Sql_Act020_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

public class Act051_Main_Presenter implements Act051_Main_Contract.I_Presenter {

    private Context context;
    private Act051_Main_Contract.I_View mView;

    private MD_ProductDao productDao;
    private MD_Product_SerialDao serialDao;

    private HMAux hmAux_Trans;
    private MD_Product mdProduct;

    private String mProduct_id;
    private String mSerial_id;
    private String mTracking;
    private boolean allowBlindMove =true;

    public Act051_Main_Presenter(Context context, Act051_Main_Contract.I_View mView, MD_ProductDao productDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.productDao = productDao;
        this.hmAux_Trans = hmAux_Trans;

        this.serialDao = new MD_Product_SerialDao(context);
    }

    @Override
    public void getMD_Products() {
        ArrayList<MD_Product> productList = (ArrayList<MD_Product>) productDao.query(
                new MD_Product_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setProduto(productList);
    }

    @Override
    public void executeSerialProcessSearch(String product_id, String serial_id, String tracking) {
        mdProduct = searchProduct(product_id);
        mProduct_id = product_id;
        mSerial_id = serial_id;
        mTracking = tracking;
        boolean isOnline = ToolBox_Con.isOnline(context);
        if (isOnline) {
            mView.setWsProcess(WS_IO_Serial_Process_Search.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("dialog_serial_search_ttl"),
                    hmAux_Trans.get("dialog_serial_search_start")
            );
            //
            Intent mIntent = new Intent(context, WBR_IO_Serial_Process_Search.class);
            Bundle bundle = new Bundle();
            //
            bundle.putString(MD_SiteDao.SITE_CODE, ToolBox_Con.getPreference_Site_Code(context));
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct != null ? String.valueOf(mdProduct.getProduct_code()) : null);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, tracking);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
            ToolBox.sendBCStatus(context, "STATUS", hmAux_Trans.get("dialog_serial_search_start"), "", "0");
        } else {
            /**
             *
             *
             * CRIAR METODO QUE GERARÁ ITENS OFFLINE
             *
             * PRIMEIRO NECESSARIO CRIAÇÃO DAS TABELAS DE IO
             *
             *
             */

            ArrayList<IO_Serial_Process_Record> serial_list = hasLocalSerial(product_id, serial_id, tracking);
            //
            defineSearchResultFlow(serial_list, (long) serial_list.size(), (long) serial_list.size(), isOnline);
        }
    }

    public void getPendencies() {

    }

    @Override
    public MD_Product searchProduct(String product_id) {
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

    private ArrayList<IO_Serial_Process_Record> hasLocalSerial(String product_id, String serial_id, String tracking) {
        ArrayList<IO_Serial_Process_Record> serial_process_records = new ArrayList<>();
        ArrayList<HMAux> serial_list = new ArrayList<>();
        List<HMAux> move_list =
                (ArrayList<HMAux>) serialDao.query_HM(
                        new IO_Move_Order_Item_Sql_009(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context),
                                product_id,
                                serial_id,
                                tracking
                        ).toSqlQuery()
                );


        List<HMAux> in_conf_list = (ArrayList<HMAux>) serialDao.query_HM(
                        new IO_Move_Order_Item_Sql_010(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context),
                                product_id,
                                serial_id,
                                tracking
                        ).toSqlQuery()
                );
        serial_list.addAll(move_list);
        serial_list.addAll(in_conf_list);
        getSerialProcessRecordListFromHMaux(serial_process_records, serial_list, serial_id);
        return serial_process_records;
    }

    private void getSerialProcessRecordListFromHMaux(ArrayList<IO_Serial_Process_Record> serial_process_records, ArrayList<HMAux> serial_list, String serial_id) {
        for (HMAux serial_record : serial_list) {
            IO_Serial_Process_Record record = new IO_Serial_Process_Record();

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.CUSTOMER_CODE)) {
                record.setCustomer_code(Long.parseLong(serial_record.get(MD_Product_SerialDao.CUSTOMER_CODE)));
            }
            if (serial_record.hasConsistentValue(MD_Product_SerialDao.PRODUCT_CODE)) {
                record.setProduct_code(Long.parseLong(serial_record.get(MD_Product_SerialDao.PRODUCT_CODE)));
            }

            record.setProduct_id(serial_record.get(MD_Product_SerialDao.PRODUCT_ID));
            record.setProduct_desc(serial_record.get(MD_Product_SerialDao.PRODUCT_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.SERIAL_CODE)) {
                record.setSerial_code(Integer.parseInt(serial_record.get(MD_Product_SerialDao.SERIAL_CODE)));
            }
            if(serial_id != null && serial_id.equalsIgnoreCase(serial_record.get(MD_Product_SerialDao.SERIAL_ID))){
                allowBlindMove = false;
            }else {
                allowBlindMove = true;
            }
            record.setSerial_id(serial_record.get(MD_Product_SerialDao.SERIAL_ID));
            if (serial_record.hasConsistentValue(MD_Product_SerialDao.SITE_CODE)) {
                record.setSite_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.SITE_CODE)));
            }
            record.setSite_id(serial_record.get(MD_Product_SerialDao.SITE_ID));
            record.setSite_desc(serial_record.get(MD_Product_SerialDao.SITE_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.ZONE_CODE)) {
                record.setZone_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.ZONE_CODE)));
            }

            record.setZone_id(serial_record.get(MD_Product_SerialDao.ZONE_ID));
            record.setZone_desc(serial_record.get(MD_Product_SerialDao.ZONE_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.LOCAL_CODE)) {
                record.setLocal_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.LOCAL_CODE)));
            }

            record.setLocal_id(serial_record.get(MD_Product_SerialDao.LOCAL_ID));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.BRAND_CODE)) {
                try {
                    record.setBrand_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.BRAND_CODE)));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    record.setBrand_code(null);
                }
            }

            record.setBrand_id(serial_record.get(MD_Product_SerialDao.BRAND_ID));
            record.setBrand_desc(serial_record.get(MD_Product_SerialDao.BRAND_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.MODEL_CODE)) {
                try {
                    record.setModel_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.MODEL_CODE)));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    record.setModel_code(null);
                }
            }

            record.setModel_id(serial_record.get(MD_Product_SerialDao.MODEL_ID));
            record.setModel_desc(serial_record.get(MD_Product_SerialDao.MODEL_DESC));

            if (serial_record.hasConsistentValue(MD_Product_SerialDao.COLOR_CODE)) {
                try {
                    record.setColor_code(Integer.valueOf(serial_record.get(MD_Product_SerialDao.COLOR_CODE)));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    record.setColor_code(null);
                }
            }

            record.setColor_id(serial_record.get(MD_Product_SerialDao.COLOR_ID));
            record.setColor_desc(serial_record.get(MD_Product_SerialDao.COLOR_DESC));

            if(serial_record.hasConsistentValue(IO_MoveDao.MOVE_TYPE)) {
                record.setProcess_type(serial_record.get(IO_MoveDao.MOVE_TYPE));
            }else if(serial_record.hasConsistentValue(IO_Inbound_ItemDao.STATUS)
            && serial_record.get(IO_Inbound_ItemDao.STATUS).equals(ConstantBaseApp.SYS_STATUS_PENDING)){
                record.setProcess_type(ConstantBaseApp.IO_PROCESS_IN_CONF);
            }

            serial_process_records.add(record);

        }
    }

    @Override
    public void processSearchResult(String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        T_IO_Serial_Process_Response rec = gson.fromJson(
                result,
                T_IO_Serial_Process_Response.class);
        //
        ArrayList<IO_Serial_Process_Record> serial_list = rec.getRecord();
        //Ultimo parametro forçado como true pois a resposta veio do WS
        defineSearchResultFlow(serial_list, rec.getRecord_count(), rec.getRecord_page(), true);
    }

    @Override
    public void defineSearchResultFlow(ArrayList<IO_Serial_Process_Record> serial_list, long record_count, long record_page, boolean isOnline) {
        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_no_serial_found_ttl"),
                    hmAux_Trans.get("alert_no_serial_found_msg")
            );
        } else {

            ArrayList<IO_Serial_Process_Record> results = processEqualCheck(serial_list);

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

            bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, mSerial_id);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
            bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_IS_ONLINE_PROCESS, isOnline);
            bundle.putBoolean(IO_Blind_MoveDao.FLAG_BLIND, allowBlindMove);
            //
            bundle.putString(Constant.FRAG_SEARCH_PRODUCT_ID_RECOVER, mProduct_id); //mdProduct != null ? mdProduct.getProduct_id() : "");
            bundle.putString(Constant.FRAG_SEARCH_SERIAL_ID_RECOVER, mSerial_id != null ? mSerial_id : "");
            bundle.putString(Constant.FRAG_SEARCH_TRACKING_ID_RECOVER, mTracking != null ? mTracking : "");

            mView.callAct052(context, bundle);
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }

    private ArrayList<IO_Serial_Process_Record> processEqualCheck(ArrayList<IO_Serial_Process_Record> serial_list) {
        ArrayList<IO_Serial_Process_Record> results = new ArrayList<>();

        if (mdProduct == null) {
            return results;
        } else {

            for (IO_Serial_Process_Record psAux : serial_list) {
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
}
