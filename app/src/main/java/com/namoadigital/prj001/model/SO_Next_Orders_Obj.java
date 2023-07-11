package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class SO_Next_Orders_Obj {

    @SerializedName("so_prefix")
    private String so_prefix;
    @SerializedName("so_code")
    private String so_code;
    @SerializedName("so_id")
    private String so_id;
    @SerializedName("so_desc")
    private String so_desc;
    //private String product_code;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("product_desc")
    private String product_desc;
    @SerializedName("serial_code")
    private String serial_code;
    @SerializedName("serial_id")
    private String serial_id;
    @SerializedName("status")
    private String status;
    @SerializedName("edit_user")
    private String editUser;
    @SerializedName("so_scn")
    private int soScn;
    @SerializedName("deadline")
    private String deadline;
    @SerializedName("tracking")
    private String tracking;
    @SerializedName("brand_model_color")
    private String brand_model_color;
    @SerializedName("brand_desc")
    private String brand_desc;
    @SerializedName("model_desc")
    private String model_desc;
    @SerializedName("color_desc")
    private String color_desc;

    @SerializedName("comments")
    private String comments;
    @SerializedName("service")
    private String service;
    @SerializedName("serial_site_code")
    private String serial_site_code;
    @SerializedName("serial_site_desc")
    private String serial_site_desc;
    @SerializedName("serial_zone_desc")
    private String serial_zone_desc;
    @SerializedName("serial_local_desc")
    private String serial_local_desc;
    @SerializedName("create_user")
    private String create_user;
    @SerializedName("last_approval_budget_user")
    private String last_approval_budget_user;
    @SerializedName("deadline_filter")
    private String deadline_filter;
    @SerializedName("status_filter")
    private String status_filter;
    //LUCHE - 13/07/2021
    @SerializedName("segment_category_price")
    private String segment_category_price;
    @SerializedName("pipeline_desc")
    private String pipeline_desc;
    @SerializedName("add_inf1")
    private String add_inf1;
    @SerializedName("add_inf2")
    private String add_inf2;
    @SerializedName("add_inf3")
    private String add_inf3;
    @SerializedName("add_inf4")
    private String add_inf4;
    @SerializedName("add_inf5")
    private String add_inf5;
    @SerializedName("add_inf6")
    private String add_inf6;
    @SerializedName("client_so_id")
    private String client_so_id;
    @SerializedName("priority_code")
    private int priority_code;
    @SerializedName("priority_desc")
    private String priority_desc;
    @SerializedName("priority_weight")
    private Integer priority_weight;
    @SerializedName("priority_color")
    private String priority_color;
    @SerializedName("create_date")
    private String create_date;
    private String create_date_filter;

    @SerializedName("deadline_manual")
    private Integer deadline_manual;

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

    public String getSo_id() {
        return so_id;
    }

    public void setSo_id(String so_id) {
        this.so_id = so_id;
    }

    public String getSo_desc() {
        return so_desc;
    }

    public void setSo_desc(String so_desc) {
        this.so_desc = so_desc;
    }

//    public String getProduct_code() {
//        return product_code;
//    }
//
//    public void setProduct_code(String product_code) {
//        this.product_code = product_code;
//    }

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

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getBrand_model_color() {
        return brand_model_color;
    }

    public void setBrand_model_color(String brand_model_color) {
        this.brand_model_color = brand_model_color;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSerial_site_code() {
        return serial_site_code;
    }

    public void setSerial_site_code(String serial_site_code) {
        this.serial_site_code = serial_site_code;
    }

    public String getSerial_site_desc() {
        return serial_site_desc;
    }

    public void setSerial_site_desc(String serial_site_desc) {
        this.serial_site_desc = serial_site_desc;
    }

    public String getSerial_zone_desc() {
        return serial_zone_desc;
    }

    public void setSerial_zone_desc(String serial_zone_desc) {
        this.serial_zone_desc = serial_zone_desc;
    }

    public String getSerial_local_desc() {
        return serial_local_desc;
    }

    public void setSerial_local_desc(String serial_local_desc) {
        this.serial_local_desc = serial_local_desc;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getLast_approval_budget_user() {
        return last_approval_budget_user;
    }

    public void setLast_approval_budget_user(String last_approval_budget_user) {
        this.last_approval_budget_user = last_approval_budget_user;
    }

    public String getDeadline_filter() {
        return deadline_filter;
    }

    public void setDeadline_filter(String deadline_filter) {
        this.deadline_filter = deadline_filter;
    }

    public String getStatus_filter() {
        return status_filter;
    }

    public void setStatus_filter(String status_filter) {
        this.status_filter = status_filter;
    }

    public String getSegment_category_price() {
        return segment_category_price;
    }

    public void setSegment_category_price(String segment_category_price) {
        this.segment_category_price = segment_category_price;
    }

    public String getPipeline_desc() {
        return pipeline_desc;
    }

    public void setPipeline_desc(String pipeline_desc) {
        this.pipeline_desc = pipeline_desc;
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

    public String getAdd_inf4() {
        return add_inf4;
    }

    public void setAdd_inf4(String add_inf4) {
        this.add_inf4 = add_inf4;
    }

    public String getAdd_inf5() {
        return add_inf5;
    }

    public void setAdd_inf5(String add_inf5) {
        this.add_inf5 = add_inf5;
    }

    public String getAdd_inf6() {
        return add_inf6;
    }

    public void setAdd_inf6(String add_inf6) {
        this.add_inf6 = add_inf6;
    }

    public String getClient_so_id() {
        return client_so_id;
    }

    public void setClient_so_id(String client_so_id) {
        this.client_so_id = client_so_id;
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


    public String getCreateDate() {
        return create_date;
    }

    public void setCreateDate(String date) {
        this.create_date = date;
    }

    /**
     * LUCHE - 17/03/2021
     * Metodo que retorna todos os campos filtraveis e que será usado no filtro da
     * lista de proximas o.s
     * LUCHE - 13/07/2021
     * Add novos campos do card
     *
     * @return
     */
    public String getAllFieldForFilter() {
        return (
                so_prefix + "." +
                        so_code + "|" +
                        so_id + "|" +
                        so_desc + "|" +
                        product_desc + "|" +
                        //serial_code+ "|" +
                        serial_id + "|" +
                        status_filter + "|" +
                        deadline_filter + "|" +
                        tracking + "|" +
                        brand_desc + "|" +
                        model_desc + "|" +
                        color_desc + "|" +
                        segment_category_price + "|" +
                        pipeline_desc + "|" +
                        client_so_id + "|" +
                        priority_desc + "|" +
                        create_date_filter + "|" +
                        create_user + "|" +
                        last_approval_budget_user
                    /*+
                comments+ "|" +
                service+ "|" +
                //serial_site_code+ "|" +
                serial_site_desc+ "|" +
                serial_zone_desc+ "|" +
                serial_local_desc+ "|" +
                */
        )
                .replace("null|", "")
                .replace("null", "")
                ;
    }

    public String getEditUser() {
        return editUser;
    }

    public void setEditUser(String editUser) {
        this.editUser = editUser;
    }

    public int getSoScn() {
        return soScn;
    }

    public void setSoScn(int soScn) {
        this.soScn = soScn;
    }

    public Integer getPriority_weight() {
        return priority_weight;
    }

    public void setPriority_weight(Integer priority_weight) {
        this.priority_weight = priority_weight;
    }

    public String getPriority_color() {
        return priority_color;
    }

    public void setPriority_color(String priority_color) {
        this.priority_color = priority_color;
    }

    public String getColor_desc() {
        return color_desc;
    }

    public void setColor_desc(String color_desc) {
        this.color_desc = color_desc;
    }

    public String getModel_desc() {
        return model_desc;
    }

    public void setModel_desc(String model_desc) {
        this.model_desc = model_desc;
    }

    public String getBrand_desc() {
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public Integer getDeadline_manual() {
        return deadline_manual;
    }

    public void setDeadline_manual(Integer deadline_manual) {
        this.deadline_manual = deadline_manual;
    }

    public String getCreate_date_filter() {
        return create_date_filter;
    }

    public void setCreate_date_filter(String create_date_filter) {
        this.create_date_filter = create_date_filter;
    }
}
