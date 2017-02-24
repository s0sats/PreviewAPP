package com.namoadigital.prj001.ui.act014;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.sql.Sql_Act014_001;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Act014_Main_Presenter_Impl implements Act014_Main_Presenter {

    private Context context;
    private Act014_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private HMAux hmAux_Trans;

    public Act014_Main_Presenter_Impl(Context context, Act014_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSentData() {
        List<HMAux> senList =
                customFormLocalDao.query_HM(
                        new Sql_Act014_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                hmAux_Trans
                        ).toSqlQuery()
                );
        //
        mView.loadSentData(senList);
    }

    @Override
    public void checkSentFlow(HMAux item) {

        if(!item.get(Sql_Act014_001.SENT_QTY).equalsIgnoreCase("0")){
            mView.callAct015(context);
        }else{
            mView.showMsg();
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);

    }
}
