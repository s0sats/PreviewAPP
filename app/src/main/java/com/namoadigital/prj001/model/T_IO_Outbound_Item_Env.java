package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class T_IO_Outbound_Item_Env extends Main_Header_Env{

    @Expose
    private String token;
    @Expose
    private ArrayList<IO_Outbound_Header> inbound = new ArrayList<>();
    @Expose
    private int reprocess;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<IO_Outbound_Header> getOutbound() {
        return inbound;
    }

    public void setOutbound(ArrayList<IO_Outbound_Header> inbound) {
        this.inbound = inbound;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }

    public static class IO_Outbound_Header {
        @Expose
        private long customer_code;
        @Expose
        private int inbound_prefix;
        @Expose
        private int inbound_code;
        @Expose
        private int scn;
        @Expose
        private ArrayList<IO_Outbound_Item> items = new ArrayList<>();
        @Expose
        private ArrayList<IO_Move> move = new ArrayList<>();
        //
        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public int getOutbound_prefix() {
            return inbound_prefix;
        }

        public void setOutbound_prefix(int inbound_prefix) {
            this.inbound_prefix = inbound_prefix;
        }

        public int getOutbound_code() {
            return inbound_code;
        }

        public void setOutbound_code(int inbound_code) {
            this.inbound_code = inbound_code;
        }

        public int getScn() {
            return scn;
        }

        public void setScn(int scn) {
            this.scn = scn;
        }

        public ArrayList<IO_Outbound_Item> getItems() {
            return items;
        }

        public void setItems(ArrayList<IO_Outbound_Item> items) {
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
