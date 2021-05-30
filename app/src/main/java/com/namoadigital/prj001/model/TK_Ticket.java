package com.namoadigital.prj001.model;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.namoa_digital.namoa_library.util.HMAux;
import com.namoadigital.prj001.R;
import com.namoadigital.prj001.dao.TK_TicketDao;
import com.namoadigital.prj001.dao.TK_Ticket_CtrlDao;
import com.namoadigital.prj001.dao.TK_Ticket_StepDao;
import com.namoadigital.prj001.sql.SqlAct083_002;
import com.namoadigital.prj001.sql.TK_Ticket_Ctrl_Sql_001;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Con;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class TK_Ticket implements Cloneable, Serializable {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int scn;
    private int user_level_min;
    private String ticket_id;
    private int type_code;
    private String type_id;
    private String type_desc;
    private int app_personal_data;
    private String type_path;
    private String open_comments;
    private String open_photo;
    private String open_photo_local;
    private String open_name;
    private String open_email;
    private String open_phone;
    private String open_date;
    private int open_user;
    private String open_user_name;
    @Expose
    private String internal_comments;
    private int open_site_code;
    private String open_site_id;
    private String open_site_desc;
    private int open_operation_code;
    private String open_operation_id;
    private String open_operation_desc;
    private int open_product_code;
    private String open_product_id;
    private String open_product_desc;
    private int open_serial_code;
    private String open_serial_id;
    @Expose
    private String change_date;
    private String start_date;
    @Nullable
    private String forecast_date;
    @Expose
    private String forecast_time;
    private String ticket_status;
    @Nullable
    private String close_date;
    private Integer close_user;
    @Nullable
    private String close_user_name;
    private Integer duration_minutes;
    private Integer barcode_code;
    @Nullable
    private Integer pc_code;
    @Nullable
    private String pc_id;
    @Nullable
    private String pc_desc;
    @Nullable
    @Expose
    private Integer main_user;
    @Nullable
    @Expose
    private String main_user_nick;
    @Nullable
    @Expose
    private String main_user_name;
    private Integer pipeline_code;
    @Nullable
    private String pipeline_id;
    @Nullable
    private String pipeline_desc;
    private Integer current_step_order;
    private int approval_rejected;
    private String origin_type;
    private String origin_desc;
    private int valid_structure_step;
    private int inventory_control;
    private int user_focus;
    private int allow_step_approval;
    private int sync_required;
    private int update_required;
    private int update_required_product;
    @Expose
    private String token;
    @Expose
    @Nullable
    private Integer schedule_prefix;
    @Expose
    @Nullable
    private Integer schedule_code;
    @Expose
    @Nullable
    private Integer schedule_exec;
    @Nullable
    private Integer client_code;
    @Nullable
    private String client_id;
    @Nullable
    private String client_name;
    @Nullable
    private String address_country;
    @Nullable
    private String address_state;
    @Nullable
    private String address_city;
    @Nullable
    private String address_district;
    @Nullable
    private String address_street;
    @Nullable
    private String address_num;
    @Nullable
    private String address_complement;
    @Nullable
    private String address_zipcode;
    @Nullable
    private String address_lat;
    @Nullable
    private String address_lng;
    @Nullable
    private Integer contract_code;
    @Nullable
    private String contract_id;
    @Nullable
    private String contract_desc;
    //LUCHE - 03/12/2020 - Propriedade que grava o SCN recebido via FCM
    private int fcm_scn;
    @Expose
    String time_action;
    @Expose
    Integer move_other_date;
    @Expose
    Integer move_steps;
    @Expose
    String apply_perc_steps;
    @Expose
    private int tag_operational_code;
    private String tag_operational_id;
    private String tag_operational_desc;
    @Expose
    private ArrayList<TK_Ticket_Step> step = new ArrayList<>();
    @Expose
    private ArrayList<TK_Ticket_Product> product = new ArrayList<>();

    public void setPK() {
        for (int i = 0; i < step.size(); i++) {
            step.get(i).setPK(this);
        }
        for (int i = 0; i < product.size(); i++) {
            product.get(i).setPK(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(int ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public int getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(int ticket_code) {
        this.ticket_code = ticket_code;
    }

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
    }

    public int getUser_level_min() {
        return user_level_min;
    }

    public void setUser_level_min(int user_level_min) {
        this.user_level_min = user_level_min;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getType_code() {
        return type_code;
    }

    public void setType_code(int type_code) {
        this.type_code = type_code;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_desc() {
        return type_desc;
    }

    public void setType_desc(String type_desc) {
        this.type_desc = type_desc;
    }

    public int getApp_personal_data() {
        return app_personal_data;
    }

    public void setApp_personal_data(int app_personal_data) {
        this.app_personal_data = app_personal_data;
    }

    @Nullable
    public String getType_path() {
        return type_path;
    }

    public void setType_path(@Nullable String type_path) {
        this.type_path = type_path;
    }

    @Nullable
    public String getOpen_comments() {
        return open_comments;
    }

    public void setOpen_comments(@Nullable String open_comments) {
        this.open_comments = open_comments;
    }

    @Nullable
    public String getOpen_photo() {
        return open_photo;
    }

    public void setOpen_photo(@Nullable String open_photo) {
        this.open_photo = open_photo;
    }

    @Nullable
    public String getOpen_photo_local() {
        return open_photo_local;
    }

    public void setOpen_photo_local(@Nullable String open_photo_local) {
        this.open_photo_local = open_photo_local;
    }

    @Nullable
    public String getOpen_name() {
        return open_name;
    }

    public void setOpen_name(@Nullable String open_name) {
        this.open_name = open_name;
    }

    @Nullable
    public String getOpen_email() {
        return open_email;
    }

    public void setOpen_email(@Nullable String open_email) {
        this.open_email = open_email;
    }

    @Nullable
    public String getOpen_phone() {
        return open_phone;
    }

    public void setOpen_phone(@Nullable String open_phone) {
        this.open_phone = open_phone;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public int getOpen_user() {
        return open_user;
    }

    public void setOpen_user(int open_user) {
        this.open_user = open_user;
    }

    public String getOpen_user_name() {
        return open_user_name;
    }

    public void setOpen_user_name(String open_user_name) {
        this.open_user_name = open_user_name;
    }

    @Nullable
    public String getInternal_comments() {
        return internal_comments;
    }

    public void setInternal_comments(@Nullable String internal_comments) {
        this.internal_comments = internal_comments;
    }

    public int getOpen_site_code() {
        return open_site_code;
    }

    public void setOpen_site_code(int open_site_code) {
        this.open_site_code = open_site_code;
    }

    public String getOpen_site_id() {
        return open_site_id;
    }

    public void setOpen_site_id(String open_site_id) {
        this.open_site_id = open_site_id;
    }

    public String getOpen_site_desc() {
        return open_site_desc;
    }

    public void setOpen_site_desc(String open_site_desc) {
        this.open_site_desc = open_site_desc;
    }

    public int getOpen_operation_code() {
        return open_operation_code;
    }

    public void setOpen_operation_code(int open_operation_code) {
        this.open_operation_code = open_operation_code;
    }

    public String getOpen_operation_id() {
        return open_operation_id;
    }

    public void setOpen_operation_id(String open_operation_id) {
        this.open_operation_id = open_operation_id;
    }

    public String getOpen_operation_desc() {
        return open_operation_desc;
    }

    public void setOpen_operation_desc(String open_operation_desc) {
        this.open_operation_desc = open_operation_desc;
    }

    public int getOpen_product_code() {
        return open_product_code;
    }

    public void setOpen_product_code(int open_product_code) {
        this.open_product_code = open_product_code;
    }

    public String getOpen_product_id() {
        return open_product_id;
    }

    public void setOpen_product_id(String open_product_id) {
        this.open_product_id = open_product_id;
    }

    public String getOpen_product_desc() {
        return open_product_desc;
    }

    public void setOpen_product_desc(String open_product_desc) {
        this.open_product_desc = open_product_desc;
    }

    public int getOpen_serial_code() {
        return open_serial_code;
    }

    public void setOpen_serial_code(int open_serial_code) {
        this.open_serial_code = open_serial_code;
    }

    public String getOpen_serial_id() {
        return open_serial_id;
    }

    public void setOpen_serial_id(String open_serial_id) {
        this.open_serial_id = open_serial_id;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Nullable
    public String getForecast_date() {
        return forecast_date;
    }

    public void setForecast_date(@Nullable String forecast_date) {
        this.forecast_date = forecast_date;
    }

    public String getForecast_time() {
        return forecast_time;
    }

    public void setForecast_time(String forecast_time) {
        this.forecast_time = forecast_time;
    }

    public String getTicket_status() {
        return ticket_status;
    }

    public void setTicket_status(String ticket_status) {
        this.ticket_status = ticket_status;
    }

    @Nullable
    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(@Nullable String close_date) {
        this.close_date = close_date;
    }

    public Integer getClose_user() {
        return close_user;
    }

    public void setClose_user(Integer close_user) {
        this.close_user = close_user;
    }

    @Nullable
    public String getClose_user_name() {
        return close_user_name;
    }

    public void setClose_user_name(@Nullable String close_user_name) {
        this.close_user_name = close_user_name;
    }

    public Integer getDuration_minutes() {
        return duration_minutes;
    }

    public void setDuration_minutes(Integer duration_minutes) {
        this.duration_minutes = duration_minutes;
    }

    public Integer getBarcode_code() {
        return barcode_code;
    }

    public void setBarcode_code(Integer barcode_code) {
        this.barcode_code = barcode_code;
    }

    public Integer getPc_code() {
        return pc_code;
    }

    public void setPc_code(Integer pc_code) {
        this.pc_code = pc_code;
    }

    public String getPc_id() {
        return pc_id;
    }

    public void setPc_id(String pc_id) {
        this.pc_id = pc_id;
    }

    public String getPc_desc() {
        return pc_desc;
    }

    public void setPc_desc(String pc_desc) {
        this.pc_desc = pc_desc;
    }

    @Nullable
    public Integer getMain_user() {
        return main_user;
    }

    public void setMain_user(@Nullable Integer main_user) {
        this.main_user = main_user;
    }

    public String getMain_user_nick() {
        return main_user_nick;
    }

    public void setMain_user_nick(String main_user_nick) {
        this.main_user_nick = main_user_nick;
    }

    @Nullable
    public String getMain_user_name() {
        return main_user_name;
    }

    public void setMain_user_name(@Nullable String main_user_name) {
        this.main_user_name = main_user_name;
    }

    public Integer getPipeline_code() {
        return pipeline_code;
    }

    public void setPipeline_code(Integer pipeline_code) {
        this.pipeline_code = pipeline_code;
    }

    public String getPipeline_id() {
        return pipeline_id;
    }

    public void setPipeline_id(String pipeline_id) {
        this.pipeline_id = pipeline_id;
    }

    public String getPipeline_desc() {
        return pipeline_desc;
    }

    public void setPipeline_desc(String pipeline_desc) {
        this.pipeline_desc = pipeline_desc;
    }

    public Integer getCurrent_step_order() {
        return current_step_order;
    }

    public void setCurrent_step_order(Integer current_step_order) {
        this.current_step_order = current_step_order;
    }

    public int getApproval_rejected() {
        return approval_rejected;
    }

    public void setApproval_rejected(int approval_rejected) {
        this.approval_rejected = approval_rejected;
    }

    public String getOrigin_type() {
        return origin_type;
    }

    public void setOrigin_type(String origin_type) {
        this.origin_type = origin_type;
    }

    public String getOrigin_desc() {
        return origin_desc;
    }

    public void setOrigin_desc(String origin_desc) {
        this.origin_desc = origin_desc;
    }

    public int getValid_structure_step() {
        return valid_structure_step;
    }

    public void setValid_structure_step(int valid_structure_step) {
        this.valid_structure_step = valid_structure_step;
    }

    public int getInventory_control() {
        return inventory_control;
    }

    public void setInventory_control(int inventory_control) {
        this.inventory_control = inventory_control;
    }

    public int getUser_focus() {
        return user_focus;
    }

    public void setUser_focus(int user_focus) {
        this.user_focus = user_focus;
    }

    public int getAllow_step_approval() {
        return allow_step_approval;
    }

    public void setAllow_step_approval(int allow_step_approval) {
        this.allow_step_approval = allow_step_approval;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getUpdate_required_product() {
        return update_required_product;
    }

    public void setUpdate_required_product(int update_required_product) {
        this.update_required_product = update_required_product;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Nullable
    public Integer getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(@Nullable Integer schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    @Nullable
    public Integer getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(@Nullable Integer schedule_code) {
        this.schedule_code = schedule_code;
    }

    @Nullable
    public Integer getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(@Nullable Integer schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    @Nullable
    public Integer getClient_code() {
        return client_code;
    }

    public void setClient_code(@Nullable Integer client_code) {
        this.client_code = client_code;
    }

    @Nullable
    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(@Nullable String client_id) {
        this.client_id = client_id;
    }

    @Nullable
    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(@Nullable String client_name) {
        this.client_name = client_name;
    }

    @Nullable
    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(@Nullable String address_country) {
        this.address_country = address_country;
    }

    @Nullable
    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(@Nullable String address_state) {
        this.address_state = address_state;
    }

    @Nullable
    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(@Nullable String address_city) {
        this.address_city = address_city;
    }

    @Nullable
    public String getAddress_district() {
        return address_district;
    }

    public void setAddress_district(@Nullable String address_district) {
        this.address_district = address_district;
    }

    @Nullable
    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(@Nullable String address_street) {
        this.address_street = address_street;
    }

    @Nullable
    public String getAddress_num() {
        return address_num;
    }

    public void setAddress_num(@Nullable String address_num) {
        this.address_num = address_num;
    }

    @Nullable
    public String getAddress_complement() {
        return address_complement;
    }

    public void setAddress_complement(@Nullable String address_complement) {
        this.address_complement = address_complement;
    }

    @Nullable
    public String getAddress_zipcode() {
        return address_zipcode;
    }

    public void setAddress_zipcode(@Nullable String address_zipcode) {
        this.address_zipcode = address_zipcode;
    }

    @Nullable
    public String getAddress_lat() {
        return address_lat;
    }

    public void setAddress_lat(@Nullable String address_lat) {
        this.address_lat = address_lat;
    }

    @Nullable
    public String getAddress_lng() {
        return address_lng;
    }

    public void setAddress_lng(@Nullable String address_lng) {
        this.address_lng = address_lng;
    }

    @Nullable
    public Integer getContract_code() {
        return contract_code;
    }

    public void setContract_code(@Nullable Integer contract_code) {
        this.contract_code = contract_code;
    }

    @Nullable
    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(@Nullable String contract_id) {
        this.contract_id = contract_id;
    }

    @Nullable
    public String getContract_desc() {
        return contract_desc;
    }

    public void setContract_desc(@Nullable String contract_desc) {
        this.contract_desc = contract_desc;
    }

    public int getFcm_scn() {
        return fcm_scn;
    }

    public void setFcm_scn(int fcm_scn) {
        this.fcm_scn = fcm_scn;
    }


    public String getTime_action() {
        return time_action;
    }

    public void setTime_action(String time_action) {
        this.time_action = time_action;
    }

    public Integer getMove_other_date() {
        return move_other_date;
    }

    public void setMove_other_date(Integer move_other_date) {
        this.move_other_date = move_other_date;
    }

    public Integer getMove_steps() {
        return move_steps;
    }

    public void setMove_steps(Integer move_steps) {
        this.move_steps = move_steps;
    }

    public String getApply_perc_steps() {
        return apply_perc_steps;
    }

    public void setApply_perc_steps(String apply_perc_steps) {
        this.apply_perc_steps = apply_perc_steps;
    }

    public int getTag_operational_code() {
        return tag_operational_code;
    }

    public void setTag_operational_code(int tag_operational_code) {
        this.tag_operational_code = tag_operational_code;
    }

    public String getTag_operational_id() {
        return tag_operational_id;
    }

    public void setTag_operational_id(String tag_operational_id) {
        this.tag_operational_id = tag_operational_id;
    }

    public String getTag_operational_desc() {
        return tag_operational_desc;
    }

    public void setTag_operational_desc(String tag_operational_desc) {
        this.tag_operational_desc = tag_operational_desc;
    }

    public ArrayList<TK_Ticket_Step> getStep() {
        return step;
    }

    public void setStep(ArrayList<TK_Ticket_Step> step) {
        this.step = step;
    }

    public ArrayList<TK_Ticket_Product> getProduct() {
        return product;
    }

    public void setProduct(ArrayList<TK_Ticket_Product> product) {
        this.product = product;
    }

//TODO - VERIFICAR SE METODO AINDA UTEIS E MOVER PARA STEP QE É QUE TEM LISTA DE CONTROLS AGORA.
//region NOVO_TICKET_CTRL_NA_STEP
    /**
     * Metodo que seta a referente a imagem local no campos *_photo_local.
     */
    public void updateLocalImagesPathIfExists() {
        //Se existe foto no servidor, busca referencia local.
        if(open_photo != null && !open_photo.isEmpty()) {
            setOpen_photo_local(
                getLocalPath(
                    ToolBox_Inf.buildTicketImgPath(this)
                )
            );
        }
        //
        if(getStep() != null) {
            for (TK_Ticket_Step ticketStep : getStep()) {
                if(ticketStep.getCtrl() != null){
                    for (TK_Ticket_Ctrl ctrl : ticketStep.getCtrl()) {
                        switch (ctrl.getCtrl_type()){
                            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                                if(ctrl.getAction() != null) {
                                    //Se existe foto no servidor, busca referencia local.
                                    if (existsActionPhotoInServer(ctrl)) {
                                        ctrl.getAction().setAction_photo_local(
                                            getLocalPath(
                                                ToolBox_Inf.buildTicketActionImgPath(ctrl)
                                            )
                                        );
                                    } else {
                                        //Se não existe foto no server, mas existe local, significa que a foto foi apagada
                                        //nesse caso, deleta a imagem local
                                        if (getLocalPath(ToolBox_Inf.buildTicketActionImgPath(ctrl)) != null) {
                                            //Apaga arquivo local
                                            ToolBox_Inf.deleteDownloadFile(
                                                Constant.CACHE_PATH_PHOTO + "/" + ToolBox_Inf.buildTicketActionImgPath(ctrl)
                                            );
                                        }
                                    }
                                }
                                break;
                            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                                checkUpdateFormUrlLocal(ctrl);
                                break;
                            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * LUCHE - 28/08/2020
     * Metdoo que analisa o PDF do form ja foi baixado e atualiza a utrl local
     * @param ctrl
     */
    private void checkUpdateFormUrlLocal(TK_Ticket_Ctrl ctrl) {
        if(ctrl.getForm() != null){
            if( ctrl.getForm().getPdf_url() != null && !ctrl.getForm().getPdf_url().isEmpty()
                && (ctrl.getForm().getPdf_url_local() == null || ctrl.getForm().getPdf_url_local().isEmpty())
            ){
                String pdfUrlLocalName = ctrl.getForm().getPdfUrlLocalName(true);
                File file = new File(ConstantBaseApp.CACHE_PATH, pdfUrlLocalName );
                if(file.exists()){
                    ctrl.getForm().setPdf_url_local(pdfUrlLocalName);
                }
            }
        }
    }
    /**
     * Metodo que varre todas as actions do ticket e verifica se houve mudança no photo_code.
     * Esse metodo serve para atualizar a foto da action quando alguem alterou a foto via web
     * Caso haja, deleta foto local forçando o download da nova foto
     *
     * @param dbTicket - Ticket no db local
     * @param tkTicket - Mesmo ticket só que retornado do servidor
     */
    public static void checkActionPhotoResetNeeds(TK_Ticket dbTicket, TK_Ticket tkTicket) {
        //Se existe o ticket localmente, começa a analisar as fotos das action
        if(dbTicket != null && dbTicket.getStep() != null && dbTicket.getStep().size() > 0 ){
            for (int stepIdx = 0; stepIdx < tkTicket.getStep().size(); stepIdx++) {
                TK_Ticket_Step tkTicketStep = tkTicket.getStep().get(stepIdx);
                if(tkTicketStep.getCtrl() != null && tkTicketStep.getCtrl().size() > 0){
                    for (TK_Ticket_Ctrl tkTicketCtrl : tkTicketStep.getCtrl()) {
                        if( stepIdx <= dbTicket.getStep().size() //Garante que não haja indexOutOfBound
                            && tkTicketCtrl.getCtrl_type().equalsIgnoreCase(ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION)
                            && tkTicketCtrl.getAction() != null
                        ){
                            if (haveToResetPhoto(dbTicket.getStep().get(stepIdx),tkTicketCtrl)) {
                                //Apaga arquivo local
                                ToolBox_Inf.deleteDownloadFile(
                                    Constant.CACHE_PATH_PHOTO + "/" +ToolBox_Inf.buildTicketActionImgPath(tkTicketCtrl)
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * LUCHE - 26/12/2019
     *
     * Metodo que verifica se a foto da action precisa ser resetada(apagada).
     *
     * O metodo busca o controle passado por parametro na lista de controles do ticketDB e avalia a nessecidade de reset.
     * A logica para reset é:
     *  - O ticket local, possui foto baixada(getAction_photo_local() != null)
     *  - O ticket recebido, possui o codigo da foto(getAction_photo_code() != null)
     *  - (O ticket local não possui codigo da foto OU possui mas é diferente de 0 e diferente do codigo do ticket recebido)
     *  A ultima codição se refere:
     *    "O ticket local não possui codigo da foto":
     *    - Se não possui codigo da foto e um codigo foi retornado, significa que essa ação teve uma foto adicionada(via web ou por outro usr)
     *    "possui mas é diferente de 0":
     *    - Se o codigo for 0 , signifca que a action não tinha foto e o proprio usr adicionou a foto.Nesse caso não é necessario reset
     *    " e diferente do codigo do ticket recebido":
     *    - Se o codigo for != 0, significa que ja existia uma foto, então, só é necessario o reset caso o codigo recebido seja diferente do atual.
     * LUCHE - 27/08/2020
     * Modificado metodo, adicionando uma ultima condição, de quando a action é rejeitado pois ja foi executado por outro usr
     *  - Se codigo da imagem é 0, o codigo retornado for != de 0 e o usr da action é diferente, tb reseta a foto.
     *
     * @param ticketStepsDb - Ticket no DB local
     * @param tkTicketCtrl - Controle do ticket vindo do server
     * @return - Verdadeiro se é necessario resetar / apagar a foto
     */
    private static boolean haveToResetPhoto(TK_Ticket_Step ticketStepsDb, TK_Ticket_Ctrl tkTicketCtrl) {
        for (TK_Ticket_Ctrl ctrlDb : ticketStepsDb.getCtrl()) {
            if( ctrlDb.getCustomer_code() == tkTicketCtrl.getCustomer_code()
                && ctrlDb.getTicket_prefix() == tkTicketCtrl.getTicket_prefix()
                && ctrlDb.getTicket_code() == tkTicketCtrl.getTicket_code()
                //TODO REVER ESSE OR, pois como o ticket qu vem do server nunca tem tmp, talvez não faça sentido....
                && ctrlDb.getTicket_seq() == tkTicketCtrl.getTicket_seq()
                && ctrlDb.getStep_code() == tkTicketCtrl.getStep_code()
                && ctrlDb.getAction() != null
                && tkTicketCtrl.getAction() != null
                && ctrlDb.getAction().getAction_photo_local() != null
                && tkTicketCtrl.getAction().getAction_photo_code() != null
                && ( ctrlDb.getAction().getAction_photo_code() == null
                     || hasExistingPhotoCodeChanged(tkTicketCtrl, ctrlDb)
                     || hasPhotoCodeChangedByActionPlannedRejection(tkTicketCtrl, ctrlDb)
                   )
            ){
                return true;
            }
        }
        //
        return false;
    }

    /**
     * LUCHE - 27/08/2020
     * <p></p>
     * Metodo que avalia a condição:
     *  - Usuario enviou uma action planejada como foto, porem foi rejeitado pelo servidor,
     *  pois essa ação ja constava como executada no servidor e com foto.
     *
     * @param tkTicketCtrl
     * @param ctrlDb
     * @return
     */
    private static boolean hasPhotoCodeChangedByActionPlannedRejection(TK_Ticket_Ctrl tkTicketCtrl, TK_Ticket_Ctrl ctrlDb) {
        return ctrlDb.getAction().getAction_photo_code().equals(0)
           && !ctrlDb.getAction().getAction_photo_code().equals(tkTicketCtrl.getAction().getAction_photo_code())
           && ctrlDb.getCtrl_end_user() != null
           && !ctrlDb.getCtrl_end_user().equals(tkTicketCtrl.getCtrl_end_user());
    }

    /**
     * LUCHE - 27/08/2020
     * <p></p>
     * Metodo que avalia se foto daquela action ja possuia um foto e ela foi trocada.
     * Acontece quando um action esta em processo no servidor teve sua foto alterada.
     * @param tkTicketCtrl
     * @param ctrlDb
     * @return
     */
    private static boolean hasExistingPhotoCodeChanged(TK_Ticket_Ctrl tkTicketCtrl, TK_Ticket_Ctrl ctrlDb) {
        return !ctrlDb.getAction().getAction_photo_code().equals(0)
            && !ctrlDb.getAction().getAction_photo_code().equals(tkTicketCtrl.getAction().getAction_photo_code());
    }
//endregion
    /**
     * Verifica se existe referencia de imagem no servidor
     * A referencia de imagem no servidor se pela regra:
     *  - Action_photo ou action_photo_name existem
     * @param ctrl
     * @return
     */
    private boolean existsActionPhotoInServer(TK_Ticket_Ctrl ctrl) {
        return (ctrl.getAction().getAction_photo_name() != null && !ctrl.getAction().getAction_photo_name().isEmpty())
            || (ctrl.getAction().getAction_photo_url() != null && !ctrl.getAction().getAction_photo_url().isEmpty());
    }

    private static String getLocalPath(String imgLocalPath) {
        String localPath = Constant.CACHE_PATH_PHOTO + "/" +imgLocalPath;
        File file = new File(localPath);
        if (file.exists()) {
            return imgLocalPath;
        }
        return null;
    }

    /**
     * LUCHE - 11/03/2020
     * <p></p>
     * Metodo que valida se o ticket passada por parametro é valido
     * @param tk_ticket - Ticket a ser avaliado
     * @return  - Verdadeiro se ticket tiver os dados da pk diferente de 0
     */
    public static boolean isValidTkTicket(TK_Ticket tk_ticket){
        return  tk_ticket != null
                && tk_ticket.getCustomer_code() > 0
                && tk_ticket.getTicket_prefix() > 0
                && tk_ticket.getTicket_code() > 0 ;

    }

    @Override
    public TK_Ticket clone() throws CloneNotSupportedException {
        return (TK_Ticket) super.clone();
    }

    public void updateTicketCtrlFormInProcess(Context context) {
        if(getStep() != null) {
            for (TK_Ticket_Step ticketStep : getStep()) {
                //
                if(ticketStep.getCtrl() != null){
                    for (TK_Ticket_Ctrl ticketCtrl : ticketStep.getCtrl()) {
                        switch (ticketCtrl.getCtrl_type()){
                            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                                checkUpdateCtrlFormInProcess(context,ticketCtrl);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void checkUpdateCtrlFormInProcess(Context context, TK_Ticket_Ctrl serverCtrl) {
        if(ConstantBaseApp.SYS_STATUS_PENDING.equals(serverCtrl.getCtrl_status())) {
            TK_Ticket_CtrlDao ticketCtrlDao = new TK_Ticket_CtrlDao(
                context,
                ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                Constant.DB_VERSION_CUSTOM
            );
            //
            TK_Ticket_Ctrl dbTicketCtrl = ticketCtrlDao.getByString(
                new TK_Ticket_Ctrl_Sql_001(
                    serverCtrl.getCustomer_code(),
                    serverCtrl.getTicket_prefix(),
                    serverCtrl.getTicket_code(),
                    serverCtrl.getTicket_seq(),
                    serverCtrl.getStep_code()
                ).toSqlQuery()
            );
            //
            if ( dbTicketCtrl != null
                 && ( ConstantBaseApp.SYS_STATUS_PROCESS.equals(dbTicketCtrl.getCtrl_status())
                      || ConstantBaseApp.SYS_STATUS_WAITING_SYNC.equals(dbTicketCtrl.getCtrl_status()))
                 && dbTicketCtrl.getForm() != null
                 && dbTicketCtrl.getForm().getCustom_form_data_tmp() != null
            ) {
                serverCtrl.setCtrl_status(dbTicketCtrl.getCtrl_status());
                serverCtrl.setCtrl_start_date(dbTicketCtrl.getCtrl_start_date());
                serverCtrl.setCtrl_start_user(dbTicketCtrl.getCtrl_start_user());
                serverCtrl.setCtrl_start_user_name(dbTicketCtrl.getCtrl_start_user_name());
                serverCtrl.copyCtrlStatusForInnerProcess();
                //Atualiza custom_form_data_tmp com o do banco.
                if(serverCtrl.getForm() != null){
                    //LUCHE - 22/04/2021 - Correção form perdido ao trocar versão do form quando
                    //ja existia um aberto localmente.
                    serverCtrl.getForm().setCustom_form_version(dbTicketCtrl.getForm().getCustom_form_version());
                    serverCtrl.getForm().setCustom_form_data_tmp(dbTicketCtrl.getForm().getCustom_form_data_tmp());
                }
            }
        }
    }

    public void getNextUserFocus(int stepIdx){

        TK_Ticket_Step current_step = getStep().get(stepIdx);

        boolean hasUserFocus = false;
        boolean hasSingleSteps = current_step.getStep_order_seq() == null;
        int pendingSteps = getPendingStepAmount(current_step.getStep_order(),stepIdx);
        if(!hasSingleSteps && pendingSteps >0) {
            for (int i = 0; i < step.size(); i++) {
                if (i != stepIdx) {
                    TK_Ticket_Step ticket_step = step.get(i);
                    if (current_step.getStep_order() == ticket_step.getStep_order()) {
                        if ((ticket_step.getStep_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PENDING)
                                        || ticket_step.getStep_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS))
                                && ticket_step.getUser_focus() == 1) {
                            hasUserFocus = true;
                        }
                    }
                }
            }
            if (hasUserFocus) {
                this.setUser_focus(1);
            } else {
                this.setUser_focus(0);
            }
        }
    }

    private int getPendingStepAmount(int step_order, int stepIdx) {
        int pendingSteps=0;
        for(int i=0; i< step.size(); i++){
            TK_Ticket_Step ticket_step = step.get(i);
            if(ticket_step.getStep_order() == step_order
            && stepIdx != i
            && (ticket_step.getStep_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PENDING)
                || ticket_step.getStep_status().equalsIgnoreCase(ConstantBaseApp.SYS_STATUS_PROCESS))){
                pendingSteps++;
            }
        }
        return pendingSteps;
    }

    public boolean isReadOnlyStatus() {
        return !ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(ticket_status)
                && !ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(ticket_status);
    }

    public static boolean isReadOnlyStatus(String ticketStatus) {
        return !ConstantBaseApp.SYS_STATUS_PENDING.equalsIgnoreCase(ticketStatus)
                && !ConstantBaseApp.SYS_STATUS_PROCESS.equalsIgnoreCase(ticketStatus);
    }

    public boolean isReadOnlyUserLevel(Context context) {
        EV_User_Customer evUserCustomer = ToolBox_Inf.getCurrentEvUsrCustomerInfo(context);
        //
        if(evUserCustomer.getLicense_user_level_value() == null){
            return 0 < user_level_min;
        }
        return evUserCustomer.getLicense_user_level_value() < user_level_min;
    }

    public boolean isReadOnly(Context context) {
        return isReadOnlyUserLevel(context)
                || isReadOnlyStatus();
    }

//    public MyActions toMyActionsObj(Context context){
//        int rightIcon;
//        if(update_required == 0 && sync_required == 0) {
//            rightIcon = R.drawable.ic_baseline_cloud_done_24_blue;
//        }else {
//            if(update_required == 1 && sync_required == 1){
//                rightIcon = R.drawable.ic_sync_main_menu_data;
//            }else if(update_required == 1){
//                rightIcon = R.drawable.ic_cloud_upload_24_red;
//            }else{
//                rightIcon = R.drawable.ic_baseline_cloud_download_24_gray;
//            }
//        }
//
//       return new MyActions(
//                    MyActions.MY_ACTION_TYPE_TICKET,
//                    ticket_prefix+"."+ticket_code,
//                    ConstantBaseApp.HMAUX_TRANS_LIB.get(ticket_status),
//                    null,
//                    rightIcon,
//                    ToolBox_Inf.getMyActionStartEndDateFormated(context,forecast_date,forecast_date),
//                    tag_operational_desc,
//                    open_product_desc,
//                    open_serial_id,
//                    origin_desc,
//                    type_desc,
//                    "Traze nome do step autal ou varias etapas",
//                    ToolBox_Inf.equalsToLoggedSite(context,String.valueOf(open_site_code)) ? null : open_site_desc,
//                    client_id != null ? client_id +" - "+ client_name: null,
//                    contract_id != null ? contract_id +" - "+ contract_desc: null,
//                    null,
//                    null,
//                    ToolBox_Inf.millisecondsToString(
//                        ToolBox_Inf.dateToMilliseconds(this.getForecast_date()),
//                        "yyyyMMddHHmm"
//                    ),
//                    origin_type,
//           false,
//                    ToolBox_Inf.isItemLate(forecast_date)
//                );
//    }

    public static MyActions toMyActionsObj(Context context, HMAux hmAux){
        int rightIcon;
        if("0".equals(hmAux.get(SqlAct083_002.TOTAL_UPDATE_REQUIRED)) && "0".equals(hmAux.get(TK_TicketDao.SYNC_REQUIRED))) {
            rightIcon = R.drawable.ic_baseline_cloud_done_24_blue;
        }else {
            if("1".equals(hmAux.get(SqlAct083_002.TOTAL_UPDATE_REQUIRED)) && "1".equals(hmAux.get(TK_TicketDao.SYNC_REQUIRED))){
                rightIcon = R.drawable.ic_sync_main_menu_data;
            }else if("1".equals(hmAux.get(SqlAct083_002.TOTAL_UPDATE_REQUIRED))){
                rightIcon = R.drawable.ic_cloud_upload_24_red;
            }else{
                rightIcon = R.drawable.ic_baseline_cloud_download_24_yellow;
            }
        }
        String clientInf = !hmAux.get(TK_TicketDao.CLIENT_ID).isEmpty() ? hmAux.get(TK_TicketDao.CLIENT_ID) +" - "+ hmAux.get(TK_TicketDao.CLIENT_NAME): null;
        String contractInf = !hmAux.get(TK_TicketDao.CONTRACT_ID).isEmpty() ? hmAux.get(TK_TicketDao.CONTRACT_ID) +" - "+ hmAux.get(TK_TicketDao.CONTRACT_CODE): null;
        String plannedDate = ToolBox_Inf.getMyActionStartEndDateFormated(context,hmAux.get(TK_TicketDao.FORECAST_DATE),hmAux.get(TK_TicketDao.FORECAST_DATE));
        String orderByDate = hmAux.get(TK_TicketDao.FORECAST_DATE);
        String periodStartDate = hmAux.get(TK_TicketDao.FORECAST_DATE);
        String lateDate = hmAux.get(TK_TicketDao.FORECAST_DATE);
        //
        if(hmAux.hasConsistentValue(TK_Ticket_StepDao.FORECAST_START)
           && hmAux.hasConsistentValue(TK_Ticket_StepDao.FORECAST_END)
           && !hmAux.get(TK_Ticket_StepDao.FORECAST_START).isEmpty()
           && !hmAux.get(TK_Ticket_StepDao.FORECAST_END).isEmpty()
        ){
            plannedDate = ToolBox_Inf.getMyActionStartEndDateFormated(context,hmAux.get(TK_Ticket_StepDao.FORECAST_START),hmAux.get(TK_Ticket_StepDao.FORECAST_END));
            orderByDate = hmAux.get(TK_Ticket_StepDao.FORECAST_START);
            periodStartDate = hmAux.get(TK_Ticket_StepDao.FORECAST_START);
            lateDate = hmAux.get(TK_Ticket_StepDao.FORECAST_END);
        }

        return new MyActions(
            MyActions.MY_ACTION_TYPE_TICKET,
            hmAux.get(TK_TicketDao.TICKET_PREFIX)+"."+hmAux.get(TK_TicketDao.TICKET_CODE),
            hmAux.get(TK_TicketDao.TICKET_PREFIX)+"."+hmAux.get(TK_TicketDao.TICKET_CODE),
            hmAux.get(TK_TicketDao.TICKET_STATUS),
            ConstantBaseApp.HMAUX_TRANS_LIB.get(hmAux.get(TK_TicketDao.TICKET_STATUS)),
            null,
            rightIcon,
            plannedDate,
            hmAux.get(TK_TicketDao.TAG_OPERATIONAL_DESC),
            hmAux.get(TK_TicketDao.OPEN_PRODUCT_DESC),
            hmAux.get(TK_TicketDao.OPEN_SERIAL_ID),
            hmAux.get(TK_TicketDao.ORIGIN_DESC),
            hmAux.get(TK_TicketDao.TYPE_DESC),
            hmAux.get(TK_Ticket_StepDao.STEP_DESC),
            ToolBox_Inf.convertStringToInt(hmAux.get(TK_TicketDao.OPEN_SITE_CODE)),
            hmAux.get(TK_TicketDao.OPEN_SITE_DESC),
            clientInf,
            contractInf,
            null,
            null,
            ToolBox_Inf.millisecondsToString(
                ToolBox_Inf.dateToMilliseconds(orderByDate),
                "yyyyMMddHHmm"
            ),
            hmAux.get(TK_TicketDao.ORIGIN_TYPE),
            !"0".equals(hmAux.get(MyActions.MY_ACTION_TYPE_FORM)),
            ToolBox_Inf.isItemLate(periodStartDate),
            ToolBox_Inf.isItemLate(lateDate)
        );
    }


}
