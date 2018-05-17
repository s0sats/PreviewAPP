package com.namoadigital.prj001.model;

public class MD_Class {
    private long customer_code;
    private int class_code;
    private String class_id;
    private String class_type;
    private String class_color;
    private int class_available;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getClass_code() {
        return class_code;
    }

    public void setClass_code(int class_code) {
        this.class_code = class_code;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public String getClass_color() {
        return class_color;
    }

    public void setClass_color(String class_color) {
        this.class_color = class_color;
    }

    public int getClass_available() {
        return class_available;
    }

    public void setClass_available(int class_available) {
        this.class_available = class_available;
    }
}
