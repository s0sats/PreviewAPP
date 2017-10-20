package com.namoadigital.prj001.ui.act010;

import android.content.Context;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_002;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main_Presenter_Impl implements Act010_Main_Presenter {

    private Context context;
    private Act010_Main_View mView;
    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_DataDao customFormDataDao;
    private long product_code;
    private String serial_id;
    private String so_prefix;
    private String so_code;
    private HMAux hmAux_Trans = new HMAux();

    public Act010_Main_Presenter_Impl(Context context, Act010_Main_View mView, GE_Custom_FormDao custom_formDao, GE_Custom_Form_DataDao customFormDataDao, long product_code, String serial_id, String so_prefix, String so_code, HMAux hmAux_Trans) {
        this.context = context;
        this.mView = mView;
        this.custom_formDao = custom_formDao;
        this.customFormDataDao = customFormDataDao;
        this.product_code = product_code;
        this.serial_id = serial_id;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.hmAux_Trans = hmAux_Trans;
    }

    @Override
    public void setAdapterData(long product_code, int custom_form_type, String filter) {

        List<HMAux> data =
                custom_formDao.query_HM(
                        new GE_Custom_Form_Sql_002(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                custom_form_type,
                                ToolBox_Con.getPreference_Translate_Code(context),
                                String.valueOf(this.product_code),
                                ToolBox_Con.getPreference_Operation_Code(context)
                        ).toSqlQuery()
                );
        //
        if (data != null && data.size() == 1) {
            validateOpenForm(data.get(0));
        }
        //
        mView.loadForms(data);
    }

    @Override
    public void validateOpenForm(HMAux item) {
        //
        if (ToolBox_Inf.checkFormIsReady(
                context,
                Long.parseLong(item.get(GE_Custom_FormDao.CUSTOMER_CODE)),
                Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_TYPE)),
                Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE)),
                Integer.parseInt(item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION))
                )
        ) {

            if(validateFormSORestriction(item)) {
                //
                mView.addFormInfoToBundle(item);
                //
                mView.callAct011(context);
            }

        } else {
            mView.alertFormNotReady();
        }
    }

    /**
     * Fluxo de Abertura pelo N-Form(Act008)
     * Valida se o form que o usr esta tentando abrir
     * já esta aberto para uma S.O.
     * <p>
     * Fluxo de Abertura pelo N-Service(Act027,028)
     * Valida se existe o form aberto para outra S.O ou
     * form aberto sem S.O
     *
     * @param item
     * @return
     */
    private boolean validateFormSORestriction(HMAux item) {
        //private boolean hasOpenFormLinkedToSO(HMAux item) {

        List<GE_Custom_Form_Data> formDataList = customFormDataDao.query(
                new GE_Custom_Form_Data_Sql_004(
                        item.get(GE_Custom_Form_LocalDao.CUSTOMER_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_CODE),
                        item.get(GE_Custom_Form_LocalDao.CUSTOM_FORM_VERSION),
                        String.valueOf(product_code),
                        serial_id
                ).toSqlQuery().toLowerCase()
        );

        if (formDataList == null || formDataList.size() == 0) {
            return true;
        } else if (formDataList.size() > 1) {
            //isso nunca deveria acontecer
            mView.showAlertMsg(
                    hmAux_Trans.get("alert_more_than_one_form_ttl"),
                    hmAux_Trans.get("alert_more_than_one_form_msg")
            );
            return false;
        } else {
            GE_Custom_Form_Data formData = formDataList.get(0);
            //
            if (
                    (so_prefix.equals("") && so_code.equals("")
                            && formData.getSo_prefix() == null && formData.getSo_code() == null)
                            ||
                            (so_prefix.equals(String.valueOf(formData.getSo_prefix()))
                                    && so_code.equals(String.valueOf(formData.getSo_code())))
                    ) {
                return true;
            } else {
                if (!so_prefix.equals("") && !so_code.equals("")
                        && (!so_prefix.equals(String.valueOf(formData.getSo_prefix())) ||
                        !so_code.equals(String.valueOf(formData.getSo_code())))
                        ) {
                    if(formData.getSo_prefix() == null && formData.getSo_code() == null){
                        //msg de não e possivel abrir form via S.O,pois ele ja existe sem S.O o.O
                        mView.showAlertMsg(
                                hmAux_Trans.get("alert_so_form_exits_no_so_ttl"),
                                hmAux_Trans.get("alert_so_form_exits_no_so_msg")
                        );
                    }else{
                        mView.showAlertMsg(
                                hmAux_Trans.get("alert_so_form_exits_with_so_ttl"),
                                hmAux_Trans.get("alert_so_form_exits_with_so_msg")
                                + "\n" + hmAux_Trans.get("alert_so_lbl")
                                + ":    " + formData.getSo_prefix() +"."+formData.getSo_code()
                        );
                    }
                    return false;
                }
                //
                if (so_prefix.equals("") && so_code.equals("")
                        && formData.getSo_prefix() != null && formData.getSo_code() != null
                        ) {
                    //msg de não e possivel abrir form pois ele ja existe para uma S.O
                    mView.showAlertMsg(
                            hmAux_Trans.get("alert_form_exits_with_so_ttl"),
                            hmAux_Trans.get("alert_form_exits_with_so_msg")
                            + "\n" + hmAux_Trans.get("alert_so_lbl")
                            + ":    " + formData.getSo_prefix() +"."+formData.getSo_code()
                    );

                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct009(context);
    }
}
