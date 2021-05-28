package com.namoadigital.prj001.ui.act042;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_009;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act042_Main_Presenter_Impl implements Act042_Main_Presenter {

    private Context context;
    private Act042_Main_View mView;
    private HMAux hmAux_Trans;
    private SO_Pack_Express_LocalDao expressLocalDao;

    public Act042_Main_Presenter_Impl(Context context, Act042_Main mView, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        //
        this.expressLocalDao = new SO_Pack_Express_LocalDao(context);
    }

    @Override
    public void getSoExpressList() {
        ArrayList<HMAux> so_express_list = (ArrayList<HMAux>)
                expressLocalDao.query_HM(
                        new SO_Pack_Express_Local_Sql_009(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                Constant.RETURN_SQL_HM_AUX
                        ).toSqlQuery()
                );
        //
        mView.loadSoExpress(so_express_list);
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct040(context);
    }
}
