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
    public static String WS_GETCUSTOMERS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_customer.ws";
    public static String WS_GET_CUSTOMERS_SITE_LICENSE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_customer_site.ws";
    public static String WS_GETSESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_session.ws";
    public static String WS_GOOGLE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_gcm_set.ws";

    public static String WS_SYNC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sync.ws";
    public static String WS_SESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_session.ws";
    public static String WS_SAVE = "https://" + WS_PREFIX + ".namoadigital.com//ws/prj001/server_save_checklist.ws";
    public static String WS_SERIAL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial.ws";
    public static String WS_SERIAL_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial.ws";
    public static String WS_SERIAL_TRACKING_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial_tracking.ws";
    public static String WS_PRODUCT_SERIAL_STRUCTURE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial_structure.ws";
    public static String WS_PRODUCT_SERIAL_BACKUP = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial_backup.ws";

    public static String WS_UPLOAD = "https://" + WS_PREFIX + ".namoadigital.com/inc/ws/aws_file.ws";

    public static String WS_UPLOAD_CHAT = "https://" + WS_PREFIX + ".namoadigital.com/inc/ws/aws_file_node.ws";

    public static String WS_LOGOUT = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_logout.ws";
    public static String WS_SO_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_so.ws";
    public static String WS_SO_SERVICE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_list_app_2.ws";
    public static String WS_SO_SERVICE_EDIT_GET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_get.ws";
    public static String WS_SO_SERVICE_EDIT_SET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_set.ws";
    public static String WS_SO_SERVICE_REMOVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_cancel_app.ws";
    public static String WS_SO_CLIENT_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_client.ws";
    public static String WS_SO_FAVORITE_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_main.ws";
    public static String WS_SO_CREATION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_save_so.ws";
    public static String WS_SO_PRODUCT_EVENT_CANCEL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_product_event_cancel.ws";
    public static String WS_SO_NEXT_ORDERS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_to_be_executed.ws";
    public static String WS_SO_CREATE_ROOM = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_create_room.ws";
    public static String WS_SO_STATUS_CHANGE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_change_status.ws";
    public static String WS_SO_PRIROTY_CHANGE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_change_priority.ws";


    public static String WS_USER_LIST_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/wg/server_user_list.ws";

    public static String WS_SO_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_so.ws";
    public static String WS_SO_PACK_SERVICE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_new_app_2.ws";

    public static String WS_AP_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_ap.ws";
    public static String WS_AP_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_ap.ws";

    public static String WS_SERIAL_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_serial.ws";
    //IO
    public static String WS_IO_SERIAL_PROCESS_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_product_serial.ws";
    public static String WS_IO_SERIAL_PROCESS_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_product_serial_obj.ws";
    public static String WS_IO_MOVE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move.ws";
    public static String WS_IO_INBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound.ws";
    public static String WS_IO_OUTBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound.ws";
    public static String WS_IO_MOVE_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move_obj.ws";
    public static String WS_IO_MOVE_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move_save.ws";
    public static String WS_IO_INBOUND_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_obj.ws";
    public static String WS_IO_OUTBOUND_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_obj.ws";
    public static String WS_IO_MASTER_DATA = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_master_data.ws";
    public static String WS_IO_FROM_OUTBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_search.ws";
    public static String WS_IO_INBOUND_HEADER_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_save.ws";
    public static String WS_IO_OUTBOUND_HEADER_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_save.ws";
    public static String WS_IO_ADDRESS_SUGGESTION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_address_suggestion.ws";
    public static String WS_IO_BLIND_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_blind_save.ws";
    public static String WS_IO_INBOUND_ITEM_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_item_save.ws";
    public static String WS_IO_OUTBOUND_ITEM_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_item_save.ws";
    public static String WS_IO_TRANSPORT_ORDER_OUT_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_transport_order_out_search.ws";
    //TICKET
    public static String WS_TICKET_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket.ws";
    public static String WS_TICKET_DOWNLOAD_SERIAL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_serial.ws";
    public static String WS_TICKET_CHECKIN = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_checkin.ws";
    public static String WS_TICKET_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_set.ws";
    public static String WS_NEXT_TICKET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_next.ws";
    public static String WS_TICKET_DOWNLOAD_CLIENT_CONTRACT = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_search.ws";
    public static String WS_TICKET_GET_WORKGROUP_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_workgroup.ws";
    public static String WS_TICKET_SERVER_MAIN_USER = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_main_user.ws";
    public static String WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_search_not_focus.ws";
    public static String WS_TICKET_CREATION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_create.ws";
    public static String WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_open_without_focus_and_historic.ws";

    public static String WS_ENABLE_NFC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_enable_nfc_auth.ws";
    public static String WS_CANCEL_NFC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_cancel_nfc_auth.ws";

    public static String WS_SERVER_AUTH_USER = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_auth_user.ws";
    public static String WS_SO_PACK_EXPRESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_so_pack_express.ws";

    public static String WEB_SOCKET_CHAT = "https://nchat" + WS_PREFIX + ".namoadigital.com";
    public static String WS_UPLOAD_NODE_CHAT = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageFile";
    public static String WS_CHAT_ROOM_INFO = "https://nchat" + WS_PREFIX + ".namoadigital.com/roomMemberList";
    public static String WS_CHAT_MESSAGE_INFO = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageDeliverList";
    public static String WS_CHAT_ROOM_USER_LIST = "https://nchat" + WS_PREFIX + ".namoadigital.com/userList";
    public static String WS_CHAT_MESSAGE_DIST = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageDist?";
    public static String WS_CHAT_POST_DELIVERED = "https://nchat" + WS_PREFIX + ".namoadigital.com/delivered";
    public static String WS_CHAT_ROOM_PRIVATE = "https://nchat" + WS_PREFIX + ".namoadigital.com/roomPrivateCustomer";
    public static String WS_CHAT_LEAVE_ROOM = "https://nchat" + WS_PREFIX + ".namoadigital.com/leaveRoom";
    public static String WS_CHAT_ROOM_AP = "https://nchat" + WS_PREFIX + ".namoadigital.com/joinFormAp";
    public static String WS_CHAT_ADD_USER_FORM_AP = "https://nchat" + WS_PREFIX + ".namoadigital.com/addUserFormAp";
    //
    public static String WS_SERIAL_LOG = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial_log.ws";
    public static String WS_GENERATE_FORM_PDF = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_checklist_url.ws";
    public static String WS_WORKGROUP_MEMBER_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/wg/server_workgroup_member_list.ws";
    public static String WS_WORKGROUP_MEMBER_EDIT = "https://" + WS_PREFIX + ".namoadigital.com//ws/prj001/wg/server_workgroup_member_set.ws";
    //
    public static String WS_SCHEDULE_NOT_EXECUTED = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_not_executed_schedule.ws";
    // Connection Test
    public static String WS_HOST = WS_PREFIX + ".namoadigital.com";
    public static int WS_PORT = 443;
    public static int WS_TIMEOUT = 3000;
    public static void refreshUrl(){
        WS_GETCUSTOMERS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_customer.ws";
        WS_GET_CUSTOMERS_SITE_LICENSE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_customer_site.ws";
        WS_GETSESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_session.ws";
        WS_GOOGLE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_gcm_set.ws";
        WS_SYNC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sync.ws";
        WS_SESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_get_session.ws";
        WS_SAVE = "https://" + WS_PREFIX + ".namoadigital.com//ws/prj001/server_save_checklist.ws";
        WS_SERIAL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial.ws";
        WS_SERIAL_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial.ws";
        WS_SERIAL_TRACKING_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial_tracking.ws";
        WS_PRODUCT_SERIAL_STRUCTURE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial_structure.ws";
        WS_PRODUCT_SERIAL_BACKUP = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_product_serial_backup.ws";
        WS_UPLOAD = "https://" + WS_PREFIX + ".namoadigital.com/inc/ws/aws_file.ws";
        WS_UPLOAD_CHAT = "https://" + WS_PREFIX + ".namoadigital.com/inc/ws/aws_file_node.ws";
        WS_LOGOUT = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_logout.ws";
        WS_SO_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_so.ws";
        WS_SO_SERVICE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_list_app_2.ws";
        WS_SO_SERVICE_EDIT_GET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_get.ws";
        WS_SO_SERVICE_EDIT_SET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_set.ws";
        WS_SO_SERVICE_REMOVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_cancel_app.ws";
        WS_SO_CLIENT_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_client.ws";
        WS_SO_FAVORITE_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_main.ws";
        WS_SO_CREATION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/sm_order/server_save_so.ws";
        WS_SO_PRODUCT_EVENT_CANCEL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_product_event_cancel.ws";
        WS_SO_NEXT_ORDERS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_to_be_executed.ws";
        WS_SO_CREATE_ROOM = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_create_room.ws";
        WS_SO_STATUS_CHANGE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_change_status.ws";
        WS_SO_PRIROTY_CHANGE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_change_priority.ws";
        WS_USER_LIST_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/wg/server_user_list.ws";
        WS_SO_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_so.ws";
        WS_SO_PACK_SERVICE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_sm_so_pack_service_new_app_2.ws";
        WS_AP_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_ap.ws";
        WS_AP_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_ap.ws";
        WS_SERIAL_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_serial.ws";
        WS_IO_SERIAL_PROCESS_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_product_serial.ws";
        WS_IO_SERIAL_PROCESS_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_product_serial_obj.ws";
        WS_IO_MOVE_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move.ws";
        WS_IO_INBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound.ws";
        WS_IO_OUTBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound.ws";
        WS_IO_MOVE_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move_obj.ws";
        WS_IO_MOVE_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_move_save.ws";
        WS_IO_INBOUND_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_obj.ws";
        WS_IO_OUTBOUND_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_obj.ws";
        WS_IO_MASTER_DATA = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_master_data.ws";
        WS_IO_FROM_OUTBOUND_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_search.ws";
        WS_IO_INBOUND_HEADER_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_save.ws";
        WS_IO_OUTBOUND_HEADER_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_save.ws";
        WS_IO_ADDRESS_SUGGESTION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_address_suggestion.ws";
        WS_IO_BLIND_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_blind_save.ws";
        WS_IO_INBOUND_ITEM_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_inbound_item_save.ws";
        WS_IO_OUTBOUND_ITEM_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_outbound_item_save.ws";
        WS_IO_TRANSPORT_ORDER_OUT_SEARCH = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/io/server_transport_order_out_search.ws";
        WS_TICKET_DOWNLOAD = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket.ws";
        WS_TICKET_DOWNLOAD_SERIAL = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_serial.ws";
        WS_TICKET_CHECKIN = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_checkin.ws";
        WS_TICKET_SAVE = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_set.ws";
        WS_NEXT_TICKET = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_next.ws";
        WS_TICKET_DOWNLOAD_CLIENT_CONTRACT = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_search.ws";
        WS_TICKET_GET_WORKGROUP_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_workgroup.ws";
        WS_TICKET_SERVER_MAIN_USER = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_main_user.ws";
        WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_search_not_focus.ws";
        WS_TICKET_CREATION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/tk/server_ticket_create.ws";
        WS_DOWNLOAD_NOT_FOCUS_AND_HISTORIC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_open_without_focus_and_historic.ws";
        WS_ENABLE_NFC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_enable_nfc_auth.ws";
        WS_CANCEL_NFC = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_cancel_nfc_auth.ws";
        WS_SERVER_AUTH_USER = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_auth_user.ws";
        WS_SO_PACK_EXPRESSION = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_save_so_pack_express.ws";
        WEB_SOCKET_CHAT = "https://nchat" + WS_PREFIX + ".namoadigital.com";
        WS_UPLOAD_NODE_CHAT = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageFile";
        WS_CHAT_ROOM_INFO = "https://nchat" + WS_PREFIX + ".namoadigital.com/roomMemberList";
        WS_CHAT_MESSAGE_INFO = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageDeliverList";
        WS_CHAT_ROOM_USER_LIST = "https://nchat" + WS_PREFIX + ".namoadigital.com/userList";
        WS_CHAT_MESSAGE_DIST = "https://nchat" + WS_PREFIX + ".namoadigital.com/messageDist?";
        WS_CHAT_POST_DELIVERED = "https://nchat" + WS_PREFIX + ".namoadigital.com/delivered";
        WS_CHAT_ROOM_PRIVATE = "https://nchat" + WS_PREFIX + ".namoadigital.com/roomPrivateCustomer";
        WS_CHAT_LEAVE_ROOM = "https://nchat" + WS_PREFIX + ".namoadigital.com/leaveRoom";
        WS_CHAT_ROOM_AP = "https://nchat" + WS_PREFIX + ".namoadigital.com/joinFormAp";
        WS_CHAT_ADD_USER_FORM_AP = "https://nchat" + WS_PREFIX + ".namoadigital.com/addUserFormAp";
        WS_SERIAL_LOG = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_serial_log.ws";
        WS_GENERATE_FORM_PDF = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_checklist_url.ws";
        WS_WORKGROUP_MEMBER_LIST = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/wg/server_workgroup_member_list.ws";
        WS_WORKGROUP_MEMBER_EDIT = "https://" + WS_PREFIX + ".namoadigital.com//ws/prj001/wg/server_workgroup_member_set.ws";
        WS_SCHEDULE_NOT_EXECUTED = "https://" + WS_PREFIX + ".namoadigital.com/ws/prj001/server_not_executed_schedule.ws";
    }
}
