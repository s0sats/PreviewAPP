package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class MD_All_Product_Group {

    private long customer_code;
    private long group_code;
    private long recursive_code;
    private Long recursive_code_father;
    private String group_id;
    private String group_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getGroup_code() {
        return group_code;
    }

    public void setGroup_code(long group_code) {
        this.group_code = group_code;
    }

    public long getRecursive_code() {
        return recursive_code;
    }

    public void setRecursive_code(long recursive_code) {
        this.recursive_code = recursive_code;
    }

    public Long getRecursive_code_father() {
        return recursive_code_father;
    }

    public void setRecursive_code_father(Long recursive_code_father) {
        this.recursive_code_father = recursive_code_father;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }
}
