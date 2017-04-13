package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 12/04/17.
 */

public class FCMMessage {

    private int fcmmessage_code;
    private String customer;
    private String type;
    private String title;
    private String msg_short;
    private String msg_long;
    private String module;
    private String sender;
    private String sync;
    private String status;
    private String date_create;
    private long date_create_ms;

    public FCMMessage() {
        this.fcmmessage_code = -1;
        this.customer = "no customer";
        this.type = "no type";
        this.title = "no title";
        this.msg_short = "no msg_short";
        this.msg_long = "no msg_long";
        this.module = "no module";
        this.sender = "no sender";
        this.sync = "0";
        this.status = "1";
        this.date_create = "1900-01-01";
    }

    public int getFcmmessage_code() {
        return fcmmessage_code;
    }

    public void setFcmmessage_code(int fcmmessage_code) {
        this.fcmmessage_code = fcmmessage_code;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg_short() {
        return msg_short;
    }

    public void setMsg_short(String msg_short) {
        this.msg_short = msg_short;
    }

    public String getMsg_long() {
        return msg_long;
    }

    public void setMsg_long(String msg_long) {
        this.msg_long = msg_long;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }

    public long getDate_create_ms() {
        return date_create_ms;
    }

    public void setDate_create_ms(long date_create_ms) {
        this.date_create_ms = date_create_ms;
    }
}
