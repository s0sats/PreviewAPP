package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.namoa_digital.namoa_library.ctls.MKEditTextNM;
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
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.dao.SoPackExpressPacksLocalDao;
import com.namoadigital.prj001.extensions.TSOServiceSearchDetailObjKt;
import com.namoadigital.prj001.extensions.TSoServiceSearchRecKt;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MD_Site_Zone;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.model.SoPackExpressPacksLocal;
import com.namoadigital.prj001.model.SoPackExpressServicesLocal;
import com.namoadigital.prj001.model.TSO_Service_Search_Detail_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Obj;
import com.namoadigital.prj001.model.TSO_Service_Search_Rec;
import com.namoadigital.prj001.model.TSerial_Search_Rec;
import com.namoadigital.prj001.receiver.WBR_SO_Pack_Express_Local;
import com.namoadigital.prj001.receiver.WBR_SO_Service_Search;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Serial_Search;
import com.namoadigital.prj001.service.WS_SO_Pack_Express_Local;
import com.namoadigital.prj001.service.WS_SO_Service_Search;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.service.WS_Serial_Search;
import com.namoadigital.prj001.sql.MD_Operation_Sql_003;
import com.namoadigital.prj001.sql.MD_Partner_Sql_SS;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_004;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_001;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_011;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_013;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_014;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_015;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_005;
import com.namoadigital.prj001.sql.SoPackExpressPacksLocalSql001;
import com.namoadigital.prj001.sql.SoPackExpressPacksLocalSql004;
import com.namoadigital.prj001.sql.Sql_Act012_004;
import com.namoadigital.prj001.sql.Sql_Act040_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main_Presenter_Impl implements Act040_Main_Presenter {

    private final Context context;
    private final Act040_Main mView;
    private final HMAux hmAux_Trans;
    private final SO_Pack_ExpressDao so_pack_expressDao;
    private final SO_Pack_Express_LocalDao so_pack_express_localDao;
    private final MD_ProductDao md_productDao;
    private final MD_PartnerDao md_partnerDao;
    private final MD_SiteDao mdSiteDao;
    private final MD_OperationDao mdOperationDao;
    private final MD_Product_SerialDao productSerialDao;
    private final MD_Site_ZoneDao zoneDao;
    private final SoPackExpressPacksLocalDao soPackExpressLocalDao;

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, SO_Pack_Express_LocalDao so_pack_express_localDao, MD_ProductDao md_productDao, MD_PartnerDao md_partnerDao, MD_SiteDao mdSiteDao, MD_Site_ZoneDao zoneDao, MD_OperationDao mdOperationDao, MD_Product_SerialDao productSerialDao, SoPackExpressPacksLocalDao soPackExpressLocalDao) {
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
        this.soPackExpressLocalDao = soPackExpressLocalDao;
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
    public void getLastExpressInfoInSiteOper() {
        SO_Pack_Express_Local lastExpressInSiteOper = so_pack_express_localDao.getByString(
            new SO_Pack_Express_Local_Sql_014(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context),
                ToolBox_Con.getPreference_Operation_Code(context)
            ).toSqlQuery()
        );
        //
        mView.setLastExpressInfo(lastExpressInSiteOper);
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
    public void onCreateSo_Pack_Express(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, String billingInfo1, String billingInfo2, String billingInfo3, long bundle_express_tmp) {
        SO_Pack_Express_Local so_pack_express_local = getExpressPackLocal(
                mSo_pack_express.getCustomer_code(),
                mSo_pack_express.getProduct_code(),
                mSo_pack_express.getSite_code(),
                mSo_pack_express.getOperation_code(),
                mSo_pack_express.getExpress_code(),
                (int) bundle_express_tmp);
        //
        if(so_pack_express_local == null) {
            so_pack_express_local = new SO_Pack_Express_Local();
        }
        //
        MD_Site md_site = getSiteInfo();
        MD_Operation md_operation = getOperationInfo();
        MD_Site_Zone md_zone = getZoneInfo();
        //
        if (md_site == null || md_zone == null || md_operation == null) {
            mView.showMsg(
                    hmAux_Trans.get("alert_site_or_operation_not_found_ttl"),
                    hmAux_Trans.get("alert_site_or_operation_not_found_msg")
            );
            //
            return;
        }
        //
        setSoPackExpressLocal(mSo_pack_express, md_partner, md_product, serial, billingInfo1, billingInfo2, billingInfo3, so_pack_express_local, md_site, md_operation, md_zone, Constant.SYS_STATUS_WAITING_SYNC, bundle_express_tmp);
        //
        so_pack_express_localDao.addUpdate(so_pack_express_local);
        //
        mView.setBundle_express_tmp(-1);
        //
        executeSerialSave();
    }

    @Override
    public SO_Pack_Express_Local onCreateSo_Pack_Express_Structure(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, String billingInfo1, String billingInfo2, String billingInfo3) {
        SO_Pack_Express_Local so_pack_express_local = new SO_Pack_Express_Local();
        MD_Site md_site = getSiteInfo();
        MD_Operation md_operation = getOperationInfo();
        MD_Site_Zone md_zone = getZoneInfo();
        //
        setSoPackExpressLocal(mSo_pack_express, md_partner, md_product, serial, billingInfo1, billingInfo2, billingInfo3, so_pack_express_local, md_site, md_operation, md_zone, Constant.SYS_STATUS_PROCESS, -1);
        //
        getPackDefault(so_pack_express_local, mSo_pack_express);
        //
        so_pack_express_localDao.addUpdate(so_pack_express_local);
        return so_pack_express_local;
    }

    private List<SoPackExpressPacksLocal> getPackDefault(SO_Pack_Express_Local so_pack_express_local, SO_Pack_Express so_pack_express) {
        List<SoPackExpressPacksLocal> packs = new ArrayList<>();
        //
        String fileName = ToolBox_Inf.getExpressSOFileName(so_pack_express.getContract_code(), so_pack_express.getProduct_code(), so_pack_express.getCategory_price_code(), so_pack_express.getSite_code(), so_pack_express.getOperation_code());
        File file = new File(ConstantBaseApp.SO_EXPRESS_JSON_PATH, fileName);
        if(file.exists()) {
            String contents = ToolBox_Inf.getContents(file);
            Gson gson = new GsonBuilder().serializeNulls().create();
            TSO_Service_Search_Rec rec = gson.fromJson(contents, TSO_Service_Search_Rec.class);
            List<TSO_Service_Search_Obj> packageDefault = TSoServiceSearchRecKt.getPackageDefault(
                    rec,
                    "P",
                    so_pack_express.getCustomer_code(),
                    so_pack_express.getPrice_list_code(),
                    so_pack_express.getPack_code(),
                    so_pack_express.getPack_desc()
            );

            SoPackExpressPacksLocal soPackExpressPacksLocal = new SoPackExpressPacksLocal(packageDefault.get(0), so_pack_express_local, -1);
            so_pack_express_local.getPacksLocals().add(soPackExpressPacksLocal);
            so_pack_express_localDao.addUpdate(so_pack_express_local);
            //
        }
        return null;
    }

    private void setSoPackExpressLocal(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, String billingInfo1, String billingInfo2, String billingInfo3, SO_Pack_Express_Local so_pack_express_local, MD_Site md_site, MD_Operation md_operation, MD_Site_Zone md_zone, String so_status, long bundle_express_tmp) {
        long nTemp = bundle_express_tmp;
        if(nTemp < 0){
            nTemp = Long.parseLong(so_pack_express_localDao.getByStringHM(
                    new SO_Pack_Express_Local_Sql_006(
                            mSo_pack_express.getCustomer_code(),
                            mSo_pack_express.getSite_code(),
                            mSo_pack_express.getOperation_code(),
                            mSo_pack_express.getProduct_code(),
                            mSo_pack_express.getExpress_code()
                    ).toSqlQuery()
            ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));
            mView.setBundle_express_tmp(nTemp);
        }
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
        if(md_partner != null){
            so_pack_express_local.setPartner_code(md_partner.getPartner_code());
        }else{
            so_pack_express_local.setPartner_code(0);
        }
        so_pack_express_local.setSerial_id(serial);
        so_pack_express_local.setStatus(Constant.SO_EXPRESS_STATUS_NEW);
        //
        so_pack_express_local.setSo_desc(mSo_pack_express.getPack_desc());
        so_pack_express_local.setPipeline_desc(mSo_pack_express.getPipeline_desc());
        so_pack_express_local.setSo_status(so_status);
        so_pack_express_local.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        //
        so_pack_express_local.setBilling_add_inf1_value(billingInfo1);
        so_pack_express_local.setBilling_add_inf2_value(billingInfo2);
        so_pack_express_local.setBilling_add_inf3_value(billingInfo3);
        //
        so_pack_express_local.setBilling_add_inf1_tracking(mSo_pack_express.getBilling_add_inf1_tracking());
        so_pack_express_local.setBilling_add_inf2_tracking(mSo_pack_express.getBilling_add_inf2_tracking());
        so_pack_express_local.setBilling_add_inf3_tracking(mSo_pack_express.getBilling_add_inf3_tracking());
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
    public SO_Pack_Express_Local checkOrderAlreadyExists(long customer_code, String site_code, long operation_code, long product_code, String express_code, String serial_id) {
        SO_Pack_Express_Local auxPack = so_pack_express_localDao.getByString(
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
        return auxPack;
    }

    @Override
    public void executeSerialSave() {
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
                mView.showMsgToast(hmAux_Trans.get("toast_express_saved_msg"));
            }else{
                onBackPressedClicked();
            }
        }
    }

    @Override
    public void executeSO_Pack_Express_Local() {
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
                mView.showMsgToast(hmAux_Trans.get("toast_express_saved_msg"));
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

    @Override
    public boolean hasSerialOrExpressOsPendency() {
        return getExpressSoPendency(hmAux_Trans) > 0 || hasSerialUpdateRequired();
    }

    @Override
    public boolean hasSerialUpdateRequired() {
        List<HMAux> auxSerials = productSerialDao.query_HM(
            new MD_Product_Serial_Sql_004(
                ToolBox_Con.getPreference_Customer_Code(context)
            ).toSqlQuery()
        );
        //
       return auxSerials != null && auxSerials.size() > 0 ;
    }

    /**
     * Metodo que verifica se existe um tracking repitido nos campos de tracking.
     * Varre lista views config como tracking verificando se o texto ja existe na lista de tracking String.
     * Se texto não existir, adiciona na lista e parte para o proximo, se existir retorna erro
     * @param trackingFields
     * @return True se não houver itens duplicados.
     */
    @Override
    public boolean hasNoTrackingDuplicated(ArrayList<MKEditTextNM> trackingFields) {
        List<String> trackingList = new ArrayList<>();
        for (MKEditTextNM trackingField : trackingFields) {
            if(trackingField.getText() != null) {
                String tracking = trackingField.getText().toString().trim();
                if (tracking != null && !tracking.isEmpty()) {
                    if (!trackingList.contains(tracking)) {
                        trackingList.add(tracking);
                    }else{
                        return false;
                    }
                }
            }
        }
        //
        return true;
    }

    /**
     * Metodo que gera msg de tracking duplicada
     * Faz loop na lista de mket e resgata a tag, que tem o helper com o nome do campo pra ser listado.
     * Lista  todos os mket config como tracking, independente dele ter item duplicado ou não
     * @param tracking_duplicated_msg Lbl principal da msg
     * @param trackingFields Lista de mket config como tracking
     * @return Msg de erro formatada.
     */
    @Override
    public String getFormattedTrackingDuplicated(String tracking_duplicated_msg, ArrayList<MKEditTextNM> trackingFields) {
        String finalMsg = tracking_duplicated_msg;
        //
        for (MKEditTextNM trackingField : trackingFields) {
            if(trackingField.getTag() != null) {
                String tracking = trackingField.getTag().toString().trim();
                if (tracking != null && !tracking.isEmpty()) {
                    finalMsg += "\n" + tracking;
                }
            }
        }
        //
        return finalMsg;
    }

    @Override
    public int getExpressSoPendency(HMAux hmAux_Trans) {
        SO_Pack_Express_LocalDao expressLocalDao = new SO_Pack_Express_LocalDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );

        HMAux soExpressPendencies =
                expressLocalDao.getByStringHM(
                        new Sql_Act012_004(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                hmAux_Trans
                        ).toSqlQuery()
                );
        //
        if(soExpressPendencies.hasConsistentValue(Sql_Act012_004.PENDING_QTY)){
            return  Integer.parseInt(soExpressPendencies.get(Sql_Act012_004.PENDING_QTY));
        }
        return 0;
    }

    public void defineSearchResultFlow(MD_Product mdProduct, String serial_id, String tracking, ArrayList<MD_Product_Serial> serial_list, long record_count, long record_page) {
        //A condição do if nunca DEVERIA acontecer, pois a consulta só existe se produto != null
//        if ((serial_list == null || serial_list.size() == 0) && mdProduct == null) {
//            mView.showMsg(
//                    hmAux_Trans.get("alert_no_serial_found_ttl"),
//                    hmAux_Trans.get("alert_no_serial_found_msg")
//            );
//        } else {

//        ArrayList<MD_Product_Serial> results = processEqualCheck(mdProduct, serial_id, tracking, serial_list);

        Bundle bundle = new Bundle();
        bundle.putString(MD_ProductDao.PRODUCT_ID, mdProduct != null ? mdProduct.getProduct_id() : "");
        bundle.putLong(MD_ProductDao.PRODUCT_CODE, mdProduct != null ? mdProduct.getProduct_code() : -1L);

//        if (results.size() != 0) {
//            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
//            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, results);
//        } else {
        if (serial_list.size() == 1 && serial_list.get(0).getSerial_id().equalsIgnoreCase(serial_id)) {
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, true);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
        } else {
            bundle.putBoolean(Constant.MAIN_MD_PRODUCT_SERIAL_JUMP, false);
            bundle.putSerializable(Constant.MAIN_MD_PRODUCT_SERIAL, serial_list);
        }
//        }

        bundle.putString(Constant.MAIN_MD_PRODUCT_SERIAL_ID, serial_id);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_COUNT, record_count);
        bundle.putLong(Constant.MAIN_MD_PRODUCT_SERIAL_RECORD_PAGE, record_page);
        bundle.putString(Constant.MAIN_REQUESTING_PROCESS, Constant.MODULE_SO_SEARCH_SERIAL_EXPRESS);
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
            onBackPressedClicked();
        }else{
            if(ToolBox_Con.isOnline(context)) {
                mView.setExitProcess(true);
                //
                executeSerialSave();
            }else{
                onBackPressedClicked();
            }
        }
    }


    /**
     * LUCHE - 07/11/2019
     *
     * Metodo gera a msg a ser exibida no textHelper do Serial
     * Msg variavel baseado nos campos preenchidos.
     * @param hmAux_Trans
     * @param serial_rule - Regra do serial ou null se vazio
     * @param min - Qtd min de caracteres do serial ou null se vazio
     * @param max - Qtd max de caracteres do serial ou null se vazio
     * @return - Msg formatada para exibição.
     */
    @Override
    public String getFormattedRuleHelper(HMAux hmAux_Trans, String serial_rule, Integer min, Integer max) {
        String sHelper = "";
        String sMin ="";
        String sMax ="";
        //
        if(serial_rule == null && min == null && max == null){
            return null;
        }
        //
        if(serial_rule != null && !serial_rule.trim().isEmpty()){
            sHelper += hmAux_Trans.get("serial_rule_lbl") + " " + hmAux_Trans.get(serial_rule) + " ";
        }
        //
        if(min != null && min > 0){
            sMin = hmAux_Trans.get("serial_min_length_lbl") + min;
        }
        //
        if(max != null && max > 0){
            sMax = (min != null && min > 0 ?  hmAux_Trans.get("serial_min_max_separator_lbl") : "") + hmAux_Trans.get("serial_max_length_lbl") + max;
        }
        //
        if(!sMin.isEmpty() || !sMax.isEmpty() ){
            sHelper += "(" +sMin + sMax +")";
        }
        //
        return sHelper;
    }

    @Override
    public void handleHistClick() {
        HMAux auxLocalExpress  = so_pack_express_localDao.getByStringHM(
            new SO_Pack_Express_Local_Sql_011(
                ToolBox_Con.getPreference_Customer_Code(context),
                hmAux_Trans
            ).toSqlQuery()
        );
        //
        if( auxLocalExpress != null
            && auxLocalExpress.hasConsistentValue(SO_Pack_Express_Local_Sql_011.SENT_QTY)
            && ToolBox_Inf.convertStringToInt(auxLocalExpress.get(SO_Pack_Express_Local_Sql_011.SENT_QTY)) > 0
        ){
            mView.callAct042(context);
        }else{
            mView.showMsg(
                hmAux_Trans.get("alert_no_express_os_history_found_ttl"),
                hmAux_Trans.get("alert_no_express_os_history_found_msg")
            );
        }
    }

    private void onBackPressedClicked(){
        onBackPressedClicked(null, false, null , true);
    }

    @Override
    public void onBackPressedClicked(SO_Pack_Express mSoPackExpress, boolean hasPackServices, String serialID, boolean skipConfirm) {
        if((mSoPackExpress == null && !hasPackServices && (serialID == null || serialID.isEmpty())) || skipConfirm) {
            deleteExpressAllPackLocal();

            mView.callAct005(context);
        }else {
            ToolBox.alertMSG_YES_NO(
                context,
                hmAux_Trans.get("alert_leave_express_creation_ttl"),
                hmAux_Trans.get("alert_leave_express_creation_confirm"),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressedClicked(mSoPackExpress, hasPackServices,serialID,true);
                    }
                },
                1
            );
        }
    }
    @Override
    public void executeWS_SO_Service_Search(SO_Pack_Express mSo_pack_express, String serialId, SoPackExpressPacksLocal soPackExpressPacksLocal) {
        mView.setWsProcess(WS_SO_Service_Search.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_service_search_ttl"),
                hmAux_Trans.get("dialog_service_search_msg")
        );
        //
        Intent mIntent = new Intent(context, WBR_SO_Service_Search.class);
        Bundle bundle = new Bundle();
        //
        bundle.putInt(SM_SODao.CONTRACT_CODE, mSo_pack_express.getContract_code());
        bundle.putInt(SM_SODao.PRODUCT_CODE, (int) mSo_pack_express.getProduct_code());
        bundle.putInt(SM_SODao.CATEGORY_PRICE_CODE, mSo_pack_express.getCategory_price_code());
        bundle.putInt(SM_SODao.SEGMENT_CODE, mSo_pack_express.getSegment_code());
        bundle.putInt(SM_SODao.SITE_CODE, (int) mSo_pack_express.getSite_code());
        bundle.putInt(SM_SODao.OPERATION_CODE, (int) mSo_pack_express.getOperation_code());
        bundle.putInt(WS_SO_Service_Search.WS_EXPRESS_MODE, 1);
        if(soPackExpressPacksLocal != null){
            bundle.putString(WS_SO_Service_Search.EDIT_DEFAULT_PACKAGE, "1");
        }
        //
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public List<SoPackExpressPacksLocal> getExpressPacks(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product) {
        List<SoPackExpressPacksLocal> packs = new ArrayList<>();
//
        String fileName = ToolBox_Inf.getExpressSOFileName(mSo_pack_express.getContract_code(), mSo_pack_express.getProduct_code(), mSo_pack_express.getCategory_price_code(), mSo_pack_express.getSite_code(), mSo_pack_express.getOperation_code());
        File file = new File(ConstantBaseApp.SO_EXPRESS_JSON_PATH, fileName);
        TSO_Service_Search_Rec rec = null;
        if(file.exists()) {
            String contents = ToolBox_Inf.getContents(file);
            Gson gson = new GsonBuilder().serializeNulls().create();
            rec = gson.fromJson(contents, TSO_Service_Search_Rec.class);
        }
        List<SoPackExpressServicesLocal> services = new ArrayList<>();
        if(rec != null) {
            List<TSO_Service_Search_Obj> packageDefault = TSoServiceSearchRecKt.getPackageDefault(
                    rec,
                    "P",
                    mSo_pack_express.getCustomer_code(),
                    mSo_pack_express.getPrice_list_code(),
                    mSo_pack_express.getPack_code(),
                    mSo_pack_express.getPack_desc()
            );
            //
            for (TSO_Service_Search_Detail_Obj tso_service_search_detail_obj : packageDefault.get(0).getService_list()) {
                services.add(TSOServiceSearchDetailObjKt.toSoPackExpressServicesLocal(tso_service_search_detail_obj, mSo_pack_express));
            }
            onCreateSo_Pack_Express_Structure(mSo_pack_express, md_partner, md_product, "", "", "", "");
        }
        //
        packs.add(new SoPackExpressPacksLocal(
                        mSo_pack_express.getCustomer_code(),
                        mSo_pack_express.getSite_code(),
                        mSo_pack_express.getOperation_code(),
                        mSo_pack_express.getProduct_code(),
                        mSo_pack_express.getExpress_code(),
                        -1,
                        mSo_pack_express.getPack_code(),
                        -1,
                        mSo_pack_express.getPrice_list_code(),
                        mSo_pack_express.getPack_desc(),
                        mSo_pack_express.getPack_desc(),
                        0,
                        (double) mSo_pack_express.getPrice(),
                        1,
                        null,
                        "P",
                        "",
                        services
                )
        );
        return packs;

    }

    @Override
    public boolean hasPackServiceFile(int contract_code, long product_code, int category_price_code, long site_code, long operation_code) {
        String fileName = ToolBox_Inf.getExpressSOFileName(contract_code, product_code, category_price_code, site_code, operation_code);
        File file = new File(ConstantBaseApp.SO_EXPRESS_JSON_PATH, fileName);
        return file.exists();
    }

    @Override
    public SO_Pack_Express_Local getExpressPackLocal(long customer_code, long product_code, long site_code, long operation_code, String express_code, int bundle_express_tmp) {
        return so_pack_express_localDao.getByString(
                new SO_Pack_Express_Local_Sql_001(
                        customer_code,
                        site_code,
                        operation_code,
                        product_code,
                        express_code,
                        bundle_express_tmp
                ).toSqlQuery()
            );
    }

    @Override
    public void deleteExpressAllPackLocal() {
        SO_Pack_Express_Local expressPackLocal = getCurrentExpressPackLocal();
        //
        if(expressPackLocal != null){
            so_pack_express_localDao.removeFull(expressPackLocal);
        }
        //
    }

    @Override
    public void updateExpressPackage(SoPackExpressPacksLocal item, long customer_code, long product_code, long site_code, long operation_code, String express_code, int bundle_express_tmp, int position) {
        SO_Pack_Express_Local currentExpressPackLocal = getCurrentExpressPackLocal();
        currentExpressPackLocal.getPacksLocals().set(position, item);
        so_pack_express_localDao.addUpdate(currentExpressPackLocal);
        //
        if(currentExpressPackLocal != null) {
            mView.refreshPackServiceList(currentExpressPackLocal.getPacksLocals(), item, position);
        }
    }

    private SoPackExpressPacksLocal getPackExpressPacksAndServicesLocal(long customer_code, long site_code, long operation_code, long product_code, String express_code, int bundle_express_tmp, int price_list_code, int pack_code, int packSeq, String type_ps) {
        return soPackExpressLocalDao.getByString(
                new SoPackExpressPacksLocalSql001(
                    customer_code,
                    site_code,
                    operation_code,
                    product_code,
                    express_code,
                    bundle_express_tmp,
                    price_list_code,
                    pack_code,
                    packSeq,
                    type_ps
                ).toSqlQuery()
        );
    }

    @Override
    public void deleteSelectedExpressPackLocal(SoPackExpressPacksLocal item, long customer_code, long product_code, long site_code, long operation_code, String express_code, int bundle_express_tmp, int position) {
        soPackExpressLocalDao.removeFull(item);
        SO_Pack_Express_Local expressPackLocal = getExpressPackLocal(customer_code, product_code, site_code, operation_code, express_code, bundle_express_tmp);
        if(expressPackLocal != null){
            mView.refreshPackServiceList(expressPackLocal.getPacksLocals(), item, position);
        } else {
            mView.refreshPackServiceList(new ArrayList<>(), item, position);
        }
    }

    @Override
    public SO_Pack_Express_Local createExpressPackLocal(int bundle_express_tmp, SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial_id, String billing_add_inf1, String billing_add_inf2, String billing_add_inf3) {
        return null;
    }

    @Override
    public String getServicesDetailsResume(SO_Pack_Express_Local lastExpressInSiteOper) {

        StringBuilder serviceResume = new StringBuilder();
        List<SoPackExpressPacksLocal> packsLocals   = soPackExpressLocalDao.query(
                new SoPackExpressPacksLocalSql004(
                        lastExpressInSiteOper.getCustomer_code(),
                        lastExpressInSiteOper.getSite_code(),
                        lastExpressInSiteOper.getOperation_code(),
                        lastExpressInSiteOper.getProduct_code(),
                        lastExpressInSiteOper.getExpress_code(),
                        lastExpressInSiteOper.getExpress_tmp()
                ).toSqlQuery()
        );

        if (packsLocals != null
        && packsLocals.size() > 0) {
            for (SoPackExpressPacksLocal packsLocal : packsLocals) {
                serviceResume.append(packsLocal.getQty()).append("x ").append(packsLocal.getPack_service_desc_full()).append("\n");
            }
            return serviceResume.substring(0, serviceResume.length() -1);
        }else{
            return lastExpressInSiteOper.getSo_desc();
        }
    }

    private SO_Pack_Express_Local getCurrentExpressPackLocal() {
        return so_pack_express_localDao.getByString(
                new SO_Pack_Express_Local_Sql_015(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
    }

    private int getSerialCode(long customer_code, long product_code, String serialId) {
        MD_Product_Serial serial = new MD_Product_SerialDao(context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM).getByString(new MD_Product_Serial_Sql_002(
                        customer_code,
                        product_code,
                        serialId
                ).toSqlQuery()
        );
        return serial != null ? (int) serial.getSerial_code() : -1;
    }

}
