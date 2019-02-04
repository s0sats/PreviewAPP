package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SO_Favorite_Pipeline {
    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("pipeline_code")
    @Expose
    private Integer pipelineCode;
    @SerializedName("pipeline_desc")
    @Expose
    private String pipelineDesc;

    public Integer getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Integer customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getPipelineCode() {
        return pipelineCode;
    }

    public void setPipelineCode(Integer pipelineCode) {
        this.pipelineCode = pipelineCode;
    }

    public String getPipelineDesc() {
        return pipelineDesc;
    }

    public void setPipelineDesc(String pipelineDesc) {
        this.pipelineDesc = pipelineDesc;
    }
}
