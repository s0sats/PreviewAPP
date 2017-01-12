package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Env {

    private long user_code;
    private String customer_code;
    private int translate_code;
    private String date_db_customer;
    private String app_code;
    private String app_version;
    private String date_db; // Data do Usuário
    private String email_p;
    private String password;
    private String nfc_code;
    private String device_code;
    private String manufacturer;
    private String model;
    private String os;
    private String version_os;
    private String force_login;

    public long getUser_code() {
        return user_code;
    }

    public void setUser_code(long user_code) {
        this.user_code = user_code;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public int getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(int translate_code) {
        this.translate_code = translate_code;
    }

    public String getDate_db_customer() {
        return date_db_customer;
    }

    public void setDate_db_customer(String date_db_customer) {
        this.date_db_customer = date_db_customer;
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

    public String getDate_db() {
        return date_db;
    }

    public void setDate_db(String date_db) {
        this.date_db = date_db;
    }

    public String getEmail_p() {
        return email_p;
    }

    public void setEmail_p(String email_p) {
        this.email_p = email_p;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNfc_code() {
        return nfc_code;
    }

    public void setNfc_code(String nfc_code) {
        this.nfc_code = nfc_code;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion_os() {
        return version_os;
    }

    public void setVersion_os(String version_os) {
        this.version_os = version_os;
    }

    public String getForce_login() {
        return force_login;
    }

    public void setForce_login(String force_login) {
        this.force_login = force_login;
    }
}
