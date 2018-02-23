package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/02/2018.
 */

public class MD_Department {
    private long customer_code;
    private int department_code;
    private String department_id;
    private String department_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getDepartment_code() {
        return department_code;
    }

    public void setDepartment_code(int department_code) {
        this.department_code = department_code;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_desc() {
        return department_desc;
    }

    public void setDepartment_desc(String department_desc) {
        this.department_desc = department_desc;
    }
}
