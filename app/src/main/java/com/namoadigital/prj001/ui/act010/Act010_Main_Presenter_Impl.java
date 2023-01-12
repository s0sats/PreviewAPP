package com.namoadigital.prj001.ui.act010;

import android.content.Context;
import android.content.Intent;
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
import com.namoadigital.prj001.dao.MdOrderTypeDao;
import com.namoadigital.prj001.dao.MeMeasureTpDao;
import com.namoadigital.prj001.dao.TkTicketTypeDao;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GeOs;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device;
import com.namoadigital.prj001.model.MdOrderType;
import com.namoadigital.prj001.receiver.WBR_Serial_Save;
import com.namoadigital.prj001.receiver.WBR_Ticket_Creation;
import com.namoadigital.prj001.service.WSTicketCreation;
import com.namoadigital.prj001.service.WS_Serial_Save;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Sql_004;
import com.namoadigital.prj001.sql.GeOsSql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Tp_Device_Sql_002;
import com.namoadigital.prj001.sql.MdOrderTypeSql_002;
import com.namoadigital.prj001.sql.MeMeasureTpSql_001;
import com.namoadigital.prj001.sql.Sql_Act010_001;
import com.namoadigital.prj001.sql.Sql_Act010_002;
import com.namoadigital.prj001.ui.act087.Act087Main;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act010_Main_Presenter_Impl implements Act010_Main_Presenter {

    private Context context;
    private Act010_Main_View mView;
    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_DataDao customFormDataDao;
    private MdOrderTypeDao orderTypeDao;
    private MeMeasureTpDao measureTpDao;
    private long product_code;
    private String serial_id;
    private String so_prefix;
    private String so_code;
    private String site_code_form_param;
    private HMAux hmAux_Trans = new HMAux();
    private MD_Product_SerialDao serialDao;
    private MD_Product_Serial_Tp_DeviceDao serialTpDeviceDao;
    private GeOsDao geOsDao;
    private TkTicketTypeDao tkTicketTypeDao;

    public Act010_Main_Presenter_Impl(Context context, Act010_Main_View mView, GE_Custom_FormDao custom_formDao, GE_Custom_Form_DataDao customFormDataDao, long product_code, String serial_id, String so_prefix, String so_code, String site_code_form_param, HMAux hmAux_Trans, MD_Product_SerialDao serialDao, MD_Product_Serial_Tp_DeviceDao serial_tp_deviceDao, GeOsDao geOsDao, TkTicketTypeDao tkTicketTypeDao, MdOrderTypeDao mdOrderTypeDao, MeMeasureTpDao measureTpDao) {
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
        this.tkTicketTypeDao = tkTicketTypeDao;
        this.orderTypeDao = mdOrderTypeDao;
        this.measureTpDao = measureTpDao;
    }

    @Override
    public void setAdapterData(long product_code, int tagCode, Integer blockSpontaneous, boolean has_tk_ticket_is_form_off_hand) {
        List<HMAux> forms =
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

        if(!has_tk_ticket_is_form_off_hand) {
            List<HMAux> tickets =
                    tkTicketTypeDao.query_HM(
                            new Sql_Act010_002(
                                    ToolBox_Con.getPreference_Customer_Code(context),
                                    tagCode,
                                    String.valueOf(this.product_code),
                                    ToolBox_Con.getPreference_Operation_Code(context),
                                    site_code_form_param,
                                    serial_id
                            ).toSqlQuery()
                    );
            //
            forms.addAll(tickets);
        }
        if (forms != null && forms.size() == 1) {
            HMAux aux = forms.get(0);
            if(aux.hasConsistentValue(Act010_Main.IS_FORM)
            && "1".equals(aux.get(Act010_Main.IS_FORM))) {
                validateOpenForm(aux);
            }else{
                mView.createTicketDialog(aux);
            }
        }
        //
        if(!has_tk_ticket_is_form_off_hand) {
            Collections.sort(forms, new Comparator<HMAux>() {
                public int compare(HMAux obj1, HMAux obj2) {
                    return Objects.requireNonNull(obj1.get(Act010_Main.CUSTOM_DESC)).compareToIgnoreCase(Objects.requireNonNull(obj2.get(Act010_Main.CUSTOM_DESC)));
                }
            });
        }
        //
        mView.loadForms(forms);
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
                    defineFormOrFormOsFlow(item);
                }
            }
        } else {
            mView.alertFormNotReady();
        }
    }

    private void defineFormOrFormOsFlow(HMAux item) {
        if(isOsForm(item)) {
            if(osFormAlreadyExists(item)
            && !mView.isHas_tk_ticket_is_form_off_hand()) {
                setAct011Call(item);
            }else{
                if (serialHasStructure()) {
                    if(serialHasMeasureTp()) {
                        prepareOsFormCreation(item);
                    }else{
                        mView.showAlertMsg(
                                hmAux_Trans.get("alert_os_form_ttl"),
                                hmAux_Trans.get("alert_serial_without_measure_type_msg")
                        );
                    }
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

    private boolean osFormAlreadyExists(HMAux item) {
        GeOs geOs = geOsDao.getByString(
            new GeOsSql_002(
                item.get(GE_Custom_FormDao.CUSTOMER_CODE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_TYPE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_CODE),
                item.get(GE_Custom_FormDao.CUSTOM_FORM_VERSION),
                product_code,
                serial_id
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
        String orderTypeQuery = new MdOrderTypeSql_002(
                ToolBox_Con.getPreference_Customer_Code(context)
        ).toSqlQuery();
        List<MdOrderType> orderTypeList = orderTypeDao.query(orderTypeQuery);
        if(orderTypeList.isEmpty()){
            mView.showAlertMsg(
                    hmAux_Trans.get("alert_order_type_empty_ttl"),
                    hmAux_Trans.get("alert_order_type_empty_msg"));
        }else {
            mView.callAct087();
        }
        //
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

    private boolean serialHasMeasureTp() {
        if(serial_id != null && !serial_id.isEmpty()) {
            MD_Product_Serial serial = getMd_product_serial();

            return measureTpDao.getByString(
                    new MeMeasureTpSql_001(
                            serial.getCustomer_code(),
                            serial.getMeasure_tp_code() != null ? serial.getMeasure_tp_code() : -1
                    ).toSqlQuery()
            ) != null ;
        }

        return false;

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
    public void callTicketCreationService(long customer_code, int type_code, String site_code, long operation_code, long product_code, long serial_code, String comments) {
        mView.setWsProcess(WSTicketCreation.class.getName());
        //
        mView.showPD(
                hmAux_Trans.get("dialog_ticket_creation_ttl"),
                hmAux_Trans.get("dialog_ticket_creation_start")
        );
        //
        Intent mIntent = new Intent(context, WBR_Ticket_Creation.class);
        Bundle bundle = new Bundle();

        bundle.putLong(WSTicketCreation.WS_BUNDLE_CUSTOMER_CODE,customer_code);
        bundle.putInt(WSTicketCreation.WS_BUNDLE_TYPE_CODE,type_code);
        bundle.putInt(WSTicketCreation.WS_BUNDLE_SITE_CODE, Integer.parseInt(site_code));
        bundle.putLong(WSTicketCreation.WS_BUNDLE_OPERATION_CODE,operation_code);
        bundle.putLong(WSTicketCreation.WS_BUNDLE_PRODUCT_CODE, product_code);
        bundle.putLong(WSTicketCreation.WS_BUNDLE_SERIAL_CODE,serial_code);
        bundle.putString(WSTicketCreation.WS_BUNDLE_COMMENTS,comments);
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
    }

    @Override
    public void executeSerialSave() {
        if (ToolBox_Con.isOnline(context)) {
            mView.setWsProcess(WS_Serial_Save.class.getName());
            //
            mView.showPD(
                    hmAux_Trans.get("progress_serial_save_ttl"),
                    hmAux_Trans.get("progress_serial_save_msg")
            );
            //
            Intent mIntent = new Intent(context, WBR_Serial_Save.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.PROCESS_MENU_SEND, true);
            //
            mIntent.putExtras(bundle);
            //
            context.sendBroadcast(mIntent);
        } else {
            ToolBox_Inf.showNoConnectionDialog(context);
        }
    }

    @Override
    public boolean verifyProductForForm(HMAux hmAux) {
        return false;
    }

    @Override
    public long getSerialCode(long product_code, String serial_id) {
        MD_Product_SerialDao serialDao = new MD_Product_SerialDao(context);
        //
        MD_Product_Serial objSerial = serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        ToolBox_Con.getPreference_Customer_Code(context),
                        product_code,
                        serial_id
                ).toSqlQuery()
        );
        return objSerial.getSerial_code();
    }

    @Override
    public String getSerialSiteDescription(String site_code_form_param) {
        MD_Product_Serial md_product_serial = getMd_product_serial();
        return md_product_serial.getSite_desc() != null ? md_product_serial.getSite_desc() : "";
    }

    @Override
    public void onBackPressedClicked() {
        mView.callAct009(context);
    }

    @Override
    public void validateGPSResource(HMAux item) {
            if (ToolBox_Con.hasGPSResourceActive(context)) {
                //setAct011Call(item);
                //LUCHE - 22/10/2021
                //Substituido pelo defineFormOrFormOsFlow, para validar se é um form ou form tipo o.s
                defineFormOrFormOsFlow(item);
            }else{
                mView.alertActiveGPSResource(item);
            }
    }

}
