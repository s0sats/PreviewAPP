package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.namoadigital.prj001.util.ConstantBaseApp;
import com.namoadigital.prj001.util.ToolBox_Inf;

import java.io.Serializable;
import java.util.ArrayList;

public class TK_Ticket_Ctrl implements Serializable {
    private long customer_code;//pk
    private int ticket_prefix;//pk
    private int ticket_code;//pk
    @Expose
    private int ticket_seq;//pk
    @Expose
    private int ticket_seq_tmp;//pk
    @Expose
    private String ctrl_type;
    @Nullable
    private Integer product_code;
    @Nullable
    private String product_id;
    @Nullable
    private String product_desc;
    @Nullable
    private Integer serial_code;
    @Nullable
    private String serial_id;
    @Expose
    @Nullable
    private String ctrl_start_date;
    @Expose
    private Integer ctrl_start_user;
    private String ctrl_start_user_name;
    @Nullable
    @Expose
    private String ctrl_end_date;
    private Integer ctrl_end_user;
    @Nullable
    private String ctrl_end_user_name;
    @Expose
    private String ctrl_status;
    private Integer partner_code;
    @Nullable
    private String partner_id;
    @Nullable
    private String partner_desc;
    private int step_code;
    @Nullable//Somente se for agendado
    private Integer step_order;
    private int obj_planned;
    private int update_required;
    @Expose
    @Nullable
    private TK_Ticket_Action action;
    @Expose
    @Nullable
    private TK_Ticket_Measure measure;
    @Expose
    @Nullable
    private TK_Ticket_Approval approval;
    @Nullable
    private ArrayList<TK_Ticket_Approval_Rejection> rejection = new ArrayList<>();
    @Nullable
    private TK_Ticket_Form form;

    public TK_Ticket_Ctrl() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
        this.ticket_seq = -1;
        this.step_order = -1;
    }

    public TK_Ticket_Ctrl(int ticket_seq, int ticket_seq_tmp, String ctrl_type, @Nullable Integer product_code, @Nullable String product_id, @Nullable String product_desc, @Nullable Integer serial_code, @Nullable String serial_id, String ctrl_status, int step_order, int obj_planned) {
        this.ticket_seq = ticket_seq;
        this.ticket_seq_tmp = ticket_seq_tmp;
        this.ctrl_type = ctrl_type;
        this.product_code = product_code;
        this.product_id = product_id;
        this.product_desc = product_desc;
        this.serial_code = serial_code;
        this.serial_id = serial_id;
        this.ctrl_status = ctrl_status;
        this.step_order = step_order;
        this.obj_planned = obj_planned;
    }

    //    public void setPK(TK_Ticket tk_ticket) {
//        this.customer_code = tk_ticket.getCustomer_code();
//        this.ticket_prefix = tk_ticket.getTicket_prefix();
//        this.ticket_code = tk_ticket.getTicket_code();
//        //Seta a PK no tipo do controle
//        switch (this.ctrl_type) {
//            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
//                if(this.action != null) {
//                    this.action.setPK(this);
//                }
//                break;
//            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
//                if(this.measure != null) {
//                    this.measure.setPK(this);
//                }
//                break;
//            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
//                if(this.approval != null){
//                    this.approval.setPK(this);
//                }
//                if(this.rejection != null){
//                    for (TK_Ticket_Approval_Rejection tk_ticket_approval_rejection : this.rejection) {
//                        tk_ticket_approval_rejection.setPK(this);
//                    }
//                }
//                break;
//        }
//    }

    public void setPK(TK_Ticket_Step tk_ticket_step) {
        this.customer_code = tk_ticket_step.getCustomer_code();
        this.ticket_prefix = tk_ticket_step.getTicket_prefix();
        this.ticket_code = tk_ticket_step.getTicket_code();
        this.step_code = tk_ticket_step.getStep_code();
        //Seta a PK no tipo do controle
        if(this.ctrl_type != null && !this.ctrl_type.isEmpty()) {
            setPKIntoProcess();
        }
    }

    public void setPKIntoProcess() {
        try {
            switch (this.ctrl_type) {
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                    if (this.action != null) {
                        this.action.setPK(this);
                    }
                    break;
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                    if (this.measure != null) {
                        this.measure.setPK(this);
                    }
                    break;
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                    if (this.approval != null) {
                        this.approval.setPK(this);
                    }
                    if (this.rejection != null) {
                        for (TK_Ticket_Approval_Rejection tk_ticket_approval_rejection : this.rejection) {
                            tk_ticket_approval_rejection.setPK(this);
                        }
                    }
                    break;
                case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                    if (this.form != null) {
                        this.form.setPK(this);
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
            ToolBox_Inf.registerException(getClass().getName(),e);
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

    public int getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(int ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public int getTicket_seq_tmp() {
        return ticket_seq_tmp;
    }

    public void setTicket_seq_tmp(int ticket_seq_tmp) {
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    public String getCtrl_type() {
        return ctrl_type;
    }

    public void setCtrl_type(String ctrl_type) {
        this.ctrl_type = ctrl_type;
    }

    @Nullable
    public Integer getProduct_code() {
        return product_code;
    }

    public void setProduct_code(@Nullable Integer product_code) {
        this.product_code = product_code;
    }

    @Nullable
    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(@Nullable String product_id) {
        this.product_id = product_id;
    }

    @Nullable
    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(@Nullable String product_desc) {
        this.product_desc = product_desc;
    }

    @Nullable
    public Integer getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(@Nullable Integer serial_code) {
        this.serial_code = serial_code;
    }

    @Nullable
    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(@Nullable String serial_id) {
        this.serial_id = serial_id;
    }

    public String getCtrl_start_date() {
        return ctrl_start_date;
    }

    public void setCtrl_start_date(String ctrl_start_date) {
        this.ctrl_start_date = ctrl_start_date;
    }

    public Integer getCtrl_start_user() {
        return ctrl_start_user;
    }

    public void setCtrl_start_user(Integer ctrl_start_user) {
        this.ctrl_start_user = ctrl_start_user;
    }

    public String getCtrl_start_user_name() {
        return ctrl_start_user_name;
    }

    public void setCtrl_start_user_name(String ctrl_start_user_name) {
        this.ctrl_start_user_name = ctrl_start_user_name;
    }

    @Nullable
    public String getCtrl_end_date() {
        return ctrl_end_date;
    }

    public void setCtrl_end_date(@Nullable String ctrl_end_date) {
        this.ctrl_end_date = ctrl_end_date;
    }

    public Integer getCtrl_end_user() {
        return ctrl_end_user;
    }

    public void setCtrl_end_user(Integer ctrl_end_user) {
        this.ctrl_end_user = ctrl_end_user;
    }

    @Nullable
    public String getCtrl_end_user_name() {
        return ctrl_end_user_name;
    }

    public void setCtrl_end_user_name(@Nullable String ctrl_end_user_name) {
        this.ctrl_end_user_name = ctrl_end_user_name;
    }

    public String getCtrl_status() {
        return ctrl_status;
    }

    public void setCtrl_status(String ctrl_status) {
        this.ctrl_status = ctrl_status;
    }

    public Integer getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(Integer partner_code) {
        this.partner_code = partner_code;
    }

    @Nullable
    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(@Nullable String partner_id) {
        this.partner_id = partner_id;
    }

    @Nullable
    public String getPartner_desc() {
        return partner_desc;
    }

    public void setPartner_desc(@Nullable String partner_desc) {
        this.partner_desc = partner_desc;
    }

    public TK_Ticket_Action getAction() {
        return action;
    }

    public void setAction(TK_Ticket_Action action) {
        this.action = action;
    }

    public TK_Ticket_Measure getMeasure() {
        return measure;
    }

    public void setMeasure(TK_Ticket_Measure measure) {
        this.measure = measure;
    }

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    @Nullable
    public Integer getStep_order() {
        return step_order;
    }

    public void setStep_order(@Nullable Integer step_order) {
        this.step_order = step_order;
    }

    public int getObj_planned() {
        return obj_planned;
    }

    public void setObj_planned(int obj_planned) {
        this.obj_planned = obj_planned;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    @Nullable
    public TK_Ticket_Approval getApproval() {
        return approval;
    }

    public void setApproval(@Nullable TK_Ticket_Approval approval) {
        this.approval = approval;
    }

    @Nullable
    public ArrayList<TK_Ticket_Approval_Rejection> getRejection() {
        return rejection;
    }

    public void setRejection(@Nullable ArrayList<TK_Ticket_Approval_Rejection> rejection) {
        this.rejection = rejection;
    }

    @Nullable
    public TK_Ticket_Form getForm() {
        return form;
    }

    public void setForm(@Nullable TK_Ticket_Form form) {
        this.form = form;
    }

    public void setFinalNameIntoActionPhoto(String finalName){
        if(ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION.equals(this.ctrl_type)
            && this.getAction() != null
            && !finalName.equals(this.getAction().getAction_photo_local())
        ){
            this.getAction().setAction_photo_local(finalName);
            this.getAction().setAction_photo_name(finalName);
        }
    }

    public void removeEndInfo(){
        this.setCtrl_end_user_name(null);
        this.setCtrl_end_user(null);
        this.setCtrl_end_date(null);
    }

    public void copyCtrlStatusForInnerProcess(){
        switch (this.ctrl_type) {
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_ACTION:
                if(this.action != null) {
                    this.action.setAction_status(this.ctrl_status);
                }
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_MEASURE:
                if(this.measure != null) {
                    //this.measure.setPK(this);
                }
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_APPROVAL:
                if(this.approval != null){
                    this.approval.setApproval_status(this.ctrl_status);
                }
                //Não ha necessidade de mexer no rejeitdo, pois ele tem o proprio status que sempre é rejeitado.
                break;
            case ConstantBaseApp.TK_TICKET_CRTL_TYPE_FORM:
                if(this.form != null){
                    this.form.setForm_status(this.ctrl_status);
                }
                break;
        }
    }
}
