package com.namoadigital.prj001.ui.act046;

import android.content.Context;

import com.namoa_digital.namoa_library.ctls.CalendarView;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.sql.Sql_Act046_001;
import com.namoadigital.prj001.util.ToolBox_Con;

import java.util.ArrayList;

public class Act046_Main_Presenter implements Act046_Main_Contract.I_Presenter {

    private Context context;
    private Act046_Main_Contract.I_View mView;
    private HMAux hmAux_Trans = new HMAux();
    private MD_Product_SerialDao serialDao;
    private MD_ProductDao mdProductDao;

    public Act046_Main_Presenter(Context context, Act046_Main_Contract.I_View mView, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, MD_ProductDao mdProductDao) {
        this.context = context;
        this.mView = mView;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = serialDao;
        this.mdProductDao = mdProductDao;
    }

    @Override
    public int getTotalDelay(boolean filter_form, boolean filter_form_ap) {

        ArrayList<HMAux> results = (ArrayList<HMAux>) mdProductDao.query_HM(
                new Sql_Act046_001(
                        context,
                        String.valueOf(ToolBox_Con.getPreference_Customer_Code(context)),
                        filter_form,
                        filter_form_ap
                ).toSqlQuery()
        );

        if (results.size() == 1){
            try{
                return Integer.parseInt(results.get(0).get(CalendarView.DELAYED_COUNT));
            } catch (Exception e){
                return 0;
            }
        }

        return 0;
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que salva na preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param isChecked - Valor do checkbox.
     */
    @Override
    public void saveCheckBoxStatusIntoPreference(String checkboxConstant, boolean isChecked) {
        ToolBox_Con.setBooleanPreference(context,checkboxConstant,isChecked);
    }

    /**
     * LUCHE - 21/02/2020
     * Metodo que resgata da preferencia o valor do checkbox
     *
     * @param checkboxConstant - Constante da preferecia do checkbox.
     * @param defaultValue - Caso a preferencia não exista, retorna o valor definido via codigo.
     * @return
     */
    @Override
    public boolean loadCheckboxStatusFromPreferencie(String checkboxConstant, boolean defaultValue) {
        return ToolBox_Con.getBooleanPreferencesByKey(context, checkboxConstant, defaultValue);
    }
}
