package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_OperationDao;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.receiver.WBR_SO_Pack_Express_Local;
import com.namoadigital.prj001.sql.MD_Operation_Sql_003;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Site_Sql_003;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_013;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

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

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, SO_Pack_Express_LocalDao so_pack_express_localDao, MD_ProductDao md_productDao, MD_PartnerDao md_partnerDao, MD_SiteDao mdSiteDao, MD_OperationDao mdOperationDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.so_pack_expressDao = so_pack_expressDao;
        this.so_pack_express_localDao = so_pack_express_localDao;
        this.md_productDao = md_productDao;
        this.md_partnerDao = md_partnerDao;
        this.mdSiteDao = mdSiteDao;
        this.mdOperationDao = mdOperationDao;
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
    public void loadPartners() {
        ArrayList<HMAux> partnerList = (ArrayList<HMAux>) md_partnerDao.query_HM(
                new MD_Partner_Sql_001(
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
        //
        if(md_site == null || md_operation == null){
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
        so_pack_express_local.setSite_id(md_site.getSite_id());
        so_pack_express_local.setSite_desc(md_site.getSite_desc());
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
        executeSO_Pack_Express_Local(connectionStatusAlter);
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
        if(auxPack != null) {
            boolean packExistis = (auxPack.containsKey(SO_Pack_Express_Local_Sql_013.ALREADY_NEW_EXPRESS_ORDER) && ToolBox_Inf.convertStringToInt(auxPack.get(SO_Pack_Express_Local_Sql_013.ALREADY_NEW_EXPRESS_ORDER)) > 0);
            //
            if(packExistis){
                return true;
            }
        }
        //
        return false;
    }

    @Override
    public void executeSO_Pack_Express_Local(boolean connectionStatusAlter) {
        if (ToolBox_Con.isOnline(context)) {
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
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct021(context);
    }
}
