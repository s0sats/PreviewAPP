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

    public CH_Room toCH_RoomObj(){
        CH_Room ch_room = new CH_Room();
        //
        ch_room.setRoom_code(this.room_code);
        ch_room.setRoom_type(this.room_type);
        ch_room.setRoom_desc(this.room_desc);
        ch_room.setCustomer_code(this.customer_code);
        ch_room.setRoom_obj(this.room_obj.toString());
        ch_room.setRoom_image(this.room_image);
        ch_room.setRoom_image_local(null);

        return ch_room;
    }

    public static ArrayList<CH_Room> toCH_RoomList(ArrayList<Chat_C_Room> chat_c_rooms){
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
            ch_room.setRoom_image_local(null);
            //
            chRooms.add(ch_room);
        }

        return chRooms;
    }

}
