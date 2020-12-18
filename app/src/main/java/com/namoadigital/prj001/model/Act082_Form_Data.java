package com.namoadigital.prj001.model;

public class Act082_Form_Data {
    private String main_user_code;
    private String main_user_id;
    private String main_user_desc;
    private String internal_comments;
    private String rb_value;
    private String start_date;
    private String end_date;
    private String forecast_time;
    private boolean chk_shift_ticket_start_date;
    private boolean chk_shift_step_start_date;
    private boolean chk_shift_ticket_end_date;
    private boolean chk_shift_step_end_date;

    public Act082_Form_Data() {
    }

    public Act082_Form_Data(String main_user_code, String main_user_id, String main_user_desc, String internal_comments, String rb_value, String start_date, String end_date, String forecast_time, boolean chk_shift_ticket_start_date, boolean chk_shift_step_start_date, boolean chk_shift_ticket_end_date, boolean chk_shift_step_end_date) {
        this.main_user_code = main_user_code;
        this.main_user_id = main_user_id;
        this.main_user_desc = main_user_desc;
        this.internal_comments = internal_comments;
        this.rb_value = rb_value;
        this.start_date = start_date;
        this.end_date = end_date;
        this.forecast_time = forecast_time;
        this.chk_shift_ticket_start_date = chk_shift_ticket_start_date;
        this.chk_shift_step_start_date = chk_shift_step_start_date;
        this.chk_shift_ticket_end_date = chk_shift_ticket_end_date;
        this.chk_shift_step_end_date = chk_shift_step_end_date;
    }

    public String getMain_user_code() {
        return main_user_code;
    }

    public void setMain_user_code(String main_user_code) {
        this.main_user_code = main_user_code;
    }

    public String getMain_user_id() {
        return main_user_id;
    }

    public void setMain_user_id(String main_user_id) {
        this.main_user_id = main_user_id;
    }

    public String getMain_user_desc() {
        return main_user_desc;
    }

    public void setMain_user_desc(String main_user_desc) {
        this.main_user_desc = main_user_desc;
    }

    public String getInternal_comments() {
        return internal_comments;
    }

    public void setInternal_comments(String internal_comments) {
        this.internal_comments = internal_comments;
    }

    public String getRb_value() {
        return rb_value;
    }

    public void setRb_value(String rb_value) {
        this.rb_value = rb_value;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getForecast_time() {
        return forecast_time;
    }

    public void setForecast_time(String forecast_time) {
        this.forecast_time = forecast_time;
    }

    public boolean isChk_shift_ticket_start_date() {
        return chk_shift_ticket_start_date;
    }

    public void setChk_shift_ticket_start_date(boolean chk_shift_ticket_start_date) {
        this.chk_shift_ticket_start_date = chk_shift_ticket_start_date;
    }

    public boolean isChk_shift_step_start_date() {
        return chk_shift_step_start_date;
    }

    public void setChk_shift_step_start_date(boolean chk_shift_step_start_date) {
        this.chk_shift_step_start_date = chk_shift_step_start_date;
    }

    public boolean isChk_shift_ticket_end_date() {
        return chk_shift_ticket_end_date;
    }

    public void setChk_shift_ticket_end_date(boolean chk_shift_ticket_end_date) {
        this.chk_shift_ticket_end_date = chk_shift_ticket_end_date;
    }

    public boolean isChk_shift_step_end_date() {
        return chk_shift_step_end_date;
    }

    public void setChk_shift_step_end_date(boolean chk_shift_step_end_date) {
        this.chk_shift_step_end_date = chk_shift_step_end_date;
    }
}
