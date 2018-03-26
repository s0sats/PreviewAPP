package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 3/22/18.
 */

public class SO_Pack_Express_Local {

    private long customer_code;
    private long site_code;
    private long operation_code;
    private long product_code;
    private String express_code;
    private long express_tmp;
    private String serial_id;
    private long partner_code;

    private Long so_prefix;
    private Long so_code;
    private String so_id;
    private String so_desc;
    private String so_status;
    private Long contract_code;
    private String contract_desc;
    private Long priority_code;
    private String priority_desc;
    private String site_id;
    private String site_desc;
    private String operation_id;
    private String operation_desc;
    private String product_id;
    private String product_desc;
    private Long serial_code;
    private Integer segment_code;
    private String segment_id;
    private String segment_desc;
    private String ret_code;
    private String ret_msg;
    private String status;

    public SO_Pack_Express_Local() {
        this.customer_code = -1L;
        this.site_code = -1L;
        this.operation_code = -1L;
        this.product_code = -1L;
        this.express_code = "";
        this.express_tmp = -1L;
        this.serial_id = "";
        this.partner_code = -1L;

        this.so_prefix = null;
        this.so_code = null;
        this.so_id = null;
        this.so_desc = null;
        this.so_status = null;
        this.contract_code = null;
        this.contract_desc = null;
        this.priority_code = null;
        this.priority_desc = null;
        this.site_id = null;
        this.site_desc = null;
        this.operation_id = null;
        this.operation_desc = null;
        this.product_id = null;
        this.product_desc = null;
        this.serial_code = null;
        this.segment_code = null;
        this.segment_id = null;
        this.segment_desc = null;
        this.ret_code = null;
        this.ret_msg = null;
        this.status = "NEW";
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getSite_code() {
        return site_code;
    }

    public void setSite_code(long site_code) {
        this.site_code = site_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public String getExpress_code() {
        return express_code;
    }

    public void setExpress_code(String express_code) {
        this.express_code = express_code;
    }

    public long getExpress_tmp() {
        return express_tmp;
    }

    public void setExpress_tmp(long express_tmp) {
        this.express_tmp = express_tmp;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public long getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(long partner_code) {
        this.partner_code = partner_code;
    }

    public Long getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(Long so_prefix) {
        this.so_prefix = so_prefix;
    }

    public Long getSo_code() {
        return so_code;
    }

    public void setSo_code(Long so_code) {
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

    public String getSo_status() {
        return so_status;
    }

    public void setSo_status(String so_status) {
        this.so_status = so_status;
    }

    public Long getContract_code() {
        return contract_code;
    }

    public void setContract_code(Long contract_code) {
        this.contract_code = contract_code;
    }

    public String getContract_desc() {
        return contract_desc;
    }

    public void setContract_desc(String contract_desc) {
        this.contract_desc = contract_desc;
    }

    public Long getPriority_code() {
        return priority_code;
    }

    public void setPriority_code(Long priority_code) {
        this.priority_code = priority_code;
    }

    public String getPriority_desc() {
        return priority_desc;
    }

    public void setPriority_desc(String priority_desc) {
        this.priority_desc = priority_desc;
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

    public Long getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(Long serial_code) {
        this.serial_code = serial_code;
    }

    public Integer getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(Integer segment_code) {
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

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
