package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Serial_Save_Rec {
    @Expose
    private String app;
    @Expose
    private String validation;
    @Expose
    private String link_url;
    @Expose
    private String error_msg;
    @Expose
    private ArrayList<Serial_Save_Return> serial_return;
    @Expose
    private ArrayList<So_Save_Return> so_return;
    @Expose
    private ArrayList<SM_SO> so;
    @Expose
    private So_From_To so_from_to;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public ArrayList<So_Save_Return> getSo_return() {
        return so_return;
    }

    public void setSo_return(ArrayList<So_Save_Return> so_return) {
        this.so_return = so_return;
    }

    public So_From_To getSo_from_to() {
        return so_from_to;
    }

    public void setSo_from_to(So_From_To so_from_to) {
        this.so_from_to = so_from_to;
    }

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }

    //    public ArrayList<SM_SO> getSo() {
//        return so;
//    }
//
//    public void setSo(ArrayList<SM_SO> so) {
//        this.so = so;
//    }

    public ArrayList<Serial_Save_Return> getSerial_return() {
        return serial_return;
    }

    public void setSerial_return(ArrayList<Serial_Save_Return> serial_return) {
        this.serial_return = serial_return;
    }

    public class Serial_Save_Return{
        private long customer_code;
        private long product_code;
        private int serial_code;
        private String ret_status;
        private String ret_msg;

        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public long getProduct_code() {
            return product_code;
        }

        public void setProduct_code(long product_code) {
            this.product_code = product_code;
        }

        public int getSerial_code() {
            return serial_code;
        }

        public void setSerial_code(int serial_code) {
            this.serial_code = serial_code;
        }

        public String getRet_status() {
            return ret_status;
        }

        public void setRet_status(String ret_status) {
            this.ret_status = ret_status;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }
    }

    public class So_Save_Return{
        @Expose
        private long customer_code;
        @Expose
        private int so_prefix;
        @Expose
        private int so_code;
        @Expose
        private int so_scn;
        @Expose
        private int so_update;
        @Expose
        private String ret_status;
        @Expose
        private String ret_msg;

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

        public int getSo_scn() {
            return so_scn;
        }

        public void setSo_scn(int so_scn) {
            this.so_scn = so_scn;
        }

        public int getSo_update() {
            return so_update;
        }

        public void setSo_update(int so_update) {
            this.so_update = so_update;
        }

        public String getRet_status() {
            return ret_status;
        }

        public void setRet_status(String ret_status) {
            this.ret_status = ret_status;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }
    }

    public class So_From_To{
        @Expose
        private ArrayList<SM_SO_Service_Exec_Task> task;
        @Expose
        private ArrayList<SM_SO_Service_Exec_Task_File> task_file;

        public ArrayList<SM_SO_Service_Exec_Task> getTask() {
            return task;
        }

        public void setTask(ArrayList<SM_SO_Service_Exec_Task> task) {
            this.task = task;
        }

        public ArrayList<SM_SO_Service_Exec_Task_File> getTask_file() {
            return task_file;
        }

        public void setTask_file(ArrayList<SM_SO_Service_Exec_Task_File> task_file) {
            this.task_file = task_file;
        }
    }
}
