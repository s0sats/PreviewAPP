package com.namoadigital.prj001.util;

import com.namoa_digital.namoa_library.util.ConstantBase;


/**
 * Created by neomatrix on 05/04/17.
 */

public class ConstantBaseApp extends ConstantBase {


    /**
     * PROJECT ID - PRJ001 - SMS
     */
    public static final String PRJ001_CODE = "PRJ001";
    public static final String PRJ001_VERSION = "2.6.2.10";
    public static final String PKG_CLEAN_APP = "61";
    //          UPDATE ESSA VAR NOS APP BASE PKG_CLEAN

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
    public static final String ACT021 = "act021";
    public static final String ACT022 = "act022";
    public static final String ACT023 = "act023";
    public static final String ACT024 = "act024";
    public static final String ACT025 = "act025";
    public static final String ACT026 = "act026";
    public static final String ACT027 = "act027";
    public static final String ACT028 = "act028";
    public static final String ACT029 = "act029";
    public static final String ACT030 = "act030";
    public static final String ACT031 = "act031";
    public static final String ACT032 = "act032";
    public static final String ACT033 = "act033";
    public static final String ACT034 = "act034";
    public static final String ACT035 = "act035";
    public static final String ACT036 = "act036";
    public static final String ACT037 = "act037";
    public static final String ACT038 = "act038";

    //Contantes classes relativas ao chat
    public static final String MY_FIRE_BASE_MESSAGING_SERVICE = "MyFirebaseMessagingService";
    public static final String WBR_BOOTCOMPLETED = "WBR_BootCompleted";
    public static final String SCREEN_STATUS_RECEIVER = "ScreenStatusReceiver";
    public static final String WBR_CONNECTIONS_CHANGE = "WBR_Connections_Change";

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

    public static final String ACT022_REQUESTING_PROCESS = "requesting_process";
    public static final String ACT022_MSTACKVALUES = "act022_mstackvalues";

    public static final String ACT023_SO_HEADER_LIST = "so_header_list";

    public static final String ACT027_IS_SHORTCUT = "ACT027_IS_SHORTCUT";
    public static final String ACT027_ORIGINAL_UPDATE_REQUIRED = "ACT027_ORIGINAL_UPDATE_REQUIRED";
    public static final String ACT028_SERVICE_UPDATED = "ACT028_SERVICE_UPDATED";

    public static final String MAIN_REQUESTING_PROCESS = "main_requesting_process";
    public static final String MAIN_MSTACKVALUES = "main_mstackvalues";
    public static final String MAIN_PRODUCT_CODE = "main_product_code";
    public static final String MAIN_SERIAL_ID = "main_serial_id";
    public static final String MAIN_MD_PRODUCT_SERIAL = "main_md_product_serial";
    public static final String MAIN_IS_SCHEDULE = "main_is_schedule";
    public static final String MAIN_REQUESTING_ACT = "main_requesting_act";
    public static final String MAIN_SERIAL_TRACKING = "main_serial_tracking";
    //Constante de concatenação
    public static final String MAIN_CONCAT_STRING = "@##N@M0@##@";
    public static final String MAIN_CONCAT_STRING_2 = "##@n@m0@@##";


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

    public static String DB_NAME_CHAT = "namoa_chat.db3";
    public static String DB_FULL_CHAT = DB_PATH + "/" + DB_NAME_CHAT;
    public static int DB_VERSION_CHAT;

    public static String DB_MODE_MULTI = "MULTI";
    public static String DB_MODE_SINGLE = "SINGLE";
    public static final String DB_MODE_CHAT = "CHAT";

    /**
     * Directory Definition
     */
    public static String ZIP_PATH;
    public static String IMG_PATH;
    public static String THU_PATH;
    public static String SUPPORT_PATH;
    public static String TOKEN_PATH;
    public static String CHAT_PATH;
    public static String CACHE_CHAT_PATH;

    public static String ZIP_NAME;
    public static String ZIP_NAME_FULL;

    public static String SUPPORT_NAME;
    public static String SUPPORT_NAME_FULL;

    public static String TOKEN_SO_PREFIX;
    public static String TOKEN_SERIAL_PREFIX;

    public static String TOKEN_SO_NAME_FULL;
    public static String TOKEN_SERIAL_NAME_FULL;

    public static String CHAT_PREFIX;
    public static String CHAT_NAME_FULL;

    public static final String THUMB_SUFFIX = "_thumb";

    /**
     * SM_SO_Service Login Parameter
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
    public static final String FOOTER_ZONE_LBL = "footer_zone_lbl";
    public static final String FOOTER_ZONE = "footer_zone";
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
     * SO Search WS
     */
    public static final String WS_SO_SEARCH_PRODUCT_CODE = "sosearchproductcode";
    public static final String WS_SO_SEARCH_SERIAL_ID = "sosearchserialid";
    public static final String WS_SO_SEARCH_SO_MULT = "sosearchmult";
    public static final String WS_SO_SEARCH_SAVE_SERIAL = "so_search_save_serial";
    public static final String WS_SO_SEARCH_CREATE_SERIAL = "so_search_create_serial";

    /**
     * SO Serial Save
     */
    public static final String WS_SO_SERIAL_SAVE_PRODUCT_CODE = "so_serial_save_product_code";
    public static final String WS_SO_SERIAL_SAVE_SERIAL_ID = "so_serial_save_serial_id";
    public static final String WS_SO_SERIAL_SAVE_SO_PREFIX = "so_serial_save_product_code";
    public static final String WS_SO_SERIAL_SAVE_SO_CODE = "so_serial_save_serial_id";
    //As 4 constantes não serão mais utilizadas após processo de save so offline

    /**
     * SO Save
     */
    public static final String WS_SO_SAVE_SO_ACTION = "ws_so_save_so_action";
    public static final String SO_ACTION_EXECUTION = "EXECUTION";


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
    public static final String WS_SERIAL_SEARCH_TRACKING = "serial_search_tracking";
    public static final String WS_SERIAL_SEARCH_EXACT = "ws_serial_search_exact";
    public static final String WS_SERIAL_SEARCH_SAVE_PROCESS = "ws_serial_search_save_process";
    public static final String WS_SERIAL_SEARCH_NEW_PROCESS = "ws_serial_search_new_process";

    /**
     * WS Serial Tracking Search
     */
    public static final String WS_SERIAL_TRACKING_SEARCH_PRODUCT_CODE = "serial_tracking_search_product_code";
    public static final String WS_SERIAL_TRACKING_SEARCH_SERIAL_CODE = "serial_tracking_search_serial_code";
    public static final String WS_SERIAL_TRACKING_SEARCH_TRACKING = "serial_tracking_search_tracking";
    public static final String WS_SERIAL_TRACKING_SEARCH_SITE_CODE = "serial_tracking_search_site_code";

    /**
     * Retorno dos Ws de Envio
     */

    public static final String WS_SEND_RETURN = "ws_send_return";

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
    public static final String LOGIN_ZONE_CODE = "login_zone_code";
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
    public static final int NOTIFICATION_CHAT_MSG = 668;
    public static final int NOTIFICATION_CHAT_ROOM = 669;

    //Helper
    public static final String BACK_ACTION = "back_action";

    //Parametros de Permissões - EV_USER_CUSTOMER_PARAMETERS
    public static final String PARAM_SCHEDULE_CHECKLIST = "SCHEDULE_CHECKLIST";
    public static final String PARAM_CHECKLIST = "CHECKLIST";// "CHECKLIST";
    public static final String PARAM_WM = "WM";
    public static final String PARAM_SO = "SO";
    public static final String PARAM_SO_MOV = "SO_MOV";
    public static final String CLIENT_TYPE_CLIENT = "CLIENT";
    public static final String CLIENT_TYPE_USER = "USER";
    public static final String PARAM_CHAT = "CHAT";

    /**
     * Parametros EV_PROFILE
     */

    public static final String APPROVAL_TYPE = "approval_type";
    public static final String PROFILE_MENU_SO = "PRJ001_SO";
    public static final String PROFILE_MENU_SO_PARAM_APPROVE_CLIENT = "APPROVE_CLIENT";
    public static final String PROFILE_MENU_SO_PARAM_APPROVE_QUALITY = "APPROVE_QUALITY";
    public static final String PROFILE_MENU_SO_PARAM_EXECUTION = "EXECUTION";
    public static final String SO_ORIGIN_CHANGE_APP = "APP";
    public static final String PROFILE_MENU_AP = "PRJ001_AP";
    public static final String PROFILE_MENU_AP_PARAM_CHANGE_STATUS = "CHANGE_STATUS";
    public static final String PROFILE_MENU_AP_PARAM_EDIT = "EDIT";



    //Modulos
    public static final String MODULE_CHECKLIST = "MODULE_CHECKLIST";
    public static final String MODULE_SO = "MODULE_SO";
    public static final String MODULE_SO_SEARCH_SERIAL = "MODULE_SO_SEARCH_SERIAL";
    public static final String MODULE_SO_SEARCH_SERIAL_EXPRESS = "MODULE_SO_SEARCH_SERIAL_EXPRESS";
    public static final String MODULE_WM = "MODULE_WM";
    public static final String MODULE_TO = "MODULE_TO";


    //Constantes Status da SO
    public static final String SO_STATUS_EDIT = "EDIT";
    public static final String SO_STATUS_STOP = "STOP";
    public static final String SO_STATUS_PENDING = "PENDING";
    public static final String SO_STATUS_PROCESS = "PROCESS";
    public static final String SO_STATUS_WAITING_BUDGET = "WAITING_BUDGET";
    public static final String SO_STATUS_WAITING_QUALITY = "WAITING_QUALITY";
    public static final String SO_STATUS_WAITING_CLIENT = "WAITING_CLIENT";
    public static final String SO_STATUS_DONE = "DONE";
    public static final String SO_STATUS_WAITING_SYNC = "WAITING_SYNC";
    public static final String SO_STATUS_CANCELLED = "CANCELLED";
    public static final String SO_STATUS_INCONSISTENT = "INCONSISTENT";
    public static final String SO_STATUS_NOT_EXECUTED = "NOT_EXECUTED";
    public static final String SO_STATUS_BLOCKED = "STOP";
    public static final String SO_SERVICE_TYPE_YES_NO = "YES_NO";
    public static final String SO_SERVICE_TYPE_START_STOP = "START_STOP";

//    public static final String SO_PARAM_PREFIX = "SO_PARAM_PREFIX";
//    public static final String SO_PARAM_CODE = "SO_PARAM_CODE";
//    public static final String SO_PARAM_STATUS = "SO_PARAM_STATUS";
//    public static final String SO_PARAM_CLIENT_TYPE = "SO_PARAM_CLIENT_TYPE";

    public static final String SO_PARAM_RETURN_STATUS = "so_param_return_status";
    public static final String SO_PARAM_RETURN_MSG = "so_param_return_msg";
    public static final String SO_PARAM_CUSTOMER_CODE = "so_param_customer_code";
    public static final String SO_PARAM_SO_PREFIX = "so_param_so_prefix";
    public static final String SO_PARAM_SO_CODE = "so_param_so_code";
    public static final String SO_PARAM_AUTH_TYPE = "so_param_auth_type";

    public static final String SO_PARAM_AUTH_TYPE_BUDGET = "BUDGET";
    public static final String SO_PARAM_AUTH_TYPE_QUALITY = "QUALITY";
    public static final String SO_PARAM_AUTH_TYPE_CLIENT = "CLIENT";

    public static final String SO_PARAM_AUTH_NICK_MAIL = "so_param_auth_nick_mail";
    public static final String SO_PARAM_AUTH_PASSWORD = "so_param_auth_password";
    public static final String SO_PARAM_AUTH_NFC = "so_param_auth_nfc";


    public static final String WS_EXCEPTION_HTTP_STATUS_ERROR = "HTTP_STATUS_ERROR";

    public static final String WS_FCM = "WS_FCM";
    /*
     Constante Status AP
     */
    public static final String SYS_STATUS_EDIT = "EDIT";
    public static final String SYS_STATUS_PROCESS = "PROCESS";
    public static final String SYS_STATUS_CANCELLED = "CANCELLED";
    public static final String SYS_STATUS_DONE = "DONE";
    public static final String SYS_STATUS_WAITING_ACTION = "WAITING_ACTION";
    /*
    * Constantes CHAT
    */
    //Eventos Client
    public static final String CHAT_EVENT_C_LOGIN = "cLogin";
    public static final String CHAT_EVENT_C_ROOM = "cRoom";
    public static final String CHAT_EVENT_C_ERROR = "cError";
    public static final String CHAT_EVENT_C_ERROR_LOGIN = "cErrorLogin";
    public static final String CHAT_EVENT_C_PENDING_MESSAGES = "cPendingMessages";
    public static final String CHAT_EVENT_C_HISTORICAL_MESSAGES = "cHistoricalMessages";
    public static final String CHAT_EVENT_C_MESSAGE = "cMessage";
    public static final String CHAT_EVENT_C_MESSAGE_FCM = "cMessageFCM";
    public static final String CHAT_EVENT_C_MESSAGE_TMP = "cMessageTmp";
    public static final String CHAT_EVENT_C_ADD_ROOM = "cAddRoom";
    public static final String CHAT_EVENT_C_REMOVE_ROOM = "cRemoveRoom";
    public static final String CHAT_EVENT_C_ALL_DELIVERED = "cAllDelivered";
    public static final String CHAT_EVENT_C_ALL_READ = "cAllRead";
    public static final String CHAT_EVENT_C_ROOM_PRIVATE = "cRoomPrivateCustomer";
    public static final String CHAT_EVENT_C_ROOM_PRIVATE_REMOVE = "cRoomPrivateRemove";

    //Eventos Server
    public static final String CHAT_EVENT_S_LOGIN = "sLogin";
    public static final String CHAT_EVENT_S_ROOM = "sRoom";
    public static final String CHAT_EVENT_S_PENDING_MESSAGES = "sPendingMessages";
    public static final String CHAT_EVENT_S_HISTORICAL_MESSAGES = "sHistoricalMessages";
    public static final String CHAT_EVENT_S_MESSAGE = "sMessage";
    public static final String CHAT_EVENT_S_MESSAGE2 = "sMessage2";
    public static final String CHAT_EVENT_S_MESSAGE_TMP = "sMessageTmp";
    public static final String CHAT_EVENT_S_DELIVERED = "sDelivered";
    public static final String CHAT_EVENT_S_READ = "sRead";
    public static final String CHAT_EVENT_S_ROOM_PRIVATE = "sRoomPrivateCustomer";
    public static final String CHAT_EVENT_POST_ROOM_PRIVATE = "POST_ROOM_PRIVATE";
    public static final String CHAT_EVENT_S_LEAVEROOM = "sLeaveRoom";
    public static final String CHAT_EVENT_POST_LEAVEROOM = "EVENT_POST_LEAVEROOM";

    //
    public static final String CHAT_WS_JSON_PARAM = "WS_JSON_PARAM";
    public static final String CHAT_WS_EVENT_PARAM = "WS_EVENT_PARAM";
    public static final String CHAT_WS_HISTORICAL_ACTION_PARAM = "WS_HISTORICAL_ACTION_PARAM";
    public static final String CHAT_WS_MSG_TMP_PARAM = "WS_MSG_TMP_PARAM";
    public static final String CHAT_WS_MSG_COUNTER_PARAM = "WS_MSG_COUNTER_PARAM";
    public static final String CHAT_MESSAGE_TYPE_IMAGE = "IMAGE";
    public static final String CHAT_MESSAGE_TYPE_TEXT = "TEXT";
    public static final String CHAT_MESSAGE_TYPE_TRANSLATE = "TRANSLATE";
    public static final String CHAT_ROOM_TYPE_WORKGROUP = "WORKGROUP";
    public static final String CHAT_ROOM_TYPE_SO = "SO";
    public static final String CHAT_ROOM_TYPE_PA = "FORM_AP";
    public static final String CHAT_ROOM_TYPE_SYS = "SYS";
    public static final String CHAT_ROOM_TYPE_PRIVATE_CUSTOMER = "PRIVATE_CUSTOMER";
    public static final String CHAT_WS_SOCKET_ID_PARAM = "WS_SOCKET_ID_PARAM";
    public static final String CHAT_WS_ROOM_CODE_PARAM = "WS_ROOM_CODE_PARAM";
    public static final String CHAT_WS_ROOM_PRIVATE_ACTIVE_PARAM = "WS_ROOM_PRIVATE_ACTIVE_PARAM";
    //filters
    public static final String CHAT_BR_FILTER_ROOM = "CHAT_BR_FILTER_ROOM";
    public static final String CHAT_BR_FILTER = "CHAT_BR_FILTER";
    public static final String CHAT_BR_FILTER_DOWNLOAD = "CHAT_BR_FILTER_DOWNLOAD";
    public static final String CHAT_FINISH_ACT_FILTER = "CHAT_FINISH_ACT_FILTER";
    //
    public static final String CHAT_BR_TYPE = "CHAT_BR_TYPE";
    public static final String CHAT_BR_PARAM = "CHAT_BR_PARAM";
    public static final String CHAT_BR_PARAM_RECONNECTING_QTD = "CHAT_BR_PARAM_RECONNECTING_QTD";
    public static final String CHAT_BR_TYPE_CHAT_STATUS_CHANGE = "CHAT_BR_TYPE_CHAT_STATUS_CHANGE";
    public static final String CHAT_BR_TYPE_CHAT_LOGGED_STATUS_CHANGE = "CHAT_LOGGED_STATUS_CHANGE";
    public static final String CHAT_BR_TYPE_ROOM = "CHAT_BR_TYPE_ROOM";
    public static final String CHAT_BR_TYPE_MSG = "CHAT_BR_TYPE_MSG";
    public static final String CHAT_BR_TYPE_ROOM_INFO = "CHAT_BR_TYPE_ROOM_INFO";
    public static final String CHAT_BR_TYPE_MSG_SCROLL_UP = "CHAT_BR_TYPE_MSG_SCROLL_UP";
    public static final String CHAT_BR_TYPE_MSG_TMP = "CHAT_BR_TYPE_MSG_TMP";
    public static final String CHAT_BR_TYPE_MSG_ALL_DELIVERED = "CHAT_BR_TYPE_MSG_ALL_DELIVERED";
    public static final String CHAT_BR_TYPE_MSG_ALL_READ = "CHAT_BR_TYPE_MSG_ALL_READ";
    public static final String CHAT_BR_TYPE_RECONNECTED = "CHAT_BR_TYPE_RECONNECTED";
    public static final String CHAT_BR_TYPE_RECONNECTING = "CHAT_BR_TYPE_RECONNECTING";
    public static final String CHAT_BR_TYPE_ROOM_PRIVATE_ADD = "CHAT_BR_TYPE_ROOM_PRIVATE_ADD";
    public static final String CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE = "CHAT_BR_TYPE_ROOM_PRIVATE_REMOVE";
    public static final String CHAT_BR_TYPE_LEAVE_ROOM = "CHAT_BR_TYPE_LEAVE_ROOM";
    public static final String CHAT_PREFERENCE_MSG_PREFIX = "CHAT_PREFERENCE_MSG_PREFIX";
    public static final String CHAT_PREFERENCE_MSG_CODE = "CHAT_PREFERENCE_MSG_CODE";
    public static final String CHAT_PREFERENCE_MSG_TOKEN = "CHAT_PREFERENCE_MSG_TOKEN";
    public static final String CHAT_HISTORICAL_MSG_ACTION_LOGIN = "LOGIN";
    public static final String CHAT_HISTORICAL_MSG_ACTION_SCROLL_UP = "SCROLL_UP";
    public static final String CHAT_NOTIFICATION_TYPE_MESSAGE = "MESSAGE";
    public static final String CHAT_NOTIFICATION_TYPE_RECONNECTING = "RECONNECTING";
    public static final String CHAT_NOTIFICATION_TYPE_CHAT = "CHAT";
    public static final String CHAT_START_SERVICE_CALLER = "CHAT_START_SERVICE_CALLER";
    public static final String CHAT_NOTIFICATION_FCM_MSG = "<CHAT_MSG>";
    public static final String CHAT_NOTIFICATION_FCM_ADD_ROOM = "<CHAT_ADD_ROOM>";
    public static final String CHAT_NOTIFICATION_FCM_REMOVE_ROOM = "<CHAT_REMOVE_ROOM>";

    //Errors
    public static final String CHAT_ERROR_CHAT_SESSION_NOT_FOUND = "CHAT_SESSION_NOT_FOUND";
    public static final String CHAT_ERROR_SESSION_NOT_FOUND = "SESSION_NOT_FOUND";
    public static final String CHAT_ERROR_CUSTOMER_NOT_ACCESS_CHAT = "CUSTOMER_NOT_ACCESS_CHAT";
    //
    public static final String CHAT_START_WITH_IMAGE_MSG = "{\"message\":{\"type\":\"IMAGE\",";
    public static final String CHAT_NO_USER_IMAGE = "no_user";

    //Chat Reload
    public static final String CHAT_RELOAD = "chat_reload";
    public static final String CHAT_ROOM_POSITION = "chat_room_position";

}
