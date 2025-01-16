package com.namoadigital.prj001.util;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Constant extends ConstantBaseApp {


    /**
     * WebService
     */

    /**
     * Desenvolvimento
     */
    public static String WS_GETCUSTOMERS;
    public static String WS_GET_CUSTOMERS_SITE_LICENSE;
    public static String WS_GETSESSION;
    public static String WS_GOOGLE;

    public static String WS_SYNC;
    public static String WS_SESSION;
    public static String WS_SAVE;
    public static String WS_SERIAL;
    public static String WS_SERIAL_SEARCH;
    public static String WS_SERIAL_TRACKING_SEARCH;
    public static String WS_PRODUCT_SERIAL_STRUCTURE_SEARCH;
    public static String WS_PRODUCT_SERIAL_BACKUP;

    public static String WS_UPLOAD;

    public static String WS_UPLOAD_CHAT;

    public static String WS_LOGOUT;
    public static String WS_SO_SEARCH;
    public static String WS_SO_SERVICE_SEARCH;
    public static String WS_SO_SERVICE_EDIT_GET;
    public static String WS_SO_SERVICE_EDIT_SET;
    public static String WS_SO_SERVICE_REMOVE;
    public static String WS_SO_CLIENT_LIST;
    public static String WS_SO_FAVORITE_LIST;
    public static String WS_SO_CREATION;
    public static String WS_SO_PRODUCT_EVENT_CANCEL;
    public static String WS_SO_NEXT_ORDERS;
    public static String WS_SO_CREATE_ROOM;
    public static String WS_SO_STATUS_CHANGE;
    public static String WS_SO_PRIROTY_CHANGE;
    public static String WS_USER_LIST_SEARCH;
    public static String WS_SO_SAVE;
    public static String WS_SO_PACK_SERVICE;
    public static String WS_AP_SEARCH;
    public static String WS_AP_SAVE;
    public static String WS_SERIAL_SAVE;
    public static String WS_IO_SERIAL_PROCESS_SEARCH;
    public static String WS_IO_SERIAL_PROCESS_DOWNLOAD;
    public static String WS_IO_MOVE_SEARCH;
    public static String WS_IO_INBOUND_SEARCH;
    public static String WS_IO_OUTBOUND_SEARCH;
    public static String WS_IO_MOVE_DOWNLOAD;
    public static String WS_IO_MOVE_SAVE;
    public static String WS_IO_INBOUND_DOWNLOAD;
    public static String WS_IO_OUTBOUND_DOWNLOAD;
    public static String WS_IO_MASTER_DATA;
    public static String WS_IO_FROM_OUTBOUND_SEARCH;
    public static String WS_IO_INBOUND_HEADER_SAVE;
    public static String WS_IO_OUTBOUND_HEADER_SAVE;
    public static String WS_IO_ADDRESS_SUGGESTION;
    public static String WS_IO_BLIND_SAVE;
    public static String WS_IO_INBOUND_ITEM_SAVE;
    public static String WS_IO_OUTBOUND_ITEM_SAVE;
    public static String WS_IO_TRANSPORT_ORDER_OUT_SEARCH;
    public static String WS_TICKET_DOWNLOAD;
    public static String WS_TICKET_DOWNLOAD_SERIAL;
    public static String WS_TICKET_CHECKIN;
    public static String WS_TICKET_SAVE;
    public static String WS_NEXT_TICKET;
    public static String WS_TICKET_DOWNLOAD_CLIENT_CONTRACT;
    public static String WS_TICKET_GET_WORKGROUP_LIST;
    public static String WS_TICKET_SERVER_MAIN_USER;
    public static String WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS;
    public static String WS_TICKET_CREATION;
    public static String WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC;
    public static String WS_ENABLE_NFC;
    public static String WS_CANCEL_NFC;
    public static String WS_SERVER_AUTH_USER;
    public static String WS_SO_PACK_EXPRESSION;
    public static String WEB_SOCKET_CHAT;
    public static String WS_UPLOAD_NODE_CHAT;
    public static String WS_CHAT_ROOM_INFO;
    public static String WS_CHAT_MESSAGE_INFO;
    public static String WS_CHAT_ROOM_USER_LIST;
    public static String WS_CHAT_MESSAGE_DIST;
    public static String WS_CHAT_POST_DELIVERED;
    public static String WS_CHAT_ROOM_PRIVATE;
    public static String WS_CHAT_LEAVE_ROOM;
    public static String WS_CHAT_ROOM_AP;
    public static String WS_CHAT_ADD_USER_FORM_AP;
    public static String WS_SERIAL_LOG;
    public static String WS_GENERATE_FORM_PDF;
    public static String WS_WORKGROUP_MEMBER_LIST;
    public static String WS_WORKGROUP_MEMBER_EDIT;
    public static String WS_SCHEDULE_NOT_EXECUTED;
    public static String WS_SERIAL_SITE_INV;
    public static String WS_DESTINATION_AVAILABLE;
    public static String WS_SELECT_DESTINATION;
    public static String WS_DESTINATION_CHANGE_STATUS;
    public static String WS_TRIP_AVAILABLE_USERS;
    public static String WS_TRIP_SAVE_USER;
    public static String WS_TRIP_USER_POSITION;
    public static String WS_TRIP;
    public static String WS_TRIP_NEW;
    public static String WS_TRIP_FLEET_SET;
    public static String WS_TRIP_CHANGE_STATUS;
    public static String WS_TRIP_ORIGIN_SET;
    public static String WS_TRIP_DESTINATION_EDIT;
    public static String WS_TRIP_EVENT;
    public static String WS_TRIP_UPDATE;
    public static String RESERVED_LIST_USER;
    public static String SET_LIST_USER;
    public static String WS_HOST;
    public static int WS_PORT = 443;
    public static int WS_TIMEOUT = 3000;

    public static void refreshUrl() {
        WS_HOST = WS_PREFIX + ".namoadigital.com";
        String HTTPS = "https://";
        WS_GETCUSTOMERS = HTTPS + WS_HOST + "/ws/prj001/server_get_customer.ws";
        WS_GET_CUSTOMERS_SITE_LICENSE = HTTPS + WS_HOST + "/ws/prj001/server_get_customer_site.ws";
        WS_GETSESSION = HTTPS + WS_HOST + "/ws/prj001/server_get_session.ws";
        WS_GOOGLE = HTTPS + WS_HOST + "/ws/prj001/server_gcm_set.ws";
        WS_SYNC = HTTPS + WS_HOST + "/ws/prj001/server_sync.ws";
        WS_SESSION = HTTPS + WS_HOST + "/ws/prj001/server_get_session.ws";
        WS_SAVE = HTTPS + WS_HOST + "//ws/prj001/server_save_checklist.ws";
        WS_SERIAL = HTTPS + WS_HOST + "/ws/prj001/server_serial.ws";
        WS_SERIAL_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_product_serial.ws";
        WS_SERIAL_TRACKING_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_serial_tracking.ws";
        WS_PRODUCT_SERIAL_STRUCTURE_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_product_serial_structure.ws";
        WS_PRODUCT_SERIAL_BACKUP = HTTPS + WS_HOST + "/ws/prj001/server_product_serial_backup.ws";
        WS_UPLOAD = HTTPS + WS_HOST + "/inc/ws/aws_file.ws";
        WS_UPLOAD_CHAT = HTTPS + WS_HOST + "/inc/ws/aws_file_node.ws";
        WS_LOGOUT = HTTPS + WS_HOST + "/ws/prj001/server_logout.ws";
        WS_SO_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_so.ws";
        WS_SO_SERVICE_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_pack_service_list_app_2.ws";
        WS_SO_SERVICE_EDIT_GET = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_pack_service_edit_get.ws";
        WS_SO_SERVICE_EDIT_SET = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_pack_service_edit_set.ws";
        WS_SO_SERVICE_REMOVE = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_pack_service_cancel_app.ws";
        WS_SO_CLIENT_LIST = HTTPS + WS_HOST + "/ws/prj001/sm_order/server_client.ws";
        WS_SO_FAVORITE_LIST = HTTPS + WS_HOST + "/ws/prj001/sm_order/server_main.ws";
        WS_SO_CREATION = HTTPS + WS_HOST + "/ws/prj001/sm_order/server_save_so.ws";
        WS_SO_PRODUCT_EVENT_CANCEL = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_product_event_cancel.ws";
        WS_SO_NEXT_ORDERS = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_to_be_executed.ws";
        WS_SO_CREATE_ROOM = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_create_room.ws";
        WS_SO_STATUS_CHANGE = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_change_status.ws";
        WS_SO_PRIROTY_CHANGE = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_change_priority.ws";
        WS_USER_LIST_SEARCH = HTTPS + WS_HOST + "/ws/prj001/wg/server_user_list.ws";
        WS_SO_SAVE = HTTPS + WS_HOST + "/ws/prj001/server_save_so.ws";
        WS_SO_PACK_SERVICE = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_pack_service_new_app_2.ws";
        WS_AP_SEARCH = HTTPS + WS_HOST + "/ws/prj001/server_ap.ws";
        WS_AP_SAVE = HTTPS + WS_HOST + "/ws/prj001/server_save_ap.ws";
        WS_SERIAL_SAVE = HTTPS + WS_HOST + "/ws/prj001/server_save_serial.ws";
        WS_IO_SERIAL_PROCESS_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_product_serial.ws";
        WS_IO_SERIAL_PROCESS_DOWNLOAD = HTTPS + WS_HOST + "/ws/prj001/io/server_product_serial_obj.ws";
        WS_IO_MOVE_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_move.ws";
        WS_IO_INBOUND_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_inbound.ws";
        WS_IO_OUTBOUND_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_outbound.ws";
        WS_IO_MOVE_DOWNLOAD = HTTPS + WS_HOST + "/ws/prj001/io/server_move_obj.ws";
        WS_IO_MOVE_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_move_save.ws";
        WS_IO_INBOUND_DOWNLOAD = HTTPS + WS_HOST + "/ws/prj001/io/server_inbound_obj.ws";
        WS_IO_OUTBOUND_DOWNLOAD = HTTPS + WS_HOST + "/ws/prj001/io/server_outbound_obj.ws";
        WS_IO_MASTER_DATA = HTTPS + WS_HOST + "/ws/prj001/io/server_master_data.ws";
        WS_IO_FROM_OUTBOUND_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_outbound_search.ws";
        WS_IO_INBOUND_HEADER_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_inbound_save.ws";
        WS_IO_OUTBOUND_HEADER_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_outbound_save.ws";
        WS_IO_ADDRESS_SUGGESTION = HTTPS + WS_HOST + "/ws/prj001/io/server_address_suggestion.ws";
        WS_IO_BLIND_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_blind_save.ws";
        WS_IO_INBOUND_ITEM_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_inbound_item_save.ws";
        WS_IO_OUTBOUND_ITEM_SAVE = HTTPS + WS_HOST + "/ws/prj001/io/server_outbound_item_save.ws";
        WS_IO_TRANSPORT_ORDER_OUT_SEARCH = HTTPS + WS_HOST + "/ws/prj001/io/server_transport_order_out_search.ws";
        WS_TICKET_DOWNLOAD = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket.ws";
        WS_TICKET_DOWNLOAD_SERIAL = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_serial.ws";
        WS_TICKET_CHECKIN = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_checkin.ws";
        WS_TICKET_SAVE = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_set.ws";
        WS_NEXT_TICKET = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_next.ws";
        WS_TICKET_DOWNLOAD_CLIENT_CONTRACT = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_search.ws";
        WS_TICKET_GET_WORKGROUP_LIST = HTTPS + WS_HOST + "/ws/prj001/tk/server_workgroup.ws";
        WS_TICKET_SERVER_MAIN_USER = HTTPS + WS_HOST + "/ws/prj001/tk/server_main_user.ws";
        WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_search_not_focus.ws";
        WS_TICKET_CREATION = HTTPS + WS_HOST + "/ws/prj001/tk/server_ticket_create.ws";
        WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC = HTTPS + WS_HOST + "/ws/prj001/server_open_without_focus_and_historic.ws";
        WS_ENABLE_NFC = HTTPS + WS_HOST + "/ws/prj001/server_enable_nfc_auth.ws";
        WS_CANCEL_NFC = HTTPS + WS_HOST + "/ws/prj001/server_cancel_nfc_auth.ws";
        WS_SERVER_AUTH_USER = HTTPS + WS_HOST + "/ws/prj001/server_auth_user.ws";
        WS_SO_PACK_EXPRESSION = HTTPS + WS_HOST + "/ws/prj001/server_save_so_pack_express.ws";
        WEB_SOCKET_CHAT = "https://nchat" + WS_HOST;
        WS_UPLOAD_NODE_CHAT = WEB_SOCKET_CHAT + "/messageFile";
        WS_CHAT_ROOM_INFO = WEB_SOCKET_CHAT + "/roomMemberList";
        WS_CHAT_MESSAGE_INFO = WEB_SOCKET_CHAT + "/messageDeliverList";
        WS_CHAT_ROOM_USER_LIST = WEB_SOCKET_CHAT + "/userList";
        WS_CHAT_MESSAGE_DIST = WEB_SOCKET_CHAT + "/messageDist?";
        WS_CHAT_POST_DELIVERED = WEB_SOCKET_CHAT + "/delivered";
        WS_CHAT_ROOM_PRIVATE = WEB_SOCKET_CHAT + "/roomPrivateCustomer";
        WS_CHAT_LEAVE_ROOM = WEB_SOCKET_CHAT + "/leaveRoom";
        WS_CHAT_ROOM_AP = WEB_SOCKET_CHAT + "/joinFormAp";
        WS_CHAT_ADD_USER_FORM_AP = WEB_SOCKET_CHAT + "/addUserFormAp";
        WS_SERIAL_LOG = HTTPS + WS_HOST + "/ws/prj001/server_serial_log.ws";
        WS_GENERATE_FORM_PDF = HTTPS + WS_HOST + "/ws/prj001/server_checklist_url.ws";
        WS_WORKGROUP_MEMBER_LIST = HTTPS + WS_HOST + "/ws/prj001/wg/server_workgroup_member_list.ws";
        WS_WORKGROUP_MEMBER_EDIT = HTTPS + WS_HOST + "//ws/prj001/wg/server_workgroup_member_set.ws";
        WS_SCHEDULE_NOT_EXECUTED = HTTPS + WS_HOST + "/ws/prj001/server_not_executed_schedule.ws";
        WS_SERIAL_SITE_INV = HTTPS + WS_HOST + "/ws/prj001/server_site_serial_inv.ws";
        WS_DESTINATION_AVAILABLE = HTTPS + WS_HOST + "/ws/prj001/trip/trip_available_destinations.ws";
        WS_SELECT_DESTINATION = HTTPS + WS_HOST + "/ws/prj001/trip/trip_destination_add.ws";
        WS_DESTINATION_CHANGE_STATUS = HTTPS + WS_HOST + "/ws/prj001/trip/trip_destination_status.ws";
        WS_TRIP_AVAILABLE_USERS = HTTPS + WS_HOST + "/ws/prj001/trip/trip_available_users.ws";
        WS_TRIP_SAVE_USER = HTTPS + WS_HOST + "/ws/prj001/trip/trip_user_action.ws";
        WS_TRIP_USER_POSITION = HTTPS + WS_HOST + "/ws/prj001/trip/user_position.ws";
        WS_TRIP = HTTPS + WS_HOST + "/ws/prj001/trip/trip.ws";
        WS_TRIP_NEW = HTTPS + WS_HOST + "/ws/prj001/trip/trip_new.ws";
        WS_TRIP_FLEET_SET = HTTPS + WS_HOST + "/ws/prj001/trip/trip_fleet_set.ws";
        WS_TRIP_CHANGE_STATUS = HTTPS + WS_HOST + "/ws/prj001/trip/trip_status.ws";
        WS_TRIP_ORIGIN_SET = HTTPS + WS_HOST + "/ws/prj001/trip/trip_origin_set.ws";
        WS_TRIP_DESTINATION_EDIT = HTTPS + WS_HOST + "/ws/prj001/trip/trip_destination_edit.ws";
        WS_TRIP_EVENT = HTTPS + WS_HOST + "/ws/prj001/trip/trip_event_set.ws";
        WS_TRIP_UPDATE = HTTPS + WS_HOST + "/ws/prj001/trip/trip_full_update.ws";
        RESERVED_LIST_USER = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_user.ws";
        SET_LIST_USER = HTTPS + WS_HOST + "/ws/prj001/server_sm_so_reserved_user.ws";
    }
}
