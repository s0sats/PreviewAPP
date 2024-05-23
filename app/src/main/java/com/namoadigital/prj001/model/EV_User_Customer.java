package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 5/2/16.
 */
public class EV_User_Customer {

    @SerializedName("user_code") private long user_code;
    @SerializedName("customer_code") private long customer_code;
    @SerializedName("customer_name") private String customer_name;
    @SerializedName("translate_code") private int translate_code;
    @SerializedName("language_code") private String language_code;
    @SerializedName("translate_desc") private String translate_desc;
    @SerializedName("nls_date_format") private String nls_date_format;
    @SerializedName("keyuser") private int keyuser;
    @SerializedName("blocked") private int blocked;
    @SerializedName("session_app") private String session_app;
    @SerializedName("pending") private int pending;
    @SerializedName("logo_url") private String logo_url;
    @SerializedName("tracking") private int tracking;
    @SerializedName("sync_required") private int sync_required;
    @SerializedName("timezone") private String timezone;
    @SerializedName("license_control_type") private String license_control_type;
    //LUCHE - 07/01/2021 - Propriedades referente a licença
    @SerializedName("license_site_code") private Integer license_site_code;
    @SerializedName("license_site_desc") private String license_site_desc;
    @SerializedName("license_user_level_code") private Integer license_user_level_code;
    @SerializedName("license_user_level_id") private String license_user_level_id;
    @SerializedName("license_user_level_value") private Integer license_user_level_value;
    @SerializedName("license_user_level_changed") private Integer license_user_level_changed;
    @SerializedName("automatic_site_code") private Integer automatic_site_code;
    @SerializedName("field_service") private int field_service;
    @SerializedName("field_service_mode_only") private int field_service_mode_only;

    public long getUser_code() {
        return user_code;
    }

    public void setUser_code(long user_code) {
        this.user_code = user_code;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(int translate_code) {
        this.translate_code = translate_code;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public String getTranslate_desc() {
        return translate_desc;
    }

    public void setTranslate_desc(String translate_desc) {
        this.translate_desc = translate_desc;
    }

    public String getNls_date_format() {
        return nls_date_format;
    }

    public void setNls_date_format(String nls_date_format) {
        this.nls_date_format = nls_date_format;
    }

    public int getKeyuser() {
        return keyuser;
    }

    public void setKeyuser(int keyuser) {
        this.keyuser = keyuser;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public int getTracking() {
        return tracking;
    }

    public void setTracking(int tracking) {
        this.tracking = tracking;
    }

    public int getSync_required() {
        return sync_required;
    }

    public void setSync_required(int sync_required) {
        this.sync_required = sync_required;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLicense_control_type() {
        return license_control_type;
    }

    public void setLicense_control_type(String license_control_type) {
        this.license_control_type = license_control_type;
    }

    public Integer getLicense_site_code() {
        return license_site_code;
    }

    public void setLicense_site_code(Integer license_site_code) {
        this.license_site_code = license_site_code;
    }

    public String getLicense_site_desc() {
        return license_site_desc;
    }

    public void setLicense_site_desc(String license_site_desc) {
        this.license_site_desc = license_site_desc;
    }

    public Integer getLicense_user_level_code() {
        return license_user_level_code;
    }

    public void setLicense_user_level_code(Integer license_user_level_code) {
        this.license_user_level_code = license_user_level_code;
    }

    public String getLicense_user_level_id() {
        return license_user_level_id;
    }

    public void setLicense_user_level_id(String license_user_level_id) {
        this.license_user_level_id = license_user_level_id;
    }

    public Integer getLicense_user_level_value() {
        return license_user_level_value;
    }

    public void setLicense_user_level_value(Integer license_user_level_value) {
        this.license_user_level_value = license_user_level_value;
    }

    public Integer getLicense_user_level_changed() {
        return license_user_level_changed;
    }

    public void setLicense_user_level_changed(Integer license_user_level_changed) {
        this.license_user_level_changed = license_user_level_changed;
    }

    public Integer getAutomatic_site_code() {
        return automatic_site_code;
    }

    public void setAutomatic_site_code(Integer automatic_site_code) {
        this.automatic_site_code = automatic_site_code;
    }

    public int getField_service() {
        return field_service;
    }

    public void setField_service(int field_service) {
        this.field_service = field_service;
    }

    public int getField_service_mode_only() {
        return field_service_mode_only;
    }

    public void setField_service_mode_only(int field_service_mode_only) {
        this.field_service_mode_only = field_service_mode_only;
    }
}
