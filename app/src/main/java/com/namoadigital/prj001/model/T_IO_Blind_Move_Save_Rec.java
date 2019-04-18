package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Blind_Move_Save_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<IO_Blind_Move_Return> result = new ArrayList<>();

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

    public ArrayList<IO_Blind_Move_Return> getResult() {
        return result;
    }

    public void setResult(ArrayList<IO_Blind_Move_Return> result) {
        this.result = result;
    }

    public class IO_Blind_Move_Return {
         private long customer_code;
         private Integer blind_tmp;
         private Integer blind_prefix;
         private Integer blind_code;
         private Integer move_prefix;
         private Integer move_code;
         private String ret_status;
         private String ret_msg;
         private ArrayList<MD_Product_Serial> serial = new ArrayList<>();
         private ArrayList<IO_Move> move = new ArrayList<>();

        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public Integer getBlind_tmp() {
            return blind_tmp;
        }

        public void setBlind_tmp(Integer blind_tmp) {
            this.blind_tmp = blind_tmp;
        }

        public Integer getBlind_prefix() {
            return blind_prefix;
        }

        public void setBlind_prefix(Integer blind_prefix) {
            this.blind_prefix = blind_prefix;
        }

        public Integer getBlind_code() {
            return blind_code;
        }

        public void setBlind_code(Integer blind_code) {
            this.blind_code = blind_code;
        }

        public Integer getMove_prefix() {
            return move_prefix;
        }

        public void setMove_prefix(Integer move_prefix) {
            this.move_prefix = move_prefix;
        }

        public Integer getMove_code() {
            return move_code;
        }

        public void setMove_code(Integer move_code) {
            this.move_code = move_code;
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

        public ArrayList<MD_Product_Serial> getSerial() {
            return serial;
        }

        public void setSerial(ArrayList<MD_Product_Serial> serial) {
            this.serial = serial;
        }

        public ArrayList<IO_Move> getMove() {
            return move;
        }

        public void setMove(ArrayList<IO_Move> move) {
            this.move = move;
        }
    }

}
