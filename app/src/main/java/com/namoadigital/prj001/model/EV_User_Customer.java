package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/2/16.
 */
public class EV_User_Customer {

    private long user_code;
    private long customer_code;
    private String customer_name;
    private int translate_code;
    private String language_code;
    private String translate_desc;
    private String nls_date_format;
    private int keyuser;
    private int blocked;
    private String session_app;
    private int pending;

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
}
