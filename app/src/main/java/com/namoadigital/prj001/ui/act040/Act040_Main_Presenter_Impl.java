package com.namoadigital.prj001.ui.act040;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_PartnerDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.model.SO_Pack_Express;
import com.namoadigital.prj001.sql.MD_Partner_Sql_001;
import com.namoadigital.prj001.sql.MD_Partner_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_001;
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
    private MD_ProductDao md_productDao;
    private MD_PartnerDao md_partnerDao;

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, MD_ProductDao md_productDao, MD_PartnerDao md_partnerDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.so_pack_expressDao = so_pack_expressDao;
        this.md_productDao = md_productDao;
        this.md_partnerDao = md_partnerDao;
    }

    @Override
    public void setSO_Pack_Express(long customer_code, long site_code, long operation_code, long product_code, String express_code) {
        mView.loadSO_Pack_Express(
                so_pack_expressDao.getByString(
                        new SO_Pack_Express_Sql_001(
                                customer_code,
                                site_code,
                                operation_code,
                                product_code,
                                express_code
                        ).toSqlQuery()
                )
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
    public boolean processValidation(SO_Pack_Express mSo_pack_express, long partner_code, long product_code, String serial) {
        return false;
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
            mView.jumpToOne();
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
                    Long.parseLong(partnerList.get(0).get("partner_id"))
            );
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
