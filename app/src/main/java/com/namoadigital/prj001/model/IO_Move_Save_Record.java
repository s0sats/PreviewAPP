package com.namoadigital.prj001.model;

import java.util.List;

class IO_Move_Save_Record {

    private String customer_code;

    private String move_prefix;

    private String move_code;

    private Integer to_zone_code;

    private Integer to_local_code;

    private String to_class_code;

    private Integer reason_code;

    private String save_date;

    private List<MD_Product_Serial_Tracking> tracking_list = null;

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getMove_prefix() {
        return move_prefix;
    }

    public void setMove_prefix(String move_prefix) {
        this.move_prefix = move_prefix;
    }

    public String getMove_code() {
        return move_code;
    }

    public void setMove_code(String move_code) {
        this.move_code = move_code;
    }

    public Integer getTo_zone_code() {
        return to_zone_code;
    }

    public void setTo_zone_code(Integer to_zone_code) {
        this.to_zone_code = to_zone_code;
    }

    public Integer getTo_local_code() {
        return to_local_code;
    }

    public void setTo_local_code(Integer to_local_code) {
        this.to_local_code = to_local_code;
    }

    public String getTo_class_code() {
        return to_class_code;
    }

    public void setTo_class_code(String to_class_code) {
        this.to_class_code = to_class_code;
    }

    public Integer getReason_code() {
        return reason_code;
    }

    public void setReason_code(Integer reason_code) {
        this.reason_code = reason_code;
    }

    public String getSave_date() {
        return save_date;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }

    public List<MD_Product_Serial_Tracking> getTracking_list() {
        return tracking_list;
    }

    public void setTracking_list(List<MD_Product_Serial_Tracking> tracking_list) {
        this.tracking_list = tracking_list;
    }
}
