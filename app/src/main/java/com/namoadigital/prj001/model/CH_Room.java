package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2017.
 */

public class CH_Room {
    private String room_code;
    private String room_type;
    private String room_desc;
    private long customer_code;
    private String room_obj;
    private String room_image;
    private String room_image_local;

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

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
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

    public String getRoom_image_local() {
        return room_image_local;
    }

    public void setRoom_image_local(String room_image_local) {
        this.room_image_local = room_image_local;
    }
}
