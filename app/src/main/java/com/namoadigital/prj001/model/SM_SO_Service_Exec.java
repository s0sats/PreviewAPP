package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO_Service_Exec {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    private int pack_code;
    private int category_price_code;
    private int service_code;
    @Expose
    private int task_exec;
    @Expose
    private String status;
    @Expose
    private int last_update_user;
    @Expose
    private String last_update_date;
    @Expose
    private ArrayList<SM_SO_Service_Exec_Task> task;

    public SM_SO_Service_Exec() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.price_list_code = -1;
        this.pack_code = -1;
        this.category_price_code = -1;
        this.service_code = -1;
        this.task = new ArrayList<>();
    }

    public void setPK(SM_SO_Service service){
        this.customer_code = service.getCustomer_code();
        this.so_prefix = service.getSo_prefix();
        this.so_code = service.getSo_code();
        this.price_list_code = service.getPrice_list_code();
        this.pack_code = service.getPack_code();
        this.category_price_code = service.getCategory_price_code();
        this.service_code = service.getService_code();

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getLast_update_user() {
        return last_update_user;
    }

    public void setLast_update_user(int last_update_user) {
        this.last_update_user = last_update_user;
    }

    public String getLast_update_date() {
        return last_update_date;
    }

    public void setLast_update_date(String last_update_date) {
        this.last_update_date = last_update_date;
    }

    public ArrayList<SM_SO_Service_Exec_Task> getTask() {
        return task;
    }

    public void setTask(ArrayList<SM_SO_Service_Exec_Task> task) {
        this.task = task;
    }
}
