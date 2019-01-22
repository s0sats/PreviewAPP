package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/02/2018.
 *
 * Luche - 22/01/2019
 *
 * Add extends da classe Main_Header_Env e comentado propriedades da
 * propria classe.
 */

public class TSearch_Ap_Env extends Main_Header_Env {

    private DataPackage data_package;

    public DataPackage getData_package() {
        return data_package;
    }

    public void setData_package(DataPackage data_package) {
        this.data_package = data_package;
    }

    public static class ObjAp{
        private String customer_code;
        private String custom_form_type;
        private String custom_form_code;
        private String custom_form_version;
        private String custom_form_data;
        private String ap_code;
        private String ap_scn;

        public String getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(String customer_code) {
            this.customer_code = customer_code;
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

        public String getAp_scn() {
            return ap_scn;
        }

        public void setAp_scn(String ap_scn) {
            this.ap_scn = ap_scn;
        }
    }
}
