package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SO_Favorite_PO  implements Serializable {

    public static final String BILLING_ADD_INF1_VIEW = "BILLING_ADD_INF1_VIEW";
    public static final String BILLING_ADD_INF1_TEXT = "BILLING_ADD_INF1_TEXT";
    public static final String BILLING_ADD_INF1_TRACKING = "BILLING_ADD_INF1_TRACKING";
    public static final String BILLING_ADD_INF2_VIEW = "BILLING_ADD_INF2_VIEW";
    public static final String BILLING_ADD_INF2_TEXT = "BILLING_ADD_INF2_TEXT";
    public static final String BILLING_ADD_INF2_TRACKING = "BILLING_ADD_INF2_TRACKING";
    public static final String BILLING_ADD_INF3_VIEW = "BILLING_ADD_INF3_VIEW";
    public static final String BILLING_ADD_INF3_TEXT = "BILLING_ADD_INF3_TEXT";
    public static final String BILLING_ADD_INF3_TRACKING = "BILLING_ADD_INF3_TRACKING";

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

    @SerializedName("billing_add_inf1_view")
    @Expose
    private String  billingAddInf1View;

    @SerializedName("billing_add_inf1_text")
    @Expose
    private String  billingAddInf1Text;

    @SerializedName("billing_add_inf1_tracking")
    @Expose
    private Integer billingAddInf1Tracking;

    @SerializedName("billing_add_inf2_view")
    @Expose
    private String  billingAddInf2View;

    @SerializedName("billing_add_inf2_text")
    @Expose
    private String  billingAddInf2Text;

    @SerializedName("billing_add_inf2_tracking")
    @Expose
    private Integer billingAddInf2Tracking;

    @SerializedName("billing_add_inf3_view")
    @Expose
    private String  billingAddInf3View;

    @SerializedName("billing_add_inf3_text")
    @Expose
    private String  billingAddInf3Text;

    @SerializedName("billing_add_inf3_tracking")
    @Expose
    private Integer billingAddInf3Tracking;

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

    public String getBillingAddInf1View() {
        return billingAddInf1View;
    }

    public void setBillingAddInf1View(String billingAddInf1View) {
        this.billingAddInf1View = billingAddInf1View;
    }

    public String getBillingAddInf1Text() {
        return billingAddInf1Text;
    }

    public void setBillingAddInf1Text(String billingAddInf1Text) {
        this.billingAddInf1Text = billingAddInf1Text;
    }

    public Integer getBillingAddInf1Tracking() {
        return billingAddInf1Tracking;
    }

    public void setBillingAddInf1Tracking(Integer billingAddInf1Tracking) {
        this.billingAddInf1Tracking = billingAddInf1Tracking;
    }

    public String getBillingAddInf2View() {
        return billingAddInf2View;
    }

    public void setBillingAddInf2View(String billingAddInf2View) {
        this.billingAddInf2View = billingAddInf2View;
    }

    public String getBillingAddInf2Text() {
        return billingAddInf2Text;
    }

    public void setBillingAddInf2Text(String billingAddInf2Text) {
        this.billingAddInf2Text = billingAddInf2Text;
    }

    public Integer getBillingAddInf2Tracking() {
        return billingAddInf2Tracking;
    }

    public void setBillingAddInf2Tracking(Integer billingAddInf2Tracking) {
        this.billingAddInf2Tracking = billingAddInf2Tracking;
    }

    public String getBillingAddInf3View() {
        return billingAddInf3View;
    }

    public void setBillingAddInf3View(String billingAddInf3View) {
        this.billingAddInf3View = billingAddInf3View;
    }

    public String getBillingAddInf3Text() {
        return billingAddInf3Text;
    }

    public void setBillingAddInf3Text(String billingAddInf3Text) {
        this.billingAddInf3Text = billingAddInf3Text;
    }

    public Integer getBillingAddInf3Tracking() {
        return billingAddInf3Tracking;
    }

    public void setBillingAddInf3Tracking(Integer billingAddInf3Tracking) {
        this.billingAddInf3Tracking = billingAddInf3Tracking;
    }
}
