package com.namoadigital.prj001.core.trip.data.destination.action.mapping

import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.core.trip.domain.model.FormStatus
import com.namoadigital.prj001.core.trip.domain.model.TripSiteExtract
import com.namoadigital.prj001.dao.GE_Custom_Form_DataDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao.*
import com.namoadigital.prj001.dao.GE_Custom_Form_LocalDao.CUSTOM_FORM_TYPE
import com.namoadigital.prj001.extensions.toIntOrNegative
import com.namoadigital.prj001.extensions.toLongOrNegative
import com.namoadigital.prj001.model.GE_Custom_Form_Data
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.trip.FsTripDestinationAction

fun HMAux.toTripForms() = TripSiteExtract(
    formStatus = FormStatus.IN_PROCESS,
    date = this[GE_Custom_Form_DataDao.DATE_START] ?: "",
    model = GE_Custom_Form_Local().also { model ->
        model.customer_code = this[CUSTOMER_CODE]?.toLongOrNegative() ?: -1L
        model.custom_form_type = this[CUSTOM_FORM_TYPE]?.toIntOrNegative() ?: -1
        model.custom_form_code = this[CUSTOM_FORM_CODE]?.toIntOrNegative() ?: -1
        model.custom_form_version = this[CUSTOM_FORM_VERSION]?.toIntOrNegative() ?: -1
        model.custom_form_data = this[CUSTOM_FORM_DATA]?.toLongOrNegative() ?: -1L
        model.custom_form_pre = this[CUSTOM_FORM_PRE]
        model.custom_form_status = this[CUSTOM_FORM_STATUS]
        model.require_signature = this[REQUIRE_SIGNATURE]?.toIntOrNegative() ?: -1
        model.require_location = this[REQUIRE_LOCATION]?.toIntOrNegative() ?: -1
        model.require_serial_done = this[REQUIRE_SERIAL_DONE]?.toIntOrNegative() ?: -1
        model.automatic_fill = this[AUTOMATIC_FILL]
        model.custom_product_code = this[CUSTOM_PRODUCT_CODE]?.toIntOrNegative() ?: -1
        model.custom_product_desc = this[CUSTOM_PRODUCT_DESC]
        model.custom_product_id = this[CUSTOM_PRODUCT_ID]
        model.custom_product_icon_name = this[CUSTOM_PRODUCT_ICON_NAME]
        model.custom_product_icon_url = this[CUSTOM_PRODUCT_ICON_URL]
        model.custom_product_icon_url_local = this[CUSTOM_PRODUCT_ICON_URL_LOCAL]
        model.custom_form_desc = this[CUSTOM_FORM_DESC]
        model.serial_id = this[SERIAL_ID]
        model.schedule_date_start_format = this[SCHEDULE_DATE_START_FORMAT]
        model.schedule_date_end_format = this[SCHEDULE_DATE_END_FORMAT]
        model.schedule_date_start_format_ms = this[SCHEDULE_DATE_START_FORMAT_MS]?.toLongOrNegative() ?: -1L
        model.schedule_date_end_format_ms = this[SCHEDULE_DATE_END_FORMAT_MS]?.toLongOrNegative() ?: -1L
        model.require_serial = this[REQUIRE_SERIAL]?.toIntOrNegative() ?: -1
        model.allow_new_serial_cl = this[ALLOW_NEW_SERIAL_CL]?.toIntOrNegative() ?: -1
        model.all_site = this[ALL_SITE]?.toIntOrNegative() ?: -1
        model.all_operation = this[ALL_OPERATION]?.toIntOrNegative() ?: -1
        model.all_product = this[ALL_PRODUCT]?.toIntOrNegative() ?: -1
        model.site_code = this[SITE_CODE]?.toIntOrNegative() ?: -1
        model.site_id = this[SITE_ID]
        model.site_desc = this[SITE_DESC]
        model.zone_code = this[ZONE_CODE]?.toIntOrNull()
        model.zone_id = this[ZONE_ID]
        model.zone_desc = this[ZONE_DESC]
        model.io_control = this[IO_CONTROL]?.toIntOrNegative() ?: -1
        model.inbound_auto_create = this[INBOUND_AUTO_CREATE]?.toIntOrNegative() ?: -1
        model.operation_code = this[OPERATION_CODE]?.toIntOrNegative() ?: -1
        model.operation_id = this[OPERATION_ID]
        model.operation_desc = this[OPERATION_DESC]
        model.local_control = this[LOCAL_CONTROL]?.toIntOrNegative() ?: -1
        model.product_io_control = this[PRODUCT_IO_CONTROL]?.toIntOrNegative() ?: -1
        model.site_restriction = this[SITE_RESTRICTION]?.toIntOrNegative() ?: -1
        model.serial_rule = this[SERIAL_RULE]
        model.serial_min_length = this[SERIAL_MIN_LENGTH]?.toIntOrNull()
        model.serial_max_length = this[SERIAL_MAX_LENGTH]?.toIntOrNull()
        model.schedule_comments = this[SCHEDULE_COMMENTS] ?: ""

        model.schedule_prefix = this[SCHEDULE_PREFIX]?.toIntOrNull()
        model.schedule_code = this[SCHEDULE_CODE]?.toIntOrNull()
        model.schedule_exec = this[SCHEDULE_EXEC]?.toIntOrNull()

        model.tag_operational_id = this[TAG_OPERATIONAL_ID] ?: ""
        model.tag_operational_code = this[TAG_OPERATIONAL_CODE]?.toIntOrNegative() ?: -1
        model.tag_operational_desc = this[TAG_OPERATIONAL_DESC] ?: ""

        model.ticket_prefix = this[TICKET_PREFIX]?.toIntOrNull()
        model.ticket_code = this[TICKET_CODE]?.toIntOrNull()
        model.ticket_seq = this[TICKET_SEQ]?.toIntOrNull()
        model.ticket_seq_tmp = this[TICKET_SEQ_TMP]?.toIntOrNull()
        model.step_code = this[STEP_CODE]?.toIntOrNull()
        model.tag_operational_code = this[TAG_OPERATIONAL_CODE]?.toIntOrNegative() ?: -1
        model.tag_operational_id = this[TAG_OPERATIONAL_ID]
        model.tag_operational_desc = this[TAG_OPERATIONAL_DESC]
        model.is_so = this[IS_SO]?.toIntOrNegative() ?: 0
        model.so_edit_start_end = this[SO_EDIT_START_END]?.toIntOrNegative() ?: -1
        model.so_order_type_code_default = this[SO_ORDER_TYPE_CODE_DEFAULT]?.toIntOrNull()
        model.so_allow_change_order_type = this[SO_ALLOW_CHANGE_ORDER_TYPE]?.toIntOrNegative() ?: -1
        model.so_allow_backup = this[SO_ALLOW_BACKUP]?.toIntOrNegative() ?: -1
        model.so_optional_justify_problem = this[SO_OPTIONAL_JUSTIFY_PROBLEM]?.toIntOrNegative() ?: -1
        model.nc_recognize_email_in_comment = this[NC_RECOGNIZE_EMAIL_IN_COMMENT]?.toIntOrNegative() ?: -1
    }
)

fun FsTripDestinationAction.toTripForms() = TripSiteExtract(
    formStatus = FormStatus.DONE,
    date = this.dateStart,
    model = this
)