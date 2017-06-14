package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 12/06/2017.
 */

public class SM_SO_Service_Exec_Task_File {


    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int category_price_code;
    private int service_code;
    private int task_exec;
    private int task_seq;
    @Expose
    private int file_code;

    public SM_SO_Service_Exec_Task_File() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.price_list_code = -1;
        this.pack_code = -1;
        this.category_price_code = -1;
        this.service_code = -1;
        this.task_exec = -1;
        this.task_seq = -1;
    }

    public void setPK(SM_SO_Service_Exec_Task task){
        this.customer_code = task.getCustomer_code();
        this.so_prefix = task.getSo_prefix();
        this.so_code = task.getSo_code();
        this.price_list_code = task.getPrice_list_code();
        this.pack_code = task.getPack_code();
        this.category_price_code = task.getCategory_price_code();
        this.service_code = task.getService_code();
        this.task_exec = task.getTask_exec();
        this.task_seq = task.getTask_seq();
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

    public int getFile_code() {
        return file_code;
    }

    public void setFile_code(int file_code) {
        this.file_code = file_code;
    }


}
