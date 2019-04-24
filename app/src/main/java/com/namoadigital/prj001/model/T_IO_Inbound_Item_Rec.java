package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Inbound_Item_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String save;
    private ArrayList<IO_Inbound_Item_Save_Return> result = new ArrayList<>();
    private ArrayList<IO_Inbound> inbound = new ArrayList<>();
    private ArrayList<IO_Move> move = new ArrayList<>();

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

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public ArrayList<IO_Inbound_Item_Save_Return> getResult() {
        return result;
    }

    public void setResult(ArrayList<IO_Inbound_Item_Save_Return> result) {
        this.result = result;
    }

    public ArrayList<IO_Inbound> getInbound() {
        return inbound;
    }

    public void setInbound(ArrayList<IO_Inbound> inbound) {
        this.inbound = inbound;
    }

    public ArrayList<IO_Move> getMove() {
        return move;
    }

    public void setMove(ArrayList<IO_Move> move) {
        this.move = move;
    }

    public class  IO_Inbound_Item_Save_Return{
        private long customer_code;
        private int inbound_prefix;
        private int inbound_code;
        private int scn;
        private String ret_status;
        private String ret_msg;
        private ArrayList<IO_Inbound_Item_Save_Return_Item> items = new ArrayList<>();

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

        public ArrayList<IO_Inbound_Item_Save_Return_Item> getItems() {
            return items;
        }

        public void setItems(ArrayList<IO_Inbound_Item_Save_Return_Item> items) {
            this.items = items;
        }
    }

    public class  IO_Inbound_Item_Save_Return_Item{
        private Integer inbound_item;
        private Integer move_prefix;
        private Integer move_code;
        private String ret_status;
        private String ret_msg;
        private ArrayList<MD_Product_Serial> serial = new ArrayList<>();
        //
        public Integer getInbound_item() {
            return inbound_item;
        }

        public void setInbound_item(Integer inbound_item) {
            this.inbound_item = inbound_item;
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
    }
}
