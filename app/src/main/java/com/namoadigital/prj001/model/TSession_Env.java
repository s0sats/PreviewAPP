package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 20/01/2017.
 * Classe com propriedade que serão enviadas para o
 * servidor na chamada do WS server_get_session.
 */
public class TSession_Env extends Main_Header_Env {

    private String email_p;
    private String password;
    private String nfc_code;
    private String device_code;
    private String manufacturer;
    private String model;
    private String version_os;
    private String os;
    private String force_login;
    private String customer_code;
    private int translate_code;
    private String gcm_id = "";

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

    public String getVersion_os() {
        return version_os;
    }

    public void setVersion_os(String version_os) {
        this.version_os = version_os;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getForce_login() {
        return force_login;
    }

    public void setForce_login(String force_login) {
        this.force_login = force_login;
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

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }
}
