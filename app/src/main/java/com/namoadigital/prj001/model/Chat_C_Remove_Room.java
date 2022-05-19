package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 07/02/2018.
 */

public class Chat_C_Remove_Room {
    @SerializedName("room_code") String room_code;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }
}
