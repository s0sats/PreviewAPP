package com.namoadigital.prj001.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by d.luche on 01/12/2017.
 */

public class Chat_C_Message {

    private String room_type;
    private String room_code;
    private Long customer_code;
    private int msg_prefix;
    private int msg_code;
    private String msg_date;
    private JsonObject msg_obj;
    private String msg_origin;
    private int delivered;
    private String delivered_date;
    private int read;
    private String read_date;
    private String msg_pk;
    private int user_code;
    private String user_nick;
    private int delivered_user;
    private String socket_id;

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

    public Long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Long customer_code) {
        this.customer_code = customer_code;
    }

    public int getMsg_prefix() {
        return msg_prefix;
    }

    public void setMsg_prefix(int msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    public int getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(int msg_code) {
        this.msg_code = msg_code;
    }

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }

    public JsonObject getMsg_obj() {
        return msg_obj;
    }

    public void setMsg_obj(JsonObject msg_obj) {
        this.msg_obj = msg_obj;
    }

    public String getMsg_origin() {
        return msg_origin;
    }

    public void setMsg_origin(String msg_origin) {
        this.msg_origin = msg_origin;
    }

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getRead_date() {
        return read_date;
    }

    public void setRead_date(String read_date) {
        this.read_date = read_date;
    }

    public String getMsg_pk() {
        return msg_pk;
    }

    public void setMsg_pk(String msg_pk) {
        this.msg_pk = msg_pk;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public int getDelivered_user() {
        return delivered_user;
    }

    public void setDelivered_user(int delivered_user) {
        this.delivered_user = delivered_user;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public CH_Message toCH_MessageObj(){
        Chat_C_Message chat_c_message = this;
        CH_Message ch_message = new CH_Message();
        //
        ch_message.setMsg_prefix(chat_c_message.getMsg_prefix());
        ch_message.setMsg_code(chat_c_message.getMsg_code());
        ch_message.setTmp(0);
        ch_message.setRoom_code(chat_c_message.getRoom_code());
        ch_message.setMsg_date(chat_c_message.getMsg_date());
        ch_message.setMsg_obj(chat_c_message.getMsg_obj().toString());
        ch_message.setMsg_origin(chat_c_message.getMsg_origin());
        ch_message.setDelivered(chat_c_message.getDelivered());
        ch_message.setDelivered_date(chat_c_message.getDelivered_date());
        ch_message.setRead(chat_c_message.getRead());
        ch_message.setRead_date(chat_c_message.getRead_date());
        ch_message.setMsg_pk(chat_c_message.getMsg_pk());
        ch_message.setUser_code(chat_c_message.getUser_code());
        ch_message.setUser_nick(chat_c_message.getUser_nick());

        return ch_message;
    }

    public CH_Message toCH_MessageObj(Chat_C_Message chat_c_message){
        CH_Message ch_message = new CH_Message();
        //
        ch_message.setMsg_prefix(chat_c_message.getMsg_prefix());
        ch_message.setMsg_code(chat_c_message.getMsg_code());
        ch_message.setMsg_date(chat_c_message.getMsg_date());
        ch_message.setTmp(0);
        ch_message.setRoom_code(chat_c_message.getRoom_code());
        ch_message.setMsg_obj(chat_c_message.getMsg_obj().toString());
        ch_message.setMsg_origin(chat_c_message.getMsg_origin());
        ch_message.setDelivered(chat_c_message.getDelivered());
        ch_message.setDelivered_date(chat_c_message.getDelivered_date());
        ch_message.setRead(chat_c_message.getRead());
        ch_message.setRead_date(chat_c_message.getRead_date());
        ch_message.setMsg_pk(chat_c_message.getMsg_pk());
        ch_message.setUser_code(chat_c_message.getUser_code());
        ch_message.setUser_nick(chat_c_message.getUser_nick());

        return ch_message;
    }

    public static ArrayList<CH_Message> toCH_MessageList(ArrayList<Chat_C_Message>  chat_c_messages){
        ArrayList<CH_Message> chMessages = new ArrayList<>();

        for (Chat_C_Message chat_c_message : chat_c_messages) {
            CH_Message ch_message = chat_c_message.toCH_MessageObj();
            //
            chMessages.add(ch_message);
        }

        return chMessages;
    }

}

