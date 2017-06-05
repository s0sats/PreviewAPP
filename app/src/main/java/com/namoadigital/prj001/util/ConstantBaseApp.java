package com.namoadigital.prj001.util;

import com.namoa_digital.namoa_library.util.ConstantBase;

/**
 * Created by neomatrix on 05/04/17.
 */

public class ConstantBaseApp  extends ConstantBase {


    /**
     * PROJECT ID - PRJ001 - SMS
     */
    public static final String PRJ001_CODE = "PRJ001";
    public static final String PRJ001_VERSION = "1.1.1";

    /**
     * UI Module
     */
    public static final String ACT001 = "act001";
    public static final String ACT002 = "act002";
    public static final String ACT003 = "act003";
    public static final String ACT004 = "act004";
    public static final String ACT005 = "act005";
    public static final String ACT006 = "act006";
    public static final String ACT007 = "act007";
    public static final String ACT008 = "act008";
    public static final String ACT009 = "act009";
    public static final String ACT010 = "act010";
    public static final String ACT011 = "act011";
    public static final String ACT012 = "act012";
    public static final String ACT013 = "act013";
    public static final String ACT014 = "act014";
    public static final String ACT015 = "act015";
    public static final String ACT016 = "act016";
    public static final String ACT017 = "act017";
    public static final String ACT018 = "act018";
    public static final String ACT019 = "act019";
    public static final String ACT020 = "act020";

    public static final String ACT007_PRODUCT_CODE = "product_code";
    public static final String ACT007_PRODUCT_SEARCH = "product_search";
    public static final String ACT007_CURRENTINDEX = "currentIndex";
    public static final String ACT007_MSTACKVALUES = "mstackvalues";

    public static final String ACT008_SERIAL_ID = "serial_id";
    public static final String ACT008_PRODUCT_DESC = "product_desc";
    public static final String ACT008_PRODUCT_ID = "product_id";

    public static final String ACT009_CUSTOM_FORM_TYPE = "custom_form_type";
    public static final String ACT009_CUSTOM_FORM_TYPE_DESC = "custom_form_type_desc";

    public static final String ACT010_CUSTOM_FORM_CODE = "custom_form_code";
    public static final String ACT010_CUSTOM_FORM_VERSION = "custom_form_version";
    public static final String ACT010_CUSTOM_FORM_CODE_DESC = "custom_form_code_desc";

    public static final String ACT013_CUSTOM_FORM_DATA = "custom_form_data";

    public static final String ACT020_BACK_FLOW = "back_flow";

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
    public static String SUPPORT_PATH;

    public static String ZIP_NAME;
    public static String ZIP_NAME_FULL;

    public static String SUPPORT_NAME;
    public static String SUPPORT_NAME_FULL;



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
    /**
     * Translation params
     */
    public static final String APP_MODULE = "APP_PRJ001";
    public static final String LIB_RESOURCE_NAME = "lib";

    /**
     * Footer info constants
     */
    public static final String FOOTER_CUSTOMER_LBL = "footer_customer_lbl";
    public static final String FOOTER_CUSTOMER = "footer_customer";
    public static final String FOOTER_SITE_LBL = "footer_site_lbl";
    public static final String FOOTER_SITE = "footer_site";
    public static final String FOOTER_OPERATION_LBL = "footer_operation_lbl";
    public static final String FOOTER_OPERATION = "footer_operation";
    public static final String FOOTER_BTN_OK = "footer_btn_ok";
    public static final String FOOTER_VERSION_LBL = "footer_version_lbl";
    public static final String FOOTER_IMEI_LBL = "footer_imei_lbl";
    public static final String FOOTER_IMEI = "footer_imei";

    /**
     * Sync WS
     */
    public static final String GS_SESSION_APP = "sessionapp";
    public static final String GS_DATA_PACKAGE = "datapackage";
    public static final String GS_PRODUCT_CODE = "product_code";
    /**
     * Serial WS
     */
    public static final String GS_SERIAL_PRODUCT_CODE = "serialproductcode";
    public static final String GS_SERIAL_REQUIRED = "serialrequired";
    public static final String GS_SERIAL_ALLOW_NEW = "serialallownew";
    public static final String GS_SERIAL_ID = "serialserialid";

    /**
     * WS Logout
     */
    public static final String WS_LOGOUT_CUSTOMER_LIST = "logoutcustomerlist";
    public static final String WS_LOGOUT_USER_CODE = "logoutusercode";

    /**
     * WS Support
     */
    public static final String WS_SUPPORT_MSG = "supportmsg";

    /**
     * WS Serial Search
     */

    public static final String WS_SERIAL_SEARCH_PRODUCT_CODE = "serial_search_product_code";
    public static final String WS_SERIAL_SEARCH_PRODUCT_ID = "serial_search_product_id";
    public static final String WS_SERIAL_SEARCH_SERIAL_ID = "serial_search_serial_id";


    public static final String SW_TYPE_BR = "sw_type_br";
    public static final String SW_TYPE = "sw_type";
    public static final String SW_VALUE = "sw_value";
    public static final String SW_LINK = "sw_link";
    public static final String SW_REQUIRED = "sw_required";

    public static final String PKG_CLEAN_KEY = "pkg_clean_key";

    public static final String LOGIN_USER_CODE = "login_user_code";
    public static final String LOGIN_USER_CODE_NICK = "login_user_code_nick";
    public static final String LOGIN_USER_EMAIL = "login_user_email";
    public static final String LOGIN_USER_PWD = "login_user_pwd";
    public static final String LOGIN_USER_NFC = "login_user_nfc";
    public static final String LOGIN_LAST_USER_CODE_LOGGED = "last_user_logged";
    public static final String LOGIN_CUSTOMER_CODE = "login_customer_code";
    public static final String LOGIN_CUSTOMER_CODE_NAME = "login_customer_code_name";
    public static final String LOGIN_SITE_CODE = "login_site_code";
    public static final String LOGIN_OPERATION_CODE = "login_operation_code";
    public static final String PHONE_UUID_CODE = "phone_uuid_code";
    public static final String NLS_DATE_FORMAT = "nls_date_format";
    public static final String SESSION_APP = "session_app";
    public static final String GOOGLE_ID = "google_id";
    public static final String GOOGLE_ID_OK = "google_id_ok";
    public static final String GOOGLE_ID_DT = "google_id_dt";
    public static final String AL_DT = "al_dt";
    public static final String EXECUTE_WS_GET_CUSTOMER = "session_app";
    public static final String SYNC_REQUIRED = "sync_required";
    public static final String MESSAGE_CLEAR = "message_clear";
    public static final String LOCATION_MESSAGE = "location_message";
    public static final String LOCATION_TYPE = "location_type";
    public static final String LOCATION_LAT = "location_lat";
    public static final String LOCATION_LNG = "location_lng";

    public static final String LOGIN_USER_NFC_TMP = "login_user_nfc_tmp";
    public static final String LOGIN_CUSTOMER_CODE_TMP = "login_customer_code_tmp";
    public static final String USER_CUSTOMER_TRANSLATE_CODE_TMP = "user_customer_translate_code_temp";

    //Constantes do Status do Cabeçalho
    public static final String CUSTOM_FORM_STATUS_RECEIVED = "RECEIVED";
    public static final String CUSTOM_FORM_STATUS_IN_PROCESSING = "IN_PROCESSING";
    public static final String CUSTOM_FORM_STATUS_FINALIZED = "FINALIZED";
    public static final String CUSTOM_FORM_STATUS_SENT = "SENT";
    public static final String CUSTOM_FORM_STATUS_SCHEDULED = "SCHEDULE";

    //ID de Notificações
    public static final int NOTIFICATION_UPLOAD = 666;
    public static final int NOTIFICATION_DOWNLOAD = 667;

    //Helper
    public static final String BACK_ACTION = "back_action";

    //Parametros de Permissões
    public static final String PARAM_SCHEDULE_CHECKLIST = "SCHEDULE_CHECKLIST";
    public static final String PARAM_CHECKLIST ="N-FORM";// "CHECKLIST";
    public static final String PARAM_WM = "WM";
    public static final String PARAM_SO = "SO";

    //Modulos
    public static final String MODULE_CHECKLIST = "MODULE_CHECKLIST";
    public static final String MODULE_SO = "MODULE_SO";
    public static final String MODULE_WM= "MODULE_WM";
    public static final String MODULE_TO= "MODULE_TO";


}
