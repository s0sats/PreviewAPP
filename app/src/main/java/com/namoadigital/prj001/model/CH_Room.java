package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2017.
 */

public class CH_Room {
    private String room_code;
    private String room_type;
    private String room_desc;
    private Long customer_code;
    private String room_obj;
    private String room_image;
    private String room_image_name;
    private String room_image_local;
    private int first_msg_prefix;
    private int first_msg_code;
    private Long user_code;
    private Integer msg_prefix;
    private Integer msg_code;
    private int status_update;

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getRoom_desc() {
        return room_desc;
    }

    public void setRoom_desc(String room_desc) {
        this.room_desc = room_desc;
    }

    public Long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Long customer_code) {
        this.customer_code = customer_code;
    }

    public String getRoom_obj() {
        return room_obj;
    }

    public void setRoom_obj(String room_obj) {
        this.room_obj = room_obj;
    }

    public String getRoom_image() {
        return room_image;
    }

    public void setRoom_image(String room_image) {
        this.room_image = room_image;
    }

    public String getRoom_image_name() {
        return room_image_name;
    }

    public void setRoom_image_name(String room_image_name) {
        this.room_image_name = room_image_name;
    }

    public String getRoom_image_local() {
        return room_image_local;
    }

    public void setRoom_image_local(String room_image_local) {
        this.room_image_local = room_image_local;
    }

    public int getFirst_msg_prefix() {
        return first_msg_prefix;
    }

    public void setFirst_msg_prefix(int first_msg_prefix) {
        this.first_msg_prefix = first_msg_prefix;
    }

    public int getFirst_msg_code() {
        return first_msg_code;
    }

    public void setFirst_msg_code(int first_msg_code) {
        this.first_msg_code = first_msg_code;
    }

    public Long getUser_code() {
        return user_code;
    }

    public void setUser_code(Long user_code) {
        this.user_code = user_code;
    }

    public Integer getMsg_prefix() {
        return msg_prefix;
    }

    public void setMsg_prefix(Integer msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    public Integer getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(Integer msg_code) {
        this.msg_code = msg_code;
    }

    public void setMsg_code(int msg_code) {
        this.msg_code = msg_code;
    }

    public int getStatus_update() {
        return status_update;
    }

    public void setStatus_update(int status_update) {
        this.status_update = status_update;
    }
}
