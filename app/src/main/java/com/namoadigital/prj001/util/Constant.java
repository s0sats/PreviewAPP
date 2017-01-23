package com.namoadigital.prj001.util;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Constant {

    /**
     * PROJECT ID - PRJ001 - SMS
     */
    public static final String PRJ001_CODE = "PRJ001";
    public static final String PRJ001_VERSION = "0.1";

    /**
     * UI Module
     */
    public static final String ACT001 = "act001";

    /**
     * DataBase Definition
     */
    //public static int DB_VERSION;
    public static String DB_PATH;

    public static String DB_NAME_BASE;
    public static String DB_FULL_BASE = DB_PATH + "/" + DB_NAME_BASE;
    public static int DB_VERSION_BASE;

    public static String DB_NAME_CUSTOM;
    public static String DB_FULL_CUSTOM = DB_PATH + "/" + DB_NAME_CUSTOM;
    public static int DB_VERSION_CUSTOM;

    public static String DB_MODE_MULTI = "MULTI";
    public static String DB_MODE_SINGLE = "SINGLE";

    /**
     * Directory Definition
     */
    public static String ZIP_PATH;
    public static String IMG_PATH;
    public static String THU_PATH;

    public static String ZIP_NAME;
    public static String ZIP_NAME_FULL;

    public static String CACHE_PATH;
    public static String CACHE_PATH_PHOTO;

    /**
     * Service Login Parameter
     */
    public static final String GC_USER_CODE = "userid";
    public static final String GC_PWD = "userpwd";
    public static final String GC_NFC = "usernfc";
    public static final String GC_STATUS = "userstatus";
    public static final String GC_STATUS_JUMP = "userstatusjump";
    public static final String USER_CUSTOMER_CODE = "usercustomercode";
    public static final String USER_TYPE = "usertype";
    public static final String USER_CUSTOMER_TRANSLATE_CODE = "usercustomertranslatecode";
    public static final String FORCED_LOGIN = "forcedlogin";

    public static final String SW_TYPE_BR = "sw_type_br";
    public static final String SW_TYPE = "sw_type";
    public static final String SW_VALUE = "sw_value";
    public static final String SW_LINK = "sw_link";
    public static final String SW_REQUIRED = "sw_required";

    public static final String LOGIN_USER_CODE = "login_user_code";
    public static final String LOGIN_USER_CODE_NICK = "login_user_code_nick";
    public static final String LOGIN_USER_EMAIL = "login_user_email";
    public static final String LOGIN_USER_PWD = "login_user_pwd";
    public static final String LOGIN_USER_NFC = "login_user_nfc";
    public static final String LOGIN_CUSTOMER_CODE = "login_customer_code";
    public static final String LOGIN_CUSTOMER_CODE_NAME = "login_customer_code_name";
    public static final String NLS_DATE_FORMAT = "nls_date_format";

    public static final String LOGIN_USER_NFC_TMP = "login_user_nfc_tmp";
    public static final String LOGIN_CUSTOMER_CODE_TMP = "login_customer_code_tmp";
    public static final String USER_CUSTOMER_TRANSLATE_CODE_TEMP = "user_customer_translate_code_temp";

    /**
     * WebService
     */
    public static final String WS_GETCUSTOMERS = "https://dev.namoadigital.com/ws/prj001/server_get_customer.ws";
    public static final String WS_GETSESSION = "https://dev.namoadigital.com/ws/prj001/server_get_session.ws";



    public static final String WS_SYNC = "https://dev.namoadigital.com/ws/prj001/server_sync.ws";
    public static final String WS_SESSION = "https://dev.namoadigital.com/ws/prj001/server_get_session.ws";
    public static final String WS_SAVE = "https://dev.namoadigital.com/ws/prj001/server_save.ws";
    public static final String WS_SERIAL = "https://dev.namoadigital.com/ws/prj001/server_serial.ws";




}
