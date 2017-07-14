package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Serial_Save_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;

    //private ArrayList<SM_SO> so;
    private ArrayList<Serial_Save_Return> serial_return;


    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

//    public ArrayList<SM_SO> getSo() {
//        return so;
//    }
//
//    public void setSo(ArrayList<SM_SO> so) {
//        this.so = so;
//    }

    public ArrayList<Serial_Save_Return> getSerial_return() {
        return serial_return;
    }

    public void setSerial_return(ArrayList<Serial_Save_Return> serial_return) {
        this.serial_return = serial_return;
    }

    public class Serial_Save_Return{
        private long customer_code;
        private long product_code;
        private int serial_code;
        private String ret_status;
        private String ret_msg;

        public long getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(long customer_code) {
            this.customer_code = customer_code;
        }

        public long getProduct_code() {
            return product_code;
        }

        public void setProduct_code(long product_code) {
            this.product_code = product_code;
        }

        public int getSerial_code() {
            return serial_code;
        }

        public void setSerial_code(int serial_code) {
            this.serial_code = serial_code;
        }

        public String getRet_status() {
            return ret_status;
        }

        public void setRet_status(String ret_status) {
            this.ret_status = ret_status;
        }

        public String getRet_msg() {
            return ret_msg;
        }

        public void setRet_msg(String ret_msg) {
            this.ret_msg = ret_msg;
        }
    }
}
