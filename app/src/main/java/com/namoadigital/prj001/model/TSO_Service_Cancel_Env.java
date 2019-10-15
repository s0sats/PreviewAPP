package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 15/10/2019.
 */

public class TSO_Service_Cancel_Env extends Main_Header_Env {

    private String so_prefix;
    private String so_code;
    private String type_ps;
    private String price_list_code;
    private String pack_code;
    private String pack_seq;
    private String category_price_code;
    private String service_code;
    private String service_seq;
    private String exec_code;
    private String token;

    public String getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(String so_prefix) {
        this.so_prefix = so_prefix;
    }

    public String getSo_code() {
        return so_code;
    }

    public void setSo_code(String so_code) {
        this.so_code = so_code;
    }

    public String getType_ps() {
        return type_ps;
    }

    public void setType_ps(String type_ps) {
        this.type_ps = type_ps;
    }

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }

    public String getPack_code() {
        return pack_code;
    }

    public void setPack_code(String pack_code) {
        this.pack_code = pack_code;
    }

    public String getPack_seq() {
        return pack_seq;
    }

    public void setPack_seq(String pack_seq) {
        this.pack_seq = pack_seq;
    }

    public String getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(String category_price_code) {
        this.category_price_code = category_price_code;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getService_seq() {
        return service_seq;
    }

    public void setService_seq(String service_seq) {
        this.service_seq = service_seq;
    }

    public String getExec_code() {
        return exec_code;
    }

    public void setExec_code(String exec_code) {
        this.exec_code = exec_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
