package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_Add_User_Into_Room_Rec {

    private String socket_id;
    private String room_code;
    private String custom_form_type;
    private String custom_form_code;
    private String custom_form_version;
    private String custom_form_data;
    private String ap_code;
    private String user_code_sql;

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(String custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public String getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(String custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public String getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(String custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public String getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(String custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    public String getAp_code() {
        return ap_code;
    }

    public void setAp_code(String ap_code) {
        this.ap_code = ap_code;
    }

    public String getUser_code_sql() {
        return user_code_sql;
    }

    public void setUser_code_sql(String user_code_sql) {
        this.user_code_sql = user_code_sql;
    }
}
