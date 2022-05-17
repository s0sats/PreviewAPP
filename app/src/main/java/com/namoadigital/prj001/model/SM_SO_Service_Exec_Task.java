package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO_Service_Exec_Task {

    @SerializedName("customer_code") private long customer_code; //pk
    @SerializedName("so_prefix") private int so_prefix; //pk
    @SerializedName("so_code") private int so_code; //pk
    @SerializedName("price_list_code") private int price_list_code; //pk
    @SerializedName("pack_code") private int pack_code; //pk
    @SerializedName("pack_seq") private int pack_seq; //pk
    @SerializedName("category_price_code") private int category_price_code; //pk
    @SerializedName("service_code") private int service_code; //pk
    @SerializedName("service_seq") private int service_seq; //pk
    @SerializedName("exec_code") private Integer exec_code; //pk

    // Novos Criados
    @Expose
    @SerializedName("task_code")
    private Integer task_code; //pk #SQN
    @SerializedName("exec_tmp") private long exec_tmp; //pk
    @Expose
    @SerializedName("task_tmp")
    private long task_tmp; //pk
    @SerializedName("task_seq_oper") private int task_seq_oper;
    @SerializedName("task_user") private int task_user;
    @SerializedName("task_user_nick") private String task_user_nick;
    @Expose
    @SerializedName("start_date")
    private String start_date;

    // Null
    @Expose
    @SerializedName("end_date")
    private String end_date;
    @SerializedName("exec_time") private Integer exec_time;

    // Novos Criados
    // Null
    @SerializedName("exec_time_format") private String exec_time_format;
    @Expose
    @SerializedName("task_perc")
    private int task_perc;
    @Expose
    @SerializedName("qty_people")
    private int qty_people;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("site_code")
    private Integer site_code;
    @SerializedName("site_id") private String site_id;
    @SerializedName("site_desc") private String site_desc;
    @Expose
    @SerializedName("zone_code")
    private Integer zone_code;
    @SerializedName("zone_id") private String zone_id;
    @SerializedName("zone_desc") private String zone_desc;
    @SerializedName("local_code") private Integer local_code;
    @SerializedName("local_id") private String local_id;

    // Null
    @Expose
    @SerializedName("comments")
    private String comments;
    @Expose
    @SerializedName("task_file")
    private ArrayList<SM_SO_Service_Exec_Task_File> task_file;

    public SM_SO_Service_Exec_Task() {
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
        this.task_file = new ArrayList<>();
    }

    public void setPK(SM_SO_Service_Exec exec) {
        this.customer_code = exec.getCustomer_code();
        this.so_prefix = exec.getSo_prefix();
        this.so_code = exec.getSo_code();
        this.price_list_code = exec.getPrice_list_code();
        this.pack_code = exec.getPack_code();
        this.pack_seq = exec.getPack_seq();
        this.category_price_code = exec.getCategory_price_code();
        this.service_code = exec.getService_code();
        this.service_seq = exec.getService_seq();
        this.exec_code = exec.getExec_code();
        this.exec_tmp = exec.getExec_tmp();

        for (int i = 0; i < task_file.size(); i++) {
            task_file.get(i).setPK(this);
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

    public Integer getTask_code() {
        return task_code;
    }

    public void setTask_code(Integer task_code) {
        this.task_code = task_code;
    }

    public long getExec_tmp() {
        return exec_tmp;
    }

    public void setExec_tmp(long exec_tmp) {
        this.exec_tmp = exec_tmp;
    }

    public long getTask_tmp() {
        return task_tmp;
    }

    public void setTask_tmp(long task_tmp) {
        this.task_tmp = task_tmp;
    }

    public int getTask_seq_oper() {
        return task_seq_oper;
    }

    public void setTask_seq_oper(int task_seq_oper) {
        this.task_seq_oper = task_seq_oper;
    }

    public int getTask_user() {
        return task_user;
    }

    public void setTask_user(int task_user) {
        this.task_user = task_user;
    }

    public String getTask_user_nick() {
        return task_user_nick;
    }

    public void setTask_user_nick(String task_user_nick) {
        this.task_user_nick = task_user_nick;
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

    public Integer getExec_time() {
        return exec_time;
    }

    public void setExec_time(Integer exec_time) {
        this.exec_time = exec_time;
    }

    public String getExec_time_format() {
        return exec_time_format;
    }

    public void setExec_time_format(String exec_time_format) {
        this.exec_time_format = exec_time_format;
    }

    public int getTask_perc() {
        return task_perc;
    }

    public void setTask_perc(int task_perc) {
        this.task_perc = task_perc;
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

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
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

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
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

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
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

    public ArrayList<SM_SO_Service_Exec_Task_File> getTask_file() {
        return task_file;
    }

    public void setTask_file(ArrayList<SM_SO_Service_Exec_Task_File> task_file) {
        this.task_file = task_file;
    }
}
