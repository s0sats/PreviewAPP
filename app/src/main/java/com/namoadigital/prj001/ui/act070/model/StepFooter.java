package com.namoadigital.prj001.ui.act070.model;

public class StepFooter extends BaseStep {

    private String endDate;
    private String ticketStatus;
    public StepFooter() {}

    public StepFooter(String ticketStatus, String endDate) {
        this.ticketStatus = ticketStatus;
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }
}
