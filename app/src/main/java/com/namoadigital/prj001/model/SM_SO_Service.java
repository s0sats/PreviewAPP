package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO_Service {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    private int price_list_code; //pk
    private int pack_code; //pk
    private int pack_seq; //pk
    private int category_price_code; //pk
    @Expose
    private int service_code; //pk

    // Novos Criados
    private int service_seq; //pk
    private String service_id;
    private String service_desc;

    // Null
    private String service_oper_id;

    // Novos Criados
    private String status;
    @Expose
    private int qty;
    @Expose
    private int optional;
    @Expose
    private int manual_price;
    @Expose
    private int express;
    @Expose
    private int exec_time_standard;

    // Null
    @Expose
    private Double price;
    @Expose
    private Double cost;
    @Expose
    private String exec_type;
    @Expose
    private int exec_seq_oper;

    // Null
    @Expose
    private Integer approval_budget_user;
    @Expose
    private String approval_budget_date;
    @Expose
    private Integer partner_code;

    // Novos Criados Null
    private String partner_id;
    private String partner_desc;

    private String require_approval;

    @Expose
    private ArrayList<SM_SO_Service_Exec> exec;

    public SM_SO_Service() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.price_list_code = -1;
        this.pack_code = -1;
        this.pack_seq = -1;
        this.category_price_code = -1;
        this.service_code = -1;
        this.exec = new ArrayList<>();
    }

    public void setPK(SM_SO_Pack pack) {
        this.customer_code = pack.getCustomer_code();
        this.so_prefix = pack.getSo_prefix();
        this.so_code = pack.getSo_code();
        this.price_list_code = pack.getPrice_list_code();
        this.pack_code = pack.getPack_code();
        this.pack_seq = pack.getPack_seq();

        for (int i = 0; i < exec.size(); i++) {
            exec.get(i).setPK(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(int so_prefix) {
        this.so_prefix = so_prefix;
    }

    public int getSo_code() {
        return so_code;
    }

    public void setSo_code(int so_code) {
        this.so_code = so_code;
    }

    public int getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(int price_list_code) {
        this.price_list_code = price_list_code;
    }

    public int getPack_code() {
        return pack_code;
    }

    public void setPack_code(int pack_code) {
        this.pack_code = pack_code;
    }

    public int getPack_seq() {
        return pack_seq;
    }

    public void setPack_seq(int pack_seq) {
        this.pack_seq = pack_seq;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public int getService_code() {
        return service_code;
    }

    public void setService_code(int service_code) {
        this.service_code = service_code;
    }

    public int getService_seq() {
        return service_seq;
    }

    public void setService_seq(int service_seq) {
        this.service_seq = service_seq;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }

    public String getService_oper_id() {
        return service_oper_id;
    }

    public void setService_oper_id(String service_oper_id) {
        this.service_oper_id = service_oper_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getOptional() {
        return optional;
    }

    public void setOptional(int optional) {
        this.optional = optional;
    }

    public int getManual_price() {
        return manual_price;
    }

    public void setManual_price(int manual_price) {
        this.manual_price = manual_price;
    }

    public int getExpress() {
        return express;
    }

    public void setExpress(int express) {
        this.express = express;
    }

    public int getExec_time_standard() {
        return exec_time_standard;
    }

    public void setExec_time_standard(int exec_time_standard) {
        this.exec_time_standard = exec_time_standard;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getExec_type() {
        return exec_type;
    }

    public void setExec_type(String exec_type) {
        this.exec_type = exec_type;
    }

    public int getExec_seq_oper() {
        return exec_seq_oper;
    }

    public void setExec_seq_oper(int exec_seq_oper) {
        this.exec_seq_oper = exec_seq_oper;
    }

    public Integer getApproval_budget_user() {
        return approval_budget_user;
    }

    public void setApproval_budget_user(Integer approval_budget_user) {
        this.approval_budget_user = approval_budget_user;
    }

    public String getApproval_budget_date() {
        return approval_budget_date;
    }

    public void setApproval_budget_date(String approval_budget_date) {
        this.approval_budget_date = approval_budget_date;
    }

    public Integer getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(Integer partner_code) {
        this.partner_code = partner_code;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_desc() {
        return partner_desc;
    }

    public void setPartner_desc(String partner_desc) {
        this.partner_desc = partner_desc;
    }

    public String getRequire_approval() {
        return require_approval;
    }

    public void setRequire_approval(String require_approval) {
        this.require_approval = require_approval;
    }

    public ArrayList<SM_SO_Service_Exec> getExec() {
        return exec;
    }

    public void setExec(ArrayList<SM_SO_Service_Exec> exec) {
        this.exec = exec;
    }
}
