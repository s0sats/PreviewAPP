package com.namoadigital.prj001.model;

import java.io.Serializable;

/**
 *
 * LUCHE - 04/02/2019
 *
 * Objeto json esperado pelo server no enviao da criação da O.S
 *
 *
 */

public class SO_Creation_Obj implements Serializable {
    private static final long serialVersionUID = -5246847163545343896L;

    private long customer_code;
    private Integer so_prefix;
    private Integer so_code;
    private Integer so_desc;
    private Integer so_scn;
    private long product_code;
    private long serial_code;
    private String serial_id;
    private int category_price_code;
    private int segment_code;
    private int site_code;
    private int operation_code;
    private int contract_code;
    private Integer priority_code;
    private Integer pipeline_code;
    private int deadline_manual;
    private String deadline;
    private String origin;
    private String origin_change;
    private String client_type;
    private String pack_default;
    private Integer client_code;
    private String client_id;
    private String client_name;
    private String client_email;
    private String client_phone;
    private String action;
    private String so_id;
    private String add_inf1;
    private String add_inf2;
    private String add_inf3;
    private int edit_user;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public Integer getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(Integer so_prefix) {
        this.so_prefix = so_prefix;
    }

    public Integer getSo_code() {
        return so_code;
    }

    public void setSo_code(Integer so_code) {
        this.so_code = so_code;
    }

    public Integer getSo_desc() {
        return so_desc;
    }

    public void setSo_desc(Integer so_desc) {
        this.so_desc = so_desc;
    }

    public Integer getSo_scn() {
        return so_scn;
    }

    public void setSo_scn(Integer so_scn) {
        this.so_scn = so_scn;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public long getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(long serial_code) {
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

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
        this.operation_code = operation_code;
    }

    public int getContract_code() {
        return contract_code;
    }

    public void setContract_code(int contract_code) {
        this.contract_code = contract_code;
    }

    public Integer getPriority_code() {
        return priority_code;
    }

    public void setPriority_code(Integer priority_code) {
        this.priority_code = priority_code;
    }

    public Integer getPipeline_code() {
        return pipeline_code;
    }

    public void setPipeline_code(Integer pipeline_code) {
        this.pipeline_code = pipeline_code;
    }

    public int getDeadline_manual() {
        return deadline_manual;
    }

    public void setDeadline_manual(int deadline_manual) {
        this.deadline_manual = deadline_manual;
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

    public String getOrigin_change() {
        return origin_change;
    }

    public void setOrigin_change(String origin_change) {
        this.origin_change = origin_change;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getPack_default() {
        return pack_default;
    }

    public void setPack_default(String pack_default) {
        this.pack_default = pack_default;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSo_id() {
        return so_id;
    }

    public void setSo_id(String so_id) {
        this.so_id = so_id;
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

    public int getEdit_user() {
        return edit_user;
    }

    public void setEdit_user(int edit_user) {
        this.edit_user = edit_user;
    }
}
