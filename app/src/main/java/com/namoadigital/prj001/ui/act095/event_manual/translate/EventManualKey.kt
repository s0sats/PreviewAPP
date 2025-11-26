package com.namoadigital.prj001.ui.act095.event_manual.translate

import com.namoadigital.prj001.core.translate.TranslateWildCard

enum class EventManualKey(
    override val key: String,
    override val placeholders: List<String> = emptyList()
) : TranslateWildCard {

    //Historico
    EventHistoricCardTitle("event_historic_card_title"),
    EventHistoricCardQuantity("event_historic_card_quantity", listOf("quantity")),

    // Seleção de evento
    EventTypeTitleLbl("event_type_title_lbl"),
    EventTypeSearchHintLbl("event_type_search_hint_lbl"),

    // Formulário do evento
    CostLbl("cost_lbl"),
    CommentLbl("comment_lbl"),
    AddPhotoBtn("add_photo_btn"),
    PhotoLbl("photo_lbl"),

    // Datas e horários
    StartDateLbl("start_date_lbl"),
    EndDateLbl("end_date_lbl"),
    DateHintLbl("date_hint_lbl"),
    TimeHintLbl("time_hint_lbl"),
    ShowEndDateSwitchLbl("show_end_date_switch_lbl"),

    // Botões de ação
    ReportButton("report_button_lbl"),
    SaveButtonLbl("save_button_lbl"),
    FinalizeButtonLbl("finalize_button_lbl"),
    DeleteButtonLbl("delete_button_lbl"),

    //Titulo progress dialog
    SavingEventTitle("saving_event_title"),
    SavingEventMessage("saving_event_message"),

    // Mensagens de erro
    ErrorStartEndConflictDateMsg("error_start_end_conflict_date_msg"),
    ErrorRequiredFieldMsg("error_required_field_msg"),
    ErrorSaveEventMsg("error_save_event_msg"),
    ErrorPhotoRequiredMsg("error_photo_required_msg"),

    // Mensagens de status
    SavingEventOfflineMsg("saving_event_offline_msg"),
    ProcessingEventMsg("processing_event_msg"),
    ErrorEventInExecutionTitle("error_event_in_execution_title"),
    ErrorEventInExecutionMsg("error_event_in_execution_msg"),
    ErrorInvalidFutureStartDateMsg("error_invalid_future_start_date_msg"),
    ErrorInvalidFutureEndDateMsg("error_invalid_future_end_date_msg"),
    ErrorInvalidEventDateMsg("error_invalid_event_date_msg"),
    ErrorInvalidFormDateMsg("error_invalid_form_date_msg"),


    ElapsedJustNowLbl("elapsed_just_now_lbl"),
    ElapsedMinutesLbl("elapsed_minutes_lbl", listOf("minutes")),
    ElapsedHoursLbl("elapsed_hours_lbl", listOf("hours")),
    ElapsedHoursMinutesLbl("elapsed_hours_minutes_lbl", listOf("hours", "minutes")),

    CancelButtonLbl("cancel_button_lbl"),
    ConfirmSaveMsg("confirm_save_msg"),
    ConfirmFinalizeMsg("confirm_finalize_msg"),
    ConfirmDeleteMsg("confirm_delete_msg"),
    EventHistoryTitle("event_history_title"),
    SearchEventHistoryHint("search_event_history_hint"),
    EventHistoryEmpty("event_history_empty"),
    TypeEventEmpty("event_type_empty"),
    EditBtn("event_history_edit_btn"),
    DownloadingPhotoLbl("event_download_photo_lbl"),
    NoInternetPhotoLbl("event_no_internet_photo_lbl"),
    RetryButtonLbl("event_retry_button_lbl"),
}
