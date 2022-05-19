package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 12/12/2017.
 */

public class Chat_S_Pending_Message {

    @SerializedName("room_code") private String room_code;
    @SerializedName("ref_json") private ArrayList<Chat_Ref_Json> ref_json;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public ArrayList<Chat_Ref_Json> getRef_json() {
        return ref_json;
    }

    public void setRef_json(ArrayList<Chat_Ref_Json> ref_json) {
        this.ref_json = ref_json;
    }
}
