package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Get_Workgroup_List_Rec extends Main_Header_Rec {
    @SerializedName("scn_valid") private int scn_valid;
    @SerializedName("data") private ArrayList<Data> data;

    public int getScn_valid() {
        return scn_valid;
    }

    public void setScn_valid(int scn_valid) {
        this.scn_valid = scn_valid;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("customer_code") private int customer_code;
        @SerializedName("group_code") private int group_code;
        @SerializedName("group_desc") private String group_desc;

        public int getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(int customer_code) {
            this.customer_code = customer_code;
        }

        public int getGroup_code() {
            return group_code;
        }

        public void setGroup_code(int group_code) {
            this.group_code = group_code;
        }

        public String getGroup_desc() {
            return group_desc;
        }

        public void setGroup_desc(String group_desc) {
            this.group_desc = group_desc;
        }
    }

}
