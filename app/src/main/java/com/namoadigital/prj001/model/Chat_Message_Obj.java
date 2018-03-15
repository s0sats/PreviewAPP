package com.namoadigital.prj001.model;

import com.google.gson.JsonElement;

import org.json.JSONObject;

/**
 * Created by d.luche on 15/03/2018.
 */

public class Chat_Message_Obj {

    private String type;
    protected Object data;
    private String data_string;
    private JsonElement data_json;
    private String module_code;
    private String resource_name;
    private Chat_Message_Obj data_converted;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getData_string() {
        return data_string ;
    }

    public void setData_string(String data_string) {
        this.data_string = data_string;
    }

    public JsonElement getData_json() {
        return data_json;
    }

    public void setData_json(JsonElement data_json) {
        this.data_json = data_json;
    }

    public String getModule_code() {
        return module_code;
    }

    public void setModule_code(String module_code) {
        this.module_code = module_code;
    }

    public String getResource_name() {
        return resource_name;
    }

    public void setResource_name(String resource_name) {
        this.resource_name = resource_name;
    }

    public void dataConverter(){
        if(data instanceof String){
            this.data_converted = new Chat_Message_Obj_String(data);
        }
        if(data instanceof JSONObject){
            this.data_converted = new Chat_Message_Obj_Json(data);
        }
    }

    public class Chat_Message_Obj_String extends Chat_Message_Obj{
        private String data;

        public Chat_Message_Obj_String(Object data) {
            this.data = (String) data;
        }
        @Override
        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public class Chat_Message_Obj_Json extends Chat_Message_Obj{
        private JSONObject data;

        public Chat_Message_Obj_Json(Object data) {
            this.data = (JSONObject) data;
        }
        //
        @Override
        public JSONObject getData() {
            return data;
        }

        public void setData(JSONObject data) {
            this.data = data;
        }
    }

}
