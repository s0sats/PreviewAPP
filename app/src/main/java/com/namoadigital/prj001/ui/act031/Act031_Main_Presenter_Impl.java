package com.namoadigital.prj001.ui.act031;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_TrackingDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tracking;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Tracking_Search;
import com.namoadigital.prj001.service.WS_Serial_Tracking_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tracking_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by neomatrix on 03/07/17.
 */

public class Act031_Main_Presenter_Impl implements Act031_Main_Presenter {


    private Context context;
    private Act031_Main_View mView;
    private HMAux hmAux_Trans;
    private MD_ProductDao mdProductDao;
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_TrackingDao trackingDao;
    private long product_code;
    private boolean new_serial;
    private ArrayList<MD_Product_Serial_Tracking> tracking_list;

    public Act031_Main_Presenter_Impl(Context context, Act031_Main_View mView, HMAux hmAux_Trans, MD_ProductDao mdProductDao, MD_Product_SerialDao serialDao, MD_Product_Serial_TrackingDao trackingDao, long product_code, boolean new_serial, ArrayList<MD_Product_Serial_Tracking> tracking_list) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.mdProductDao = mdProductDao;
        this.serialDao = serialDao;
        this.trackingDao = trackingDao;
        this.product_code = product_code;
        this.new_serial = new_serial;
        this.tracking_list = tracking_list;
    }

    /**
     * Fluxo de Serial quando
     *
     * @param serial
     */
    @Override
    public void serialFlow(String serial) {

        if (hasSerial(serial)) {

            if (ToolBox_Con.isOnline(context)) {
                mView.showPD(
                        hmAux_Trans.get("progress_serial_search_ttl"),
                        hmAux_Trans.get("progress_serial_search_msg")
                );
                //
                executeSerialSearch(product_code, serial);
            } else {
                ToolBox_Inf.showNoConnectionDialog(context);
            }

        } else {
            mView.fieldFocus();
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_no_serial_typed_title"),
                    hmAux_Trans.get("alert_no_serial_typed_msg")
            );
        }
    }

    public boolean hasSerial(String serial) {
        //Verifica se Serial foi preenchido
        if (serial.trim().length() > 0) {
            return true;
        }
        return false;
    }

    public void executeSerialSearch(Long product_code, String serial_id) {
        mView.setWs_process(Act031_Main.WS_SEARCH_SERIAL);

        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_SAVE_PROCESS, true);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_NEW_PROCESS, new_serial);
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void getProductInfo() {
        MD_Product md_product = null;
        md_product = mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
        //
        if (md_product.getCustomer_code() > 0) {
            mView.setProductValues(md_product);
        } else {
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }


    }

    @Override
    public void executeSaveSerial(Long product_code, String serial_id, boolean save_serial) {
        mView.setWs_process(Act031_Main.WS_SAVE_SERIAL);
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_save_ttl"),
                hmAux_Trans.get("progress_serial_save_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
//        bundle.putLong(Constant.WS_SO_SEARCH_PRODUCT_CODE,product_code);
//        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID,serial_id);
//        bundle.putBoolean(Constant.WS_SO_SEARCH_SAVE_SERIAL,save_serial);
//        bundle.putBoolean(Constant.WS_SO_SEARCH_CREATE_SERIAL,true);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);

    }

    @Override
    public void executeTrackingSearch(long product_code, long serial_code, String tracking) {
        mView.setWs_process(Act031_Main.WS_SEARCH_TRACKING);
        //
        mView.showPD(
                hmAux_Trans.get("progress_tracking_search_ttl"),
                hmAux_Trans.get("progress_tracking_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Tracking_Search.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE, String.valueOf(serial_code));
        bundle.putString(Constant.WS_SERIAL_TRACKING_SEARCH_TRACKING, tracking);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void saveNewSerialInfo(MD_Product_Serial md_product_serial) {
        serialDao.addUpdate(md_product_serial);
        //
        getSerialInfo(product_code, md_product_serial.getSerial_id());
    }

    @Override
    public void saveNewSerialInfo(Long product_code, String serial_id) {
        MD_Product_Serial productSerial = new MD_Product_Serial();
        productSerial.setCustomer_code(ToolBox_Con.getPreference_Customer_Code(context));
        productSerial.setProduct_code(product_code);
        productSerial.setSerial_code(0);
        productSerial.setSerial_id(serial_id);
        //
        serialDao.addUpdate(productSerial);
        //
        getSerialInfo(product_code, serial_id);
    }

    @Override
    public void getSerialInfo(Long product_code, String serial_id) {
        HMAux md_product_serial = serialDao.getByStringHM(
                new MD_Product_Serial_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //Reseta tracking_list
       // tracking_list.clear();
        //
        /*if (md_product_serial != null) {
            ArrayList<MD_Product_Serial_Tracking> auxList =
                    (ArrayList<MD_Product_Serial_Tracking>) trackingDao.query(new MD_Product_Serial_Tracking_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(md_product_serial.get(MD_Product_SerialDao.PRODUCT_CODE)),
                                    Long.parseLong(md_product_serial.get(MD_Product_SerialDao.SERIAL_CODE))
                            ).toSqlQuery()
                    );
            //
            tracking_list.addAll(auxList);
        }*/
        //
        //mView.setSerialValues(md_product_serial);
        mView.setSerialValuesV2(md_product_serial);
    }


    @Override
    public void updateSerialInfo(MD_Product_Serial productSerial) {
        //Remove os tracking para reinserir os que ficaram
        trackingDao.remove(new
                MD_Product_Serial_Tracking_Sql_002(
                        productSerial.getCustomer_code(),
                        productSerial.getProduct_code(),
                        productSerial.getSerial_code()
                ).toSqlQuery()
        );
        //Salva dados alterados do S.O
        serialDao.addUpdate(productSerial);
        //
        refreshUI(productSerial.getProduct_code(),productSerial.getSerial_id());
        //
        if (ToolBox_Con.isOnline(context)) {
            //Chama consulta de S.O informando qe o serial precisa ser alterado.
            executeSaveSerial(productSerial.getProduct_code(), productSerial.getSerial_id(), true);
        } else {
            /*
            *
            * MODIFICAR PARA POSIBILITAR OFFLINE
            *
            *  MODIFICAR VAR new_serial PARA FALSE;
            *
            * EXIBER MSG E CHAMAR O METODO getSerialInfo
            *
            * */
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public void processSerialSaveResult(long product_code, String serial_id, HMAux hmSaveResult) {
        if (hmSaveResult.size() > 0) {
            ArrayList<HMAux> returnList = new ArrayList<>();
            String ttl = "";
            String msg = "";
            //
            for (Map.Entry<String, String> item : hmSaveResult.entrySet()) {
                HMAux aux = new HMAux();
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();

                MD_Product mdProduct = mdProductDao.getByString(
                        new MD_Product_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                Long.parseLong(pk[0])
                        ).toSqlQuery()
                );
                //
                if (mdProduct != null) {
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mdProduct.getProduct_code() + " - " + mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
                }
                aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);
                returnList.add(aux);
                //
                if (product_code == Long.parseLong(pk[0])
                        && serial_id.equals(pk[1])
                        ) {

                    if (status.equals("OK")) {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_ok_msg");
                    } else {
                        ttl = hmAux_Trans.get("alert_save_serial_return_ttl");
                        msg = hmAux_Trans.get("alert_save_serial_error_msg") + "\n" + status;

                    }
                }
            }
            //Atualiza dados dos serial na tela e spinners
            //getSerialInfo(product_code, serial_id);
            //
            //if(returnList.size() == 1){
            if (returnList.size() == 1) {
                mView.showSingleResultMsg(ttl, msg);
            } else {
                mView.showSerialResults(returnList);
            }
        } else {
            mView.showSingleResultMsg(
                    hmAux_Trans.get("alert_save_serial_return_ttl"),
                    hmAux_Trans.get("alert_no_serial_return_msg")
            );
        }
    }

    @Override
    public void processTrackingResult(HMAux auxResult, MD_Product_Serial serialObj) {
        if (auxResult.containsKey(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY)) {
            if (auxResult.get(WS_Serial_Tracking_Search.TRACKING_RESULT_KEY).equals(WS_Serial_Tracking_Search.NOT_EXISTS)) {
                //
                tracking_list.add(
                        buildTrackingObj(serialObj, mView.getSearched_tracking())
                );
                //
                mView.appendTracking(mView.getSearched_tracking());
                //
                mView.setTrackingListChanged(true);
                //
                mView.cleanSearched_tracking();
                //
                mView.scrollToTracking();
            } else {
                mView.showAlertDialog(
                        hmAux_Trans.get("alert_tracking_unavailable_ttl"),
                        hmAux_Trans.get("alert_tracking_unavailable_msg")
                );
            }
        }
    }

    private MD_Product_Serial_Tracking buildTrackingObj(MD_Product_Serial serialObj, String searched_tracking) {
        MD_Product_Serial_Tracking auxTracking = new MD_Product_Serial_Tracking();
        //
        auxTracking.setTracking(searched_tracking);
        auxTracking.setPk(serialObj);
        //
        return auxTracking;
    }

    @Override
    public boolean isTrackingListed(String tracking) {

        for (int i = 0; i < tracking_list.size(); i++) {
            if (tracking_list.get(i).getTracking().equals(tracking)) {
                return true;
            }
        }
        return false;
    }

    private void refreshUI(long product_code, String serial_id){
        new_serial = false;
        //Reseta tracking_list
        tracking_list.clear();
        //
        mView.setTrackingListChanged(false);
        //
        getSerialInfo(product_code, serial_id);
    }


    @Override
    public void onBackPressedClicked() {
        mView.callAct030(context);
    }

}
