package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SO_Favorite_Contract implements Serializable {
    private static final long serialVersionUID = 4323896203090648935L;


    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("contract_code")
    @Expose
    private Integer contractCode;
    @SerializedName("contract_desc")
    @Expose
    private String contractDesc;
    @SerializedName("po_list")
    @Expose
    private List<SO_Favorite_PO> poList = null;

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

    public List<SO_Favorite_PO> getPoList() {
        return poList;
    }

    public void setPoList(List<SO_Favorite_PO> poList) {
        this.poList = poList;
    }
}
