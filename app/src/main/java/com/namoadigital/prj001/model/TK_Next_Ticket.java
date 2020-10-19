package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TK_Next_Ticket {
    @SerializedName("customer_code")
    @Expose
    private Integer customerCode;
    @SerializedName("ticket_prefix")
    @Expose
    private Integer ticketPrefix;
    @SerializedName("ticket_code")
    @Expose
    private Integer ticketCode;
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("scn")
    @Expose
    private Integer scn;
    @SerializedName("open_site_code")
    @Expose
    private Integer openSiteCode;
    @SerializedName("open_site_desc")
    @Expose
    private String openSiteDesc;
    @SerializedName("open_product_desc")
    @Expose
    private String openProductDesc;
    @SerializedName("open_serial_id")
    @Expose
    private String openSerialId;
    @SerializedName("current_step_order")
    @Expose
    private Integer currentStepOrder;
    @SerializedName("ticket_status")
    @Expose
    private String ticketStatus;
    @SerializedName("origin_desc")
    @Expose
    private String originDesc;
    @SerializedName("step_desc")
    @Expose
    private String stepDesc;
    @SerializedName("step_order_seq")
    @Expose
    private String step_order_seq;
    @SerializedName("forecast_start")
    @Expose
    private String forecastStart;
    @SerializedName("forecast_end")
    @Expose
    private String forecastEnd;
    @SerializedName("step_count")
    @Expose
    private Integer stepCount;

    private int ticket_local;
    private int sync_required;

    public Integer getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(Integer customerCode) {
        this.customerCode = customerCode;
    }

    public Integer getTicketPrefix() {
        return ticketPrefix;
    }

    public void setTicketPrefix(Integer ticketPrefix) {
        this.ticketPrefix = ticketPrefix;
    }

    public Integer getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(Integer ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getScn() {
        return scn;
    }

    public void setScn(Integer scn) {
        this.scn = scn;
    }

    public Integer getOpenSiteCode() {
        return openSiteCode;
    }

    public void setOpenSiteCode(Integer openSiteCode) {
        this.openSiteCode = openSiteCode;
    }

    public String getOpenSiteDesc() {
        return openSiteDesc;
    }

    public void setOpenSiteDesc(String openSiteDesc) {
        this.openSiteDesc = openSiteDesc;
    }

    public String getOpenProductDesc() {
        return openProductDesc;
    }

    public void setOpenProductDesc(String openProductDesc) {
        this.openProductDesc = openProductDesc;
    }

    public String getOpenSerialId() {
        return openSerialId;
    }

    public void setOpenSerialId(String openSerialId) {
        this.openSerialId = openSerialId;
    }

    public Integer getCurrentStepOrder() {
        return currentStepOrder;
    }

    public void setCurrentStepOrder(Integer currentStepOrder) {
        this.currentStepOrder = currentStepOrder;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getOriginDesc() {
        return originDesc;
    }

    public void setOriginDesc(String originDesc) {
        this.originDesc = originDesc;
    }

    public String getStepDesc() {
        return stepDesc;
    }

    public void setStepDesc(String stepDesc) {
        this.stepDesc = stepDesc;
    }

    public String getStep_order_seq() {
        return step_order_seq;
    }

    public void setStep_order_seq(String step_order_seq) {
        this.step_order_seq = step_order_seq;
    }

    public String getForecastStart() {
        return forecastStart;
    }

    public void setForecastStart(String forecastStart) {
        this.forecastStart = forecastStart;
    }

    public String getForecastEnd() {
        return forecastEnd;
    }

    public void setForecastEnd(String forecastEnd) {
        this.forecastEnd = forecastEnd;
    }

    public Integer getStepCount() {
        return stepCount;
    }

    public void setStepCount(Integer stepCount) {
        this.stepCount = stepCount;
    }

    public int getTicket_local() {
        return ticket_local;
    }

    public void setTicket_local(int ticket_local) {
        this.ticket_local = ticket_local;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }
}
