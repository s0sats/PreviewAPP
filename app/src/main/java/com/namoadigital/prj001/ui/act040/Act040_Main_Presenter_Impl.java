package com.namoadigital.prj001.ui.act040;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.namoa_digital.namoa_library.ctls.SearchableSpinner;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.model.MD_Operation;
import com.namoadigital.prj001.model.MD_Partner;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.model.SO_Pack_Express_Local;
import com.namoadigital.prj001.receiver.WBR_SO_Pack_Express_Local;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_006;
import com.namoadigital.prj001.sql.SM_SO_Service_Exec_Task_File_Sql_005;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_005;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

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

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, SO_Pack_Express_LocalDao so_pack_express_localDao, MD_ProductDao md_productDao, MD_PartnerDao md_partnerDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.so_pack_expressDao = so_pack_expressDao;
        this.so_pack_express_localDao = so_pack_express_localDao;
        this.md_productDao = md_productDao;
        this.md_partnerDao = md_partnerDao;
    }

    @Override
    public void setSO_Pack_Express(long customer_code, String express_code) {
        mView.loadSO_Pack_Express(
                so_pack_expressDao.getByString(
                        new SO_Pack_Express_Sql_005(
                                customer_code,
                                express_code
                        ).toSqlQuery()
                ),
                express_code
        );
    }

    @Override
    public void setMD_Partner(long customer_code, long partner_code) {
        mView.loadMD_Partner(
                md_partnerDao.getByString(
                        new MD_Partner_Sql_002(
                                customer_code,
                                (int) partner_code
                        ).toSqlQuery()
                )
        );
    }

    @Override
    public void checkJump(long customer_code) {
        int qtd = 0;

        try {
            qtd = Integer.parseInt(md_productDao.query_HM(
                    new MD_Product_Sql_006(
                            customer_code
                    ).toSqlQuery()
            ).get(0).get("count"));
        } catch (Exception e) {
        }

        if (qtd == 0) {

        } else if (qtd == 1) {
            //mView.jumpToOne();
        } else {
        }
    }

    @Override
    public void setPartners() {
        ArrayList<HMAux> partnerList = (ArrayList<HMAux>) md_partnerDao.query_HM(
                new MD_Partner_Sql_001(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        mView.setPartnerList(partnerList);
        //
        if (partnerList.size() == 1) {
            setMD_Partner(
                    Long.parseLong(partnerList.get(0).get("customer_code")),
                    Long.parseLong(partnerList.get(0).get(SearchableSpinner.ID))
            );
        }
    }

    @Override
    public void onCreateSo_Pack_Express(SO_Pack_Express mSo_pack_express, MD_Partner md_partner, MD_Product md_product, String serial, MD_Site md_site, MD_Operation md_operation, boolean connectionStatusAlter) {
        SO_Pack_Express_Local so_pack_express_local = new SO_Pack_Express_Local();


        long nTemp = Long.parseLong(so_pack_express_localDao.getByStringHM(
                new SO_Pack_Express_Local_Sql_006(
                        mSo_pack_express.getCustomer_code(),
                        mSo_pack_express.getSite_code(),
                        mSo_pack_express.getOperation_code(),
                        mSo_pack_express.getProduct_code(),
                        mSo_pack_express.getExpress_code()
                ).toSqlQuery()
        ).get(SM_SO_Service_Exec_Task_File_Sql_005.NEXT_TMP));

        so_pack_express_local.setCustomer_code(mSo_pack_express.getCustomer_code());
        so_pack_express_local.setSite_code(mSo_pack_express.getSite_code());
        so_pack_express_local.setSite_id(md_site.getSite_id());
        so_pack_express_local.setSite_desc(md_site.getSite_desc());
        so_pack_express_local.setOperation_code(mSo_pack_express.getOperation_code());
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

        so_pack_express_local.setSo_desc(mSo_pack_express.getPack_desc());

        so_pack_express_local.setSo_status(Constant.SYS_STATUS_WAITING_SYNC);
        so_pack_express_local.setLog_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
        //
        so_pack_express_localDao.addUpdate(so_pack_express_local);
        //
        executeSO_Pack_Express_Local(connectionStatusAlter);
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
