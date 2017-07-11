package com.namoadigital.prj001.ui.act021;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.sql.SM_SO_Sql_004;
import com.namoadigital.prj001.util.ToolBox_Con;

/**
 * Created by d.luche on 21/06/2017.
 */

public class Act021_Main_Presenter_Impl implements Act021_Main_Presenter {


    private Context context;
    private Act021_Main_View mView;
    private SM_SODao soDao;


    public Act021_Main_Presenter_Impl(Context context, Act021_Main_View mView, SM_SODao soDao) {
        this.context = context;
        this.mView = mView;
        this.soDao = soDao;
    }

    @Override
    public void getPendencies() {
        //
        HMAux hmAux = soDao.getByStringHM(
                new SM_SO_Sql_004(
                        ToolBox_Con.getPreference_Customer_Code(context)
                ).toSqlQuery()
        );
        //
        int qty = Integer.parseInt(hmAux.get(SM_SO_Sql_004.PENDING_QTY));
        //
        mView.setPendencies(qty);
    }

    @Override
    public void defineFlow(HMAux item) {

        switch (item.get(Act021_Main.NEW_OPT_ID)) {
            case Act021_Main.NEW_OPT_TP_PRODUCT:
                mView.callAct022(context);
                break;
            case Act021_Main.NEW_OPT_TP_SERIAL:
                mView.callAct025(context);
                break;
            case Act021_Main.NEW_OPT_TP_LOCATION:
            default:
                break;
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);
    }
}
