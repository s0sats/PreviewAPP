package com.namoadigital.prj001.ui.act033;

import android.content.Context;
import android.os.Bundle;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_Site_ZoneDao;
import com.namoadigital.prj001.sql.MD_Site_Zone_Sql_002;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by d.luche on 31/08/2017.
 */

public class Act033_Main_Presenter_Impl implements Act033_Main_Presenter {

    private Context context;
    private Act033_Main_View mView;
    private HMAux hmAux_Trans;
    private MD_Site_ZoneDao zoneDao;
    private String requestingAct;

    public Act033_Main_Presenter_Impl(Context context, Act033_Main_View mView, HMAux hmAux_Trans, MD_Site_ZoneDao zoneDao, String requestingAct) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.zoneDao = zoneDao;
        this.requestingAct = requestingAct;
    }

    /**
     * Essa Act só deve ser exibida pelo usr se ele tiver acesso ao modulo de Serviço.
     * Para facilitar o fluxo, a Act033 sempre será chama após a Act003 e backpress da Act004
     * Esse metodo verifica se o Customer tem acesso ao modulo de Serviço
     *   Caso tenha, a tela carrega todos os seus metodos e faz parte do fluxo.
     *   Caso não tenha, o metodo analisa se é um ação de "ida" ou "volta" e define qual Act
     *   deve ser chamada.
     * @param back_action: Parametro enviado pelo bundle que indica se é uma ação de voltar.
     *
     */
    @Override
    public void accessToSoModule(int back_action) {
        if (ToolBox_Inf.profileExists(context, Constant.PROFILE_PRJ001_SO, null)) {
            getZones();
        }else{
           if(back_action == 1){
               onBackPressedClicked();
           }else{
               defineForwardFlow();
           }
        }
    }

    @Override
    public void getZones() {
        if (checkPreferenceIsSet()) {
            defineForwardFlow();
        } else {
            List<HMAux> zones = zoneDao
                    .query_HM(
                            new MD_Site_Zone_Sql_002(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    Integer.parseInt(ToolBox_Con.getPreference_Site_Code(context))
                            ).toSqlQuery()
                    );

            if (zones == null || zones.size() == 0) {
                mView.showNoZoneMsg();
            } else if (zones.size() == 1) {
                setZonePreference(zones.get(0));
            } else {
                mView.loadZones(zones);
            }
        }
    }

    @Override
    public boolean checkPreferenceIsSet() {
        if (ToolBox_Con.getPreference_Zone_Code(context) != -1) {
            return true;
        }
        return false;
    }

    @Override
    public void setZonePreference(HMAux zone) {
        //
        ToolBox_Con.setPreference_Zone_Code(
                context,
                Integer.parseInt(zone.get(MD_Site_ZoneDao.ZONE_CODE))
        );
        //
        defineForwardFlow();
    }

    @Override
    public void defineForwardFlow() {
        if (requestingAct.equals(Constant.ACT005)) {
            mView.callAct005(context);
        } else if (requestingAct.equals(Constant.ACT017)) {
            mView.callAct017(context);
        } else {
            mView.callAct004(context);
        }
    }

    @Override
    public void onBackPressedClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.BACK_ACTION, 1);
        //
        mView.callAct003(context, bundle);
    }
}
