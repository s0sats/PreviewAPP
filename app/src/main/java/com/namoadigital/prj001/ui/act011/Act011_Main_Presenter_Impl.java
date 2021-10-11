package com.namoadigital.prj001.ui.act011;

import static com.namoadigital.prj001.model.MD_Product_Serial_Tp_Device_Item.APPLY_MATERIAL_REQUIRED;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.namoa_digital.namoa_library.util.ConstantBase;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoa_digital.namoa_library.util.ToolBox;
import com.namoadigital.prj001.dao.EV_Module_Res_Txt_TransDao;
import com.namoadigital.prj001.dao.GE_Custom_FormDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_BlobDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Blob_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_FieldDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_Field_LocalDao;
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao;
import com.namoadigital.prj001.dao.GE_FileDao;
import com.namoadigital.prj001.dao.GeOsDao;
import com.namoadigital.prj001.dao.GeOsDeviceDao;
import com.namoadigital.prj001.dao.GeOsDeviceItemDao;
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.MD_SiteDao;
import com.namoadigital.prj001.dao.MdTagDao;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_FormDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.AcessoryFormView;
import com.namoadigital.prj001.model.Act011FormTab;
import com.namoadigital.prj001.model.Act011FormTabStatus;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.GeOs;
import com.namoadigital.prj001.model.GeOsDevice;
import com.namoadigital.prj001.model.GeOsDeviceItem;
import com.namoadigital.prj001.model.InspectionCell;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.MD_Site;
import com.namoadigital.prj001.model.MdOrderType;
import com.namoadigital.prj001.model.MdTag;
import com.namoadigital.prj001.model.TK_Ticket;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Form;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_MULTI_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Fields_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_019;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_002;
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_003;
import com.namoadigital.prj001.sql.GeOsDeviceItem_Sql_004;
import com.namoadigital.prj001.sql.GeOsDeviceSql_002;
import com.namoadigital.prj001.sql.GeOsSql_001;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_016;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.Sql_WS_TK_Ticket_Save_008;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_004;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_005;
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_002;
import com.namoadigital.prj001.sql.TK_Ticket_Form_Sql_005;
import com.namoadigital.prj001.sql.TK_Ticket_Sql_001;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by neomatrix on 23/01/17.
 */

public class Act011_Main_Presenter_Impl implements Act011_Main_Presenter {

    private Context context;
    private Act011_Main_View mView;

    private EV_Module_Res_Txt_TransDao module_res_txt_transDao;

    private GE_Custom_FormDao custom_formDao;
    private GE_Custom_Form_FieldDao custom_form_fieldDao;

    private GE_Custom_Form_DataDao custom_form_dataDao;
    private GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao;

    private GE_Custom_Form_LocalDao custom_form_LocalDao;
    private GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao;

    private GE_Custom_Form_BlobDao custom_form_blobDao;
    private GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao;

    private MD_Product_SerialDao md_product_serialDao;
    private MD_ProductDao md_productDao;
    private MdTagDao mdTagDao;

    private HMAux hmAux_Trans;

    private boolean bAgendado;
    private MD_Schedule_ExecDao scheduleExecDao;
    //LUCHE - 20/02/2020
    //Flag que controla se é a primeira abertura do agendamento.
    private boolean isScheduleFirstTime = false;
    //LUCHE - 24/08/2020
    private boolean isTicketProcess = false;
    private TK_Ticket_StepDao ticketStepDao;
    private Integer mTicketSeqTmp;
    private MD_SiteDao siteDao;
    private GeOsDao geOsDao;
    private GeOsDeviceDao geOsDeviceDao;
    private GeOsDeviceItemDao geOsDeviceItemDao;

    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao custom_form_dataDao, GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao, GE_Custom_Form_LocalDao custom_form_LocalDao, GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao, GE_Custom_Form_BlobDao custom_form_blobDao, GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao, MD_Product_SerialDao md_product_serialDao, MD_ProductDao md_productDao, HMAux hmAux_Trans, MD_Schedule_ExecDao scheduleExecDao, TK_Ticket_StepDao ticketStepDao, MD_SiteDao siteDao, MdTagDao mdTagDao, GeOsDao geOsDao,  GeOsDeviceDao geOsDeviceDao, GeOsDeviceItemDao geOsDeviceItemDao) {
        this.context = context;
        this.mView = mView;
        this.module_res_txt_transDao = module_res_txt_transDao;
        this.custom_formDao = custom_formDao;
        this.custom_form_fieldDao = custom_form_fieldDao;
        this.custom_form_dataDao = custom_form_dataDao;
        this.custom_form_data_fieldDao = custom_form_data_fieldDao;
        this.custom_form_LocalDao = custom_form_LocalDao;
        this.custom_form_field_LocalDao = custom_form_field_LocalDao;
        this.custom_form_blobDao = custom_form_blobDao;
        this.custom_form_blob_localDao = custom_form_blob_localDao;
        this.md_product_serialDao = md_product_serialDao;
        this.md_productDao = md_productDao;
        this.hmAux_Trans = hmAux_Trans;
        this.scheduleExecDao = scheduleExecDao;
        this.ticketStepDao = ticketStepDao;
        this.siteDao = siteDao;
        this.mdTagDao = mdTagDao;
        this.geOsDao = geOsDao;
        this.geOsDeviceDao = geOsDeviceDao;
        this.geOsDeviceItemDao = geOsDeviceItemDao;
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String s_form_data, String product_desc, String product_id, String formcode_desc, String serial_id, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code) {
        boolean hasNformPending = false;
        boolean bNew = false;
        boolean bAbortSchedule = false;
        mTicketSeqTmp = mTicket_seq_tmp;
        bAgendado = false;
        //LUCHE - 14/03/2019
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        MD_Schedule_Exec scheduleExec = new MD_Schedule_Exec();
        GE_Custom_Form_Local customFormLocal;
        //LUCHE - 30/09/2021
        GeOs geOs = null;
        //
        if(mTicket_prefix != null && mTicket_code != null &&  mTicket_seq!= null &&  mTicket_seq_tmp!= null &&  mStep_code!= null){
            customFormLocal = custom_form_LocalDao.getByString(
                    new GE_Custom_Form_Local_Sql_019(
                            customer_code,
                            formtype_code,
                            form_code,
                            formversion_code,
                            s_form_data,
                            product_code,
                            serial_id,
                            mTicket_prefix,
                            mTicket_code,
                            mTicket_seq,
                            mStep_code
                    ).toSqlQuery().toString().toLowerCase()
            );
        }else{
            customFormLocal = custom_form_LocalDao.getByString(
                new GE_Custom_Form_Local_Sql_003(
                    customer_code,
                    formtype_code,
                    form_code,
                    formversion_code,
                    s_form_data,
                    product_code,
                    serial_id
                ).toSqlQuery().toString().toLowerCase()
            );
        }
        List<HMAux> cf_fields = null;
        int index = -1;

        if (customFormLocal != null) {
            bNew = false;
            index = -1;
            //LUCHE - 14/02/2020
            //A identificação de se um form é agendamento agora verifica a pk do agendamento e não o status
            bAgendado = ToolBox_Inf.isScheduleForm(customFormLocal);
            //26/03/2020 - Tratativa para se recebeu FCM trocando status , mas o form já havia sido iniciado.
            if (bAgendado) {
                scheduleExec = getMdScheduleExec(
                    customFormLocal.getSchedule_prefix(),
                    customFormLocal.getSchedule_code(),
                    customFormLocal.getSchedule_exec()
                );
                //
                if (MD_Schedule_Exec.isValidScheduleExec(scheduleExec)
                    && scheduleExec.getFcm_new_status() != null
                    && scheduleExec.getFcm_user_nick() != null
                ) {
                    bAbortSchedule = true;
                }
            }
            //Se não deve ser abortado, continual
            if (!bAbortSchedule) {
                //Se for um agendamento e status agendado, seta serial id e muda status para in_processo
                if (bAgendado && customFormLocal.getCustom_form_status().equals(Constant.SYS_STATUS_SCHEDULE)) {
                    isScheduleFirstTime = true;
                    //LUCHE - 20/02/2020
                    //Index  = 0 significa que os campos não serão "validados como obrigatorios" ao carregar o form.
                    //Só deve acontecer a primeira vez que o form é aberto
                    index = 0;
                    daoObjReturn = updateScheduleInfos(customFormLocal, serial_id);
                }
                if (!daoObjReturn.hasError()) {
                    //
                    cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                        new GE_Custom_Form_Fields_Local_Sql_001(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version()),
                            String.valueOf(customFormLocal.getCustom_form_data())
                        ).toSqlQuery().toString().toLowerCase()
                    );
                    if (s_form_data == null || s_form_data.isEmpty() || "0".equals(s_form_data)) {
                        hasNformPending = true;
                    }
                }
            }
            if(customFormLocal.getIs_so() == 1){
                geOs = getGeOs(
                        customer_code,
                        formtype_code,
                        form_code,
                        formversion_code,
                        String.valueOf(customFormLocal.getCustom_form_data())
                );
            }
        } else {
            bNew = true;
            index = 0;
            HMAux ii = custom_formDao.getByStringHM(
                new GE_Custom_Form_Local_Sql_002(
                    customer_code,
                    formtype_code,
                    form_code,
                    formversion_code
                )
                    .toSqlQuery()
                    .toLowerCase()
            );

            GE_Custom_Form customForm = custom_formDao.getByString(
                new GE_Custom_Form_Sql_001_TT(
                    String.valueOf(customer_code),
                    String.valueOf(formtype_code),
                    String.valueOf(form_code),
                    String.valueOf(formversion_code)
                ).toSqlQuery().toString().toLowerCase()

            );
            //LUCHE - 30/09/2021
            if(customForm.getIs_so() == 1){
                geOs = getGeOs(
                    customer_code,
                    formtype_code,
                    form_code,
                    formversion_code,
                    s_form_data
                    );
                //Atualiza formData com o da
                if(geOs != null){
                    ii.put(GE_Custom_Form_Local_Sql_002.ID,String.valueOf(geOs.getCustom_form_data()));
                }
            }
            //
            MD_Product productInfo = getProduct(Integer.parseInt(product_code));
            //
            MdTag tagInfo = getTag(customForm.getTag_operational_code());
            //Resgata dados do site. O site usado pode ser o logado ou do serial, caso existe e esteja alocado
            MD_Site siteInfo = getSiteInfo(customForm.getCustomer_code(),Integer.parseInt(product_code),serial_id);
            //
            customFormLocal = new GE_Custom_Form_Local();

            customFormLocal.setCustomer_code(customForm.getCustomer_code());
            customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
            customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
            customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
            customFormLocal.setCustom_form_data(Long.parseLong(ii.get(GE_Custom_Form_Local_Sql_002.ID)));
            customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
            customFormLocal.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            customFormLocal.setTag_operational_code(tagInfo != null ? tagInfo.getTag_code() : -1);
            customFormLocal.setTag_operational_id(tagInfo != null ? tagInfo.getTag_id() : null);
            customFormLocal.setTag_operational_desc(tagInfo != null ? tagInfo.getTag_desc() : null);
            customFormLocal.setCustom_product_code(Integer.parseInt(product_code));
            customFormLocal.setCustom_product_desc(product_desc);
            customFormLocal.setCustom_product_id(product_id);
            customFormLocal.setCustom_product_icon_name(productInfo.getProduct_icon_name());
            customFormLocal.setCustom_product_icon_url(productInfo.getProduct_icon_url());
            customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
            customFormLocal.setCustom_form_desc(formcode_desc);
            customFormLocal.setSerial_id(serial_id);
            customFormLocal.setRequire_signature(customForm.getRequire_signature());
            customFormLocal.setAutomatic_fill(customForm.getAutomatic_fill());
            customFormLocal.setSchedule_date_start_format("1900-01-01 00:00:00 +00:00");
            customFormLocal.setSchedule_date_end_format("1900-01-01 00:00:00 +00:00");
            customFormLocal.setSchedule_date_start_format_ms(0);
            customFormLocal.setSchedule_date_end_format_ms(0);
            customFormLocal.setRequire_location(customForm.getRequire_location());
            customFormLocal.setRequire_serial_done(customForm.getRequire_serial_done());
            customFormLocal.setSchedule_comments("");
            //LUCHE - 24/08/2020
            customFormLocal.setTicket_prefix(mTicket_prefix);
            customFormLocal.setTicket_code(mTicket_code);
            customFormLocal.setTicket_seq(mTicket_seq);
            customFormLocal.setTicket_seq_tmp(mTicket_seq_tmp);
            customFormLocal.setStep_code(mStep_code);
            //LUCHE - 25/05/2021
            customFormLocal.setSite_code(siteInfo != null ? ToolBox_Inf.convertStringToInt(siteInfo.getSite_code()) : 0);
            customFormLocal.setSite_id(siteInfo != null ? siteInfo.getSite_id() : "");
            customFormLocal.setSite_desc(siteInfo != null ? siteInfo.getSite_desc() : "");
            //LUCHE - 30/09/2021
            customFormLocal.setIs_so(customForm.getIs_so());
            customFormLocal.setSo_edit_start_end(customForm.getSo_edit_start_end());
            customFormLocal.setSo_order_type_code_default(customForm.getSo_order_type_code_default());
            customFormLocal.setSo_allow_change_order_type(customForm.getSo_allow_change_order_type());
            customFormLocal.setSo_allow_backup(customForm.getSo_allow_backup());
            //LUCHE -  14/03/2019
            //Alteração Dao de insert com exception NOVO METODO DAO
            //custom_form_LocalDao.addUpdate(customFormLocal);
            daoObjReturn = custom_form_LocalDao.addUpdateThrowException(customFormLocal);
            //
            if (!daoObjReturn.hasError()) {
                ArrayList<HMAux> items = (ArrayList<HMAux>) custom_form_fieldDao.query_HM(
                    new Sql_Act011_002(
                        String.valueOf(customFormLocal.getCustomer_code()),
                        String.valueOf(customFormLocal.getCustom_form_type()),
                        String.valueOf(customFormLocal.getCustom_form_code()),
                        String.valueOf(customFormLocal.getCustom_form_version()),
                        ToolBox_Con.getPreference_Translate_Code(context),
                        String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
                );

                custom_form_field_LocalDao.addUpdate(items);

                cf_fields = (ArrayList<HMAux>) custom_form_field_LocalDao.query_HM(
                    new GE_Custom_Form_Fields_Local_Sql_001(
                        String.valueOf(customFormLocal.getCustomer_code()),
                        String.valueOf(customFormLocal.getCustom_form_type()),
                        String.valueOf(customFormLocal.getCustom_form_code()),
                        String.valueOf(customFormLocal.getCustom_form_version()),
                        String.valueOf(customFormLocal.getCustom_form_data())
                    ).toSqlQuery().toString().toLowerCase()
                );

                custom_form_blob_localDao.addUpdate(
                    custom_form_blob_localDao.query(
                        new GE_Custom_Form_Blob_Sql_001(
                            String.valueOf(customFormLocal.getCustomer_code()),
                            String.valueOf(customFormLocal.getCustom_form_type()),
                            String.valueOf(customFormLocal.getCustom_form_code()),
                            String.valueOf(customFormLocal.getCustom_form_version())
                        ).toSqlQuery().toString().toLowerCase()
                    )
                    ,
                    false
                );
            }
        }

        //LUCHE - 24/08/2020
        //isTicketProcess = isFormCreateByTicket(customFormLocal.getTicket_prefix(),customFormLocal.getTicket_code(),customFormLocal.getTicket_seq(),customFormLocal.getTicket_seq_tmp(),customFormLocal.getStep_code());
        isTicketProcess = isFormCreateByTicket(customFormLocal);
        //26/03/2020 - Informa usuario que o form foi abortado.
        if (bAbortSchedule) {
            mView.showFormCancelledMsg(customFormLocal,scheduleExec);
        } else {
            //Verifica se houve erro ao inserir tabela form_local.
            if (daoObjReturn.hasError()) {
                if (daoObjReturn.getTable() != null
                    && daoObjReturn.getTable().equalsIgnoreCase(MD_Schedule_ExecDao.TABLE)
                ) {
                    mView.showMsg(
                        hmAux_Trans.get("alert_error_on_update_schedule_status_ttl"),
                        hmAux_Trans.get("alert_error_on_update_schedule_status_msg"),
                        Act011_Main.SHOW_MSG_TYPE_SCHEDULE_EXEC_UPDATE_ERROR
                    );
                } else {
                    mView.showMsg(
                        hmAux_Trans.get("alert_error_on_create_form_ttl"),
                        hmAux_Trans.get("alert_error_on_create_form_msg"),
                        Act011_Main.SHOW_MSG_TYPE_FORM_LOCAL_INSERT_ERROR
                    );
                }
            } else {
                //
                GE_Custom_Form_Data formData = loadAnswer(
                    customFormLocal,
                    so_prefix,
                    so_code,
                    so_site_code,
                    so_operation_code,
                    serial_id,
                    geOs
                );
                //LUCHE - 24/08/2020
                //Atualiza TicketCtrl se form for do ticket
                if (isTicketProcess) {
                    if(bNew){
                        if(mView.isOffHandForm()){
                            createTicketCtrlObj(customFormLocal,
                                    mTicket_prefix,
                                    mTicket_code,
                                    mStep_code
                            );
                            //
                            TK_Ticket tkTicket = getTicketbyPk(mTicket_prefix, mTicket_code);
                            formData.setCustomer_code(Long.parseLong(customer_code));
                            formData.setTicket_prefix(mTicket_prefix);
                            formData.setTicket_code(mTicket_code);
                            formData.setTicket_seq(0);
                            formData.setTicket_seq_tmp(mTicketSeqTmp);
                            formData.setStep_code(mStep_code);
                            formData.setPipeline_code(tkTicket.getPipeline_code());
                            //
                        }
                        //
                        updateTicketCtrl(
                            customFormLocal.getCustomer_code(),
                            customFormLocal.getTicket_prefix(),
                            customFormLocal.getTicket_code(),
                            customFormLocal.getTicket_seq(),
                            customFormLocal.getTicket_seq_tmp(),
                            customFormLocal.getStep_code(),
                            customFormLocal.getCustom_form_data(),
                            ConstantBaseApp.SYS_STATUS_PROCESS,
                            formData.getLocation_pendency());
                    }

                    if(formData.getTicket_checkin_date() == null || formData.getTicket_checkin_date().isEmpty()) {
                        //Resgata Step para setar data de checkin no form.
                        TK_Ticket_Step ticketStep = getTicketStep(
                                customFormLocal.getTicket_prefix(),
                                customFormLocal.getTicket_code(),
                                customFormLocal.getStep_code()
                        );
                        //Seta data de checkin no formData
                        formData.setTicket_checkin_date(ticketStep.getStep_start_date());
                    }
                }
                //if (bAgendado) {
                if (isScheduleFirstTime) {
                    if (serial_id == null || serial_id.isEmpty()) {
                        formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                        formData.setZone_code(null);
                        formData.setLocal_code(null);
                    } else {
                        MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                            new MD_Product_Serial_Sql_002(
                                Long.parseLong(customer_code),
                                Long.parseLong(product_code),
                                serial_id
                            ).toSqlQuery()
                        );

                        if (md_product_serialAux != null) {
                            formData.setSite_code(md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context));
                            formData.setZone_code(md_product_serialAux.getZone_code());
                            formData.setLocal_code(md_product_serialAux.getLocal_code());
                        } else {
                            // Erro Nao deve Acontecer
                            formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                            formData.setZone_code(null);
                            formData.setLocal_code(null);
                        }
                    }

                    formData.setSite_code(String.valueOf(customFormLocal.getSite_code()));
                    //
                    custom_form_dataDao.addUpdate(formData);
                    custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);
                }

                ArrayList<HMAux> pdfs = (ArrayList<HMAux>) custom_form_blob_localDao.query_HM(

                    new GE_Custom_Form_Blob_Local_Sql_005(
                        String.valueOf(customFormLocal.getCustomer_code()),
                        String.valueOf(customFormLocal.getCustom_form_type()),
                        String.valueOf(customFormLocal.getCustom_form_code()),
                        String.valueOf(customFormLocal.getCustom_form_version())
                    ).toSqlQuery().toString()
                );
                ArrayList<AcessoryFormView> acessoryFormViews = new ArrayList<>();
                //LUCHE - 30/09/2021
                if(customFormLocal.getIs_so() == 1) {
                    acessoryFormViews =
                            getAcessoryFormView(
                                    geOs,
                                    customFormLocal
                            );
                }
                //
                if (hasNformPending) {
                    mView.showMsg(
                            hmAux_Trans.get("alert_nform_already_started_ttl"),
                            hmAux_Trans.get("alert_nform_already_started_msg") + " " +
                                    ToolBox_Inf.millisecondsToString(
                                            ToolBox_Inf.dateToMilliseconds(formData.getDate_start()),
                                            ToolBox_Inf.nlsDateFormat(context) + " HH:mm"
                                    ),
                            0
                    );
                }
                //
                mView.loadFragment_CF_Fields(cf_fields, bNew, customFormLocal, formData, customFormLocal.getCustom_form_pre(), pdfs, index, customFormLocal.getRequire_signature(), customFormLocal.getRequire_serial_done(), acessoryFormViews, geOs);
            }
        }
    }

    private ArrayList<AcessoryFormView> getAcessoryFormView(GeOs geOs, GE_Custom_Form_Local customFormLocal) {
        ArrayList<AcessoryFormView> acessoryFormViews = new ArrayList<>();
//        GeOsDeviceItem
        List<GeOsDevice> devices = getDeviceList(geOs);
        for(GeOsDevice device: devices){
            AcessoryFormView acessoryFormView = new AcessoryFormView(
                    device.getDevice_tp_desc(),
                    device.getTracking_number(),
                    !isInProcessing(customFormLocal),
                    0,
                    device.getGeOsDevicePkPrefix(),
                    new ArrayList<>()
            );
            List<InspectionCell> inspections = acessoryFormView.getInspections();
            List<GeOsDeviceItem> deviceItem =  getDeviceItem(device);
            for(GeOsDeviceItem item: deviceItem){
                inspections.add(new InspectionCell(
                    item.getItem_check_desc(),
                    ToolBox_Inf.getDateDiferenceInDays(item.getTarget_date(),ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z")),
                    getPhotoCount(item),
                    item.getMaterialList().size(),
                    item.getApply_material().equals(APPLY_MATERIAL_REQUIRED),
                    item.getExec_comment() != null && !item.getExec_comment().isEmpty(),
                    item.getRequire_justify_problem() == 1,
                        item.getItem_check_status(),
                        item.getCritical_item() == 1,
                        item.getStructure() == 0,
                        item.getStatus_answer(),
                        item.getExec_type(),
                        item.getGeOsDeviceItemCodeAndSeq()
                    )
                );
            }
            acessoryFormViews.add(acessoryFormView);
        }
        return acessoryFormViews;
    }

    @NonNull
    private List<GeOsDevice> getDeviceList(GeOs geOs) {
        return geOsDeviceDao.query(
                new GeOsDeviceSql_002(
                        geOs.getCustomer_code(),
                        geOs.getCustom_form_type(),
                        geOs.getCustom_form_code(),
                        geOs.getCustom_form_version(),
                        geOs.getCustom_form_data()
                ).toSqlQuery()
        );
    }


    private int getPhotoCount(GeOsDeviceItem item) {
        return getImageCount(item.getExec_photo1()) + getImageCount(item.getExec_photo2()) + getImageCount(item.getExec_photo3()) +getImageCount(item.getExec_photo4());
    }

    private int getImageCount(String photoPath) {
        if (photoPath != null && !photoPath.isEmpty()) {
            return 1;
        }
        return 0;
    }



    private List<GeOsDeviceItem> getDeviceItem(GeOsDevice device) {
         return geOsDeviceItemDao.query(new GeOsDeviceItem_Sql_002(
                device.getCustomer_code(),
                device.getCustom_form_type(),
                device.getCustom_form_code(),
                device.getCustom_form_version(),
                device.getCustom_form_data(),
                device.getProduct_code(),
                device.getSerial_code(),
                device.getDevice_tp_code()
        ).toSqlQuery());
    }

    private GeOs getGeOs(String customer_code, String formtype_code, String form_code, String formversion_code, String s_form_data) {
        return geOsDao.getByString(
            new GeOsSql_001(
                customer_code,
                formtype_code,
                form_code,
                formversion_code,
                s_form_data
            ).toSqlQuery()
        );
    }

    @Deprecated
    private boolean ticketCtrlExist(GE_Custom_Form_Local customFormLocal, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code) {

        TK_Ticket_FormDao ticketFormDao = getTicketFormDao();
        TK_Ticket_Form ticketForm = ticketFormDao.getByString(
                new TK_Ticket_Form_Sql_002(
                        customFormLocal.getCustomer_code(),
                        mTicket_prefix,
                        mTicket_code,
                        mTicket_seq_tmp,
                        mStep_code
                ).toSqlQuery()
        );
        return ticketForm != null;
    }

    private TK_Ticket_FormDao getTicketFormDao() {
        return new TK_Ticket_FormDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public boolean isFormCreateByTicket(GE_Custom_Form_Local customFormLocal) {
        return
                customFormLocal.getTicket_prefix() != null && customFormLocal.getTicket_prefix() > -1
                        && customFormLocal.getTicket_code() != null && customFormLocal.getTicket_code() > -1
                        && customFormLocal.getTicket_seq() != null && customFormLocal.getTicket_seq() > -1
                        && customFormLocal.getTicket_seq_tmp() != null && customFormLocal.getTicket_seq_tmp() > -1
                        && customFormLocal.getStep_code() != null && customFormLocal.getStep_code() > -1;
    }

    /**
     * LUCHE - 08/09/2020
     * Metodo que altera status do ticket e dependendo do status finaliza ou não ctrl.
     * Se o form ainda precisar de posição GPS, seta o status waiting_sync MAS NÃO UPDATE REQUIRED
     * @param customer_code
     * @param mTicket_prefix
     * @param mTicket_code
     * @param mTicket_seq
     * @param mTicket_seq_tmp
     * @param mStep_code
     * @param custom_form_data
     * @param status
     * @param location_pendency - Identificador se o form esta com pendencia de GPS.
     */
    private void updateTicketCtrl(long customer_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code, long custom_form_data, String status, int location_pendency) {
        TK_Ticket_Step tkTicketStep = getTicketStep(mTicket_prefix, mTicket_code, mStep_code);
        //
        if(tkTicketStep != null && tkTicketStep.getCustomer_code() > 0){
            if(tkTicketStep.getCtrl() != null){
                int ctrlIdx = getTicketStepCtrlIdx(customer_code, mTicket_prefix, mTicket_code, mTicket_seq_tmp, mStep_code, tkTicketStep);
                //
                if(ctrlIdx > -1){
                    TK_Ticket_Ctrl tkTicketCtrl = tkTicketStep.getCtrl().get(ctrlIdx);
                    switch (status){
                        case ConstantBaseApp.SYS_STATUS_PROCESS:
                            setStartInfoIntoCtrl(tkTicketCtrl);
                            setCheckInIntoStepWhenOneTouchStep(tkTicketStep,tkTicketCtrl);
                            tkTicketCtrl.getForm().setCustom_form_data_tmp((int) custom_form_data);
                            break;
                        case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                            setCloseInfoIntoCtrl(tkTicketCtrl);
                            checkCloseStepForWaitingSync(tkTicketStep,tkTicketCtrl);
                            //
                            /*
                             * location_pendency
                             *  0: Sem pendencia de GPS, finaliza e seta update required
                             *  1: Com pendencia de GPS, finaliza e deixa serviço de GPS setar update_required
                             */
                            if(location_pendency == 0) {
                                tkTicketCtrl.setUpdate_required(1);
                                tkTicketStep.setUpdate_required(1);
                            }
                            break;
                    }
                    tkTicketStep.getCtrl().set(ctrlIdx,tkTicketCtrl);
                    //
                    ticketStepDao.addUpdate(tkTicketStep);
                    //Se finalizou o ctrl, seta sync required tb no ticket.
                    if(tkTicketStep.getUpdate_required() == 1){
                        ticketStepDao.addUpdate(
                            new Sql_WS_TK_Ticket_Save_008(
                                tkTicketStep.getCustomer_code(),
                                tkTicketStep.getTicket_prefix(),
                                tkTicketStep.getTicket_code(),
                                1
                            ).toSqlQuery()
                        );
                    }
                } else{
                    mView.showMsg(
                        hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_ttl"),
                        hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_msg"),
                        Act011_Main.SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR
                    );
                }
            } else{
                mView.showMsg(
                    hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_ttl"),
                    hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_msg"),
                    Act011_Main.SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR
                );
            }
        } else {
            mView.showMsg(
                hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_ttl"),
                hmAux_Trans.get("alert_ticket_step_or_ctrl_not_found_msg"),
                Act011_Main.SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR
            );
        }
        //TODO TRATAR CASOS NAO ENCONTRE AS INFOS.
    }

    private int getStepIdx(TK_Ticket_Ctrl mTicketCtrl, TK_Ticket tkTicket) {
        for (int i = 0; i < tkTicket.getStep().size(); i++) {
            if (
                    tkTicket.getStep().get(i).getTicket_prefix() == mTicketCtrl.getTicket_prefix()
                            && tkTicket.getStep().get(i).getTicket_code() == mTicketCtrl.getTicket_code()
                            && tkTicket.getStep().get(i).getStep_code() == mTicketCtrl.getStep_code()
            ) {
                return i;
            }
        }
        return -1;
    }

    private int getTicketStepCtrlIdx(long customer_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq_tmp, Integer mStep_code, TK_Ticket_Step tkTicketStep) {
        int ctrlIdx = -1;
        for (int i = 0; i < tkTicketStep.getCtrl().size(); i++) {
            if(
                tkTicketStep.getCtrl().get(i).getCustomer_code() == customer_code
                && tkTicketStep.getCtrl().get(i).getTicket_prefix() == mTicket_prefix
                && tkTicketStep.getCtrl().get(i).getTicket_code() == mTicket_code
                && tkTicketStep.getCtrl().get(i).getTicket_seq_tmp() == mTicket_seq_tmp
                && tkTicketStep.getCtrl().get(i).getStep_code() == mStep_code
            ){
                ctrlIdx = i;
            }
        }
        return ctrlIdx;
    }

    private TK_Ticket_Step getTicketStep(Integer mTicket_prefix, Integer mTicket_code, Integer mStep_code) {
        return ticketStepDao.getByString(
            new TK_Ticket_Step_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                mTicket_prefix,
                mTicket_code,
                mStep_code
            ).toSqlQuery()
        );
    }

    private void setStartInfoIntoCtrl(TK_Ticket_Ctrl ticketCtrl) {
        if( ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(ticketCtrl.getCtrl_status())
            && ticketCtrl.getCtrl_start_date() == null
        ){
            ticketCtrl.setCtrl_start_date(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
            ticketCtrl.setCtrl_start_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
            ticketCtrl.setCtrl_start_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
            ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_PROCESS);
            ticketCtrl.copyCtrlStatusForInnerProcess();
        }
    }

    /**
     * LUCHE - 09/11/2020
     * Modificado metodo adicionando a chamada do metdo forceNoneObjToWaitingSync que fecha o processo none planejado caso exista
     * @param ticketStep
     * @param mTicketCtrl
     */
    private void setCheckInIntoStepWhenOneTouchStep(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        if(ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type())
            && !ToolBox_Inf.hasConsistentValueString(ticketStep.getStep_start_date())
        ) {
            ticketStep.setStep_start_date(mTicketCtrl.getCtrl_start_date());
            ticketStep.setStep_start_user(mTicketCtrl.getCtrl_start_user());
            ticketStep.setStep_start_user_nick(mTicketCtrl.getCtrl_start_user_name());
            //LUCHE - 09/11/2020
            //Com a nova definição, se o step é check in manual e seu obj planejado é none, esse deve ser
            //finalizado junto com o checkin...
            ToolBox_Inf.forceNoneObjToWaitingSync(ticketStep, true);
        }
    }

    private void setCloseInfoIntoCtrl(TK_Ticket_Ctrl tkTicketCtrl) {
        if( ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(tkTicketCtrl.getCtrl_status())
            && tkTicketCtrl.getCtrl_end_date() == null
        ){
            tkTicketCtrl.setCtrl_end_date(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
            tkTicketCtrl.setCtrl_end_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
            tkTicketCtrl.setCtrl_end_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
            tkTicketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            tkTicketCtrl.copyCtrlStatusForInnerProcess();
        }
    }

    private void checkCloseStepForWaitingSync(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        int stepCtrlsFinalizedCounter = 0;
        for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
            if(isDoneOrWaitingSync(ticketCtrl.getCtrl_status())){
                stepCtrlsFinalizedCounter++;
            }
        }
        //Se todos os ctrl estão finalizado e o step é one_touch ou for start_end com move_next_step,
        //faz checkout
        if( stepCtrlsFinalizedCounter == ticketStep.getCtrl().size()
            && ticketStep.getMove_next_step() == 1
        ){
            ticketStep.setStep_status(ConstantBaseApp.SYS_STATUS_WAITING_SYNC);
            ticketStep.setStep_end_date(mTicketCtrl.getCtrl_end_date());
            ticketStep.setStep_end_user(mTicketCtrl.getCtrl_end_user());
            ticketStep.setStep_end_user_nick(mTicketCtrl.getCtrl_end_user_name());
        }
    }

    private boolean isDoneOrWaitingSync(String ctrl_status) {
        return ConstantBaseApp.SYS_STATUS_DONE.equals(ctrl_status)
            || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(ctrl_status) ;
    }

    /**
     * LUCHE - 02/09/2020
     * <p></p>
     * Metodo que reseta referencia do custom_form_data no ticket_form
     * @param formLocal
     */
    @Override
    public void resetTicketCtrlFormDataIfNeeds(GE_Custom_Form_Local formLocal) {
        if (isFormCreateByTicket(formLocal)) {
            if (isaOffHandTicketForm(formLocal)) {
                deleteCtrlFormData(formLocal);
            } else {
                resetCtrlFormData(formLocal);
            }
        }
    }

    private boolean isaOffHandTicketForm(GE_Custom_Form_Local formLocal) {
        TK_Ticket_CtrlDao ticketCtrlDao = getTicketCtrlDao();
        TK_Ticket_Ctrl tk_ctrl = ticketCtrlDao.getByString(
                new TK_Ticket_Ctrl_Sql_004(
                        formLocal.getCustomer_code(),
                        formLocal.getTicket_prefix(),
                        formLocal.getTicket_code(),
                        formLocal.getTicket_seq_tmp()
                ).toSqlQuery()
        );

        return tk_ctrl.getObj_planned() == 0;
    }

    private void deleteCtrlFormData(GE_Custom_Form_Local customFormLocal) {

        TK_Ticket_FormDao ticketFormDao = getTicketFormDao();
        ticketFormDao.remove(
                new TK_Ticket_Form_Sql_005(
                        customFormLocal.getCustomer_code(),
                        customFormLocal.getTicket_prefix(),
                        customFormLocal.getTicket_code(),
                        customFormLocal.getTicket_seq_tmp(),
                        customFormLocal.getStep_code()
                ).toSqlQuery()
        );

        TK_Ticket_CtrlDao ticketCtrlDao = getTicketCtrlDao();
        ticketCtrlDao.remove(
                new TK_Ticket_Ctrl_Sql_005(
                        customFormLocal.getCustomer_code(),
                        customFormLocal.getTicket_prefix(),
                        customFormLocal.getTicket_code(),
                        customFormLocal.getTicket_seq_tmp(),
                        customFormLocal.getStep_code()
                ).toSqlQuery()
        );

    }

    private void resetCtrlFormData(GE_Custom_Form_Local formLocal) {
        TK_Ticket_FormDao ticketFormDao = new TK_Ticket_FormDao(
            context, ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );
        //
        TK_Ticket_Form ticketForm = ticketFormDao.getByString(
            new TK_Ticket_Form_Sql_002(
                formLocal.getCustomer_code(),
                formLocal.getTicket_prefix(),
                formLocal.getTicket_code(),
                formLocal.getTicket_seq_tmp(),
                formLocal.getStep_code()
            ).toSqlQuery()
        );
        //
        if(ticketForm != null){
            ticketForm.setCustom_form_data_tmp(null);
            DaoObjReturn daoObjReturn = ticketFormDao.addUpdate(ticketForm);
            //Tratar diferente ???, não tem rollback sem transaction
            if(daoObjReturn.hasError()){
                ToolBox_Inf.registerException(
                    getClass().getName(),
                    new Exception(daoObjReturn.getErrorMsg())
                );
            }
        }
    }

    /**
     * LUCHE - 08/09/2020
     * <p></p>
     * Metodo que verifica se form esta no status waiting_sync.
     * @param customer_code
     * @param custom_form_type
     * @param custom_form_code
     * @param custom_form_version
     * @param custom_form_data
     * @return
     */
    public boolean setForceSentByForm(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, int custom_form_data){
        GE_Custom_Form_Data formData =
            getGeCustomFormDataByPk(customer_code, custom_form_type, custom_form_code, custom_form_version, custom_form_data);
        //
        return
            formData != null
            && ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(formData.getCustom_form_status())
            && formData.getLocation_pendency() == 0;
    }

    /**
     * LUCHE - 08/09/2020
     * <p></p>
     * Metodo que seleciona o custom_form_data atual com o status atualizado.
     * @param customer_code
     * @param custom_form_type
     * @param custom_form_code
     * @param custom_form_version
     * @param custom_form_data
     * @return
     */
    private GE_Custom_Form_Data getGeCustomFormDataByPk(long customer_code, int custom_form_type, int custom_form_code, int custom_form_version, int custom_form_data) {
        return custom_form_dataDao.getByString(
            new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                String.valueOf(customer_code),
                String.valueOf(custom_form_type),
                String.valueOf(custom_form_code),
                String.valueOf(custom_form_version),
                String.valueOf(custom_form_data)
            ).toSqlQuery()
        );
    }

    private DaoObjReturn updateScheduleInfos(GE_Custom_Form_Local customFormLocal, String serial_id) {
        DaoObjReturn daoObjRet = new DaoObjReturn();
        MD_Schedule_Exec mdScheduleExec = getMdScheduleExec(customFormLocal.getSchedule_prefix(), customFormLocal.getSchedule_code(), customFormLocal.getSchedule_exec());
        String originalScheduleSerialId = MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec) ? mdScheduleExec.getSerial_id() : null ;
        Integer originalScheduleSeriaCode = MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec) ? mdScheduleExec.getSerial_code() : null ;
        MD_Product_Serial productSerial = null;
        MD_Product mdProduct = getProduct(mdScheduleExec.getProduct_code());
        //Busca dados do serial. Se não havia serial no agendamento, busca usando o serial passado no bundle
        productSerial = getSerialInfo(
            mdScheduleExec.getCustomer_code(),
            mdScheduleExec.getProduct_code(),
            originalScheduleSeriaCode != null && originalScheduleSeriaCode > 0 ? mdScheduleExec.getSerial_id() : serial_id,
            customFormLocal
        );
        //
        customFormLocal.setSerial_id(productSerial.getSerial_id());
        customFormLocal.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
        //LUCHE - 09/06/2021
        //Modificado logica para tb atualizar o serial de null para '' nos casos de agendamento
        //de produto que não requer serial e o usr prossiga pro agendamento sem informar o serial.
        if(MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec)){
            if(originalScheduleSerialId == null) {
                if( (productSerial.getSerial_id() != null
                    && !productSerial.getSerial_id().isEmpty())
                    || (mdProduct.getRequire_serial() == 0)
                ){
                    if(mdProduct.getRequire_serial() == 1) {
                        mdScheduleExec.setSerial_code((int) productSerial.getSerial_code());
                    }
                    mdScheduleExec.setSerial_id(productSerial.getSerial_id());
                }
            }
            mdScheduleExec.setStatus(Constant.SYS_STATUS_IN_PROCESSING);
        }
        //Se erro retorn obj com erro
        daoObjRet = scheduleExecDao.addUpdate(mdScheduleExec);
        if(daoObjRet.hasError()) {
            return daoObjRet;
        }
        //Se não houve erro tenta atualizar form_local
        daoObjRet = custom_form_LocalDao.addUpdateThrowException(customFormLocal);
        //se erro, faz rollback dos atualização do agendamento
        if(daoObjRet.hasError()){
            mdScheduleExec.setSerial_code(originalScheduleSeriaCode);
            mdScheduleExec.setSerial_id(originalScheduleSerialId);
            mdScheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_SCHEDULE);
            //Se der erro aqui, all hope is gone...
            scheduleExecDao.addUpdate(mdScheduleExec);
        }
        //
        return daoObjRet;
    }

    @Override
    public void cancelScheduleAndForm(GE_Custom_Form_Local customFormLocal, MD_Schedule_Exec scheduleExec) {
        //Deveriam ser o mesmo.
        String curStatus = scheduleExec.getStatus();
        boolean rollback = false;
        //
        GE_Custom_Form_Data customFormData = custom_form_dataDao.getByString(
                                    new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                                        String.valueOf(customFormLocal.getCustomer_code()),
                                        String.valueOf(customFormLocal.getCustom_form_type()),
                                        String.valueOf(customFormLocal.getCustom_form_code()),
                                        String.valueOf(customFormLocal.getCustom_form_version()),
                                        String.valueOf(customFormLocal.getCustom_form_data())
                                    ).toSqlQuery().toLowerCase()
        );
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_CANCELLED);
        customFormLocal.setCustom_form_status(ConstantBaseApp.SYS_STATUS_CANCELLED);
        //
        daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
        rollback = daoObjReturn.hasError();
        if(!rollback){
            daoObjReturn = custom_form_LocalDao.addUpdateThrowException(customFormLocal);
            rollback = daoObjReturn.hasError();
        }
        //Se não ha erro, e existe resposta, tenta o delete.
        if(customFormData != null && !rollback) {
            customFormData.setCustom_form_status(ConstantBaseApp.SYS_STATUS_CANCELLED);
            custom_form_dataDao.addUpdate(customFormData);
            //LUCHE - 18/01/2021 - Se cancelamento de form via  FCM, decrementa contador de execuções
            //do app.
            checkAppExecutionDecrementUpdateNeeds(customFormData.getSo_prefix(),customFormData.getSo_code(),customFormData);
        }
        //Segura na mão de deus e vai
        if(rollback){
            scheduleExec.setStatus(curStatus);
            customFormLocal.setCustom_form_status(curStatus);
            scheduleExecDao.addUpdate(scheduleExec);
            custom_form_LocalDao.addUpdate(customFormLocal);
            if(customFormData != null) {
                customFormData.setCustom_form_status(curStatus);
                custom_form_dataDao.addUpdate(customFormData);
            }
            //
            mView.showMsg(
                hmAux_Trans.get("alert_erro_on_cancel_schedule_form_ttl"),
                hmAux_Trans.get("alert_erro_on_cancel_schedule_form_msg"),
                Act011_Main.SHOW_MSG_TYPE_SCHEDULE_EXEC_CANCEL_ERROR
            );
        }else{
            onBackPressedClicked();
        }
    }

    @Override
    public void validateGPSResource(GE_Custom_Form_Local formLocal) {
        if( formLocal != null
            && formLocal.getRequire_location() == 1
            && formLocal.getCustom_form_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_IN_PROCESSING)
            && !ToolBox_Con.hasGPSResourceActive(context)
        ){
            mView.alertActiveGPSResource();
        }
    }

    /**
     * LUCHE - 14/02/2020
     *
     * Modificado assinatura do metodo, substituido os parametro de form e produto pelo obj GE_Custom_Form_Local
     *
     * @param formLocal - Obj Form local
     * @param so_prefix - Prefixo da O.S
     * @param so_code - Code da O.S
     * @param so_site_code - Site da O.S
     * @param so_operation_code - Operacao da O.S
     * @param serial_id - ID do Serial
     * @param geOs
     * @return - Respostas form_data
     */
    private GE_Custom_Form_Data loadAnswer(GE_Custom_Form_Local formLocal, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, String serial_id, GeOs geOs){
        GE_Custom_Form_Data form_data = custom_form_dataDao
            .getByString(
                new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                    String.valueOf(formLocal.getCustomer_code()),
                    String.valueOf(formLocal.getCustom_form_type()),
                    String.valueOf(formLocal.getCustom_form_code()),
                    String.valueOf(formLocal.getCustom_form_version()),
                    String.valueOf(formLocal.getCustom_form_data())
                ).toSqlQuery().toLowerCase()
            );

        if (form_data != null) {
            form_data.setDataFields(
                custom_form_data_fieldDao.query(
                    new GE_Custom_Form_Data_Field_MULTI_SqlSpecification(
                        String.valueOf(formLocal.getCustomer_code()),
                        String.valueOf(formLocal.getCustom_form_type()),
                        String.valueOf(formLocal.getCustom_form_code()),
                        String.valueOf(formLocal.getCustom_form_version()),
                        String.valueOf(form_data.getCustom_form_data())
                    ).toSqlQuery().toLowerCase()
                )
            );
        } else {
            form_data = new GE_Custom_Form_Data();
            //
            form_data.setCustomer_code(formLocal.getCustomer_code());
            form_data.setCustom_form_type(formLocal.getCustom_form_type());
            form_data.setCustom_form_code(formLocal.getCustom_form_code());
            form_data.setCustom_form_version(formLocal.getCustom_form_version());
            form_data.setCustom_form_data(formLocal.getCustom_form_data());
            form_data.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            form_data.setProduct_code(formLocal.getCustom_product_code());
            form_data.setSerial_id("");
            form_data.setDate_start(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
            form_data.setDate_end("1900-01-01 00:00:00 +00:00");
            form_data.setUser_code(Long.parseLong(ToolBox_Con.getPreference_User_Code(context)));
            //LUCHE - 14/02/2020
            form_data.setSchedule_prefix(formLocal.getSchedule_prefix());
            form_data.setSchedule_code(formLocal.getSchedule_code());
            form_data.setSchedule_exec(formLocal.getSchedule_exec());
            //LUCHE - 24/08/2020
            form_data.setTicket_prefix(formLocal.getTicket_prefix());
            form_data.setTicket_code(formLocal.getTicket_code());
            form_data.setTicket_seq(formLocal.getTicket_seq());
            form_data.setTicket_seq_tmp(formLocal.getTicket_seq_tmp());
            form_data.setStep_code(formLocal.getStep_code());
            //
            if (serial_id == null || serial_id.isEmpty()) {
                form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                form_data.setZone_code(null);
                form_data.setLocal_code(null);
            } else {
                MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                    new MD_Product_Serial_Sql_002(
                        formLocal.getCustomer_code(),
                        formLocal.getCustom_product_code(),
                        serial_id
                    ).toSqlQuery()
                );

                if (md_product_serialAux != null) {
                    form_data.setSite_code(md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(md_product_serialAux.getZone_code());
                    form_data.setLocal_code(md_product_serialAux.getLocal_code());
                } else {
                    // Erro Nao deve Acontecer
                    form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(null);
                    form_data.setLocal_code(null);
                }
            }
            form_data.setOperation_code(so_operation_code != null ? so_operation_code : ToolBox_Con.getPreference_Operation_Code(context));
            form_data.setSignature("");
            form_data.setToken("");
            form_data.setSo_prefix(so_prefix);
            form_data.setSo_code(so_code);
            form_data.setTag_operational_code(formLocal.getTag_operational_code());
            form_data.setSys_date_start(form_data.getDate_start());
            form_data.setSys_date_end(form_data.getDate_end());
            //LUCHE30/09/2021 - CAMPOS FORM OS
            if(formLocal.getIs_so() == 1 && geOs != null){
                form_data.setDate_start(geOs.getDate_start());
                form_data.setOrder_type_code(geOs.getOrder_type_code());
                form_data.setBackup_product_code(geOs.getBackup_product_code());
                form_data.setBackup_serial_code(geOs.getBackup_serial_code());
                form_data.setDevice_tp_code(geOs.getDevice_tp_code_main());
                form_data.setMeasure_tp_code(geOs.getMeasure_tp_code());
                form_data.setMeasure_value(geOs.getMeasure_value());
                if(geOs.getProcess_type().equalsIgnoreCase(MdOrderType.PREVENTIVE)) {
                    form_data.setMeasure_cycle_value(geOs.getMeasure_cycle_value());
                }
            }
            //
            if(isFreeExecutionControlSituation(so_prefix, so_code, form_data)){
                updateAppExecutionCounter(form_data,true);
            }
        }
        //
        return form_data;
    }

    /**
     * LUCHE - 26/05/2021
     * Metodo que resgata infos do site a serem usados.
     * Os dados de site so estavam sendo prenchidos quando criados via agendamento
     * e agora precisamos a info no myActions
     * @param customer_code
     * @param productCode
     * @param serial_id
     * @return
     */
    private MD_Site getSiteInfo(long customer_code, int productCode, String serial_id) {
        String siteCode = ToolBox_Con.getPreference_Site_Code(context);
        if (serial_id != null && !serial_id.isEmpty()) {
            MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                    customer_code,
                    productCode,
                    serial_id
                ).toSqlQuery()
            );
            //
            if (md_product_serialAux != null) {
                siteCode = md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context);
            }
        }
        MD_Site siteObjInfo = ToolBox_Inf.getSiteObjInfo(context, siteCode);
        //
        return siteObjInfo;
    }


    /**
     * LUCHE - 18/01/2021
     * Metodo que verifica se deve decrementar o contador de exe do app no site.
     * @param mSo_prefix
     * @param mSo_code
     * @param formData
     */
    @Override
    public void checkAppExecutionDecrementUpdateNeeds(Integer mSo_prefix, Integer mSo_code, GE_Custom_Form_Data formData) {
        if(isFreeExecutionControlSituation(mSo_prefix, mSo_code, formData)){
            updateAppExecutionCounter(formData,false);
        }
    }

    /**
     * LUCHE - 18/01/2021
     * Metodo que validade se deve contralar a execução no site.
     * @param so_prefix
     * @param so_code
     * @param form_data
     * @return
     */
    private boolean isFreeExecutionControlSituation(Integer so_prefix, Integer so_code, GE_Custom_Form_Data form_data) {
        return ToolBox_Inf.isConcurrentBySiteLicense(context)
                && ToolBox_Inf.isSiteLicenseDisabled(context,form_data.getSite_code())
                && !isTicketProcess
                && so_prefix == null && so_code == null;
    }

    /**
     * LUCHE - 18/01/2021
     * Metodo que atualiza o contador de exec do app incrementando ou decrementado.
     * @param form_data
     * @param increment
     */
    private void updateAppExecutionCounter(GE_Custom_Form_Data form_data, boolean increment) {
        MD_Site mdSite = ToolBox_Inf.getSiteObjInfo(context, form_data.getSite_code());
        if (mdSite != null) {
            int app_executions_count = mdSite.getApp_executions_count();
            if(increment){
                mdSite.setApp_executions_count(app_executions_count + 1);
            }else{
                mdSite.setApp_executions_count(app_executions_count - 1);
            }
            siteDao.addUpdate(mdSite);
        }
    }

    /*private GE_Custom_Form_Data loadAnswer(long customer_code, long product_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data, Long custom_form_data_serv, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, String serial_id) {

        GE_Custom_Form_Data form_data = custom_form_dataDao

                .getByString(

                        new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                                String.valueOf(customer_code),
                                String.valueOf(custom_form_type),
                                String.valueOf(custom_form_code),
                                String.valueOf(custom_form_version),
                                String.valueOf(custom_form_data)
                        ).toSqlQuery().toLowerCase()

                );

        if (form_data != null) {
            form_data.setDataFields(

                    custom_form_data_fieldDao.query(
                            new GE_Custom_Form_Data_Field_MULTI_SqlSpecification(
                                    String.valueOf(customer_code),
                                    String.valueOf(custom_form_type),
                                    String.valueOf(custom_form_code),
                                    String.valueOf(custom_form_version),
                                    String.valueOf(form_data.getCustom_form_data())
                            ).toSqlQuery().toLowerCase()
                    )
            );
        } else {
            form_data = new GE_Custom_Form_Data();
            //
            form_data.setCustomer_code(customer_code);
            form_data.setCustom_form_type((int) custom_form_type);
            form_data.setCustom_form_code((int) custom_form_code);
            form_data.setCustom_form_version((int) custom_form_version);
            form_data.setCustom_form_data(custom_form_data);
            form_data.setCustom_form_data_serv(custom_form_data_serv);
            form_data.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            form_data.setProduct_code(product_code);
            form_data.setSerial_id("");
            form_data.setDate_start(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
            form_data.setDate_end("1900-01-01 00:00:00 +00:00");
            form_data.setUser_code(Long.parseLong(ToolBox_Con.getPreference_User_Code(context)));

            if (serial_id == null || serial_id.isEmpty()) {
                form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                form_data.setZone_code(null);
                form_data.setLocal_code(null);
            } else {
                MD_Product_Serial md_product_serialAux = md_product_serialDao.getByString(
                        new MD_Product_Serial_Sql_002(
                                customer_code,
                                product_code,
                                serial_id
                        ).toSqlQuery()
                );

                if (md_product_serialAux != null) {
                    form_data.setSite_code(md_product_serialAux.getSite_code() != null ? String.valueOf(md_product_serialAux.getSite_code()) : ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(md_product_serialAux.getZone_code());
                    form_data.setLocal_code(md_product_serialAux.getLocal_code());
                } else {
                    // Erro Nao deve Acontecer
                    form_data.setSite_code(ToolBox_Con.getPreference_Site_Code(context));
                    form_data.setZone_code(null);
                    form_data.setLocal_code(null);
                }
            }

            form_data.setOperation_code(so_operation_code != null ? so_operation_code : ToolBox_Con.getPreference_Operation_Code(context));
            form_data.setSignature("");
            form_data.setToken("");
            form_data.setSo_prefix(so_prefix);
            form_data.setSo_code(so_code);
        }

        return form_data;
    }*/

    @Override
    public void saveData(GE_Custom_Form_Data formData, boolean bMsg) {
        custom_form_dataDao.addUpdate(formData);
        custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);

        if (bMsg) {
            mView.showMsg(
                    hmAux_Trans.get("alert_save_title"),//"Salvando Registro",
                    hmAux_Trans.get("alert_save_msg"),//"Registro Salvo Partialmente!!!",
                    0);
        }
    }

    @Override
    public void checkData(GE_Custom_Form_Data formData, GeOs geOs, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location) {
        if (require_serial_done == 1 && !require_serial_done_ok.equalsIgnoreCase("OK")){
            mView.callNFCResults();
            //
            return;
        }

        custom_form_LocalDao.addUpdate(
                new GE_Custom_Form_Local_Sql_004(
                        String.valueOf(formData.getCustomer_code()),
                        String.valueOf(formData.getCustom_form_type()),
                        String.valueOf(formData.getCustom_form_code()),
                        String.valueOf(formData.getCustom_form_version()),
                        String.valueOf(formData.getCustom_form_data()),
                        Constant.SYS_STATUS_WAITING_SYNC
                ).toSqlQuery().toString()
        );
        //LUCHE - 14/02/2020
        if(bAgendado) {
            updateScheduleStatus(
                formData.getSchedule_prefix(),
                formData.getSchedule_code(),
                formData.getSchedule_exec(),
                ConstantBaseApp.SYS_STATUS_WAITING_SYNC
            );
        }
        /**
         * LUCHE - 08/09/2020
         * Modificado ordem da chamadas , pois será necessario levar em consideração form com pendencia
         * de GPS no set do update_required do ticket, sendo assim, o metodo checkGpsFlow deve
         * ser SEMPRE chamado antes do update do ticket.
         */
        checkGpsFlow(formData, require_location);
        //
        if(isTicketProcess){
            updateTicketCtrl(
                formData.getCustomer_code(),
                formData.getTicket_prefix(),
                formData.getTicket_code(),
                formData.getTicket_seq(),
                formData.getTicket_seq_tmp(),
                formData.getStep_code(),
                formData.getCustom_form_data(),
                ConstantBaseApp.SYS_STATUS_WAITING_SYNC,
                formData.getLocation_pendency()
            );
        }
        formData.setCustom_form_status(Constant.SYS_STATUS_WAITING_SYNC);
        if(geOs != null){
            formData.setDate_end(geOs.getDate_end());
            formData.setDate_start(geOs.getDate_start());
        }else {
            formData.setDate_end(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));
        }
        formData.setSys_date_end(ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT));

        custom_form_dataDao.addUpdate(formData);
        custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);

        /*Adição da fila de upload */
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        geFileDao.addUpdate(geFiles, false);
        //LUCHE - 26/06/2020 - Substituido IntentService pel worker de upload de imagem
        ToolBox_Inf.scheduleUploadImgWork(context);
        /*Fim da fila de upload */
        //LUCHE - 08/09/2020
        //Se form for do tipo ticket e fluxo do ticket, seta msgType que finaliza SEM CHAMAR O WS, pois
        //o Ws será chamado encadeadamento na Act070
//        int msgType =
//                isaTicketFlowForm()
//                ? Act011_Main.SHOW_MSG_TYPE_TICKET_FORM_FINALIZED
//                : 2
//            ;
//        mView.showMsg(
//                hmAux_Trans.get("alert_finalize_title"),//"Finalizando Registro",
//                hmAux_Trans.get("alert_finalize_msg"),//"Registro Finalizado!!!",
//            msgType
//        );
        //LUCHE - 16/09/2021
        //Foi solicitado remover a msg de OK e que a ação fosse disparada diretamente então o if que
        //define o fluxo será feito aqui
        if(isaTicketFlowForm()){
            mView.flowControl();
        }else{
            mView.defineFinalizeFlow();
        }
    }

    private boolean isFromTicketActs() {
        return ConstantBaseApp.ACT070.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT068.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT012.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT035.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT017.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT073.equals(mView.getRequestingAct())
                || ConstantBaseApp.ACT074.equals(mView.getRequestingAct());
    }

    @Override
    public boolean isaTicketFlowForm() {
        return isTicketProcess && isFromTicketActs();
    }

    private void checkGpsFlow(GE_Custom_Form_Data formData, int require_location){

        final int GPS_VALID_INTERVAL = 300000;

        if (require_location == 1) {
            String latitude = ToolBox_Con.getStringPreferencesByKey(context, Constant.LOCATION_LAT, "");
            String longitude = ToolBox_Con.getStringPreferencesByKey(context, Constant.LOCATION_LNG, "");
            long locationDate = ToolBox_Con.getLongPreferencesByKey(context, Constant.LOCATION_DATE, 0);
            String location_type = ToolBox_Con.getStringPreferencesByKey(context, Constant.LOCATION_TYPE, "");
            long currentTime = Calendar.getInstance().getTime().getTime();
            long diff = currentTime - locationDate;

            if (diff >= GPS_VALID_INTERVAL) {
//                String dataRecorded = "\ncheckGpsFlow: " +
//                                      "\nGPS_VALID_INTERVAL: " + GPS_VALID_INTERVAL +
//                                      "\ndiff: " + diff;
//                recordProcess(dataRecorded);
                formData.setLocation_pendency(1);
                ToolBox_Con.setBooleanPreference(context, Constant.HAS_PENDING_LOCATION, true);
            } else {
//                String dataRecorded = "\ncheckGpsFlow: " +
//                        "\nGPS_VALID_INTERVAL: " + GPS_VALID_INTERVAL +
//                        "\ngps_date_formatted: " + new SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT).format(new Date(locationDate)) +
//                        "\ndiff: " + diff;
//                recordProcess(dataRecorded);
                if (latitude != null && !latitude.isEmpty()
                        && longitude != null && !longitude.isEmpty()) {
                    formData.setLocation_type(location_type);
                    formData.setLocation_lat(latitude);
                    formData.setLocation_lng(longitude);
                    formData.setLocation_pendency(0);
                    String gps_date_formatted = new SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT).format(new Date(locationDate));
                    formData.setDate_gps(gps_date_formatted);
                }else{
                    formData.setLocation_pendency(1);
                    ToolBox_Con.setBooleanPreference(context, Constant.HAS_PENDING_LOCATION, true);
                }
            }
        }
    }


    /**
     * LUCHE - 14/02/2020
     *
     * Atualiza status da tabela de agendamentos.
     *
     * @param schedule_prefix
     * @param schedule_code
     * @param schedule_exec
     * @param status
     * @return
     */
    private boolean updateScheduleStatus(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec, String status) {
        MD_Schedule_Exec scheduleExec = getMdScheduleExec(schedule_prefix, schedule_code, schedule_exec);
        //
        if(MD_Schedule_Exec.isValidScheduleExec(scheduleExec)){
            scheduleExec.setStatus(status);
            DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
            //Retorna verdadeiro se não teve erro.
            return !daoObjReturn.hasError();
        }
        //
        return false;
    }
    @Override
    public MD_Schedule_Exec getMdScheduleExec(Integer schedule_prefix, Integer schedule_code, Integer schedule_exec) {
        return scheduleExecDao.getByString(
                new MD_Schedule_Exec_Sql_001(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    schedule_prefix,
                    schedule_code,
                    schedule_exec
                ).toSqlQuery()
            );
    }

    @Override
    public void checkSignature(GE_Custom_Form_Data formData, GeOs geOs, int signature, int opc, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location) {

        switch (signature) {
            case 1:
                if (ToolBox.validationCheckFile(Constant.CACHE_PATH_PHOTO + "/" + formData.getSignature()) && formData.getSignature_name() != null && !formData.getSignature_name().isEmpty()) {
                    checkData(formData, geOs, geFiles, require_serial_done, require_serial_done_ok, require_location);
                } else {
//                    mView.showMsg(
//                            hmAux_Trans.get("alert_finalize_title"),//"Finalizar Registro",
//                            hmAux_Trans.get("alert_require_signature_msg"),//"Para Finalizar o Registro é preciso uma assinatura!!!",
//                            1);
                    mView.callSignature();
                }

                break;
            default:
                formData.setSignature("");
                formData.setSignature_name("");
                //
                checkData(formData, geOs, geFiles, require_serial_done, require_serial_done_ok, require_location);

//                if (opc == 1) {
//                    checkData(formData, geFiles);
//                } else {
//                    mView.showMsg(
//                            hmAux_Trans.get("alert_finalize_title"),//"Finalizar Registro",
//                            hmAux_Trans.get("alert_optional_signature_msg"),//"Para Finalizar o Registro é preciso uma assinatura!!!",
//                            3);
//                    //
//                }
                break;
        }


    }

    @Override
    public void onBackPressedClicked() {
        if (isaTicketFlowForm()) {
            mView.callAct070();
        } else if(ConstantBaseApp.ACT027.equals(mView.getRequestingAct())
                    || ConstantBaseApp.ACT028.equals(mView.getRequestingAct())
        ){
            mView.nservCall();
        } else if(ConstantBaseApp.ACT084.equals(mView.getRequestingAct())){
            mView.callAct084();
        } else {
            mView.callAct083();
        }
    }

    /**
     * Modificado em 16/04/2019 por luche
     * Antes de tentar o delete da form_local, verificar se existe o registro
     * custom_form_data para o mesmo form.
     * Caso exista, não permitir o dele.
     *
     * @param formLocal
     */
    @Override
    public void deleteFormLocal(GE_Custom_Form_Local formLocal) {
        DaoObjReturn objReturn = new DaoObjReturn();
        GE_Custom_Form_Data customFormData = custom_form_dataDao.getByString(
            new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                String.valueOf(formLocal.getCustomer_code()),
                String.valueOf(formLocal.getCustom_form_type()),
                String.valueOf(formLocal.getCustom_form_code()),
                String.valueOf(formLocal.getCustom_form_version()),
                String.valueOf(formLocal.getCustom_form_data())
            ).toSqlQuery(),
            objReturn
        );
        //Se não existe custom_form_data para esse custom_form_local,
        //permite a deleção.
        //Se não, registra exception e NÃO DELETA O custom_form_local
        if(!objReturn.hasError()) {
            if (customFormData == null || customFormData.getCustomer_code() <= 0) {
                custom_form_LocalDao.remove(
                    new GE_Custom_Form_Local_Sql_005(
                        String.valueOf(formLocal.getCustomer_code()),
                        String.valueOf(formLocal.getCustom_form_type()),
                        String.valueOf(formLocal.getCustom_form_code()),
                        String.valueOf(formLocal.getCustom_form_version()),
                        String.valueOf(formLocal.getCustom_form_data())
                    ).toSqlQuery()
                );
            } else {
                try {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    String JsonFormLocal = "";
                    String JsonFormData = "";
                    try {
                        JsonFormLocal = gson.toJson(formLocal);
                        JsonFormData = gson.toJson(customFormData);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //
                    String msg = "Erro ao tentar apagar o registro da GE_Custom_form_local"
                        + " quando existe um registro na GE_Custom_Form_Data.\n "
                        + "Situação aconteceu quando o app identificou que o form aberto era novo, não havia "
                        + " sido salva nem via 'clique na foto',(bNew = true) e o obj formData era != null e status pending.\n"
                        + " Chamado pelo metodo exitAlert() noOnBackPressed.\n"
                        + " Dados do custom_form_local: \n" + JsonFormLocal + "\n"
                        + " Dados do custom_form_data: \n" + JsonFormData + "\n";

                    throw new Exception(msg);
                } catch (Exception e) {
                    ToolBox_Inf.registerException(getClass().getName(), e);
                }
            }
        }
    }

    @Override
    public boolean checkNFormExists(GE_Custom_Form_Local formLocal) {
        GE_Custom_Form result = custom_formDao.getByString(
                new GE_Custom_Form_Sql_001(
                        String.valueOf(formLocal.getCustomer_code()),
                        String.valueOf(formLocal.getCustom_form_type()),
                        String.valueOf(formLocal.getCustom_form_code()),
                        String.valueOf(formLocal.getCustom_form_version())
                ).toSqlQuery()
        );
        return result != null;
    }


    @Override
    public MD_Product_Serial getSerialInfo(long customer_code, long product_code, String serial_id, GE_Custom_Form_Local formLocal) {
        MD_Product_Serial result = md_product_serialDao.getByString(new MD_Product_Serial_Sql_016(customer_code,
                product_code,
                serial_id).toSqlQuery());
        //LUCHE - 25/09/2019
        //Como o usr pode perder acesso ao serial, caso ele não exista na base local,
        //"cria" obj com dados basicos apenas para exibição minima
        if(result == null){
            result = new MD_Product_Serial();
            if(formLocal != null) {
                result.setCustomer_code(customer_code);
                result.setProduct_code(product_code);
                result.setProduct_id(formLocal.getCustom_product_id());
                result.setProduct_desc(formLocal.getCustom_product_desc());
                result.setSerial_id(serial_id);
            }else{
                //ISSO NUNCA DERIA ACONTECER
            }
        }
        //
        return result;
    }
    /**
     * BARRIONUEVO - 23-10-2019 - Autosave no onPause
     * Funcao que verifica o status do n-form para evitar save ao acessar o n-form via historico.
     */
    @Override
    public boolean isInProcessing(GE_Custom_Form_Local formLocal) {
        if (formLocal.getCustom_form_status().equalsIgnoreCase(Constant.SYS_STATUS_IN_PROCESSING)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean hasGpsPendecy(long customer_code, long custom_form_type, long custom_form_code, long custom_form_version, long custom_form_data) {

        GE_Custom_Form_Data form_data = custom_form_dataDao.getByString(
                        new GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification(
                                String.valueOf(customer_code),
                                String.valueOf(custom_form_type),
                                String.valueOf(custom_form_code),
                                String.valueOf(custom_form_version),
                                String.valueOf(custom_form_data)
                        ).toSqlQuery().toLowerCase()
                );
        return Constant.SYS_STATUS_WAITING_SYNC.equals(form_data.getCustom_form_status()) && form_data.getLocation_pendency() == 1;
    }

    /**
     * LUCHE - 26/09/2019
     * Metodo que busca obj do produto usado no form
     * Chamado apenas na criação de form para setar no obj formLocal
     * o nome e URL do icone do prod
     *
     * @param product_code
     * @return
     */
    private MD_Product getProduct(long product_code) {
        MD_Product result = md_productDao.getByString(
            new MD_Product_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                product_code
            ).toSqlQuery()
        );
        //
        return result != null ? result : new MD_Product() ;
    }

    @Nullable
    private MdTag getTag(int tag_operational_code) {
        MdTag mdTag = mdTagDao.getMdTagByPk(
            (int) ToolBox_Con.getPreference_Customer_Code(context),
            tag_operational_code
        );
        //
        return mdTag;
    }

    @Override
    public void processWS_SaveReturn(String wsRet) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        //
        if(wsRet == null || wsRet.isEmpty()){
            mView.afterSaveFlow();
        }else{
            ArrayList<TSave_Rec.Error_Process> errorProcesses = null;
            try {
                errorProcesses = gson.fromJson(
                    wsRet,
                    new TypeToken<ArrayList<TSave_Rec.Error_Process>>() {
                    }.getType()
                );
            }catch (Exception e){
                ToolBox_Inf.registerException(getClass().getName(),e);
            }
            //
            if(errorProcesses != null && errorProcesses.size() > 0){
                ArrayList<HMAux> auxResults = new ArrayList<>();
                for (TSave_Rec.Error_Process error_process : errorProcesses) {
                    //
                    HMAux mHmAux = ToolBox_Inf.getWsSaveErrorProcessAuxResult(error_process);
                    //
                    auxResults.add(mHmAux);
                }
                //
                mView.addWsResults(auxResults);
                mView.afterSaveFlow();
            }else{
                mView.afterSaveFlow();
            }
        }
    }

    /**
     * BARRIONUEVO - Cria ctrl e Tk_Form para formulario espontaneo.
     * @param customFormLocal
     * @param mTicketPrefix
     * @param mTicketCode
     * @param mStepCode
     */
    public void createTicketCtrlObj(GE_Custom_Form_Local customFormLocal, int mTicketPrefix, int mTicketCode, int mStepCode) {
        TK_Ticket tkTicket = getTicketbyPk(mTicketPrefix, mTicketCode);
        TK_Ticket_Step stepInfo = getStepInfo(mTicketPrefix, mTicketCode, mStepCode);
        TK_Ticket_Ctrl ticketCtrl = null;
        //
        MD_Product_Serial serialInfo = md_product_serialDao.getByString(
                new MD_Product_Serial_Sql_002(
                        customFormLocal.getCustomer_code(),
                        customFormLocal.getCustom_product_code(),
                        customFormLocal.getSerial_id()
                ).toSqlQuery()
        );
        //
        if (tkTicket != null && stepInfo != null) {
            try { ticketCtrl = new TK_Ticket_Ctrl(
                        0,
                        mTicketSeqTmp,
                        ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM,
                        customFormLocal.getCustom_product_code(),
                        customFormLocal.getCustom_product_id(),
                        customFormLocal.getCustom_product_desc(),
                        (int) serialInfo.getSerial_code(),
                        serialInfo.getSerial_id(),
                        ConstantBaseApp.SYS_STATUS_PROCESS,
                        stepInfo.getStep_order(),
                        0
                );
                //Seta PK baseado no Step recebido
                ticketCtrl.setPK(stepInfo);
                //
                ticketCtrl.setCtrl_start_date(
                        ToolBox.sDTFormat_Agora(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT)
                );
                ticketCtrl.setCtrl_start_user(ToolBox_Inf.convertStringToInt(ToolBox_Con.getPreference_User_Code(context)));
                ticketCtrl.setCtrl_start_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));

                TK_Ticket_Form tk_ticket_form = new TK_Ticket_Form();
                tk_ticket_form.setPK(ticketCtrl);
                tk_ticket_form.setCustom_form_type(customFormLocal.getCustom_form_type());
                tk_ticket_form.setCustom_form_code(customFormLocal.getCustom_form_code());
                tk_ticket_form.setCustom_form_version(customFormLocal.getCustom_form_version());
                tk_ticket_form.setCustom_form_desc(customFormLocal.getCustom_form_desc());
                tk_ticket_form.setCustom_form_data((int) customFormLocal.getCustom_form_data());
                ticketCtrl.setForm(tk_ticket_form);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        stepInfo.getCtrl().add(ticketCtrl);
        int stepIdx = getStepIdx(ticketCtrl, tkTicket);
        tkTicket.getStep().set(stepIdx, stepInfo);
        TK_TicketDao ticketDao = getTicketDao();
        ticketDao.addUpdate(tkTicket);
    }

    /**
     * BARRIONUEVO - Gera seq_tmp para form espontaneo.
     * @param context
     * @param mTicket_prefix
     * @param mTicket_code
     * @param mStep_code
     * @return
     */
    @Override
    public Integer getSeqTmpForFormOffHand(Context context, Integer mTicket_prefix, Integer mTicket_code, Integer mStep_code) {
        TK_Ticket_CtrlDao ticketCtrlDao = getTicketCtrlDao();
        try {
            mTicketSeqTmp = ticketCtrlDao.getNextCtrlTicketSeqTmp(
                    ToolBox_Con.getPreference_Customer_Code(context), mTicket_prefix,mTicket_code, mStep_code, null
            );
        } catch (Exception e) {
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(), e);
            mTicketSeqTmp = -1;
            mView.showMsg(
                    hmAux_Trans.get("alert_error_on_create_ctrl_ttl"),
                    hmAux_Trans.get("alert_error_on_create_ctrl_msg"),
                    Act011_Main.SHOW_MSG_TYPE_TICKET_STEP_OR_CTRL_ERROR
            );

        }
        return mTicketSeqTmp;
    }

    private TK_Ticket_CtrlDao getTicketCtrlDao() {
        return new TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    private TK_Ticket_Step getStepInfo(int mTicketPrefix, int mTicketCode, int mStepCode) {
        TK_Ticket_Step ticketStep =
                ticketStepDao.getByString(
                        new TK_Ticket_Step_Sql_001(
                                ToolBox_Con.getPreference_Customer_Code(context),
                                mTicketPrefix,
                                mTicketCode,
                                mStepCode
                        ).toSqlQuery()
                );
        //
        if (ticketStep != null) {
            ticketStep.setCtrl(new ArrayList<TK_Ticket_Ctrl>());
        }
        return ticketStep;
    }

    /**
     * LUCHE - 16/11/2020
     * Metodo que retorna formata da a informação do ticket a ser exibido no dialog.
     * @param ticket_prefix
     * @param ticket_code
     * @param step_code
     * @return
     */
    @Override
    public String getDialogTicketInfo(Integer ticket_prefix, Integer ticket_code, Integer step_code) {
        if(ticket_prefix != null && ticket_code != null && step_code != null) {
            TK_Ticket tkTicket = getTicketbyPk(ticket_prefix, ticket_code);
            TK_Ticket_Step stepInfo = getStepInfo(ticket_prefix, ticket_code, step_code);
            //
            if (tkTicket != null && stepInfo != null) {
                return tkTicket.getTicket_id() + " - " + stepInfo.getStep_desc();
            }
        }
        //Não deveria acontecer, mas ....
        return ticket_prefix + "." + ticket_code;
    }

    private TK_Ticket getTicketbyPk(int ticket_prefix, int ticket_code) {
        TK_TicketDao ticketDao = getTicketDao();
        return ticketDao.getByString(new TK_Ticket_Sql_001(
                ToolBox_Con.getPreference_Customer_Code(context),
                ticket_prefix,
                ticket_code
        ).toSqlQuery());
    }


    private TK_TicketDao getTicketDao() {
        return new TK_TicketDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
        );
    }

    @Override
    public void checkOriginDoneFlow(Bundle act083Bundle) {
        if(act083Bundle == null){
            mView.callAct006(context,false);
        }else{
            //FLUXO DO TICKET ESTA EM OUTRO LUGAR.
            String origin = act083Bundle.getString(ConstantBaseApp.MY_ACTIONS_ORIGIN_FLOW, "");
            if(bAgendado || !ConstantBaseApp.ACT006.equals(origin)){
                mView.callAct083();
            }else{
                mView.callAct006(context,false);
            }
        }
    }

    /**
     * LUCHE - 13/09/2021
     * Metodo que recebe o nome do icone do produto e retorna o Bitmap caso exista.
     * Caso contrario, retorna null
     * @param product_icon_name
     * @return
     */
    @Override
    public Bitmap getProductIconBitmap(String product_icon_name) {
        Bitmap myBitmap = null;
        if (product_icon_name != null && !product_icon_name.isEmpty()) {
            if (ToolBox_Inf.verifyDownloadFileInf(product_icon_name, Constant.CACHE_PATH)) {
                File imgFile = new File(Constant.CACHE_PATH + "/" + product_icon_name);
                if (imgFile.exists()) {
                    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                }
            }
        }
        //
        return myBitmap;
    }

    @Override
    public boolean hasAnyInvalidField(ArrayList<Act011FormTab> tabs) {
        for (Act011FormTab tab : tabs) {
            if(tab.getStatus() == Act011FormTabStatus.ERROR){
                return true;
            }
        }
        return false;
    }

    /**
     * LUCHE - 17/09/2021
     * Metodo que reseta registro do agendamento na deleção do form.
     * @param formLocal
     */
    @Override
    public void resetScheduleExecIfNeeds(GE_Custom_Form_Local formLocal) {
        if(ToolBox_Inf.isScheduleForm(formLocal)){
            MD_Schedule_Exec scheduleExec = getMdScheduleExec(
                formLocal.getSchedule_prefix(),
                formLocal.getSchedule_code(),
                formLocal.getSchedule_exec()
            );
            //
            if(MD_Schedule_Exec.isValidScheduleExec(scheduleExec)){
                scheduleExec.setStatus(ConstantBaseApp.SYS_STATUS_SCHEDULE);
                scheduleExec.setFcm_new_status(null);
                scheduleExec.setFcm_user_nick(null);
                scheduleExec.setClose_date(null);
                //
                if(scheduleExec.getSerial_defined_by_server() == 0){
                    scheduleExec.setSerial_code(null);
                    scheduleExec.setSerial_id(null);
                }
                //
                DaoObjReturn daoObjReturn = scheduleExecDao.addUpdate(scheduleExec);
                //Não tem o que fazer nesse ponto...
                if(daoObjReturn.hasError()){
                    ToolBox_Inf.registerException(getClass().getName(),new Exception(daoObjReturn.getErrorMsg()));
                }
            }
        }
    }

    @Override
    public void deleteGeOsFormIfNeeds(GE_Custom_Form_Local formLocal) {
        if(formLocal.getIs_so() == 1){
            GeOs geOs = getGeOs(
                String.valueOf(formLocal.getCustomer_code()),
                String.valueOf(formLocal.getCustom_form_type()),
                String.valueOf(formLocal.getCustom_form_code()),
                String.valueOf(formLocal.getCustom_form_version()),
                String.valueOf(formLocal.getCustom_form_data())
            );
            if(geOs != null) {
                DaoObjReturn daoObjReturn = geOsDao.removeFull(geOs);
                //Não tem o que fazer nesse ponto...
                if(daoObjReturn.hasError()){
                    ToolBox_Inf.registerException(getClass().getName(),new Exception(daoObjReturn.getErrorMsg()));
                }
            }
        }
    }

    @Override
    public void updateGeOsItems(GeOs geOs, int missingJustifyCounter, String comments, String dateStart, String dateEnd) {

        geOs.setDate_end(dateEnd);

        if(missingJustifyCounter > 0) {
            List<GeOsDevice> devices = getDeviceList(geOs);
            for (GeOsDevice device : devices) {
                geOsDeviceItemDao.addUpdate(
                        new GeOsDeviceItem_Sql_004(
                                device.getCustomer_code(),
                                device.getCustom_form_type(),
                                device.getCustom_form_code(),
                                device.getCustom_form_version(),
                                device.getCustom_form_data(),
                                device.getProduct_code(),
                                device.getSerial_code(),
                                comments
                        ).toSqlQuery()
                );
            }
        }

        geOsDao.addUpdate(geOs);
    }

    /**
     * Add fotos dos item se houver
     * @param formLocal
     * @param geFiles
     * @param sDate
     */
    @Override
    public void addGeOsDeviceItemPhotosIntoFiles(GE_Custom_Form_Local formLocal, ArrayList<GE_File> geFiles, String sDate) {
        if(formLocal.getIs_so() == 1){
            List<GeOsDeviceItem> deviceItemList = geOsDeviceItemDao.query(
                new GeOsDeviceItem_Sql_003(
                    String.valueOf(formLocal.getCustomer_code()),
                    String.valueOf(formLocal.getCustom_form_type()),
                    String.valueOf(formLocal.getCustom_form_code()),
                    String.valueOf(formLocal.getCustom_form_version()),
                    String.valueOf(formLocal.getCustom_form_data())
                ).toSqlQuery()
            );
            //
            if(deviceItemList != null && deviceItemList.size() > 0) {
                for (GeOsDeviceItem deviceItem : deviceItemList) {
                    if(deviceItem.getExec_photo1() != null){
                        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + deviceItem.getExec_photo1());
                        if (sFile.exists()) {
                            GE_File geFile = new GE_File();
                            geFile.setFile_code(deviceItem.getExec_photo1().replace(Act011_Main.PNG_EXTENSION, ""));
                            geFile.setFile_path(deviceItem.getExec_photo1());
                            geFile.setFile_status(GE_File.OPENED);
                            geFile.setFile_date(sDate);
                            geFiles.add(geFile);
                        }
                    }
                    //
                    if(deviceItem.getExec_photo2() != null){
                        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + deviceItem.getExec_photo2());
                        if (sFile.exists()) {
                            GE_File geFile = new GE_File();
                            geFile.setFile_code(deviceItem.getExec_photo2().replace(Act011_Main.PNG_EXTENSION, ""));
                            geFile.setFile_path(deviceItem.getExec_photo2());
                            geFile.setFile_status(GE_File.OPENED);
                            geFile.setFile_date(sDate);
                            geFiles.add(geFile);
                        }
                    }
                    //
                    if(deviceItem.getExec_photo3() != null){
                        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + deviceItem.getExec_photo3());
                        if (sFile.exists()) {
                            GE_File geFile = new GE_File();
                            geFile.setFile_code(deviceItem.getExec_photo3().replace(Act011_Main.PNG_EXTENSION, ""));
                            geFile.setFile_path(deviceItem.getExec_photo3());
                            geFile.setFile_status(GE_File.OPENED);
                            geFile.setFile_date(sDate);
                            geFiles.add(geFile);
                        }
                    }
                    //
                    if(deviceItem.getExec_photo4() != null){
                        File sFile = new File(ConstantBase.CACHE_PATH_PHOTO + "/" + deviceItem.getExec_photo4());
                        if (sFile.exists()) {
                            GE_File geFile = new GE_File();
                            geFile.setFile_code(deviceItem.getExec_photo4().replace(Act011_Main.PNG_EXTENSION, ""));
                            geFile.setFile_path(deviceItem.getExec_photo4());
                            geFile.setFile_status(GE_File.OPENED);
                            geFile.setFile_date(sDate);
                            geFiles.add(geFile);
                        }
                    }
                }
            }
        }
    }
}
