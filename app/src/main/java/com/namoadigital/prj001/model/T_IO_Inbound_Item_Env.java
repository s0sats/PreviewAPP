package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class T_IO_Inbound_Item_Env extends Main_Header_Env {

    @Expose
    @SerializedName("token") private String token;
    @Expose
    @SerializedName("inbound") private ArrayList<IO_Inbound_Header> inbound = new ArrayList<>();
    @Expose
    @SerializedName("reprocess") private int reprocess;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<IO_Inbound_Header> getInbound() {
        return inbound;
    }

    public void setInbound(ArrayList<IO_Inbound_Header> inbound) {
        this.inbound = inbound;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }

    public static class IO_Inbound_Header {
        @Expose
        @SerializedName("customer_code") private long customer_code;
        @Expose
        @SerializedName("inbound_prefix") private int inbound_prefix;
        @Expose
        @SerializedName("inbound_code") private int inbound_code;
        @Expose
        @SerializedName("scn") private int scn;
        @Expose
        @SerializedName("items") private ArrayList<IO_Inbound_Item> items = new ArrayList<>();
        @Expose
        @SerializedName("move") private ArrayList<IO_Move> move = new ArrayList<>();
        //
        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public int getInbound_prefix() {
            return inbound_prefix;
        }

        public void setInbound_prefix(int inbound_prefix) {
            this.inbound_prefix = inbound_prefix;
        }

        public int getInbound_code() {
            return inbound_code;
        }

        public void setInbound_code(int inbound_code) {
            this.inbound_code = inbound_code;
        }

        public int getScn() {
            return scn;
        }

        public void setScn(int scn) {
            this.scn = scn;
        }

        public ArrayList<IO_Inbound_Item> getItems() {
            return items;
        }

        public void setItems(ArrayList<IO_Inbound_Item> items) {
            this.items = items;
        }

        public ArrayList<IO_Move> getMove() {
            return move;
        }

        public void setMove(ArrayList<IO_Move> move) {
            this.move = move;
        }
    }
}
