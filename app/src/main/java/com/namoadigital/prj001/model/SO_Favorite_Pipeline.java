package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SO_Favorite_Pipeline implements Serializable {

    private static final long serialVersionUID = -3515162720290420035L;

    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("pipeline_code")
    @Expose
    private Integer pipelineCode;
    @SerializedName("pipeline_desc")
    @Expose
    private String pipelineDesc;
    @SerializedName("deadline_automatic")
    @Expose
    private int deadline_automatic;

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

    public int getDeadline_automatic() {
        return deadline_automatic;
    }

    public void setDeadline_automatic(int deadline_automatic) {
        this.deadline_automatic = deadline_automatic;
    }
}
