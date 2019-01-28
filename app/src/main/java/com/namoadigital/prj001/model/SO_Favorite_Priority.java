package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class SO_Favorite_Priority {

    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("priority_code")
    @Expose
    private Integer priorityCode;
    @SerializedName("priority_desc")
    @Expose
    private String priorityDesc;
    @SerializedName("priority_default")
    @Expose
    private Integer priorityDefault;

    public Integer getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Integer customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(Integer priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }

    public Integer getPriorityDefault() {
        return priorityDefault;
    }

    public void setPriorityDefault(Integer priorityDefault) {
        this.priorityDefault = priorityDefault;
    }
}
