package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO_Service_Exec_Task {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int category_price_code;
    private int service_code;
    private int task_exec;
    @Expose
    private int task_seq;
    @Expose
    private int task_user;
    @Expose
    private String start_date;
    @Expose
    private String end_date;
    @Expose
    private int exec_time;
    @Expose
    private int qty_people;
    @Expose
    private String status;
    @Expose
    private int site_code;
    private String site_id;
    private String site_desc;
    @Expose
    private int zone_code;
    private String zone_id;
    private String zone_desc;
    @Expose
    private int local_code;
    private String local_id;
    @Expose
    private String comments;
    @Expose
    private ArrayList<SM_SO_Service_Exec_Task_File> task_attach;

    public SM_SO_Service_Exec_Task() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.price_list_code = -1;
        this.pack_code = -1;
        this.category_price_code = -1;
        this.service_code = -1;
        this.task_exec = -1;
        this.task_attach = new ArrayList<>();
    }

    public void setPK(SM_SO_Service_Exec exec){
        this.customer_code = exec.getCustomer_code();
        this.so_prefix = exec.getSo_prefix();
        this.so_code = exec.getSo_code();
        this.price_list_code = exec.getPrice_list_code();
        this.pack_code = exec.getPack_code();
        this.category_price_code = exec.getCategory_price_code();
        this.service_code = exec.getService_code();
        this.task_exec = exec.getTask_exec();

        for(int i=0; i < task_attach.size();i++){
            task_attach.get(i);
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

    public int getTask_exec() {
        return task_exec;
    }

    public void setTask_exec(int task_exec) {
        this.task_exec = task_exec;
    }

    public int getTask_seq() {
        return task_seq;
    }

    public void setTask_seq(int task_seq) {
        this.task_seq = task_seq;
    }

    public int getTask_user() {
        return task_user;
    }

    public void setTask_user(int task_user) {
        this.task_user = task_user;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getExec_time() {
        return exec_time;
    }

    public void setExec_time(int exec_time) {
        this.exec_time = exec_time;
    }

    public int getQty_people() {
        return qty_people;
    }

    public void setQty_people(int qty_people) {
        this.qty_people = qty_people;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getZone_code() {
        return zone_code;
    }

    public void setZone_code(int zone_code) {
        this.zone_code = zone_code;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_desc() {
        return zone_desc;
    }

    public void setZone_desc(String zone_desc) {
        this.zone_desc = zone_desc;
    }

    public int getLocal_code() {
        return local_code;
    }

    public void setLocal_code(int local_code) {
        this.local_code = local_code;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<SM_SO_Service_Exec_Task_File> getTask_attach() {
        return task_attach;
    }

    public void setTask_attach(ArrayList<SM_SO_Service_Exec_Task_File> task_attach) {
        this.task_attach = task_attach;
    }
}
