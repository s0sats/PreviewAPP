package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_S_LeaveRoom {

    private Long customer_code;
    private int user_code;
    private String room_code;

    public Long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Long customer_code) {
        this.customer_code = customer_code;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }
}
