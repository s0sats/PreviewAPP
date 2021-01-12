package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 20/01/2017.
 * Classe com propriedade que serão recebidas do
 * servidor na chamada do WS server_get_session.*
 */

public class TSession_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String session_app;
    private Session_Options session_options;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public Session_Options getSession_options() {
        return session_options;
    }

    public void setSession_options(Session_Options session_options) {
        this.session_options = session_options;
    }

    /**
     * LUCHE - 12/01/2021
     * Criado classe de obj que representa as opções da sessão.
     * Esse obj é envia quando o o customer possui licença por site ou global
     */
    public class Session_Options{
        private Integer site_code;
        private String site_desc;
        private int user_level_code;
        private String user_level_id;
        private int user_level_value;
        private int user_level_changed;

        public Integer getSite_code() {
            return site_code;
        }

        public void setSite_code(Integer site_code) {
            this.site_code = site_code;
        }

        public String getSite_desc() {
            return site_desc;
        }

        public void setSite_desc(String site_desc) {
            this.site_desc = site_desc;
        }

        public int getUser_level_code() {
            return user_level_code;
        }

        public void setUser_level_code(int user_level_code) {
            this.user_level_code = user_level_code;
        }

        public String getUser_level_id() {
            return user_level_id;
        }

        public void setUser_level_id(String user_level_id) {
            this.user_level_id = user_level_id;
        }

        public int getUser_level_value() {
            return user_level_value;
        }

        public void setUser_level_value(int user_level_value) {
            this.user_level_value = user_level_value;
        }

        public int getUser_level_changed() {
            return user_level_changed;
        }

        public void setUser_level_changed(int user_level_changed) {
            this.user_level_changed = user_level_changed;
        }
    }
}
