package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 27/07/2017.
 */

public class TSO_Approval_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int so_scn;
    private String action;//HJ só approve_client, no futuro dependerá de qual status chamou.
    private String origin_change;//
    private String client_date;//: data da aprovação
    private String client_image;//: nome da imagem que subiu no s3 (assinatura)
    private String client_type_sig;//: client ou user (apenas quando o client_type = client)
    private String client_password;//: senha do cliente (aplicar md5)
    private String client_nfc;//: código nfc do cliente

    public TSO_Approval_Env() {
        this.action = "approve_client";
        this.origin_change = "APP";
    }

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
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

    public String getClient_password() {
        return client_password;
    }

    public void setClient_password(String client_password) {
        this.client_password = client_password;
    }

    public String getClient_nfc() {
        return client_nfc;
    }

    public void setClient_nfc(String client_nfc) {
        this.client_nfc = client_nfc;
    }
}
