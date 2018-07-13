package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 23/02/2018.
 */

public class GE_Custom_Form_Ap {

    @Expose
    private long customer_code;
    @Expose
    private int custom_form_type;
    private String custom_form_type_desc;
    @Expose
    private int custom_form_code;
    @Expose
    private int custom_form_version;
    private String custom_form_desc;
    @Expose
    private long custom_form_data;
    @Expose
    private Integer ap_code;
    @Expose
    private String ap_description;
    @Expose
    private String ap_status;
    @Expose
    private String ap_comments;
    @Expose
    private String ap_what;
    @Expose
    private String ap_where;
    @Expose
    private String ap_why;
    @Expose
    private Integer ap_who;
    private String ap_who_nick;
    @Expose
    private String ap_how;
    @Expose
    private String ap_how_much;
    @Expose
    private String ap_when;
    @Expose
    private Integer department_code;
    private String department_id;
    private String department_desc;
    private String room_code;
    private int ap_scn;
    private int product_code;
    private String product_id;
    private String product_desc;
    private Integer serial_code;
    private String serial_id;
    private int sync_required;
    private int upload_required;
    private String create_date;
    private String create_user;

    @Expose(serialize = false)
    private String custom_form_url;

    private transient String custom_form_url_local;
    private String last_update;

    public GE_Custom_Form_Ap() {
        this.custom_form_url_local = "";
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(int custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public String getCustom_form_type_desc() {
        return custom_form_type_desc;
    }

    public void setCustom_form_type_desc(String custom_form_type_desc) {
        this.custom_form_type_desc = custom_form_type_desc;
    }

    public int getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(int custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public int getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(int custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public String getCustom_form_desc() {
        return custom_form_desc;
    }

    public void setCustom_form_desc(String custom_form_desc) {
        this.custom_form_desc = custom_form_desc;
    }

    public long getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(long custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    public Integer getAp_code() {
        return ap_code;
    }

    public void setAp_code(Integer ap_code) {
        this.ap_code = ap_code;
    }

    public String getAp_description() {
        return ap_description;
    }

    public void setAp_description(String ap_description) {
        this.ap_description = ap_description;
    }

    public String getAp_status() {
        return ap_status;
    }

    public void setAp_status(String ap_status) {
        this.ap_status = ap_status;
    }

    public String getAp_comments() {
        return ap_comments;
    }

    public void setAp_comments(String ap_comments) {
        this.ap_comments = ap_comments;
    }

    public String getAp_what() {
        return ap_what;
    }

    public void setAp_what(String ap_what) {
        this.ap_what = ap_what;
    }

    public String getAp_where() {
        return ap_where;
    }

    public void setAp_where(String ap_where) {
        this.ap_where = ap_where;
    }

    public String getAp_why() {
        return ap_why;
    }

    public void setAp_why(String ap_why) {
        this.ap_why = ap_why;
    }

    public Integer getAp_who() {
        return ap_who;
    }

    public void setAp_who(Integer ap_who) {
        this.ap_who = ap_who;
    }

    public String getAp_who_nick() {
        return ap_who_nick;
    }

    public void setAp_who_nick(String ap_who_nick) {
        this.ap_who_nick = ap_who_nick;
    }

    public String getAp_how() {
        return ap_how;
    }

    public void setAp_how(String ap_how) {
        this.ap_how = ap_how;
    }

    public String getAp_how_much() {
        return ap_how_much;
    }

    public void setAp_how_much(String ap_how_much) {
        this.ap_how_much = ap_how_much;
    }

    public String getAp_when() {
        return ap_when;
    }

    public void setAp_when(String ap_when) {
        this.ap_when = ap_when;
    }

    public Integer getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(Integer department_code) {
        this.department_code = department_code;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_desc() {
        return department_desc;
    }

    public void setDepartment_desc(String department_desc) {
        this.department_desc = department_desc;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public int getAp_scn() {
        return ap_scn;
    }

    public void setAp_scn(int ap_scn) {
        this.ap_scn = ap_scn;
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

    public Integer getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(Integer serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public int getUpload_required() {
        return upload_required;
    }

    public void setUpload_required(int upload_required) {
        this.upload_required = upload_required;
    }

    public String getCustom_form_url() {
        return custom_form_url;
    }

    public void setCustom_form_url(String custom_form_url) {
        this.custom_form_url = custom_form_url;
    }

    public String getCustom_form_url_local() {
        return custom_form_url_local;
    }

    public void setCustom_form_url_local(String custom_form_url_local) {
        this.custom_form_url_local = custom_form_url_local;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }
}
