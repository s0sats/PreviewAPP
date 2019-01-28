package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class SO_Favorite_Contract {

    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("contract_code")
    @Expose
    private Integer contractCode;
    @SerializedName("contract_desc")
    @Expose
    private String contractDesc;
    @SerializedName("po_erp")
    @Expose
    private String poErp;
    @SerializedName("po_client1")
    @Expose
    private String poClient1;
    @SerializedName("po_client2")
    @Expose
    private String poClient2;
    @SerializedName("po_client3")
    @Expose
    private String poClient3;
    @SerializedName("pipeline_code")
    @Expose
    private Integer pipelineCode;
    @SerializedName("pack_default")
    @Expose
    private List<String> packDefault;

    public Integer getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Integer customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getContractCode() {
        return contractCode;
    }

    public void setContractCode(Integer contractCode) {
        this.contractCode = contractCode;
    }

    public String getContractDesc() {
        return contractDesc;
    }

    public void setContractDesc(String contractDesc) {
        this.contractDesc = contractDesc;
    }

    public String getPoErp() {
        return poErp;
    }

    public void setPoErp(String poErp) {
        this.poErp = poErp;
    }

    public String getPoClient1() {
        return poClient1;
    }

    public void setPoClient1(String poClient1) {
        this.poClient1 = poClient1;
    }

    public String getPoClient2() {
        return poClient2;
    }

    public void setPoClient2(String poClient2) {
        this.poClient2 = poClient2;
    }

    public String getPoClient3() {
        return poClient3;
    }

    public void setPoClient3(String poClient3) {
        this.poClient3 = poClient3;
    }

    public Integer getPipelineCode() {
        return pipelineCode;
    }

    public void setPipelineCode(Integer pipelineCode) {
        this.pipelineCode = pipelineCode;
    }

    public List<String> getPackDefault() {
        return packDefault;
    }

    public void setPackDefault(List<String> packDefault) {
        this.packDefault = packDefault;
    }
}
