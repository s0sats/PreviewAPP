package com.namoadigital.prj001.ui.act011.group_verification.domain.usecase

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.core.UseCases
import com.namoadigital.prj001.core.form_os.domain.repository.GeOsRepository
import com.namoadigital.prj001.model.MdOrderType.Companion.PROCESS_TYPE_PREVENTIVE
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItem
import com.namoadigital.prj001.model.masterdata.ge_os.GeOsDeviceItemStatusColor
import com.namoadigital.prj001.model.masterdata.ge_os.ProcessVg
import com.namoadigital.prj001.model.masterdata.ge_os.vg.GeOsVg
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.group_verification.composable.components.badge.model.NamoaBadges
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroup
import com.namoadigital.prj001.ui.act011.group_verification.domain.model.VerificationGroupState
import com.namoadigital.prj001.ui.act011.model.FormTicketInfo
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MapToVerificationGroupsUseCase @Inject constructor(
    private val repository: GeOsRepository,
    private val formRepository: GeCustomFormRepository
) : UseCases<MapToVerificationGroupsUseCase.Input, List<VerificationGroup>> {


    data class Input(
        val formPks: VerificationGroupState.FormPK,
        val isHistoric: Boolean,
        val hasProcessVg: ProcessVg?,
        val processType: String
    )

    override suspend fun invoke(input: Input): Flow<IResult<List<VerificationGroup>>> {
        val (formPks, isReadOnly, hasProcessVg, processType) = input
        return flow {
            emit(loading())
            runCatching {
                val form = formRepository.getCustomFormData(
                    formTypeCode = formPks.customFormType,
                    formCode = formPks.customFormCode,
                    formVersionCode = formPks.customFormVersion,
                    formData = formPks.customFormData.toLong()
                )
                val vgs = repository.getAllGeOsVgsByFormCode(
                    customerCode = formPks.customerCode,
                    customFormType = formPks.customFormType,
                    customFormCode = formPks.customFormCode,
                    customFormVersion = formPks.customFormVersion,
                    customFormData = formPks.customFormData,
                    productCode = formPks.productCode,
                    serialCode = formPks.serialCode,
                )

                val deviceItems = repository.getAllDeviceItems(
                    customerCode = formPks.customerCode,
                    customFormType = formPks.customFormType,
                    customFormCode = formPks.customFormCode,
                    customFormVersion = formPks.customFormVersion,
                    customFormData = formPks.customFormData,
                    productCode = formPks.productCode,
                    serialCode = formPks.serialCode,
                )


                mapToVerificationGroups(
                    vgs = vgs,
                    items = deviceItems,
                    hasProcessVg = hasProcessVg,
                    isReadOnly = isReadOnly,
                    ticketPrefix = form?.ticket_prefix,
                    ticketCode = form?.ticket_code,
                    isPreventiveType = processType == PROCESS_TYPE_PREVENTIVE
                )
            }.fold(
                onSuccess = { listGroup ->
                    emit(success(listGroup))
                },
                onFailure = { e ->
                    ToolBox_Inf.registerException(
                        MapToVerificationGroupsUseCase::class.simpleName,
                        e as Exception?
                    )
                    emit(failed(e))
                }
            )
        }.flowOn(Dispatchers.IO)
    }


    @Throws(Exception::class)
    fun mapToVerificationGroups(
        items: List<GeOsDeviceItem>,
        vgs: List<GeOsVg>,
        hasProcessVg: ProcessVg?,
        isReadOnly: Boolean,
        ticketPrefix: Int?,
        ticketCode: Int?,
        isPreventiveType: Boolean
    ): List<VerificationGroup> {

        val itemsByVgCode = items.groupBy { it.vg_code }
        val formTicketInfo = FormTicketInfo(ticketPrefix, ticketCode)
        val verificationGroups = vgs.map { vg ->
            val itemList = itemsByVgCode[vg.vgCode].orEmpty()

            val alerts = itemList
                .filter { it.color_item != null }
                .asSequence()
                .groupingBy { it.color_item }
                .eachCount()
                .map { (colorItem, count) ->
                    NamoaBadges(
                        color = colorItem,
                        count = count
                    )
                }.sortedBy {
                    GeOsDeviceItemStatusColor.colorPriority[it.color]
                }
                var requiredByTickets = 0
                itemList.forEach { item ->
                    if (formTicketInfo.getTicketFormType(item) == FormTicketInfo.TicketFormType.SAME_TICKET) {
                        requiredByTickets++
                    }
                }
                with(vg) {

                    val sameTicket = isSameTicket(
                        prefix = ticketPrefix,
                        code = ticketCode,
                    )

                    val inPartitionExecution = hasPartition()

                    VerificationGroup(
                        vgCode = vgCode,
                        title = vgDesc,
                        expired = isExpired(),
                        predictedDate = targetDate,
                        inExecution = inPartitionExecution && !sameTicket,
                        ticket = if (inPartitionExecution && !sameTicket) ticket else null,
                        user = if (inPartitionExecution && !sameTicket) partitionedUser else null,
                        alerts = alerts,
                        requiredByTickets = requiredByTickets,
                        isActive = vg.isActive(),
                        canToggle = checkCanToggleSwitch(
                                    inPartitionExecution,
                                    hasProcessVg,
                                    isPreventiveType,
                                    isReadOnly
                                )
                    )

                }
        }.toMutableList()

        val noVgItems = items.filter { it.vg_code == null }

        if (noVgItems.isNotEmpty()) {
            val alerts = noVgItems
                .asSequence()
                .filter { it.color_item != null }
                .groupingBy { it.color_item }
                .eachCount()
                .map { (colorItem, count) ->
                    NamoaBadges(
                        color = colorItem,
                        count = count
                    )
                }
            var requiredByTickets = 0
            noVgItems.forEach { item ->
                if (formTicketInfo.getTicketFormType(item) == FormTicketInfo.TicketFormType.SAME_TICKET) {
                    requiredByTickets++
                }
            }
            verificationGroups.add(
                VerificationGroup(
                    vgCode = null,
                    alerts = alerts,
                    isActive = true,
                    requiredByTickets = requiredByTickets
                )
            )
        }

        return verificationGroups
    }

    private fun GeOsVg.checkCanToggleSwitch(
        inPartitionExecution: Boolean,
        processVg: ProcessVg?,
        isPreventiveType: Boolean,
        isReadOnly: Boolean,
    ): Boolean = when {
        isReadOnly -> false
        (ProcessVg.isForceExecutionAllGroups(processVg))-> false
        !isPreventiveType && isExecOnlyPreventive() -> false
        processVg == ProcessVg.BLOCK_EXECUTION -> false
        inPartitionExecution -> false
        (processVg == ProcessVg.FORCE_EXECUTION_EXPIRED) && isExpired() -> false
        else -> true
    }
}