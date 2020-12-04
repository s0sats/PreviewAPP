package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 06/12/2019.
 */

public class T_TK_Get_Workgroup_List_Rec extends Main_Header_Rec {
    private int scn_valid;
    private ArrayList<Data> data;

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
        private int customer_code;
        private int group_code;
        private String group_desc;

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
