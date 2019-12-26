package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.File;
import java.util.ArrayList;

public class TK_Ticket {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int scn;
    private String ticket_id;
    @Expose
    private int type_code;
    private String type_id;
    private String type_desc;
    @Nullable
    private String type_path;
    @Nullable
    private String open_comments;
    @Nullable
    private String open_photo;
    @Nullable
    private String open_photo_local;
    @Nullable
    private String open_name;
    @Nullable
    private String open_email;
    @Nullable
    private String open_phone;
    private String open_date;
    private int open_user;
    private String open_user_name;
    @Nullable
    private String internal_comments;
    @Expose
    private int current_site_code;
    private String current_site_id;
    private String current_site_desc;
    @Expose
    private int current_operation_code;
    private String current_operation_id;
    private String current_operation_desc;
    @Expose
    private int current_product_code;
    private String current_product_id;
    private String current_product_desc;
    @Expose
    private int current_serial_code;
    private String current_serial_id;
    @Nullable
    private String forecast_date;
    private String ticket_status;
    @Nullable
    private String close_date;
    private Integer close_user;
    @Nullable
    private String close_user_name;
    private Integer duration_minutes;
    private Integer barcode_code;
    @Expose
    @Nullable
    private Integer checkin_user;
    @Expose
    private String checkin_date;
    @Expose
    @Nullable
    private String checkin_user_name;
    private int sync_required;
    private int update_required;
    @Expose
    private String token;
    @Expose
    private ArrayList<TK_Ticket_Ctrl> ctrl = new ArrayList<>();

    public void setPK() {
        for (int i = 0; i < ctrl.size(); i++) {
            ctrl.get(i).setPK(this);
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

    public int getCurrent_site_code() {
        return current_site_code;
    }

    public void setCurrent_site_code(int current_site_code) {
        this.current_site_code = current_site_code;
    }

    public String getCurrent_site_id() {
        return current_site_id;
    }

    public void setCurrent_site_id(String current_site_id) {
        this.current_site_id = current_site_id;
    }

    public String getCurrent_site_desc() {
        return current_site_desc;
    }

    public void setCurrent_site_desc(String current_site_desc) {
        this.current_site_desc = current_site_desc;
    }

    public int getCurrent_operation_code() {
        return current_operation_code;
    }

    public void setCurrent_operation_code(int current_operation_code) {
        this.current_operation_code = current_operation_code;
    }

    public String getCurrent_operation_id() {
        return current_operation_id;
    }

    public void setCurrent_operation_id(String current_operation_id) {
        this.current_operation_id = current_operation_id;
    }

    public String getCurrent_operation_desc() {
        return current_operation_desc;
    }

    public void setCurrent_operation_desc(String current_operation_desc) {
        this.current_operation_desc = current_operation_desc;
    }

    public int getCurrent_product_code() {
        return current_product_code;
    }

    public void setCurrent_product_code(int current_product_code) {
        this.current_product_code = current_product_code;
    }

    public String getCurrent_product_id() {
        return current_product_id;
    }

    public void setCurrent_product_id(String current_product_id) {
        this.current_product_id = current_product_id;
    }

    public String getCurrent_product_desc() {
        return current_product_desc;
    }

    public void setCurrent_product_desc(String current_product_desc) {
        this.current_product_desc = current_product_desc;
    }

    public int getCurrent_serial_code() {
        return current_serial_code;
    }

    public void setCurrent_serial_code(int current_serial_code) {
        this.current_serial_code = current_serial_code;
    }

    public String getCurrent_serial_id() {
        return current_serial_id;
    }

    public void setCurrent_serial_id(String current_serial_id) {
        this.current_serial_id = current_serial_id;
    }

    @Nullable
    public String getForecast_date() {
        return forecast_date;
    }

    public void setForecast_date(@Nullable String forecast_date) {
        this.forecast_date = forecast_date;
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

    @Nullable
    public String getCheckin_date() {
        return checkin_date;
    }

    public void setCheckin_date(@Nullable String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public Integer getCheckin_user() {
        return checkin_user;
    }

    public void setCheckin_user(Integer checkin_user) {
        this.checkin_user = checkin_user;
    }

    @Nullable
    public String getCheckin_user_name() {
        return checkin_user_name;
    }

    public void setCheckin_user_name(@Nullable String checkin_user_name) {
        this.checkin_user_name = checkin_user_name;
    }

    public ArrayList<TK_Ticket_Ctrl> getCtrl() {
        return ctrl;
    }

    public void setCtrl(ArrayList<TK_Ticket_Ctrl> ctrl) {
        this.ctrl = ctrl;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
        for (TK_Ticket_Ctrl ctrl : getCtrl()) {
            switch (ctrl.getCtrl_type()){
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                    break;
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                default:
                    //Se existe foto no servidor, busca referencia local.
                    if(existsActionPhotoInServer(ctrl)) {
                        ctrl.getAction().setAction_photo_local(
                            getLocalPath(
                                ToolBox_Inf.buildTicketActionImgPath(ctrl)
                            )
                        );
                    }
                    break;
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
        if(dbTicket != null){
            for (TK_Ticket_Ctrl tkTicketCtrl : tkTicket.getCtrl()) {
                if( tkTicketCtrl.getCtrl_type().equalsIgnoreCase(ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION)
                    && tkTicketCtrl.getAction() != null
                ){
                    if (haveToResetPhoto(dbTicket,tkTicketCtrl)) {
                        //Apaga arquivo local
                        ToolBox_Inf.deleteDownloadFile(
                            ToolBox_Inf.buildTicketActionImgPath(tkTicketCtrl)
                        );
                    }
                }
            }
        }
    }

    private static boolean haveToResetPhoto(TK_Ticket ticketDb, TK_Ticket_Ctrl tkTicketCtrl) {
        for (TK_Ticket_Ctrl ctrlDb : ticketDb.getCtrl()) {
            if( ctrlDb.getCustomer_code() == tkTicketCtrl.getCustomer_code()
                && ctrlDb.getTicket_prefix() == tkTicketCtrl.getTicket_prefix()
                && ctrlDb.getTicket_code() == tkTicketCtrl.getTicket_code()
                && ctrlDb.getTicket_seq() == tkTicketCtrl.getTicket_seq()
                && ctrlDb.getAction().getAction_photo_local() != null
                && tkTicketCtrl.getAction().getAction_photo_code() != null
                && (ctrlDb.getAction().getAction_photo_code() == null
                || !ctrlDb.getAction().getAction_photo_code().equals(0))
            ){
                return true;
            }
        }
        //
        return false;
    }

    /**
     * Verifica se existe referencia de imagem no servidor
     * A referencia de imagem no servidor se pela regra:
     *  - Action_photo ou action_photo_name existem
     * @param ctrl
     * @return
     */
    private boolean existsActionPhotoInServer(TK_Ticket_Ctrl ctrl) {
        return (ctrl.getAction().getAction_photo_name() != null && !ctrl.getAction().getAction_photo_name().isEmpty())
            || (ctrl.getAction().getAction_photo() != null && !ctrl.getAction().getAction_photo().isEmpty());
    }

    private String getLocalPath(String imgLocalPath) {
        String localPath = Constant.CACHE_PATH_PHOTO + "/" +imgLocalPath;
        File file = new File(localPath);
        if (file.exists()) {
            return imgLocalPath;
        }
        return null;
    }
}
