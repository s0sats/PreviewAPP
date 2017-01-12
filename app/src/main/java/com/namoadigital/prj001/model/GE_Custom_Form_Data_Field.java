package com.namoadigital.prj001.model;

import android.graphics.BitmapFactory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.journeyapps.barcodescanner.Util;
import com.namoadigital.prj001.util.ToolBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form_Data_Field {

    @Expose
    private long customer_code;
    @Expose
    private int custom_form_type;
    @Expose
    private int custom_form_code;
    @Expose
    private int custom_form_version;
    @Expose
    private long custom_form_data; // Indexador
    @Expose
    private int custom_form_seq;

    private String value; // Resposta


    private String value_extra; // Resposta Extra (Foto/Comentario/Plano Acao)

    @Expose
    @SerializedName("value_n")
    private String value_json; // Resposta Json

    @Expose
    @SerializedName("value_n_extra")
    private String value_extra_json; // Resposta Extra (Foto/Comentario/Plano Acao) Json

    public GE_Custom_Form_Data_Field() {
        this.customer_code = -1L;
        this.custom_form_type = -1;
        this.custom_form_code = -1;
        this.custom_form_version = -1;
        this.custom_form_data = -1L;
        this.custom_form_seq = -1;
        this.value = "";
        this.value_extra = "";
        this.value_json = "";
        this.value_extra_json = "";
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(int custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public int getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(int custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public int getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(int custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public long getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(long custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    public int getCustom_form_seq() {
        return custom_form_seq;
    }

    public void setCustom_form_seq(int custom_form_seq) {
        this.custom_form_seq = custom_form_seq;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        //
        if (this.value.startsWith("{\"CONTENT\":[{")){
            this.value_json = this.value;
        } else {
            try {
                if (value.trim().length() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray ja = new JSONArray();
                    //
                    jsonObject.put("CONTENT", ja);
                    //
                    this.value_json = jsonObject.toString();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObjectAux = new JSONObject();
                    //
                    if (this.value.startsWith("/sdcard/")){
                        jsonObjectAux.put("VALUE", ToolBox.BitMapToBase64(BitmapFactory.decodeFile(value)));
                    } else {
                        jsonObjectAux.put("VALUE", value);
                    }

                    JSONArray ja = new JSONArray();
                    ja.put(jsonObjectAux);
                    //
                    jsonObject.put("CONTENT", ja);
                    //
                    this.value_json = jsonObject.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getValue_extra() {
        return value_extra;
    }

    public void setValue_extra(String value_extra) {
        this.value_extra = value_extra;
        //
        if (this.value_extra.startsWith("{\"CONTENT\":[{")){
            this.value_extra_json = this.value_extra;
        } else {
            try {
                if (value_extra.trim().length() == 0) {
                    JSONObject jsonObject = new JSONObject();
                    JSONArray ja = new JSONArray();
                    //
                    jsonObject.put("CONTENT", ja);
                    //
                    this.value_extra_json = jsonObject.toString();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObjectAux = new JSONObject();
                    jsonObjectAux.put("VALUE", value_extra);
                    JSONArray ja = new JSONArray();
                    ja.put(jsonObjectAux);
                    //
                    jsonObject.put("CONTENT", ja);
                    //
                    this.value_extra_json = jsonObject.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
