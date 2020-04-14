package com.namoadigital.prj001.model;

/**
 * LUCHE - 13/04/2020
 *
 * USADO SOMENTE NO PROCESSAMENTO DO AGENDAMENTO NÃO POSSUI TABLE, É UMA LISTA TMP
 */

public class MD_Schedule_Exec_Product {
    private long customer_code;
    private int product_code;
    private String product_id;
    private String product_desc;
    private int require_serial;
    private int allow_new_serial_cl;
    private int local_control;
    private int io_control;
    private String serial_rule;
    private Integer serial_min_length;
    private Integer serial_max_length;
    private int site_restriction;
    private String product_icon_name;
    private String product_icon_url;
    private String product_icon_url_local;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getRequire_serial() {
        return require_serial;
    }

    public void setRequire_serial(int require_serial) {
        this.require_serial = require_serial;
    }

    public int getAllow_new_serial_cl() {
        return allow_new_serial_cl;
    }

    public void setAllow_new_serial_cl(int allow_new_serial_cl) {
        this.allow_new_serial_cl = allow_new_serial_cl;
    }

    public int getLocal_control() {
        return local_control;
    }

    public void setLocal_control(int local_control) {
        this.local_control = local_control;
    }

    public int getIo_control() {
        return io_control;
    }

    public void setIo_control(int io_control) {
        this.io_control = io_control;
    }

    public String getSerial_rule() {
        return serial_rule;
    }

    public void setSerial_rule(String serial_rule) {
        this.serial_rule = serial_rule;
    }

    public Integer getSerial_min_length() {
        return serial_min_length;
    }

    public void setSerial_min_length(Integer serial_min_length) {
        this.serial_min_length = serial_min_length;
    }

    public Integer getSerial_max_length() {
        return serial_max_length;
    }

    public void setSerial_max_length(Integer serial_max_length) {
        this.serial_max_length = serial_max_length;
    }

    public int getSite_restriction() {
        return site_restriction;
    }

    public void setSite_restriction(int site_restriction) {
        this.site_restriction = site_restriction;
    }

    public String getProduct_icon_name() {
        return product_icon_name;
    }

    public void setProduct_icon_name(String product_icon_name) {
        this.product_icon_name = product_icon_name;
    }

    public String getProduct_icon_url() {
        return product_icon_url;
    }

    public void setProduct_icon_url(String product_icon_url) {
        this.product_icon_url = product_icon_url;
    }

    public String getProduct_icon_url_local() {
        return product_icon_url_local;
    }

    public void setProduct_icon_url_local(String product_icon_url_local) {
        this.product_icon_url_local = product_icon_url_local;
    }
}
