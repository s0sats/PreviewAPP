package com.namoadigital.prj001.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by d.luche on 29/11/2017.
 */

public class Chat_C_Room {

    private String room_type;
    private String room_code;
    private String room_desc;
    private Long customer_code;
    private JsonObject room_obj;
    private String room_image;
    private String room_image_name;
    private int first_msg_prefix;
    private int first_msg_code;
    private Long user_code;
    private Integer msg_prefix;
    private Integer msg_code;

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

    public JsonObject getRoom_obj() {
        return room_obj;
    }

    public void setRoom_obj(JsonObject room_obj) {
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

    public void setRoom_image_name(String room_image_name) {
        this.room_image_name = room_image_name;
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

    public CH_Room toCH_RoomObj() {
        CH_Room ch_room = new CH_Room();
        //
        ch_room.setRoom_code(this.room_code);
        ch_room.setRoom_type(this.room_type);
        ch_room.setRoom_desc(this.room_desc);
        ch_room.setCustomer_code(this.customer_code);
        ch_room.setRoom_obj(this.room_obj.toString());
        ch_room.setRoom_image(this.room_image);
        ch_room.setRoom_image_name(this.room_image_name);
        ch_room.setRoom_image_local(null);
        ch_room.setFirst_msg_prefix(this.first_msg_prefix);
        ch_room.setFirst_msg_code(this.first_msg_code);
        ch_room.setUser_code(this.user_code);
        ch_room.setMsg_prefix(this.msg_prefix);
        ch_room.setMsg_code(this.msg_code);

        return ch_room;
    }

    public static ArrayList<CH_Room> toCH_RoomList(ArrayList<Chat_C_Room> chat_c_rooms) {
        ArrayList<CH_Room> chRooms = new ArrayList<>();

        for (Chat_C_Room cRoom : chat_c_rooms) {
            CH_Room ch_room = new CH_Room();
            //
            ch_room.setRoom_code(cRoom.getRoom_code());
            ch_room.setRoom_type(cRoom.getRoom_type());
            ch_room.setRoom_desc(cRoom.getRoom_desc());
            ch_room.setCustomer_code(cRoom.getCustomer_code());
            ch_room.setRoom_obj(cRoom.getRoom_obj().toString());
            ch_room.setRoom_image(cRoom.getRoom_image());
            ch_room.setRoom_image_name(cRoom.getRoom_image_name());
            ch_room.setRoom_image_local(null);
            ch_room.setFirst_msg_prefix(cRoom.getFirst_msg_prefix());
            ch_room.setFirst_msg_code(cRoom.getFirst_msg_code());
            ch_room.setUser_code(cRoom.getUser_code());
            ch_room.setMsg_prefix(cRoom.getMsg_prefix());
            ch_room.setMsg_code(cRoom.getMsg_code());
            //
            chRooms.add(ch_room);
        }

        return chRooms;
    }

}
