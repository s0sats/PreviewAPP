package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SO_Favorite_PO  implements Serializable {
    @SerializedName("po_code")
    @Expose
    private Integer poCode;
    @SerializedName("po_id")
    @Expose
    private String poId;
    @SerializedName("po_desc")
    @Expose
    private String poDesc;
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
    @SerializedName("pipeline")
    @Expose
    private List<SO_Favorite_Pipeline> pipeline;

    public Integer getPoCode() {
        return poCode;
    }

    public void setPoCode(Integer poCode) {
        this.poCode = poCode;
    }

    public String getPoId() {
        return poId;
    }

    public void setPoId(String poId) {
        this.poId = poId;
    }

    public String getPoDesc() {
        return poDesc;
    }

    public void setPoDesc(String poDesc) {
        this.poDesc = poDesc;
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

    public List<SO_Favorite_Pipeline> getPipeline() {
        return pipeline;
    }

    public void setPipeline(List<SO_Favorite_Pipeline> pipeline) {
        this.pipeline = pipeline;
    }
}
