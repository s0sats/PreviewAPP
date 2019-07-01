package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class T_IO_Outbound_Item_Env extends Main_Header_Env{

    @Expose
    private String token;
    @Expose
    private ArrayList<IO_Outbound_Header> outbound = new ArrayList<>();
    @Expose
    private int reprocess;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<IO_Outbound_Header> getOutbound() {
        return outbound;
    }

    public void setOutbound(ArrayList<IO_Outbound_Header> outbound) {
        this.outbound = outbound;
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
        private int outbound_prefix;
        @Expose
        private int outbound_code;
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
            return outbound_prefix;
        }

        public void setOutbound_prefix(int outbound_prefix) {
            this.outbound_prefix = outbound_prefix;
        }

        public int getOutbound_code() {
            return outbound_code;
        }

        public void setOutbound_code(int outbound_code) {
            this.outbound_code = outbound_code;
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
