package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private int require_signature;
    private int require_location;
    private int require_serial_done;
    private String automatic_fill;
    private int all_site;
    private int all_operation;
    private int all_product;
    private int tag_operational_code;
    private int is_so;
    private int so_edit_start_end;
    private Integer so_order_type_default;
    private int so_allow_change_order_type;
    private int so_allow_backup;
    private int block_spontaneous;

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

    public int getRequire_signature() {
        return require_signature;
    }

    public void setRequire_signature(int require_signature) {
        this.require_signature = require_signature;
    }

    public int getRequire_location() {
        return require_location;
    }

    public void setRequire_location(int require_location) {
        this.require_location = require_location;
    }

    public int getRequire_serial_done() {
        return require_serial_done;
    }

    public void setRequire_serial_done(int require_serial_done) {
        this.require_serial_done = require_serial_done;
    }

    public String getAutomatic_fill() {
        return automatic_fill;
    }

    public void setAutomatic_fill(String automatic_fill) {
        this.automatic_fill = automatic_fill;
    }

    public int getAll_site() {
        return all_site;
    }

    public void setAll_site(int all_site) {
        this.all_site = all_site;
    }

    public int getAll_operation() {
        return all_operation;
    }

    public void setAll_operation(int all_operation) {
        this.all_operation = all_operation;
    }

    public int getAll_product() {
        return all_product;
    }

    public void setAll_product(int all_product) {
        this.all_product = all_product;
    }

    public int getTag_operational_code() {
        return tag_operational_code;
    }

    public void setTag_operational_code(int tag_operational_code) {
        this.tag_operational_code = tag_operational_code;
    }

    public int getIs_so() {
        return is_so;
    }

    public void setIs_so(int is_so) {
        this.is_so = is_so;
    }

    public int getSo_edit_start_end() {
        return so_edit_start_end;
    }

    public void setSo_edit_start_end(int so_edit_start_end) {
        this.so_edit_start_end = so_edit_start_end;
    }

    public Integer getSo_order_type_default() {
        return so_order_type_default;
    }

    public void setSo_order_type_default(Integer so_order_type_default) {
        this.so_order_type_default = so_order_type_default;
    }

    public int getSo_allow_change_order_type() {
        return so_allow_change_order_type;
    }

    public void setSo_allow_change_order_type(int so_allow_change_order_type) {
        this.so_allow_change_order_type = so_allow_change_order_type;
    }

    public int getSo_allow_backup() {
        return so_allow_backup;
    }

    public void setSo_allow_backup(int so_allow_backup) {
        this.so_allow_backup = so_allow_backup;
    }

    public int getBlock_spontaneous() {
        return block_spontaneous;
    }

    public void setBlock_spontaneous(int block_spontaneous) {
        this.block_spontaneous = block_spontaneous;
    }
}
