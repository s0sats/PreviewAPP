package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_SO_Service {

    private long customer_code;
    private long so_prefix;
    private long so_code;
    private int so_scn;
    private ArrayList<TSO_SO_Service_Item> pack;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(long so_prefix) {
        this.so_prefix = so_prefix;
    }

    public long getSo_code() {
        return so_code;
    }

    public void setSo_code(long so_code) {
        this.so_code = so_code;
    }

    public int getSo_scn() {
        return so_scn;
    }

    public void setSo_scn(int so_scn) {
        this.so_scn = so_scn;
    }

    public ArrayList<TSO_SO_Service_Item> getPack() {
        return pack;
    }

    public void setPack(ArrayList<TSO_SO_Service_Item> pack) {
        this.pack = pack;
    }
}
