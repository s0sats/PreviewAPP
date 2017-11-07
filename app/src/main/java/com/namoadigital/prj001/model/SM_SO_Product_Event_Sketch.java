package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_Event_Sketch {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    private int seq; //pk - server
    private int seq_tmp; //pk
    //
    @Expose
    private int line;
    @Expose
    private int col;

    public SM_SO_Product_Event_Sketch() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.seq = -1;
    }

    public void setPK(SM_SO_Product_Event productEvent) {
        this.customer_code = productEvent.getCustomer_code();
        this.so_prefix = productEvent.getSo_prefix();
        this.so_code = productEvent.getSo_code();
        this.seq = productEvent.getSeq();
        this.seq_tmp = productEvent.getSeq_tmp();
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq_tmp() {
        return seq_tmp;
    }

    public void setSeq_tmp(int seq_tmp) {
        this.seq_tmp = seq_tmp;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
