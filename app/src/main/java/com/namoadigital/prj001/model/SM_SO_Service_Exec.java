package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO_Service_Exec {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    private int price_list_code; //pk
    private int pack_code; //pk
    private int pack_seq; //pk
    private int category_price_code; //pk
    private int service_code; //pk
    private int service_seq; //pk
    @Expose
    private Integer exec_code; //pk #SQN
    @Expose
    private long exec_tmp; //pk
    @Expose
    private String status;

    // Novos Criados Null
    @Expose
    private Integer partner_code;
    private String partner_id;
    private String partner_desc;

    @Expose
    private ArrayList<SM_SO_Service_Exec_Task> task;

    public SM_SO_Service_Exec() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.price_list_code = -1;
        this.pack_code = -1;
        this.pack_seq = -1;
        this.category_price_code = -1;
        this.service_code = -1;
        this.service_seq = -1;
        this.exec_code = -1;
        this.task = new ArrayList<>();
    }

    public void setPK(SM_SO_Service service){
        this.customer_code = service.getCustomer_code();
        this.so_prefix = service.getSo_prefix();
        this.so_code = service.getSo_code();
        this.price_list_code = service.getPrice_list_code();
        this.pack_code = service.getPack_code();
        this.pack_seq = service.getPack_seq();
        this.category_price_code = service.getCategory_price_code();
        this.service_code = service.getService_code();
        this.service_seq = service.getService_seq();

        for(int i =0; i < task.size();i++){
            task.get(i).setPK(this);
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

    public Integer getExec_code() {
        return exec_code;
    }

    public void setExec_code(Integer exec_code) {
        this.exec_code = exec_code;
    }

    public long getExec_tmp() {
        return exec_tmp;
    }

    public void setExec_tmp(long exec_tmp) {
        this.exec_tmp = exec_tmp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ArrayList<SM_SO_Service_Exec_Task> getTask() {
        return task;
    }

    public void setTask(ArrayList<SM_SO_Service_Exec_Task> task) {
        this.task = task;
    }
}
