package com.namoadigital.prj001.model;

import java.util.ArrayList;

class IO_Move_Return {

    private long customer_code;
    private int move_prefix;
    private int move_code;
    private String ret_status;
    private String ret_msg;
    private ArrayList<MD_Product_Serial> record;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getMove_prefix() {
        return move_prefix;
    }

    public void setMove_prefix(int move_prefix) {
        this.move_prefix = move_prefix;
    }

    public int getMove_code() {
        return move_code;
    }

    public void setMove_code(int move_code) {
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

    public ArrayList<MD_Product_Serial> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<MD_Product_Serial> record) {
        this.record = record;
    }
}
