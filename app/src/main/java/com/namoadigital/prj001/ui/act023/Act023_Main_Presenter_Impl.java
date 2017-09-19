package com.namoadigital.prj001.ui.act023;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.GE_Custom_Form_OperationDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.Sync_ChecklistDao;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.SM_SO;
import com.namoadigital.prj001.receiver.WBR_SO_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.service.WS_SO_Search;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act008_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by d.luche on 22/06/2017.
 */

public class Act023_Main_Presenter_Impl implements Act023_Main_Presenter {

    private Context context;
    private Act023_Main mView;
    private String requesting_process;
    private Bundle bundle;
    private Sync_ChecklistDao syncChecklistDao;
    private MD_ProductDao mdProductDao;
    private Long product_code;
    private HMAux hmAux_Trans;
    private GE_Custom_Form_OperationDao formOperationDao;
    private MD_Product_SerialDao serialDao;
    private boolean isSchedule;
    private boolean downloadStarted = false;

    public Act023_Main_Presenter_Impl(Context context, Act023_Main mView, String requesting_process, Bundle bundle, Sync_ChecklistDao syncChecklistDao, MD_ProductDao mdProductDao, Long product_code, HMAux hmAux_Trans, GE_Custom_Form_OperationDao formOperationDao, MD_Product_SerialDao serialDao, boolean isSchedule) {
        this.context = context;
        this.mView = mView;
        this.requesting_process = requesting_process;
        this.bundle = bundle;
        this.syncChecklistDao = syncChecklistDao;
        this.mdProductDao = mdProductDao;
        this.product_code = product_code;
        this.hmAux_Trans = hmAux_Trans;
        this.formOperationDao = formOperationDao;
        this.serialDao = serialDao;
        this.isSchedule = isSchedule;
    }

    @Override
    public void getProductInfo() {
        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                getNFormProductInfoFlow();
                break;
            case Constant.MODULE_SO:
            case Constant.MODULE_SO_SEARCH_SERIAL:
            case Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS:
                getSOProductInfoFlow();
                break;
            default:
                break;
        }
    }

    private void getNFormProductInfoFlow() {
        MD_Product md_product = null;
        if (isSchedule) {
            md_product = getScheduledProductInfo();

        } else {
            md_product = getMDProduct();
        }
        //
        if (isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }
    }

    private void getSOProductInfoFlow() {
        MD_Product md_product = null;
        md_product = getMDProduct();
        //
        if (isValidProduct(md_product)) {
            mView.setProductValues(md_product);
        } else {
            mView.showAlertDialog(
                    hmAux_Trans.get("alert_product_not_found_title"),
                    hmAux_Trans.get("alert_product_not_found_msg")
            );
        }


    }

    private MD_Product getScheduledProductInfo() {

        return mdProductDao.getByString(
                new Sql_Act008_002(
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        bundle.getString(Constant.ACT009_CUSTOM_FORM_TYPE),
                        bundle.getString(Constant.ACT010_CUSTOM_FORM_CODE),
                        bundle.getString(Constant.ACT010_CUSTOM_FORM_VERSION),
                        bundle.getString(Constant.ACT013_CUSTOM_FORM_DATA)
                ).toSqlQuery()
        );
    }

    private MD_Product getMDProduct() {

        return mdProductDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );
    }

    @Override
    public void getSerialInfo(Long product_code, String serial_id) {
        //
        HMAux md_product_serial = serialDao.getByStringHM(
                new MD_Product_Serial_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        //

        mView.setSerialValues(md_product_serial);

    }

    @Override
    public void updateSerialInfo(MD_Product_Serial productSerial) {
        //Salva dados alterados do S.O
        serialDao.addUpdateTmp(productSerial);
        if (ToolBox_Con.isOnline(context)) {
            //Chama consulta de S.O informando qe o serial precisa ser alterado.
            // executeSoDownload(productSerial.getProduct_code(), productSerial.getSerial_id(), true);
            executeSerialSave();
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    private boolean isValidProduct(MD_Product md_product) {
        //Erro, produto não encontrado
        if (md_product != null && md_product.getProduct_code() > 0) {
            return true;
        }
        return false;
    }


    @Override
    public void validadeSerialFlow(String serial, int required, int allow_new) {

        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                break;
            case Constant.MODULE_SO:
            case Constant.MODULE_SO_SEARCH_SERIAL:
            case Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS:
                serialSOFlow(serial);
                break;
            default:
                break;
        }

    }

    /**
     * Fluxo de Serial quando
     *
     * @param serial
     */
    private void serialSOFlow(String serial) {

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


    @Override
    public boolean hasSerial(String serial) {
        //Verifica se Serial foi preenchido
        if (serial.trim().length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void defineForwardFlow(Object param) {

        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                break;

            case Constant.MODULE_SO:
            case Constant.MODULE_SO_SEARCH_SERIAL:
            case Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS:
                MD_Product_Serial serial = (MD_Product_Serial) param;
                //
                executeSoDownload(serial.getProduct_code(), serial.getSerial_id());
                //


//                if(checkSoListExists(soList)){
//                   // bundle.putString(Constant.ACT023_SO_HEADER_LIST,soList);
//                    //mView.callAct024(context,bundle);
//
//                }else {
//                    //
//                    mView.showAlertDialog(
//                            hmAux_Trans.get("alert_no_so_found_ttl"),
//                            hmAux_Trans.get("alert_no_so_found_msg")
//                    );
//                }
                break;

            default:
                break;

        }
    }

    private boolean checkSoListExists(String soList) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        ArrayList<SM_SO> sos = gson.fromJson(
                soList,
                new TypeToken<ArrayList<SM_SO>>() {
                }.getType());
        //
        if (sos != null && sos.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void defineBackFlow() {
        switch (requesting_process) {

            case Constant.MODULE_CHECKLIST:
                //mView.callAct008(context,product_code);
                mView.callAct022(context);
                break;
            case Constant.MODULE_SO_SEARCH_SERIAL:
                mView.callAct025(context);
                break;
            case Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS:
                mView.callAct021(context);
                break;
            case Constant.MODULE_SO:
            default:
                mView.callAct022(context);
                break;

        }
    }

    @Override
    public void executeSerialSearch(Long product_code, String serial_id) {
        mView.setWs_process(Act023_Main.SO_WS_SEARCH_SERIAL);

        Intent mIntent = new Intent(context, WBR_Serial_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, String.valueOf(product_code));
        bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, "");
        bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
        bundle.putBoolean(Constant.WS_SERIAL_SEARCH_SAVE_PROCESS, true);
        bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 1);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSerialSave() {
        mView.setWs_process(Act023_Main.SO_WS_SERIAL_SAVE);
        //
        mView.showPD(
                hmAux_Trans.get("progress_serial_search_ttl"),
                hmAux_Trans.get("progress_serial_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_Serial_Save.class);
        Bundle bundle = new Bundle();
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSoDownload(Long product_code, String serial_id) {
        mView.setWs_process(Act023_Main.SO_WS_DOWNLOAD_SO);
        //
        mView.showPD(
                hmAux_Trans.get("progress_so_search_ttl"),
                hmAux_Trans.get("progress_so_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Search.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.WS_SO_SEARCH_PRODUCT_CODE, product_code);
        bundle.putString(Constant.WS_SO_SEARCH_SERIAL_ID, serial_id);
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
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
            getSerialInfo(product_code, serial_id);
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
    public void processSoDownloadResult(HMAux so_download_result) {
        if (so_download_result.containsKey(WS_SO_Search.SO_PREFIX_CODE) && so_download_result.containsKey(WS_SO_Search.SO_LIST_QTY)) {
            if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 0) {
                //
                mView.showAlertDialog(
                        hmAux_Trans.get("alert_no_so_found_ttl"),
                        hmAux_Trans.get("alert_no_so_found_msg")
                );

            } else if (Integer.parseInt(so_download_result.get(WS_SO_Search.SO_LIST_QTY)) == 1) {
                //
                if (so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).contains(Constant.MAIN_CONCAT_STRING)) {
                    String[] so_prefix_code = so_download_result.get(WS_SO_Search.SO_PREFIX_CODE).split(Constant.MAIN_CONCAT_STRING);
                    Bundle bundleSingleSO = new Bundle();
                    //
                    bundleSingleSO.putString(SM_SODao.SO_PREFIX, so_prefix_code[0]);
                    bundleSingleSO.putString(SM_SODao.SO_CODE, so_prefix_code[1]);
                    //
                    mView.callAct027(context, bundleSingleSO);
                } else {
                    //SE NÃO TEM O PARAMETRO COM UNICA S.O BAIXADA, JOGA NA LISTA DE S.O
                    mView.callAct026(context);
                }
            } else {
                mView.callAct026(context);
            }
        } else {
            ToolBox_Inf.alertBundleNotFound(mView,hmAux_Trans);
        }
    }

    @Override
    public void saveSerialInfo(MD_Product_Serial md_product_serial) {
        //Salva dados do serial
        serialDao.addUpdateTmp(md_product_serial);
        //Continua fluxo antigo
        getSerialInfo(md_product_serial.getProduct_code(),md_product_serial.getSerial_id());

    }

    @Override
    public void onBackPressedClicked() {
        defineBackFlow();
    }
}
