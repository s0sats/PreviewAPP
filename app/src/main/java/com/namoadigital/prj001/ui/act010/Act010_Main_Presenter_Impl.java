package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_TypeDao;
import com.namoadigital.prj001.dao.GeOsDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GeOs;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_004;
import com.namoadigital.prj001.sql.GeOsSql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tp_Device_Sql_002;
import com.namoadigital.prj001.sql.Sql_Act010_001;
import com.namoadigital.prj001.ui.act087.Act087Main;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
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
    private String site_code_form_param;
    private HMAux hmAux_Trans = new HMAux();
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_Tp_DeviceDao serialTpDeviceDao;
    private GeOsDao geOsDao;

    public Act010_Main_Presenter_Impl(Context context, Act010_Main_View mView, GE_Custom_FormDao custom_formDao, GE_Custom_Form_DataDao customFormDataDao, long product_code, String serial_id, String so_prefix, String so_code, String site_code_form_param, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, MD_Product_Serial_Tp_DeviceDao serial_tp_deviceDao, GeOsDao geOsDao) {
        this.context = context;
        this.mView = mView;
        this.custom_formDao = custom_formDao;
        this.customFormDataDao = customFormDataDao;
        this.product_code = product_code;
        this.serial_id = serial_id;
        this.so_prefix = so_prefix;
        this.so_code = so_code;
        this.site_code_form_param = site_code_form_param;
        this.hmAux_Trans = hmAux_Trans;
        this.serialDao = serialDao;
        this.serialTpDeviceDao = serial_tp_deviceDao;
        this.geOsDao = geOsDao;
    }

    @Override
    public void setAdapterData(long product_code, int tagCode, Integer blockSpontaneous) {
        List<HMAux> data =
                custom_formDao.query_HM(
                        new Sql_Act010_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                tagCode,
                                ToolBox_Con.getPreference_Translate_Code(context),
                                String.valueOf(this.product_code),
                                ToolBox_Con.getPreference_Operation_Code(context),
                                site_code_form_param,
                                serial_id,
                                blockSpontaneous
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
                if(item.hasConsistentValue(GE_Custom_FormDao.REQUIRE_LOCATION)
                        && item.get(GE_Custom_FormDao.REQUIRE_LOCATION).equals("1")
                        && !ToolBox_Con.hasGPSResourceActive(context)){
                    mView.alertActiveGPSResource(item);
                }else {
                    if(isOsForm(item)) {
                        if(osFormAlreadyExists(item)) {
                            setAct011Call(item);
                        }else{
                            if (serialHasStructure()) {
                                prepareOsFormCreation(item);
                            } else {
                                mView.showAlertMsg(
                                    hmAux_Trans.get("alert_os_form_ttl"),
                                    hmAux_Trans.get("alert_serial_undefined_or_without_structure_msg")
                                );
                            }
                        }
                    }else{
                        setAct011Call(item);
                    }
                }
            }
        } else {
            mView.alertFormNotReady();
        }
    }

    private boolean osFormAlreadyExists(HMAux item) {
        GeOs geOs = geOsDao.getByString(
            new GeOsSql_002(
                item.get(GE_Custom_FormDao.CUSTOMER_CODE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_TYPE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION)
            ).toSqlQuery()
        );
        //
        return geOs != null && geOs.getCustom_form_data() > 0;
    }

    /**
     * Valida se serial existe e possui estrutura
     * @return
     */
    private boolean serialHasStructure() {
        if(serial_id != null && !serial_id.isEmpty()){
            MD_Product_Serial serial = getMd_product_serial();
            //
            if(serial != null && serial.getHas_item_check() == 1){
                ArrayList<MD_Product_Serial_Tp_Device> serialTpDevices = (ArrayList<MD_Product_Serial_Tp_Device>)
                    serialTpDeviceDao.query(
                        new MD_Product_Serial_Tp_Device_Sql_002(
                            serial.getCustomer_code(),
                            serial.getProduct_code(),
                            serial.getSerial_code()
                        ).toSqlQuery()
                    );
                //
                return serialTpDevices != null && serialTpDevices.size() > 0;
            }
        }
        //
        return false;
    }

    /**
     * Pega obj serial
     * @return
     */
    private MD_Product_Serial getMd_product_serial() {
        MD_Product_Serial serial = serialDao.getByString(
            new MD_Product_Serial_Sql_002(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code,
                serial_id
            ).toSqlQuery()
        );
        return serial;
    }

    /**
     * Prepara abertura da tela de cabeçalho da o.s
     * @param item
     */
    private void prepareOsFormCreation(HMAux item) {
        mView.addFormInfoToBundle(item);
        MD_Product_Serial md_product_serial = getMd_product_serial();
        Bundle bundle = mView.getBundle();
        bundle.putAll(
            Act087Main.getBundleInstance(
                item.get(GE_Custom_Form_TypeDao.CUSTOM_FORM_TYPE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION),
                String.valueOf(product_code),
                serial_id,
                String.valueOf(md_product_serial.getSerial_code()),
                "-1",
                "-1",
                "-1"
            )
        );
        //
        mView.callAct087();
    }

    /**
     * Valida se form é tipo o.s
     * @param item
     * @return
     */
    private boolean isOsForm(HMAux item) {
        return  item.hasConsistentValue(GE_Custom_FormDao.IS_SO)
                && "1".equals(item.get(GE_Custom_FormDao.IS_SO));
    }

    private void setAct011Call(HMAux item) {
        //
        mView.addFormInfoToBundle(item);
        //
        mView.callAct011(context);
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
        } else if (formDataList.size() > 1 && !mView.isHas_tk_ticket_is_form_off_hand()) {
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
    public SpannableString getTagLblText(String tag_lbl, String tag_desc) {
        String label = tag_lbl + ": ";
        String finalText = label + tag_desc.toUpperCase();
        //
        SpannableString spannableString = new SpannableString(finalText);
        spannableString.setSpan(
                new ForegroundColorSpan(
                        context.getResources().getColor(R.color.namoa_color_pipeline_origin_icon)),
                label.length(),
                finalText.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
        );
        //
        return spannableString;
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct009(context);
    }

    @Override
    public void validateGPSResource(HMAux item) {
            if (ToolBox_Con.hasGPSResourceActive(context)) {
                setAct011Call(item);
            }else{
                mView.alertActiveGPSResource(item);
            }
    }

}
