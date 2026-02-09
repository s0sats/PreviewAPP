package com.namoadigital.prj001.core.trip.domain.model.enums

import com.namoadigital.prj001.core.translate.TranslateWildCard

enum class TimelineBlockTranslate (
    override val key: String,
    override val placeholders: List<String> = emptyList()
) : TranslateWildCard {
    //Não sei pq no passado decidi isso, mas o resource é `extract_dialog_info_resource`

    ERROR_CONFLICT_WITH_ON_SITE_LBL("error_conflict_with_on_site_lbl", listOf("destination_desc", "arrived_date", "departed_date")),
    ERROR_CONFLICT_OUT_OF_ON_SITE_LBL("error_conflict_out_of_on_site_lbl", listOf("arrived_date")),
    ERROR_DATE_RANGE_LBL("error_date_range_lbl"),
    ERROR_CONFLICT_TRIP_LBL("error_conflict_trip_lbl", listOf("conflict_description", "date_start", "date_end")),
    ERROR_CONFLICT_TRIP_DATE_END_LBL("error_conflict_trip_date_end_lbl", listOf("date_start", "date_end")),
    CREATE_TRIP_DESCRIPTION("create_trip_description", listOf("trip_create_date")),
    ERROR_CONFLICT_WITH_DATE_CREATE_TRIP_LBL("error_conflict_with_date_create_trip_lbl", listOf("trip_create_date")),
    ERROR_CONFLICT_WITH_CREATE_TRIP_LBL("error_conflict_with_create_trip_lbl", listOf("trip_create_date", "trip_start_date")),
    START_TRIP_DESCRIPTION("start_trip_description", listOf("trip_start_date")),
    ERROR_CONFLICT_WITH_START_TRIP_LBL("error_conflict_with_start_trip_lbl", listOf("trip_start_date")),
    END_TRIP_DESCRIPTION("end_trip_description", listOf("trip_end_date")),
    ERROR_CONFLICT_WITH_FIRST_DESTINATION_LBL("error_conflict_with_first_destination_lbl", listOf("destination_desc", "arrived_date", "departed_date")),
    ERROR_CONFLICT_WITH_EVENT("error_conflict_with_event", listOf("event_desc", "date_start", "date_end")),
    ERROR_CONFLICT_WITH_FORM_LBL("error_conflict_with_form_lbl", listOf("form_desc", "date_start", "date_end")),
    ERROR_DESTINATION_CONFLICT_WITH_FORM_LBL("error_destination_conflict_with_form_lbl", listOf("date_start", "date_end")),
    ERROR_CONFLICT_GENERIC_LBL("error_conflict_generic_lbl"),
    DIALOG_EVENT_DATE_EQUAL_ERROR_LBL("dialog_event_date_equal_error_lbl")

}