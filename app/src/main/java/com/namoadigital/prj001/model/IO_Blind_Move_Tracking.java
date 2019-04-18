package com.namoadigital.prj001.model;

public class IO_Blind_Move_Tracking {

    private long customer_code;
    private int blind_tmp;
    private String tracking;

    public IO_Blind_Move_Tracking() {
        this.customer_code = -1;
        this.blind_tmp = -1;
        this.tracking = "";
    }

    public void setPk(IO_Blind_Move io_blind_move){
        this.customer_code = io_blind_move.getCustomer_code();
        this.blind_tmp = io_blind_move.getBlind_tmp();
        this.tracking = "";
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getBlind_tmp() {
        return blind_tmp;
    }

    public void setBlind_tmp(int blind_tmp) {
        this.blind_tmp = blind_tmp;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
