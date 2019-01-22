package com.namoadigital.prj001.model;

import com.namoadigital.prj001.util.Constant;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/07/2017.
 */

public class TSO_Approval_Env extends Main_Header_Env{

    private String token;

    private ArrayList<SO_Approval_Item> so_status;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<SO_Approval_Item> getSo_status() {
        return so_status;
    }

    public void setSo_status(ArrayList<SO_Approval_Item> so_status) {
        this.so_status = so_status;
    }

    public TSO_Approval_Env() {
    }

    public static class SO_Approval_Item {
        private long customer_code;
        private int so_prefix;
        private int so_code;
        private int so_scn;
        private String action;//HJ só approve_client, no futuro dependerá de qual status chamou.
        private String origin_change;//

        // Quality Approval
        private String quality_user;
        private String quality_date;

        // Final Approval
        private String client_name;// nome do cliente
        private String client_date;//: data da aprovação
        private String client_image;//: nome da imagem que subiu no s3 (assinatura)
        private String client_type_sig;//: client ou user (apenas quando o client_type = client)
        private Integer client_user;//

        public SO_Approval_Item() {
            //this.action = Constant.PROFILE_MENU_SO_PARAM_APPROVE_CLIENT;
            this.origin_change = Constant.SO_ORIGIN_CHANGE_APP;
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

        public int getSo_scn() {
            return so_scn;
        }

        public void setSo_scn(int so_scn) {
            this.so_scn = so_scn;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getOrigin_change() {
            return origin_change;
        }

        public void setOrigin_change(String origin_change) {
            this.origin_change = origin_change;
        }

        public String getQuality_user() {
            return quality_user;
        }

        public void setQuality_user(String quality_user) {
            this.quality_user = quality_user;
        }

        public String getQuality_date() {
            return quality_date;
        }

        public void setQuality_date(String quality_date) {
            this.quality_date = quality_date;
        }

        public String getClient_name() {
            return client_name;
        }

        public void setClient_name(String client_name) {
            this.client_name = client_name;
        }

        public String getClient_date() {
            return client_date;
        }

        public void setClient_date(String client_date) {
            this.client_date = client_date;
        }

        public String getClient_image() {
            return client_image;
        }

        public void setClient_image(String client_image) {
            this.client_image = client_image;
        }

        public String getClient_type_sig() {
            return client_type_sig;
        }

        public void setClient_type_sig(String client_type_sig) {
            this.client_type_sig = client_type_sig;
        }

        public Integer getClient_user() {
            return client_user;
        }

        public void setClient_user(Integer client_user) {
            this.client_user = client_user;
        }
    }
}
