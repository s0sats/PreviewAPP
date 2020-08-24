package com.namoadigital.prj001.ui.act011;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import com.namoadigital.prj001.dao.MD_ProductDao;
import com.namoadigital.prj001.dao.MD_Product_SerialDao;
import com.namoadigital.prj001.dao.MD_Schedule_ExecDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.model.DaoObjReturn;
import com.namoadigital.prj001.model.GE_Custom_Form;
import com.namoadigital.prj001.model.GE_Custom_Form_Data;
import com.namoadigital.prj001.model.GE_Custom_Form_Local;
import com.namoadigital.prj001.model.GE_File;
import com.namoadigital.prj001.model.MD_Product;
import com.namoadigital.prj001.model.MD_Product_Serial;
import com.namoadigital.prj001.model.MD_Schedule_Exec;
import com.namoadigital.prj001.model.TK_Ticket_Ctrl;
import com.namoadigital.prj001.model.TK_Ticket_Step;
import com.namoadigital.prj001.model.TSave_Rec;
import com.namoadigital.prj001.receiver.WBR_Upload_Img;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Local_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_Field_MULTI_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Data_MULTI_UNIQUE_SqlSpecification;
import com.namoadigital.prj001.sql.GE_Custom_Form_Fields_Local_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_003;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_004;
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_005;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001;
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002;
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_016;
import com.namoadigital.prj001.sql.MD_Product_Sql_001;
import com.namoadigital.prj001.sql.MD_Schedule_Exec_Sql_001;
import com.namoadigital.prj001.sql.Sql_Act011_002;
import com.namoadigital.prj001.sql.TK_Ticket_Step_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

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

    private HMAux hmAux_Trans;

    private boolean bAgendado;
    private MD_Schedule_ExecDao scheduleExecDao;
    //LUCHE - 20/02/2020
    //Flag que controla se é a primeira abertura do agendamento.
    private boolean isScheduleFirstTime = false;
    //LUCHE - 24/08/2020
    private boolean isTicketProcess = false;
    private TK_Ticket_StepDao ticketStepDao;

    public Act011_Main_Presenter_Impl(Context context, Act011_Main_View mView, EV_Module_Res_Txt_TransDao module_res_txt_transDao, GE_Custom_FormDao custom_formDao, GE_Custom_Form_FieldDao custom_form_fieldDao, GE_Custom_Form_DataDao custom_form_dataDao, GE_Custom_Form_Data_FieldDao custom_form_data_fieldDao, GE_Custom_Form_LocalDao custom_form_LocalDao, GE_Custom_Form_Field_LocalDao custom_form_field_LocalDao, GE_Custom_Form_BlobDao custom_form_blobDao, GE_Custom_Form_Blob_LocalDao custom_form_blob_localDao, MD_Product_SerialDao md_product_serialDao, MD_ProductDao md_productDao, HMAux hmAux_Trans,MD_Schedule_ExecDao scheduleExecDao, TK_Ticket_StepDao ticketStepDao) {
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
    }

    @Override
    public void setData(String customer_code, String formtype_code, String form_code, String formversion_code, String product_code, String s_form_data, String product_desc, String product_id, String formtype_desc, String formcode_desc, String serial_id, Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code) {
        boolean hasNformPending = false;
        boolean bNew = false;
        boolean bAbortSchedule = false;

        bAgendado = false;
        //LUCHE - 14/03/2019
        DaoObjReturn daoObjReturn = new DaoObjReturn();
        MD_Schedule_Exec scheduleExec = new MD_Schedule_Exec();
        GE_Custom_Form_Local customFormLocal = custom_form_LocalDao.getByString(
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
            MD_Product productInfo = getProduct(Integer.parseInt(product_code));
            //
            customFormLocal = new GE_Custom_Form_Local();

            customFormLocal.setCustomer_code(customForm.getCustomer_code());
            customFormLocal.setCustom_form_type(customForm.getCustom_form_type());
            customFormLocal.setCustom_form_code(customForm.getCustom_form_code());
            customFormLocal.setCustom_form_version(customForm.getCustom_form_version());
            customFormLocal.setCustom_form_data(Long.parseLong(ii.get("id")));
            customFormLocal.setCustom_form_pre(ToolBox_Inf.getPrefix(context));
            customFormLocal.setCustom_form_status(Constant.SYS_STATUS_IN_PROCESSING);
            customFormLocal.setCustom_product_code(Integer.parseInt(product_code));
            customFormLocal.setCustom_product_desc(product_desc);
            customFormLocal.setCustom_product_id(product_id);
            customFormLocal.setCustom_product_icon_name(productInfo.getProduct_icon_name());
            customFormLocal.setCustom_product_icon_url(productInfo.getProduct_icon_url());
            customFormLocal.setCustom_product_icon_url_local(productInfo.getProduct_icon_url_local());
            customFormLocal.setCustom_form_type_desc(formtype_desc);
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
        isTicketProcess = isFormCreateByTicket(customFormLocal.getTicket_prefix(),customFormLocal.getTicket_code(),customFormLocal.getTicket_seq(),customFormLocal.getTicket_seq_tmp(),customFormLocal.getStep_code());
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
                    serial_id
                );
                //LUCHE - 24/08/2020
                //Atualiza TicketCtrl se form for do ticket
                //if(isFormCreateByTicket(mTicket_prefix,mTicket_code,mTicket_seq,mTicket_seq_tmp,mStep_code)){
                if(isTicketProcess){
                    updateTicketCtrl(
                        customFormLocal.getCustomer_code(),
                        customFormLocal.getTicket_prefix(),
                        customFormLocal.getTicket_code(),
                        customFormLocal.getTicket_seq(),
                        customFormLocal.getTicket_seq_tmp(),
                        customFormLocal.getStep_code(),
                        ConstantBaseApp.SYS_STATUS_PROCESS
                    );
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
                mView.loadFragment_CF_Fields(cf_fields, bNew, customFormLocal, formData, customFormLocal.getCustom_form_pre(), pdfs, index, customFormLocal.getRequire_signature(), customFormLocal.getRequire_serial_done());
            }
        }
    }

    private boolean isFormCreateByTicket(Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code) {
        return
            mTicket_prefix != null && mTicket_prefix > -1
            && mTicket_code != null && mTicket_code > -1
            && mTicket_seq != null && mTicket_seq > -1
            && mTicket_seq_tmp != null && mTicket_seq_tmp > -1
            && mStep_code != null && mStep_code > -1
            ;
    }

    private void updateTicketCtrl(long customer_code, Integer mTicket_prefix, Integer mTicket_code, Integer mTicket_seq, Integer mTicket_seq_tmp, Integer mStep_code, String status) {
        TK_Ticket_Step tkTicketStep = getTicketStep(mTicket_prefix, mTicket_code, mStep_code);
//        TK_Ticket_Ctrl ticketCtrl = ticketCtrlDao.getByString(
//            new TK_Ticket_Ctrl_Sql_004(
//                customer_code,
//                mTicket_prefix,
//                mTicket_code,
//                mTicket_seq_tmp
//            ).toSqlQuery()
//        );
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
                            break;
                        case ConstantBaseApp.SYS_STATUS_WAITING_SYNC:
                            setCloseInfoIntoCtrl(tkTicketCtrl);
                            checkCloseStepForWaitingSync(tkTicketStep,tkTicketCtrl);
                            break;
                    }
                    tkTicketStep.getCtrl().set(ctrlIdx,tkTicketCtrl);
                    //
                    ticketStepDao.addUpdate(tkTicketStep);
                }
            }
        }
        //TODO TRATAR CASOS NAO ENCONTRE AS INFOS.
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
            ticketCtrl.setCtrl_start_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
            ticketCtrl.setCtrl_start_user(Integer.valueOf(ToolBox_Con.getPreference_User_Code(context)));
            ticketCtrl.setCtrl_start_user_name(ToolBox_Con.getPreference_User_Code_Nick(context));
            ticketCtrl.setCtrl_status(ConstantBaseApp.SYS_STATUS_PROCESS);
            ticketCtrl.copyCtrlStatusForInnerProcess();
        }
    }

    private void setCheckInIntoStepWhenOneTouchStep(TK_Ticket_Step ticketStep, TK_Ticket_Ctrl mTicketCtrl) {
        if(ConstantBaseApp.TK_PIPELINE_STEP_TYPE_ONE_TOUCH.equals(ticketStep.getExec_type())
            && !ToolBox_Inf.hasConsistentValueString(ticketStep.getStep_start_date())
        ) {
            ticketStep.setStep_start_date(mTicketCtrl.getCtrl_start_date());
            ticketStep.setStep_start_user(mTicketCtrl.getCtrl_start_user());
            ticketStep.setStep_start_user_nick(mTicketCtrl.getCtrl_start_user_name());
        }
    }

    private void setCloseInfoIntoCtrl(TK_Ticket_Ctrl tkTicketCtrl) {
        if( ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(tkTicketCtrl.getCtrl_status())
            && tkTicketCtrl.getCtrl_end_date() == null
        ){
            tkTicketCtrl.setCtrl_end_date(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
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

    private DaoObjReturn updateScheduleInfos(GE_Custom_Form_Local customFormLocal, String serial_id) {
        DaoObjReturn daoObjRet = new DaoObjReturn();
        MD_Schedule_Exec mdScheduleExec = getMdScheduleExec(customFormLocal.getSchedule_prefix(), customFormLocal.getSchedule_code(), customFormLocal.getSchedule_exec());
        String originalScheduleSerialId = MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec) ? mdScheduleExec.getSerial_id() : null ;
        Integer originalScheduleSeriaCode = MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec) ? mdScheduleExec.getSerial_code() : null ;
        MD_Product_Serial productSerial = null;
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
        //
        if(MD_Schedule_Exec.isValidScheduleExec(mdScheduleExec)){
            if( originalScheduleSerialId == null
                && productSerial.getSerial_id() != null
                && !productSerial.getSerial_id().isEmpty()
            ) {
                mdScheduleExec.setSerial_code((int) productSerial.getSerial_code());
                mdScheduleExec.setSerial_id(productSerial.getSerial_id());
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
     * @return - Respostas form_data
     */
    private GE_Custom_Form_Data loadAnswer(GE_Custom_Form_Local formLocal,Integer so_prefix, Integer so_code, String so_site_code, Integer so_operation_code, String serial_id){
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
            form_data.setDate_start(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
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
        }

        return form_data;
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
            form_data.setDate_start(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));
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
    public void checkData(GE_Custom_Form_Data formData, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location) {
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
        //TODO CONTINUAR AQUI AMANHA POIS NÃO ENTROU
        //TODO REVER POIS STEP_CODE NÃO ESTA SENDO SALVO NO FORM DATA.--Era pau no Dao, corrigido.
        if(isTicketProcess){
            updateTicketCtrl(
                formData.getCustomer_code(),
                formData.getTicket_prefix(),
                formData.getTicket_code(),
                formData.getTicket_seq(),
                formData.getTicket_seq_tmp(),
                formData.getStep_code(),
                ConstantBaseApp.SYS_STATUS_WAITING_SYNC
            );
        }
        //
        checkGpsFlow(formData, require_location);
        formData.setCustom_form_status(Constant.SYS_STATUS_WAITING_SYNC);
        formData.setDate_end(ToolBox.sDTFormat_Agora("yyyy-MM-dd HH:mm:ss Z"));

        custom_form_dataDao.addUpdate(formData);
        custom_form_data_fieldDao.addUpdate(formData.getDataFields(), false);

        /*Adição da fila de upload */
        GE_FileDao geFileDao = new GE_FileDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)), Constant.DB_VERSION_CUSTOM
        );

        geFileDao.addUpdate(geFiles, false);

        Intent mIntent = new Intent(context, WBR_Upload_Img.class);
        Bundle bundle = new Bundle();
        bundle.putLong(Constant.LOGIN_CUSTOMER_CODE,ToolBox_Con.getPreference_Customer_Code(context));
        mIntent.putExtras(bundle);
        //
        context.sendBroadcast(mIntent);
        /*Fim da fila de upload */
        //
        mView.showMsg(
                hmAux_Trans.get("alert_finalize_title"),//"Finalizando Registro",
                hmAux_Trans.get("alert_finalize_msg"),//"Registro Finalizado!!!",
                2);


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
//                        "\ngps_date_formatted: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(new Date(locationDate)) +
//                        "\ndiff: " + diff;
//                recordProcess(dataRecorded);
                if (latitude != null && !latitude.isEmpty()
                        && longitude != null && !longitude.isEmpty()) {
                    formData.setLocation_type(location_type);
                    formData.setLocation_lat(latitude);
                    formData.setLocation_lng(longitude);
                    formData.setLocation_pendency(0);
                    String gps_date_formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").format(new Date(locationDate));
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
    public void checkSignature(GE_Custom_Form_Data formData, int signature, int opc, ArrayList<GE_File> geFiles, int require_serial_done, String require_serial_done_ok, int require_location) {

        switch (signature) {
            case 1:
                if (ToolBox.validationCheckFile(Constant.CACHE_PATH_PHOTO + "/" + formData.getSignature()) && formData.getSignature_name() != null && !formData.getSignature_name().isEmpty()) {
                    checkData(formData, geFiles, require_serial_done, require_serial_done_ok, require_location);
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
                checkData(formData, geFiles, require_serial_done, require_serial_done_ok, require_location);

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
        mView.callAct005(context);
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
                    HMAux mHmAux = new HMAux();
                    mHmAux.put("label", ToolBox_Inf.formatScheduleErroLabel(error_process));
                    mHmAux.put("type", ConstantBaseApp.SYS_STATUS_SCHEDULE);
                    mHmAux.put("status", error_process.getError());
                    mHmAux.put("final_status", ToolBox_Inf.formatFormErrorDesc(error_process));
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
}
