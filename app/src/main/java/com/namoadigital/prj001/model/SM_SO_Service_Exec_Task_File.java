package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 12/06/2017.
 */

public class SM_SO_Service_Exec_Task_File {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    private int price_list_code; //pk
    private int pack_code; //pk
    private int pack_seq; //pk
    private int category_price_code; //pk
    private int service_code; //pk
    private int service_seq; //pk
    private int exec_code; //pk server
    private long exec_tmp; //pk local
    private int task_code; //pk server
    private long task_tmp; //pk local
    @Expose
    private int file_code; //ok
    @Expose
    private long file_tmp; //ok
    @Expose
    private String file_name;
    private String file_url;

    public SM_SO_Service_Exec_Task_File() {
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
        this.task_code = -1;
        this.file_code = -1;
        this.file_name = "";
        this.file_url = "";
    }

    public void setPK(SM_SO_Service_Exec_Task task) {
        this.customer_code = task.getCustomer_code();
        this.so_prefix = task.getSo_prefix();
        this.so_code = task.getSo_code();
        this.price_list_code = task.getPrice_list_code();
        this.pack_code = task.getPack_code();
        this.pack_seq = task.getPack_seq();
        this.category_price_code = task.getCategory_price_code();
        this.service_code = task.getService_code();
        this.service_seq = task.getService_seq();
        this.exec_code = task.getExec_code();
        this.task_code = task.getTask_code();
        this.exec_tmp = task.getExec_tmp();
        this.task_tmp = task.getTask_tmp();

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

    public int getExec_code() {
        return exec_code;
    }

    public void setExec_code(int exec_code) {
        this.exec_code = exec_code;
    }

    public long getExec_tmp() {
        return exec_tmp;
    }

    public void setExec_tmp(long exec_tmp) {
        this.exec_tmp = exec_tmp;
    }

    public int getTask_code() {
        return task_code;
    }

    public void setTask_code(int task_code) {
        this.task_code = task_code;
    }

    public long getTask_tmp() {
        return task_tmp;
    }

    public void setTask_tmp(long task_tmp) {
        this.task_tmp = task_tmp;
    }

    public int getFile_code() {
        return file_code;
    }

    public void setFile_code(int file_code) {
        this.file_code = file_code;
    }

    public long getFile_tmp() {
        return file_tmp;
    }

    public void setFile_tmp(long file_tmp) {
        this.file_tmp = file_tmp;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }
}
