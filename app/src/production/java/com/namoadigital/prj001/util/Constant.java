package com.namoadigital.prj001.util;

/**
 * Created by neomatrix on 09/01/17.
 */

public class Constant extends ConstantBaseApp {
    /**
     * WebService
     */

    /**
     * Produção
     */
    public static final String WS_GETCUSTOMERS = "https://portal.namoadigital.com/ws/prj001/server_get_customer.ws";
    public static final String WS_GET_CUSTOMERS_SITE_LICENSE = "https://portal.namoadigital.com/ws/prj001/server_get_customer_site.ws";
    public static final String WS_GETSESSION = "https://portal.namoadigital.com/ws/prj001/server_get_session.ws";
    public static final String WS_GOOGLE = "https://portal.namoadigital.com/ws/prj001/server_gcm_set.ws";

    public static final String WS_SYNC = "https://portal.namoadigital.com/ws/prj001/server_sync.ws";
    public static final String WS_SESSION = "https://portal.namoadigital.com/ws/prj001/server_get_session.ws";
    public static final String WS_SAVE = "https://portal.namoadigital.com/ws/prj001/server_save_checklist.ws";
    public static final String WS_SERIAL = "https://portal.namoadigital.com/ws/prj001/server_serial.ws";
    public static final String WS_SERIAL_SEARCH  = "https://portal.namoadigital.com/ws/prj001/server_product_serial.ws";
    public static final String WS_SERIAL_TRACKING_SEARCH  = "https://portal.namoadigital.com/ws/prj001/server_serial_tracking.ws";
    public static final String WS_UPLOAD = "https://portal.namoadigital.com/inc/ws/aws_file.ws";
    public static final String WS_LOGOUT = "https://portal.namoadigital.com/ws/prj001/server_logout.ws";
    public static final String WS_SERIAL_SAVE = "https://portal.namoadigital.com/ws/prj001/server_save_serial.ws";

    public static final String WS_SO_SEARCH = "https://portal.namoadigital.com/ws/prj001/server_so.ws";
    public static final String WS_SO_SERVICE_SEARCH = "https://portal.namoadigital.com/ws/prj001/server_sm_so_pack_service_list_app_2.ws";
    public static final String WS_SO_SERVICE_EDIT_GET = "https://portal.namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_get.ws";
    public static final String WS_SO_SERVICE_EDIT_SET = "https://portal.namoadigital.com/ws/prj001/server_sm_so_pack_service_edit_set.ws";
    public static final String WS_SO_SERVICE_REMOVE = "https://portal.namoadigital.com/ws/prj001/server_sm_so_pack_service_cancel_app.ws";
    public static final String WS_SO_CLIENT_LIST = "https://portal.namoadigital.com/ws/prj001/sm_order/server_client.ws";
    public static final String WS_SO_FAVORITE_LIST = "https://portal.namoadigital.com/ws/prj001/sm_order/server_main.ws";
    public static final String WS_SO_CREATION = "https://portal.namoadigital.com/ws/prj001/sm_order/server_save_so.ws";
    public static final String WS_SO_SAVE = "https://portal.namoadigital.com/ws/prj001/server_save_so.ws";
    public static final String WS_SO_PACK_SERVICE = "https://portal.namoadigital.com/ws/prj001/server_sm_so_pack_service_new_app_2.ws";
    public static final String WS_SO_PRODUCT_EVENT_CANCEL = "https://portal.namoadigital.com/ws/prj001/server_sm_so_product_event_cancel.ws";
    public static final String WS_SO_NEXT_ORDERS = "https://portal.namoadigital.com/ws/prj001/server_sm_so_to_be_executed.ws";
    public static final String WS_SO_CREATE_ROOM = "https://portal.namoadigital.com/ws/prj001/server_sm_so_create_room.ws";
    public static final String WS_USER_LIST_SEARCH = "https://portal.namoadigital.com/ws/prj001/wg/server_user_list.ws";
    //IO
    public static final String WS_IO_SERIAL_PROCESS_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_product_serial.ws";
    public static final String WS_IO_SERIAL_PROCESS_DOWNLOAD = "https://portal.namoadigital.com/ws/prj001/io/server_product_serial_obj.ws";
    public static final String WS_IO_MOVE_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_move.ws";
    public static final String WS_IO_INBOUND_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_inbound.ws";
    public static final String WS_IO_OUTBOUND_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_outbound.ws";
    public static final String WS_IO_MOVE_DOWNLOAD = "https://portal.namoadigital.com/ws/prj001/io/server_move_obj.ws";
    public static final String WS_IO_MOVE_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_move_save.ws";
    public static final String WS_IO_INBOUND_DOWNLOAD = "https://portal.namoadigital.com/ws/prj001/io/server_inbound_obj.ws";
    public static final String WS_IO_OUTBOUND_DOWNLOAD = "https://portal.namoadigital.com/ws/prj001/io/server_outbound_obj.ws";
    public static final String WS_IO_MASTER_DATA = "https://portal.namoadigital.com/ws/prj001/io/server_master_data.ws";
    public static final String WS_IO_FROM_OUTBOUND_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_outbound_search.ws";
    public static final String WS_IO_INBOUND_HEADER_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_inbound_save.ws";
    public static final String WS_IO_OUTBOUND_HEADER_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_outbound_save.ws";
    public static final String WS_IO_ADDRESS_SUGGESTION = "https://portal.namoadigital.com/ws/prj001/io/server_address_suggestion.ws";
    public static final String WS_IO_BLIND_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_blind_save.ws";
    public static final String WS_IO_INBOUND_ITEM_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_inbound_item_save.ws";
    public static final String WS_IO_OUTBOUND_ITEM_SAVE = "https://portal.namoadigital.com/ws/prj001/io/server_outbound_item_save.ws";
    public static final String WS_IO_TRANSPORT_ORDER_OUT_SEARCH = "https://portal.namoadigital.com/ws/prj001/io/server_transport_order_out_search.ws";
    //TICKET
    public static final String WS_TICKET_DOWNLOAD = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket.ws";
    public static final String WS_TICKET_DOWNLOAD_SERIAL = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_serial.ws";
    public static final String WS_TICKET_CHECKIN = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_checkin.ws";
    public static final String WS_TICKET_SAVE = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_set.ws";
    public static final String WS_NEXT_TICKET = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_next.ws";
    public static final String WS_TICKET_DOWNLOAD_CLIENT_CONTRACT = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_search.ws";
    public static final String WS_TICKET_GET_WORKGROUP_LIST = "https://portal.namoadigital.com/ws/prj001/tk/server_workgroup.ws";
    public static final String WS_TICKET_SERVER_MAIN_USER = "https://portal.namoadigital.com/ws/prj001/tk/server_main_user.ws";
    public static final String WS_TICKET_DOWNLOAD_SEARCH_NOT_FOCUS = "https://portal.namoadigital.com/ws/prj001/tk/server_ticket_search_not_focus.ws";

    public static final String WS_ENABLE_NFC = "https://portal.namoadigital.com/ws/prj001/server_enable_nfc_auth.ws";
    public static final String WS_CANCEL_NFC = "https://portal.namoadigital.com/ws/prj001/server_cancel_nfc_auth.ws";

    public static final String WS_SERVER_AUTH_USER = "https://portal.namoadigital.com/ws/prj001/server_auth_user.ws";

    public static final String WS_AP_SEARCH = "https://portal.namoadigital.com/ws/prj001/server_ap.ws";
    public static final String WS_AP_SAVE = "https://portal.namoadigital.com/ws/prj001/server_save_ap.ws";
    public static final String WS_SO_PACK_EXPRESSION = "https://portal.namoadigital.com/ws/prj001/server_save_so_pack_express.ws";

    public static final String WS_SERIAL_LOG = "https://portal.namoadigital.com/ws/prj001/server_serial_log.ws";
    public static final String WS_GENERATE_FORM_PDF = "https://portal.namoadigital.com/ws/prj001/server_checklist_url.ws";
    public static final String WS_WORKGROUP_MEMBER_LIST = "https://portal.namoadigital.com/ws/prj001/wg/server_workgroup_member_list.ws";
    public static final String WS_WORKGROUP_MEMBER_EDIT = "https://portal.namoadigital.com//ws/prj001/wg/server_workgroup_member_set.ws";

    public static final String WEB_SOCKET_CHAT = "https://nchat.namoadigital.com";
    public static final String WS_UPLOAD_NODE_CHAT = "https://nchat.namoadigital.com/messageFile";
    public static final String WS_CHAT_ROOM_INFO = "https://nchat.namoadigital.com/roomMemberList";
    public static final String WS_CHAT_MESSAGE_INFO = "https://nchat.namoadigital.com/messageDeliverList";
    public static final String WS_CHAT_ROOM_USER_LIST = "https://nchat.namoadigital.com/userList";
    public static final String WS_CHAT_MESSAGE_DIST = "https://nchat.namoadigital.com/messageDist?";
    public static final String WS_CHAT_POST_DELIVERED = "https://nchat.namoadigital.com/delivered";
    public static final String WS_CHAT_ROOM_PRIVATE = "https://nchat.namoadigital.com/roomPrivateCustomer";
    public static final String WS_CHAT_LEAVE_ROOM = "https://nchat.namoadigital.com/leaveRoom";
    public static final String WS_CHAT_ROOM_AP = "https://nchat.namoadigital.com/joinFormAp";
    public static final String WS_CHAT_ADD_USER_FORM_AP = "https://nchat.namoadigital.com/addUserFormAp";

    // Connection Test
    public static final String WS_HOST = "portal.namoadigital.com";
    public static final int WS_PORT = 443;
    public static final int WS_TIMEOUT = 3000;

}
