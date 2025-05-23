package com.namoadigital.prj001.core.form_os.domain.usecase

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.model.masterdata.product_serial.verification_group.VgStatus
import com.namoadigital.prj001.util.ToolBox_Inf

class GeOsScanVerificationGroupUseCase(
    val repository: GeOsRepository,
) : UseCaseWithoutFlow<GeOsScanVerificationGroupUseCase.Input, List<GeOsVg>> {

    data class Input(
        val customFormType: Int,
        val customFormCode: Int,
        val customFormVersion: Int,
        val customFormData: Int,
        val productCode: Long,
        val serialCode: Long,
        val valueSuffix: String?,
        val restrictionDecimal: Int?,
        val measureConsider: Float?,
        val dateConsider: String?,
        val ticketPrefix : Int?,
        val ticketCode : Int?,

    )

    override fun invoke(input: Input): List<GeOsVg> {
        val (
            customFormType,
            customFormCode,
            customFormVersion,
            customFormData,
            productCode,
            serialCode,
            valueSuffix,
            restrictionDecimal,
            measureConsider,
            dateConsider,
            ticketPrefix,
            ticketCode,
        ) = input

        val list = repository.createGeOsVg(
            customFormType,
            customFormCode,
            customFormVersion,
            customFormData,
            productCode,
            serialCode,
            valueSuffix,
            restrictionDecimal
        )

        list.forEach { item ->
            var vgStatus = VgStatus.fromStatus(item.vgStatus)
            if (vgStatus == null) return@forEach
            // 1. Verifica se há manualDate; se houver, define status e interrompe a atualização para esse item.
            if (isManualDate(item)) {
                vgStatus = VgStatus.MANUALLY_FORCED_DATE
                if (ToolBox_Inf.getDateDiferenceInMilliseconds(item.manualDate, dateConsider) > 0) {
                    vgStatus = VgStatus.NORMAL
                }
            } else {

                // 2. Atualiza o status conforme os casos especiais
                vgStatus = when (vgStatus) {
                    VgStatus.MEASURE_ALERT -> handleMeasureAlert(
                        vgStatus,
                        item.nextCycleMeasure,
                        measureConsider
                    )

                    VgStatus.PROJECTED_DATE_REACHED -> handleProjectedDateReached(
                        vgStatus,
                        item.nextCycleMeasureDate,
                        dateConsider
                    )

                    VgStatus.LIMIT_DATE_REACHED -> handleLimitDateReached(
                        vgStatus,
                        item.nextCycleLimitDate,
                        dateConsider
                    )

                    else -> vgStatus
                }

                // 3. Se o status atual for NORMAL, verifica se precisa ser atualizado para LIMIT_DATE_REACHED ou MEASURE_ALERT
                if (vgStatus == VgStatus.NORMAL) {
                    vgStatus = handleNormal(
                        item.nextCycleLimitDate,
                        dateConsider,
                        item.nextCycleMeasure,
                        measureConsider
                    )
                }

                // 4. Se o status original era PROJECTED_DATE_REACHED e a próxima medida ultrapassa o valor considerado, força NORMAL
                if (item.vgStatus == VgStatus.PROJECTED_DATE_REACHED.status &&
                    item.nextCycleMeasure != null &&
                    measureConsider != null &&
                    item.nextCycleMeasure > measureConsider
                ) {
                    vgStatus = VgStatus.NORMAL
                }
            }


            item.isActive = isVerificationGroupActive(vgStatus, item, ticketPrefix, ticketCode)
            item.hasExpired = isGroupExpired(vgStatus)
            item.vgStatus = vgStatus.status
        }

        return list
    }

    private fun isVerificationGroupActive(
        vgStatus: VgStatus,
        item: GeOsVg,
        ticketPrefix : Int?,
        ticketCode : Int?
    ): Int{
        return if (isGroupExpired(vgStatus) == 1 || isOtherPartitionActived(item, ticketPrefix, ticketCode)){
            1
        }else{
            0
        }
    }



    private fun isOtherPartitionActived(
        vg: GeOsVg,
        ticketPrefix : Int?,
        ticketCode : Int?,
    ): Boolean {
        return vg.partitionedExecution == 1 && vg.isSameTicket(ticketPrefix, ticketCode)
    }

    private fun isGroupExpired(vgStatus: VgStatus): Int = if (vgStatus != VgStatus.NORMAL) 1 else 0

    private fun isManualDate(item: GeOsVg) = item.manualDate != null

    private fun handleMeasureAlert(
        status: VgStatus,
        nextCycleMeasure: Float?,
        measureConsider: Float?
    ): VgStatus {
        return if (nextCycleMeasure != null &&
            measureConsider != null &&
            nextCycleMeasure > measureConsider
        ) {
            VgStatus.NORMAL
        } else status
    }

    private fun handleProjectedDateReached(
        status: VgStatus,
        nextCycleMeasureDate: String?,
        dateConsider: String?
    ): VgStatus {
        return if (nextCycleMeasureDate != null && dateConsider != null &&
            ToolBox_Inf.getDateDiferenceInMilliseconds(
                nextCycleMeasureDate,
                dateConsider
            ) > 0
        ) {
            VgStatus.NORMAL
        } else status
    }

    private fun handleLimitDateReached(
        status: VgStatus,
        nextCycleLimitDate: String?,
        dateConsider: String?
    ): VgStatus {
        return if (nextCycleLimitDate != null && dateConsider != null &&
            ToolBox_Inf.getDateDiferenceInMilliseconds(nextCycleLimitDate, dateConsider) > 0
        ) {
            VgStatus.NORMAL
        } else status
    }

    private fun handleNormal(
        nextCycleLimitDate: String?,
        dateConsider: String?,
        nextCycleMeasure: Float?,
        measureConsider: Float?
    ): VgStatus {
        val limitDateReached = nextCycleLimitDate != null &&
                dateConsider != null &&
                ToolBox_Inf.getDateDiferenceInMilliseconds(
                    nextCycleLimitDate,
                    dateConsider
                ) <= 0

        val measureAlert = nextCycleMeasure != null &&
                measureConsider != null &&
                measureConsider >= nextCycleMeasure

        return when {
            limitDateReached -> VgStatus.LIMIT_DATE_REACHED
            measureAlert -> VgStatus.MEASURE_ALERT
            else -> VgStatus.NORMAL
        }
    }
}
