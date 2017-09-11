package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 13/06/2017.
 */

public class SM_SO {

    @Expose
    private long customer_code; //pk
    @Expose
    private int so_prefix; //pk
    @Expose
    private int so_code; //pk
    @Expose
    private String so_id;
    @Expose
    private int so_scn;
    @Expose
    private String so_desc;
    @Expose
    private int product_code;
    private String product_id;
    private String product_desc;
    @Expose
    private int serial_code;
    @Expose
    private String serial_id;
    @Expose
    private int category_price_code;
    private String category_price_id;
    private String category_price_desc;
    @Expose
    private int segment_code;
    private String segment_id;
    private String segment_desc;
    @Expose
    private int site_code;
    private String site_id;
    private String site_desc;
    @Expose
    private int operation_code;
    private String operation_id;
    private String operation_desc;
    @Expose
    private int contract_code;
    private String contract_desc;
    private String contract_po_erp;
    private String contract_po_client1;
    private String contract_po_client2;
    @Expose
    private int priority_code;
    private String priority_desc;
    private String status;
    private Integer quality_approval_user;
    private String quality_approval_user_nick;
    private String quality_approval_date;
    @Expose
    private String comments;
    @Expose
    private Integer so_father_prefix;
    @Expose
    private Integer so_father_code;
    @Expose
    private String deadline;
    @Expose
    private String origin;
    @Expose
    private String client_type;
    @Expose
    private Integer client_user;
    @Expose
    private Integer client_code;
    @Expose
    private String client_id;
    @Expose
    private String client_name;
    @Expose
    private String client_email;
    @Expose
    private String client_phone;
    private Integer client_approval_image;
    private String client_approval_image_name;
    private String client_approval_image_url;
    private String client_approval_date;
    private String client_approval_user_nick;
    private Integer client_approval_user;
    private String client_approval_type_sig;
    @Expose
    private String origin_change;
    private int started_flag;
    @Expose
    private String edit_origin;
    @Expose
    private Integer edit_user;
    private String edit_user_nick;
    private int total_qty_service;
    private double total_price;
    @Expose
    private String add_inf1;
    @Expose
    private String add_inf2;
    @Expose
    private String add_inf3;
    private int approve_budget;
    private int approve_client;
    private int update_required;
    private int approval_required;

    @Expose
    private String action;
    private String token;
    @Expose
    private ArrayList<SM_SO_File> so_file = new ArrayList<>();
    @Expose
    private ArrayList<SM_SO_Pack> pack = new ArrayList<>();

    public void setPK() {
        for (int i = 0; i < so_file.size(); i++) {
            so_file.get(i).setPK(this);
        }
        //
        for (int i = 0; i < pack.size(); i++) {
            pack.get(i).setPK(this);
        }
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

    public String getSo_id() {
        return so_id;
    }

    public void setSo_id(String so_id) {
        this.so_id = so_id;
    }

    public int getSo_scn() {
        return so_scn;
    }

    public void setSo_scn(int so_scn) {
        this.so_scn = so_scn;
    }

    public String getSo_desc() {
        return so_desc;
    }

    public void setSo_desc(String so_desc) {
        this.so_desc = so_desc;
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

    public int getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(int serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public String getCategory_price_id() {
        return category_price_id;
    }

    public void setCategory_price_id(String category_price_id) {
        this.category_price_id = category_price_id;
    }

    public String getCategory_price_desc() {
        return category_price_desc;
    }

    public void setCategory_price_desc(String category_price_desc) {
        this.category_price_desc = category_price_desc;
    }

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
    }

    public String getSegment_id() {
        return segment_id;
    }

    public void setSegment_id(String segment_id) {
        this.segment_id = segment_id;
    }

    public String getSegment_desc() {
        return segment_desc;
    }

    public void setSegment_desc(String segment_desc) {
        this.segment_desc = segment_desc;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
        this.operation_code = operation_code;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public String getOperation_desc() {
        return operation_desc;
    }

    public void setOperation_desc(String operation_desc) {
        this.operation_desc = operation_desc;
    }

    public int getContract_code() {
        return contract_code;
    }

    public void setContract_code(int contract_code) {
        this.contract_code = contract_code;
    }

    public String getContract_desc() {
        return contract_desc;
    }

    public void setContract_desc(String contract_desc) {
        this.contract_desc = contract_desc;
    }

    public String getContract_po_erp() {
        return contract_po_erp;
    }

    public void setContract_po_erp(String contract_po_erp) {
        this.contract_po_erp = contract_po_erp;
    }

    public String getContract_po_client1() {
        return contract_po_client1;
    }

    public void setContract_po_client1(String contract_po_client1) {
        this.contract_po_client1 = contract_po_client1;
    }

    public String getContract_po_client2() {
        return contract_po_client2;
    }

    public void setContract_po_client2(String contract_po_client2) {
        this.contract_po_client2 = contract_po_client2;
    }

    public int getPriority_code() {
        return priority_code;
    }

    public void setPriority_code(int priority_code) {
        this.priority_code = priority_code;
    }

    public String getPriority_desc() {
        return priority_desc;
    }

    public void setPriority_desc(String priority_desc) {
        this.priority_desc = priority_desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuality_approval_user() {
        return quality_approval_user;
    }

    public void setQuality_approval_user(Integer quality_approval_user) {
        this.quality_approval_user = quality_approval_user;
    }

    public String getQuality_approval_user_nick() {
        return quality_approval_user_nick;
    }

    public void setQuality_approval_user_nick(String quality_approval_user_nick) {
        this.quality_approval_user_nick = quality_approval_user_nick;
    }

    public String getQuality_approval_date() {
        return quality_approval_date;
    }

    public void setQuality_approval_date(String quality_approval_date) {
        this.quality_approval_date = quality_approval_date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getSo_father_prefix() {
        return so_father_prefix;
    }

    public void setSo_father_prefix(Integer so_father_prefix) {
        this.so_father_prefix = so_father_prefix;
    }

    public Integer getSo_father_code() {
        return so_father_code;
    }

    public void setSo_father_code(Integer so_father_code) {
        this.so_father_code = so_father_code;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public Integer getClient_user() {
        return client_user;
    }

    public void setClient_user(Integer client_user) {
        this.client_user = client_user;
    }

    public Integer getClient_code() {
        return client_code;
    }

    public void setClient_code(Integer client_code) {
        this.client_code = client_code;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }

    public Integer getClient_approval_image() {
        return client_approval_image;
    }

    public void setClient_approval_image(Integer client_approval_image) {
        this.client_approval_image = client_approval_image;
    }

    public String getClient_approval_image_name() {
        return client_approval_image_name;
    }

    public void setClient_approval_image_name(String client_approval_image_name) {
        this.client_approval_image_name = client_approval_image_name;
    }

    public String getClient_approval_image_url() {
        return client_approval_image_url;
    }

    public void setClient_approval_image_url(String client_approval_image_url) {
        this.client_approval_image_url = client_approval_image_url;
    }

    public String getClient_approval_date() {
        return client_approval_date;
    }

    public void setClient_approval_date(String client_approval_date) {
        this.client_approval_date = client_approval_date;
    }

    public String getClient_approval_user_nick() {
        return client_approval_user_nick;
    }

    public void setClient_approval_user_nick(String client_approval_user_nick) {
        this.client_approval_user_nick = client_approval_user_nick;
    }

    public Integer getClient_approval_user() {
        return client_approval_user;
    }

    public void setClient_approval_user(Integer client_approval_user) {
        this.client_approval_user = client_approval_user;
    }

    public String getClient_approval_type_sig() {
        return client_approval_type_sig;
    }

    public void setClient_approval_type_sig(String client_approval_type_sig) {
        this.client_approval_type_sig = client_approval_type_sig;
    }

    public String getOrigin_change() {
        return origin_change;
    }

    public void setOrigin_change(String origin_change) {
        this.origin_change = origin_change;
    }

    public int getStarted_flag() {
        return started_flag;
    }

    public void setStarted_flag(int started_flag) {
        this.started_flag = started_flag;
    }

    public String getEdit_origin() {
        return edit_origin;
    }

    public void setEdit_origin(String edit_origin) {
        this.edit_origin = edit_origin;
    }

    public Integer getEdit_user() {
        return edit_user;
    }

    public void setEdit_user(Integer edit_user) {
        this.edit_user = edit_user;
    }

    public String getEdit_user_nick() {
        return edit_user_nick;
    }

    public void setEdit_user_nick(String edit_user_nick) {
        this.edit_user_nick = edit_user_nick;
    }

    public int getTotal_qty_service() {
        return total_qty_service;
    }

    public void setTotal_qty_service(int total_qty_service) {
        this.total_qty_service = total_qty_service;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getAdd_inf1() {
        return add_inf1;
    }

    public void setAdd_inf1(String add_inf1) {
        this.add_inf1 = add_inf1;
    }

    public String getAdd_inf2() {
        return add_inf2;
    }

    public void setAdd_inf2(String add_inf2) {
        this.add_inf2 = add_inf2;
    }

    public String getAdd_inf3() {
        return add_inf3;
    }

    public void setAdd_inf3(String add_inf3) {
        this.add_inf3 = add_inf3;
    }

    public int getApprove_budget() {
        return approve_budget;
    }

    public void setApprove_budget(int approve_budget) {
        this.approve_budget = approve_budget;
    }

    public int getApprove_client() {
        return approve_client;
    }

    public void setApprove_client(int approve_client) {
        this.approve_client = approve_client;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getApproval_required() {
        return approval_required;
    }

    public void setApproval_required(int approval_required) {
        this.approval_required = approval_required;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<SM_SO_File> getSo_file() {
        return so_file;
    }

    public void setSo_file(ArrayList<SM_SO_File> so_file) {
        this.so_file = so_file;
    }

    public ArrayList<SM_SO_Pack> getPack() {
        return pack;
    }

    public void setPack(ArrayList<SM_SO_Pack> pack) {
        this.pack = pack;
    }
}
