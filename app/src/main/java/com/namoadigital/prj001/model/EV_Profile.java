package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 26/05/2017.
 */

public class EV_Profile {

    private long customer_code;
    private String menu_code;
    private String parameter_code;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getMenu_code() {
        return menu_code;
    }

    public void setMenu_code(String menu_code) {
        this.menu_code = menu_code;
    }

    public String getParameter_code() {
        return parameter_code;
    }

    public void setParameter_code(String parameter_code) {
        this.parameter_code = parameter_code;
    }
}
