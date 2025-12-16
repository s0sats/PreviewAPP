package com.namoadigital.prj001.model

import androidx.annotation.ColorInt
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.ui.act011.model.FormItemCheckLabelIconListItem
import com.namoadigital.prj001.ui.act011.model.FormTicketInfo.TicketFormType
import com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model.MeasureItemData
import com.namoadigital.prj001.util.ConstantBaseApp
import java.io.Serializable

data class InspectionCell(
    val description: String,
    val dayCount: Int?,
    val photoCount: Int = 0,
    val isRequiredPhoto: Boolean = false,
    val materialCount: Int = 0,
    val materialRequired: Boolean,
    var hasComment: Boolean = false,
    val commentRequired: Boolean,
    var status: String,
    val hideAlreadyOKBtn: Boolean = false,
    var isVisible: Boolean = false,
    val statusColor: GeOsDeviceItemStatusColor,
    val isCritical: Boolean,
    val isNewItem: Boolean = false,
    var answerStatus: String?,
    var execType: String?,
    val itemCodeAndSeq: String,
    val hmAuxTrans: HMAux,
    val change_adjust: Int,
    val partitionedExecution: Int,
    val requirePhotoAlreadyOk: Boolean = false,
    val read_only: Boolean = false,
    val measureItemData: MeasureItemData? = null,
    val ticketFormType: TicketFormType = TicketFormType.NO_TICKET,
    val itemCheckLabelIcon: FormItemCheckLabelIconListItem?,
    val alreadyOkLbl: FormItemCheckLabelIconListItem?,
    ) : Serializable {
    var isDone: Boolean = false

    @ColorInt
    var tagColor: Int = 0
    var statusTransalted: String = ""
    var execTypeTranslated: String = ""

    init {
        initViewVars()
    }


    fun isPartitioned() = partitionedExecution == 1

    /**
     * Fun que baseado nos dados do construtor, define vars de "visualização"
     */
    fun initViewVars() {
        answerStatus?.let {
            isDone = !it.equals(ConstantBaseApp.SYS_STATUS_PROCESS)
        }
        //
        if (isDone) {
            tagColor = R.color.namoa_color_light_green5
            statusTransalted = hmAuxTrans["inspection_status_answered_item_lbl"]!!
        } else {
            this.status = status
            when (statusColor) {
                GeOsDeviceItemStatusColor.GRAY -> {
                    tagColor = R.color.namoa_color_gray_7
                    statusTransalted = hmAuxTrans["inspection_status_non_forecast_item_lbl"]!!
                }

                GeOsDeviceItemStatusColor.RED -> {
                    tagColor = R.color.namoa_os_form_problem_red
                    statusTransalted = hmAuxTrans["inspection_status_manual_alert_item_lbl"]!!
                }

                GeOsDeviceItemStatusColor.BLUE -> {
                    this.status = FORECAST
                    tagColor = R.color.namoa_color_pipeline_origin_icon
                    statusTransalted = hmAuxTrans["inspection_status_forecast_item_lbl"]!!
                }

                GeOsDeviceItemStatusColor.YELLOW -> {
                    this.status = CRITICAL_FORECAST
                    tagColor = R.color.namoa_os_form_critical_forecast_yellow
                    statusTransalted =
                        hmAuxTrans["inspection_status_critical_forecast_item_lbl"]!!
                }
            }
        }

        when (execType) {
            GeOsDeviceItem.EXEC_TYPE_FIXED -> {
                execTypeTranslated = if (change_adjust == 1) {
                    hmAuxTrans["inspection_answer_change_lbl"]!!
                } else {
                    hmAuxTrans["inspection_answer_fixed_lbl"]!!
                }
            }

            GeOsDeviceItem.EXEC_TYPE_ADJUST -> {
                execTypeTranslated = hmAuxTrans["inspection_answer_adjust_lbl"]!!
            }

            GeOsDeviceItem.EXEC_TYPE_ALERT -> {
                execTypeTranslated =
                    if (!GeOsDeviceItem.ITEM_CHECK_STATUS_MANUAL_ALERT.equals(status, true)) {
                        hmAuxTrans["inspection_answer_alert_lbl"]!!
                    } else {
                        hmAuxTrans["inspection_answer_still_in_alert_lbl"]!!
                    }
            }

            GeOsDeviceItem.EXEC_TYPE_ALREADY_OK -> {
                execTypeTranslated = hmAuxTrans["inspection_answer_already_ok_lbl"]!!
            }

            GeOsDeviceItem.EXEC_TYPE_NOT_VERIFIED -> {
                execTypeTranslated = hmAuxTrans["inspection_answer_not_verify_lbl"]!!
            }
        }
    }

    fun getOtherTicketIdFormatted(): String {
        return hmAuxTrans["other_ticket_info_lbl"]!!
    }

    fun getSameTicketItemRequired(): String {
        return hmAuxTrans["required_by_ticket_lbl"]!!
    }

    fun getAllFieldForFilter(): String {
        return "$description|" +
                "$execTypeTranslated|" +
                "$statusTransalted|"
                    .replace("null|", "")
    }

    companion object {
        const val ANSWERED = "ANSWERED"
        const val NORMAL = "NORMAL"
        const val MANUAL_ALERT = "MANUAL_ALERT"
        const val FORECAST = "FORECAST"
        const val CRITICAL_FORECAST = "CRITICAL_FORECAST"
        const val STATUS_FORCED = "STATUS_FORCED"
    }
}

