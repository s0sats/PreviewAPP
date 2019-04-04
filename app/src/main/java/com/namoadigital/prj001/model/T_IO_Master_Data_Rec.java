package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Master_Data_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<MD_Site> site = new ArrayList<>();
    private ArrayList<MD_Partner> partner = new ArrayList<>();
    private ArrayList<ModalObj> modal = new ArrayList<>();

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

    public ArrayList<MD_Site> getSite() {
        return site;
    }

    public void setSite(ArrayList<MD_Site> site) {
        this.site = site;
    }

    public ArrayList<MD_Partner> getPartner() {
        return partner;
    }

    public void setPartner(ArrayList<MD_Partner> partner) {
        this.partner = partner;
    }

    public ArrayList<ModalObj> getModal() {
        return modal;
    }

    public void setModal(ArrayList<ModalObj> modal) {
        this.modal = modal;
    }

    public class ModalObj{
        private long customer_code;
        private int modal_code;
        private String modal_id;
        private String modal_desc;

        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public int getModal_code() {
            return modal_code;
        }

        public void setModal_code(int modal_code) {
            this.modal_code = modal_code;
        }

        public String getModal_id() {
            return modal_id;
        }

        public void setModal_id(String modal_id) {
            this.modal_id = modal_id;
        }

        public String getModal_desc() {
            return modal_desc;
        }

        public void setModal_desc(String modal_desc) {
            this.modal_desc = modal_desc;
        }
    }
}
