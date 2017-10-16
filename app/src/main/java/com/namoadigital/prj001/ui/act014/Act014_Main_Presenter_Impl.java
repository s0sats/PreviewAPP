package com.namoadigital.prj001.ui.act014;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.sql.SM_SO_Sql_015;
import com.namoadigital.prj001.sql.Sql_Act014_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Act014_Main_Presenter_Impl implements Act014_Main_Presenter {

    private Context context;
    private Act014_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private SM_SODao sm_soDao;
    private HMAux hmAux_Trans;

    public Act014_Main_Presenter_Impl(Context context, Act014_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, SM_SODao sm_soDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.sm_soDao = sm_soDao;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void getSentData() {
        ArrayList<HMAux> senListF =
                (ArrayList<HMAux>) customFormLocalDao.query_HM(
                        new Sql_Act014_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                hmAux_Trans
                        ).toSqlQuery()
                );
        ArrayList<HMAux> senList = new ArrayList<>();

        senList.addAll(senListF);
        //
        if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {
            ArrayList<HMAux> senListSO =
                    (ArrayList<HMAux>) sm_soDao.query_HM(
                            new SM_SO_Sql_015(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    hmAux_Trans
                            ).toSqlQuery()
                    );

            senList.addAll(senListSO);
        }
        //
        mView.loadSentData(senList);
    }

    @Override
    public void checkSentFlow(HMAux item) {

        if (!item.get(Sql_Act014_001.SENT_QTY).equalsIgnoreCase("0")) {

            if (item.get(Sql_Act014_001.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_CHECKLIST))) {
                mView.callAct015(context);
            }

            if (item.get(Sql_Act014_001.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_OS))) {
                mView.callAct032(context);
            }

        } else {
            mView.showMsg();
        }
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct005(context);

    }
}
