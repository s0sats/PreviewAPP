package com.namoadigital.prj001.ui.act014;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_Form_ApDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.SM_SODao;
import com.namoadigital.prj001.dao.SO_Pack_Express_LocalDao;
import com.namoadigital.prj001.sql.SM_SO_Sql_015;
import com.namoadigital.prj001.sql.SO_Pack_Express_Local_Sql_011;
import com.namoadigital.prj001.sql.Sql_Act014_001;
import com.namoadigital.prj001.sql.Sql_Act014_003;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANIEL.LUCHE on 24/02/2017.
 */

public class Act014_Main_Presenter_Impl implements Act014_Main_Presenter {

    private Context context;
    private Act014_Main mView;
    private GE_Custom_Form_LocalDao customFormLocalDao;
    private SM_SODao sm_soDao;
    private SO_Pack_Express_LocalDao soPackExpressLocalDao;

    private HMAux hmAux_Trans;

    public Act014_Main_Presenter_Impl(Context context, Act014_Main mView, GE_Custom_Form_LocalDao customFormLocalDao, SM_SODao sm_soDao, SO_Pack_Express_LocalDao soPackExpressLocalDao, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.customFormLocalDao = customFormLocalDao;
        this.sm_soDao = sm_soDao;
        this.soPackExpressLocalDao = soPackExpressLocalDao;
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
        //FINALIZADOS FORM AP
        GE_Custom_Form_ApDao formApDao = new GE_Custom_Form_ApDao(context);
        List<HMAux> NFormAPHistoric = formApDao.query_HM(
                new Sql_Act014_003(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        hmAux_Trans
                ).toSqlQuery()
        );
        //
        senList.addAll(NFormAPHistoric);
        //
        if (ToolBox_Inf.parameterExists(context, new String[]{Constant.PARAM_SO, Constant.PARAM_SO_MOV})) {
            ArrayList<HMAux> senListSO =
                    (ArrayList<HMAux>) sm_soDao.query_HM(
                            new SM_SO_Sql_015(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    hmAux_Trans
                            ).toSqlQuery()
                    );

            HMAux hmAuxSO = senListSO.get(0);
            HMAux hmAuxTotal = new HMAux();
            //
            if(ToolBox_Inf.profileExists(context,Constant.PROFILE_MENU_SO_EXPRESS,null)){
                ArrayList<HMAux> senListSOExpress =
                        (ArrayList<HMAux>) soPackExpressLocalDao.query_HM(
                                new SO_Pack_Express_Local_Sql_011(
                                        ToolBox_Con.getPreference_Customer_Code(context),
                                        hmAux_Trans
                                ).toSqlQuery()
                        );

                HMAux hmAuxSOExpress = senListSOExpress.get(0);


                int mTotal = ToolBox_Inf.convertStringToInt(hmAuxSO.get(SM_SO_Sql_015.SENT_QTY)) +
                        ToolBox_Inf.convertStringToInt(hmAuxSOExpress.get(SM_SO_Sql_015.SENT_QTY));


                hmAuxTotal.put(SM_SO_Sql_015.TYPE, hmAuxSO.get(SM_SO_Sql_015.TYPE));
                hmAuxTotal.put(SM_SO_Sql_015.SENT_QTY, String.valueOf(mTotal));
            }else{
                hmAuxTotal = hmAuxSO;
            }

            senList.add(hmAuxTotal);

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

            if (item.get(Sql_Act014_003.TYPE).equalsIgnoreCase(hmAux_Trans.get(Act014_Main.LABEL_TRANS_FORM_AP))) {
                mView.callAct039(context);
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
