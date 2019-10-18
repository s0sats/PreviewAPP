package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.adapter.Generic_Results_Adapter;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Pack_Express_Local;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.service.WS_SO_Pack_Express_Local;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.MD_Operation_Sql_003;
import com.namoadigital.prj001.sql.MD_Partner_Sql_SS;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_013;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_005;
import com.namoadigital.prj001.sql.Sql_Act040_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main_Presenter_Impl implements Act040_Main_Presenter {

    private Context context;
    private Act040_Main mView;
    private HMAux hmAux_Trans;
    private SO_Pack_ExpressDao so_pack_expressDao;
    private SO_Pack_Express_LocalDao so_pack_express_localDao;
    private MD_ProductDao md_productDao;
    private MD_PartnerDao md_partnerDao;
    private MD_SiteDao mdSiteDao;
    private MD_OperationDao mdOperationDao;
    private MD_Product_SerialDao productSerialDao;
    private MD_Site_ZoneDao zoneDao;

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, SO_Pack_Express_LocalDao so_pack_express_localDao, MD_ProductDao md_productDao, MD_PartnerDao md_partnerDao, MD_SiteDao mdSiteDao, MD_Site_ZoneDao zoneDao, MD_OperationDao mdOperationDao, MD_Product_SerialDao productSerialDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.so_pack_expressDao = so_pack_expressDao;
        this.so_pack_express_localDao = so_pack_express_localDao;
        this.md_productDao = md_productDao;
        this.md_partnerDao = md_partnerDao;
        this.mdSiteDao = mdSiteDao;
        this.mdOperationDao = mdOperationDao;
        this.productSerialDao = productSerialDao;
        this.zoneDao = zoneDao;
    }

    @Override
    public void searchSO_Pack_Express(long customer_code, String express_code) {
        mView.loadSO_Pack_Express(
                so_pack_expressDao.getByString(
                        new SO_Pack_Express_Sql_005(
                                customer_code,
                                ToolBox_Con.getPreference_Site_Code(context),
                                ToolBox_Con.getPreference_Operation_Code(context),
                                express_code
                        ).toSqlQuery()
                ),
                express_code
        );
    }

//    @Override
//    public void setMD_Partner(long customer_code, long partner_code) {
//        mView.setPartner(
//                md_partnerDao.getByString(
//                        new MD_Partner_Sql_002(
//                                customer_code,
//                                (int) partner_code
//                        ).toSqlQuery()
//                )
//        );
//    }

//    @Override
//    public void checkJump(long customer_code) {
//        int qtd = 0;
//
//        try {
//            qtd = Integer.parseInt(md_productDao.query_HM(
//                    new MD_Product_Sql_006(
//                            customer_code
//                    ).toSqlQuery()
//            ).get(0).get("count"));
//        } catch (Exception e) {
//        }
//
//        if (qtd == 0) {
//
//        } else if (qtd == 1) {
//            //mView.jumpToOne();
//        } else {
//        }
//    }

    @Override
    public void loadPartners(String partner_code) {
        ArrayList<HMAux> partnerList = (ArrayList<HMAux>) md_partnerDao.query_HM(
                new MD_Partner_Sql_SS(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setPartnerList(partnerList);
        //
        if (partnerList.size() == 1) {
//            setMD_Partner(
//                    Long.parseLong(partnerList.get(0).get("customer_code")),
//                    Long.parseLong(partnerList.get(0).get(SearchableSpinner.ID))
//            );
            mView.setPartner(partnerList.get(0));
            //
            mView.disablePartnerSelector();
        } else if (!partner_code.equalsIgnoreCase("-1")) {
            boolean partnerFound = false;
            for (HMAux aux : partnerList) {
                if (aux.get(SearchableSpinner.CODE).equalsIgnoreCase(partner_code)) {
                    partnerFound = true;
                    mView.setPartner(aux);
                    break;
                }
            }
            //Vale a pena tratar
            if (!partnerFound) {
                //Mensagem de erro ?!
            }
        }
    }

    @Override
    public MD_Product getProdutctInfo(long product_code) {
        MD_Product md_product = null;
        //
        md_product = md_productDao.getByString(
                new MD_Product_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code
                ).toSqlQuery()
        );

        return md_product;
    }

    @Override
    public void onCreateSo_Pack_Express(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, boolean connectionStatusAlter) {
        SO_Pack_Express_Local so_pack_express_local = new SO_Pack_Express_Local();
        MD_Site md_site = getSiteInfo();
        MD_Operation md_operation = getOperationInfo();
        MD_Site_Zone md_zone = getZoneInfo();
        //
        if (md_site == null ||  md_zone == null|| md_operation == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_site_or_operation_not_found_ttl"),
                    hmAux_Trans.get("alert_site_or_operation_not_found_msg")
            );
            //
            return;
        }
        //
        long nTemp = Long.parseLong(so_pack_express_localDao.getByStringHM(
                new SO_Pack_Express_Local_Sql_006(
                        mSo_pack_express.getCustomer_code(),
                        mSo_pack_express.getSite_code(),
                        mSo_pack_express.getOperation_code(),
                        mSo_pack_express.getProduct_code(),
                        mSo_pack_express.getExpress_code()
                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));
        //
        so_pack_express_local.setCustomer_code(mSo_pack_express.getCustomer_code());
        so_pack_express_local.setSite_code(Long.parseLong(md_site.getSite_code()));
        so_pack_express_local.setExec_site_code(Integer.parseInt(md_site.getSite_code()));
        so_pack_express_local.setExec_site_id(md_site.getSite_id());
        so_pack_express_local.setExec_site_desc(md_site.getSite_desc());
        so_pack_express_local.setExec_zone_code(md_zone.getZone_code());
        so_pack_express_local.setExec_zone_id(md_zone.getZone_id());
        so_pack_express_local.setExec_zone_desc(md_zone.getZone_desc());
        so_pack_express_local.setOperation_code(md_operation.getOperation_code());
        so_pack_express_local.setOperation_id(md_operation.getOperation_id());
        so_pack_express_local.setOperation_desc(md_operation.getOperation_desc());
        so_pack_express_local.setProduct_code(mSo_pack_express.getProduct_code());
        so_pack_express_local.setProduct_id(md_product.getProduct_id());
        so_pack_express_local.setProduct_desc(md_product.getProduct_desc());
        so_pack_express_local.setExpress_code(mSo_pack_express.getExpress_code());
        so_pack_express_local.setExpress_tmp(nTemp);
        so_pack_express_local.setPartner_code(md_partner.getPartner_code());
        so_pack_express_local.setSerial_id(serial);
        so_pack_express_local.setStatus("NEW");
        //
        so_pack_express_local.setSo_desc(mSo_pack_express.getPack_desc());

        so_pack_express_local.setSo_status(Constant.SYS_STATUS_WAITING_SYNC);
        so_pack_express_local.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        //
        so_pack_express_localDao.addUpdate(so_pack_express_local);
        //
        //executeSO_Pack_Express_Local(connectionStatusAlter);
        executeSerialSave(connectionStatusAlter);
    }

    private MD_Site_Zone getZoneInfo() {
        MD_Site_Zone md_site_zone = null;
        //
        md_site_zone = zoneDao.getByString(
                new MD_Site_Zone_Sql_003(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_Site_Code(context)),
                    ToolBox_Con.getPreference_Zone_Code(context)
                ).toSqlQuery()
        );
        //
        return md_site_zone;
    }

    private MD_Site getSiteInfo() {
        MD_Site md_site = null;
        //
        md_site = mdSiteDao.getByString(
                new MD_Site_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Site_Code(context)
                ).toSqlQuery()
        );
        //
        return md_site;
    }

    private MD_Operation getOperationInfo() {
        MD_Operation mdOperation = null;
        //
        mdOperation = mdOperationDao.getByString(
                new MD_Operation_Sql_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        ToolBox_Con.getPreference_Operation_Code(context)
                ).toSqlQuery()
        );
        //
        return mdOperation;
    }

    @Override
    public boolean checkOrderAlreadyExists(long customer_code, String site_code, long operation_code, long product_code, String express_code, String serial_id) {
        HMAux auxPack = so_pack_express_localDao.getByStringHM(
                new SO_Pack_Express_Local_Sql_013(
                        customer_code,
                        site_code,
                        operation_code,
                        product_code,
                        express_code,
                        serial_id
                ).toSqlQuery()
        );
        //
        if (auxPack != null) {
            boolean packExistis = (auxPack.containsKey(SO_Pack_Express_Local_Sql_013.ALREADY_NEW_EXPRESS_ORDER) && ToolBox_Inf.convertStringToInt(auxPack.get(SO_Pack_Express_Local_Sql_013.ALREADY_NEW_EXPRESS_ORDER)) > 0);
            //
            if (packExistis) {
                return true;
            }
        }
        //
        return false;
    }

    @Override
    public void executeSerialSave(boolean connectionStatusAlter) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_save_ttl"),
                    hmAux_Trans.get("progress_serial_save_msg")
            );

            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            //Como ha chama de WS encadeada, esse param é setado para true
            //assim se não houver seriala ser enviado, o processo retorna sem erro.
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            if(!mView.isExitProcess()) {
                if (!connectionStatusAlter) {
                    connectionStatusAlter = true;
                    mView.setConnectionStatusAlter(connectionStatusAlter);
                    //
                    //ToolBox_Inf.showNoConnectionDialog(context);
                    mView.showMsg(
                            hmAux_Trans.get("express_send_error_ttl"),
                            hmAux_Trans.get("express_send_error_msg")
                    );
                } else {
                }

                mView.showMsgToast(hmAux_Trans.get("toast_express_saved_msg"));

                mView.automationCleanForm();
            }else{
                onBackPressedClicked(null,null);
            }
        }
    }

    @Override
    public void executeSO_Pack_Express_Local(boolean connectionStatusAlter) {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_SO_Pack_Express_Local.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_sync_express_ttl"),
                    hmAux_Trans.get("progress_sync_express_msg")
            );

            Intent mIntent = new Intent(context, WBR_SO_Pack_Express_Local.class);
            Bundle bundle = new Bundle();
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            if(!mView.isExitProcess()) {
                if (!connectionStatusAlter) {
                    connectionStatusAlter = true;
                    mView.setConnectionStatusAlter(connectionStatusAlter);
                    //
                    //ToolBox_Inf.showNoConnectionDialog(context);
                    mView.showMsg(
                            hmAux_Trans.get("express_send_error_ttl"),
                            hmAux_Trans.get("express_send_error_msg")
                    );
                } else {
                }

                mView.showMsgToast(hmAux_Trans.get("toast_express_saved_msg"));

                mView.automationCleanForm();
            }else{
                mView.exitProcessMsg(false);
            }
        }
    }

    @Override
    public void executeSerialSearch(MD_Product mdProduct, String serial_id) {

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
            bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_CODE, mdProduct != null ? String.valueOf(mdProduct.getProduct_code()) : null);
            //bundle.putString(Constant.WS_SERIAL_SEARCH_PRODUCT_ID, product_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_SERIAL_ID, serial_id);
            bundle.putString(Constant.WS_SERIAL_SEARCH_TRACKING, "");
            bundle.putInt(Constant.WS_SERIAL_SEARCH_EXACT, 0);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ArrayList<MD_Product_Serial> localSerialList = hasLocalSerial(mdProduct.getProduct_code(), serial_id, "");
            //
            if (localSerialList.size() > 0) {
                defineSearchResultFlow(mdProduct, serial_id, "", localSerialList, (long) localSerialList.size(), (long) localSerialList.size());
            } else {
                if (mdProduct == null || mdProduct.getAllow_new_serial_cl() == 0) {
                    //
                    mView.showMsg(
                            hmAux_Trans.get("alert_offline_serial_not_found_ttl"),
                            hmAux_Trans.get("alert_product_not_allow_new_serial_msg")
                    );
                } else {
                    defineSearchResultFlow(mdProduct, serial_id, "", localSerialList, (long) localSerialList.size(), (long) localSerialList.size());
                }
            }
        }

    }

    @Override
    public void processSerialSaveResult(HMAux hmSaveResult) {
        if (hmSaveResult.size() > 0) {
            //
            for (Map.Entry<String, String> item : hmSaveResult.entrySet()) {
                HMAux aux = new HMAux();
                String[] pk = item.getKey().split(Constant.MAIN_CONCAT_STRING);
                String status = item.getValue();
                //NESSA TELA, SÓ EXIBE RESULTAOD DO SERIAL SE ERRO.
                if (status.equals("OK")) {
                    continue;
                } else {
                    MD_Product mdProduct = md_productDao.getByString(
                            new MD_Product_Sql_001(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Long.parseLong(pk[0])
                            ).toSqlQuery()
                    );
                    //
                    aux.put(Generic_Results_Adapter.LABEL_TTL, hmAux_Trans.get("serial_result_ttl"));
                    if (mdProduct != null) {
                        aux.put(Generic_Results_Adapter.LABEL_ITEM_1, hmAux_Trans.get("product_result_lbl"));
                        aux.put(Generic_Results_Adapter.VALUE_ITEM_1, mdProduct.getProduct_code() + " - " + mdProduct.getProduct_id() + " - " + mdProduct.getProduct_desc());
                    }
                    aux.put(Generic_Results_Adapter.LABEL_ITEM_2, hmAux_Trans.get("serial_result_lbl"));
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_2, pk[1]);
                    aux.put(Generic_Results_Adapter.LABEL_ITEM_3, hmAux_Trans.get("express_status"));
                    aux.put(Generic_Results_Adapter.VALUE_ITEM_3, status);
                    //
                    mView.addWsAuxResult(aux);
                }
            }
        }
    }

    public void defineSearchResultFlow(MD_Product mdProduct, String serial_id, String tracking, ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        //A condição do if nunca DEVERIA acontecer, pois a consulta só existe se produto != null
//        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
//            mView.showMsg(
//                    hmAux_Trans.get("alert_no_serial_found_ttl"),
//                    hmAux_Trans.get("alert_no_serial_found_msg")
//            );
//        } else {

        ArrayList<MD_Product_Serial> results = processEqualCheck(mdProduct, serial_id, tracking, serial_list);

        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");
        bundle.putLong(MD_ProductDao.PRODUCT_CODE, mdProduct != null ? mdProduct.getProduct_code() : -1L);

        if (results.size() != 0) {
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
        } else {
            if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(serial_id)) {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            } else {
                bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
                bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
            }
        }

        bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serial_id);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
        //
        mView.callAct048(context, bundle);
        //}
    }

    private ArrayList<MD_Product_Serial> processEqualCheck(MD_Product mdProduct, String serial_id, String tracking, ArrayList<MD_Product_Serial> serial_list) {
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
                if (serial_id.isEmpty()) {
                    res += "0";
                } else {
                    if (serial_id.equalsIgnoreCase(psAux.getSerial_id())) {
                        res += "1";
                    } else {
                        res += "0";
                    }
                }
                //Validação de tracking não é necessaria aqui
                res += "1";
//                if (tracking.isEmpty()) {
//                    res += "1";
//                } else {
//                    int mSize = psAux.getTracking_list().size();
//                    //
//                    if (mSize == 0) {
//                        res += "0";
//                    } else {
//                        for (int i = 0; i < mSize; i++) {
//                            if (tracking.equalsIgnoreCase(psAux.getTracking_list().get(i).getTracking())) {
//                                res += "1";
//                                break;
//                            }
//                        }
//                    }
//                }
                //
                if (res.equalsIgnoreCase("111")) {
                    results.add(psAux);
                }
            }

            return results;
        }
    }

    private ArrayList<MD_Product_Serial> hasLocalSerial(long product_code, String serial_id, String tracking) {
        ArrayList<MD_Product_Serial> serial_list =
                (ArrayList<MD_Product_Serial>) productSerialDao.query(
                        new Sql_Act040_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                ToolBox_Con.getPreference_Site_Code(context),
                                String.valueOf(product_code),
                                serial_id,
                                tracking
                        ).toSqlQuery()
                );
        //
        return serial_list;
    }

    @Override
    public void extractSearchResult(MD_Product mdProduct, String serial_id, String result) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        TSerial_Search_Rec rec = gson.fromJson(
                result,
                TSerial_Search_Rec.class);
        //
        ArrayList<MD_Product_Serial> serial_list = rec.getRecord();
        //
        defineSearchResultFlow(mdProduct, serial_id, "", serial_list, rec.getRecord_count(), rec.getRecord_page());
    }

    public void checkSerialUpdateRequired(long product_code, String serial_id) {
        MD_Product_Serial productSerial =
                productSerialDao.getByString(
                        new MD_Product_Serial_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                product_code,
                                serial_id
                        ).toSqlQuery()
                );
        //
        if(productSerial == null || productSerial.getUpdate_required() == 0){
            onBackPressedClicked(null, null);
        }else{
            if(ToolBox_Con.isOnline(context)) {
                mView.setExitProcess(true);
                //
                executeSerialSave(mView.isConnectionStatusAlter());
            }else{
                onBackPressedClicked(null, null);
            }
        }
    }

    @Override
    public void onBackPressedClicked(Long product_code, String serial_id) {
        if(product_code != null && serial_id != null && serial_id.length() > 0) {
            checkSerialUpdateRequired(
                    product_code,
                    serial_id
            );
        }else {
            if(ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, ConstantBaseApp.PROFILE_MENU_SO_PARAM_DIRECT_EXPRESS_ORDER)){
                mView.callAct005(context);
            }else {
                mView.callAct021(context);
            }
        }

    }
}
