package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.model.masterdata.ge_os.GeOs
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsStatusScanUseCase constructor(
): UseCaseWithoutFlow<GeOsStatusScanUseCase.Input, Unit> {

    data class Input(
        val geOs: GeOs,
        val item: GeOsDeviceItem,
        val measureConsider: Float,
        val dateStartLastMinute: String?,
        val isContinuousForm: Boolean,
    )

    override fun invoke(input: Input) {
        input.apply {
            item.hide_days_in_alert = 0
            item.has_expired_cycle = 0

            if(isContinuousForm
                && item.partitioned_execution == 1){
                item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                return
            }
            if (item.vg_code != null && checkItemStatus(item.item_check_status)) {
                item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                item.hide_days_in_alert = 1
            }

            if ((item.next_cycle_limit_date != null
                        && ToolBox_Inf.getDateDiferenceInMilliseconds(
                    item.next_cycle_limit_date,
                    dateStartLastMinute
                ) <= 0)
                || (item.next_cycle_measure != null && item.next_cycle_measure.compareTo(geOs.maxMeasureValue()) <= 0)
            ) {
                item.has_expired_cycle = 1
            }

            if (GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT.equals(
                    item.item_check_status,
                    true
                )
            ) {
                //Verifica se data projetada do proximo ciclo foi atingida
                if (item.next_cycle_measure != null
                    && item.next_cycle_measure.compareTo(measureConsider) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    item.hide_days_in_alert = 1
                }
            }

            /*
             * Se Status PROJECTED_DATE_REACHED, verifica se deve alterar o status para:
             *   NORMAL:
             *      - Se o data projetada foi alcançada no servidor, mas a data de inicio informada
             * menor que a data de projetada, volta para normal.
             * Isso só acontece no app, pois o usr pode informar uma data anterior a atual.
             */
            if (GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED.equals(
                    item.item_check_status,
                    true
                )
            ) {
                //Verifica se data projetada do proximo ciclo foi atingida
                if (item.next_cycle_measure_date != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(
                        item.next_cycle_measure_date,
                        dateStartLastMinute
                    ) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    item.hide_days_in_alert = 1
                }
            }

            /*
             * Se Status LIMIT_DATE_REACHED, verifica se deve alterar o status para:
             *   NORMAL:
             *      - Se data de limite(next_cycle_limit_date) for maior que data de inicio
             *  até o ultimo minuto(dateStartLastMinute)
             */
            if (GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED.equals(
                    item.item_check_status,
                    true
                )
            ) {
                if (item.next_cycle_limit_date != null && geOs.date_start != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(
                        item.next_cycle_limit_date,
                        dateStartLastMinute
                    ) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    item.hide_days_in_alert = 1
                }
            }

            if (GeOsDeviceItem.ITEM_CHECK_STATUS_MANUALLY_FORCED_DATE.equals(
                    item.item_check_status,
                    true
                )
            ) {
                //Verifica se data projetada do proximo ciclo foi atingida
                if (
                        (item.next_cycle_measure != null
                        && item.next_cycle_measure.compareTo(measureConsider) > 0)
                    ||
                        (item.next_cycle_limit_date != null
                                &&ToolBox_Inf.getDateDiferenceInMilliseconds(
                            item.next_cycle_limit_date,
                            dateStartLastMinute
                        ) > 0)
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    item.hide_days_in_alert = 1
                }
            }


            /*
             * Se Status Normal, verifica se deve alterar o status para:
             *   PROJECTED_DATE_REACHED:
             *      - Se data prevista do proximo ciclo(next_cycle_measure_date) for menor que a
             *      data de inicio até o ultimo minuto(dateStartLastMinute)
             *   LIMIT_DATE_REACHED:
             *      - Se data limite do proximo ciclo (next_cycle_limit_date) for menor que a
             *      data de inicio até o ultimo minuto(dateStartLastMinute)
             *   MEASURE_ALERT:
             *      - Se o valor do proximo ciclo(next_cycle_measure) for menor ou igual ao valor
             *      medido pelo usr measureConsider
             */
            if (GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL.equals(item.item_check_status, true)) {
                var newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL


//                //Verifica se data projetada do proximo ciclo foi atingida
//                if (item.next_cycle_measure_date != null
//                    && ToolBox_Inf.getDateDiferenceInMilliseconds(item.next_cycle_measure_date,dateStartLastMinute) <= 0
//                ) {
//                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED
//                }

                //Verifica se data limite do proximo ciclo foi atingida
                if (item.next_cycle_limit_date != null
                    && ToolBox_Inf.getDateDiferenceInMilliseconds(
                        item.next_cycle_limit_date,
                        dateStartLastMinute
                    ) <= 0
                ) {
                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED
                }

                //Se valor medido, maior que o proximo ciclo,muda status.(Maior prioridade)
                if (item.next_cycle_measure != null
                    && item.next_cycle_measure.compareTo(measureConsider) <= 0
                ) {
                    newCheckStatus = GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT
                }

                item.item_check_status = newCheckStatus
            }
            /*
             * Se Status PROJECTED_DATE_REACHED, verifica se deve alterar o status para:
             *   NORMAL:
             *      - Se o data projetada foi alcançada, mas o valor da medido pelo usr, é inferior
             *  ao proximo ciclo previsto(next_cycle_measure)
             */
            if (GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED.equals(
                    item.item_check_status,
                    true
                )
            ) {
                if (item.next_cycle_measure != null
                    && item.next_cycle_measure.compareTo(measureConsider) > 0
                ) {
                    item.item_check_status = GeOsDeviceItem.ITEM_CHECK_STATUS_NORMAL
                    item.hide_days_in_alert = 1
                }
            }
        }

    }

    fun checkItemStatus(status: String): Boolean {

        return when (status) {
            GeOsDeviceItem.ITEM_CHECK_STATUS_LIMIT_DATE_REACHED -> true
            GeOsDeviceItem.ITEM_CHECK_STATUS_PROJECTED_DATE_REACHED -> true
            GeOsDeviceItem.ITEM_CHECK_STATUS_MEASURE_ALERT -> true
            GeOsDeviceItem.ITEM_CHECK_STATUS_MANUALLY_FORCED_DATE -> true
            else -> {
                false
            }
        }

    }

}