package com.namoadigital.prj001.view.frag.frg_serial_search;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

public class Frg_Serial_Search_Presenter implements Frg_Serial_Search_Contract.Presenter {

    private Context context;

    public Frg_Serial_Search_Presenter(Context context) {
        this.context = context;
    }

    @Override
    public void setChkForHideSerialInfoPreference(boolean status) {
        ToolBox_Con.setBooleanPreference(context, ConstantBaseApp.FORCE_NOT_SHOW_SERIAL_INFO, status);
    }

    @Override
    public boolean getChkForForceNotShowSerialInfo() {
        return ToolBox_Con.hasForceNotShowSerialInfo(context);
    }

    @Override
    public boolean getProfileForHideSerialInfo() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL, ConstantBaseApp.HIDE_SERIAL_INFO);
    }

    @Override
    public boolean getProfileForceNotShowSerialInfo() {
        return ToolBox_Inf.profileExists(context, ConstantBaseApp.PROFILE_PRJ001_PRODUCT_SERIAL, ConstantBaseApp.FORCE_NOT_SHOW_SERIAL_INFO);
    }

    /**
     * LUCHE - 07/11/2019
     *
     * Metodo gera a msg a ser exibida no textHelper do Serial
     * Msg variavel baseado nos campos preenchidos.
     * @param hmAux_Trans
     * @param serial_rule - Regra do serial ou null se vazio
     * @param min - Qtd min de caracteres do serial ou null se vazio
     * @param max - Qtd max de caracteres do serial ou null se vazio
     * @return - Msg formatada para exibição.
     */
    @Override
    public String getFormattedRuleHelper(HMAux hmAux_Trans, String serial_rule, Integer min, Integer max) {
        String sHelper = "";
        String sMin ="";
        String sMax ="";
        //
        if(serial_rule == null && min == null && max == null){
            return null;
        }
        //
        if(serial_rule != null && !serial_rule.trim().isEmpty()){
            sHelper += hmAux_Trans.get("serial_rule_lbl") + " " + hmAux_Trans.get(serial_rule) + " ";
        }
        //
        if(min != null && min > 0){
            sMin = hmAux_Trans.get("serial_min_length_lbl") + min;
        }
        //
        if(max != null && max > 0){
            sMax = (min != null && min > 0 ?  hmAux_Trans.get("serial_min_max_separator_lbl") : "") + hmAux_Trans.get("serial_max_length_lbl") + max;
        }
        //
        if(!sMin.isEmpty() || !sMax.isEmpty() ){
            sHelper += "(" +sMin + sMax +")";
        }
        //
        return sHelper;
    }
}
