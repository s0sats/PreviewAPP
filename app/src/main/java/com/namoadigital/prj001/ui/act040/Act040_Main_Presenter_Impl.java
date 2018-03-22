package com.namoadigital.prj001.ui.act040;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.SO_Pack_ExpressDao;
import com.namoadigital.prj001.sql.MD_Product_Sql_006;
import com.namoadigital.prj001.sql.SO_Pack_Express_Sql_001;

/**
 * Created by d.luche on 09/03/2018.
 */

public class Act040_Main_Presenter_Impl implements Act040_Main_Presenter {

    private Context context;
    private Act040_Main mView;
    private HMAux hmAux_Trans;
    private SO_Pack_ExpressDao so_pack_expressDao;
    private MD_ProductDao md_productDao;

    public Act040_Main_Presenter_Impl(Context context, Act040_Main mView, HMAux hmAux_Trans, SO_Pack_ExpressDao so_pack_expressDao, MD_ProductDao md_productDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.so_pack_expressDao = so_pack_expressDao;
        this.md_productDao = md_productDao;
    }

    @Override
    public void setSO_Pack_Express(long customer_code, long site_code, long operation_code, String express_code) {
        mView.loadSO_Pack_Express(
                so_pack_expressDao.getByString(
                        new SO_Pack_Express_Sql_001(
                                customer_code,
                                site_code,
                                operation_code,
                                express_code
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
            mView.jumpToOne();
        } else {
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
