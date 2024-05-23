package com.namoadigital.prj001.ui.act005.trip.di.usecase.user

import com.namoadigital.prj001.core.UseCaseWithoutFlow
import com.namoadigital.prj001.extensions.parseFullDate
import com.namoadigital.prj001.model.trip.FSTripUser
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.repository.users.TripUserRepository
import com.namoadigital.prj001.util.ToolBox_Inf

class UserCheckIntersectionUseCase constructor(
    private val repository: TripUserRepository
) : UseCaseWithoutFlow<InputParams, OutputParams> {
    override fun invoke(input: InputParams): OutputParams {
        val (userCode, userSeq, startMillis, endMillis) = input

        repository.getListUserByCodeInTrip(userCode).takeIf { it.isNotEmpty() }?.let { listUser ->

            listUser.forEach { user ->
                val dateStartMills = ToolBox_Inf.dateToMilliseconds(user.dateStart)
                val dateEnd = user.dateEnd?.let {
                    ToolBox_Inf.dateToMilliseconds(it)
                }

                if (user.userSeq != userSeq) {
                    dateEnd?.let { dateEndMillis ->
                        val dateStartError = startMillis in dateStartMills..dateEndMillis

                        endMillis?.let { endMill ->
                            val dateEndError = endMill in dateStartMills..dateEndMillis
                            if (dateStartError || dateEndError) {
                                return OutputParams(
                                    user = user,
                                    startDateError = dateStartError,
                                    endDateError = dateEndError
                                )
                            }

                            val startDateError = dateStartMills in startMillis..endMill
                            val endDateError = dateEndMillis in startMillis..endMill

                            if (startDateError || endDateError) {
                                return OutputParams(
                                    user = user,
                                    startDateError = startDateError,
                                    endDateError = endDateError
                                )
                            }

                        }
                        if (dateStartError) {
                            return OutputParams(
                                user = user,
                                startDateError = true,
                                endDateError = false
                            )
                        }
                    } ?: run {
                        if (startMillis == dateStartMills) {
                            return OutputParams(
                                user = user,
                                startDateError = true,
                                endDateError = false
                            )
                        }

                        if (startMillis == endMillis) {
                            return OutputParams(
                                user = user,
                                startDateError = true,
                                endDateError = true
                            )
                        }

                    }
                }
            }
        }
        return OutputParams(
            user = null,
            startDateError = false,
            endDateError = false
        )
    }

    private fun checkUserIsValid(
        startMillis: Long,
        dateStartMills: Long,
        dateEnd: Long?,
        endMillis: Long?,
        user: FSTripUser
    ): OutputParams {
        dateEnd?.let { dateEndMillis ->
            val dateStartError = startMillis in dateStartMills..dateEndMillis

            endMillis?.let { endMill ->
                val dateEndError = endMill in dateStartMills..dateEndMillis
                if (dateStartError || dateEndError) {
                    return OutputParams(
                        user = user,
                        startDateError = dateStartError,
                        endDateError = dateEndError
                    )
                }

                val startDateError = dateStartMills in startMillis..endMill
                val endDateError = dateEndMillis in startMillis..endMill

                if (startDateError || endDateError) {
                    return OutputParams(
                        user = user,
                        startDateError = startDateError,
                        endDateError = endDateError
                    )
                }

            }
            if (dateStartError) {
                return OutputParams(
                    user = user,
                    startDateError = true,
                    endDateError = false
                )
            }
        } ?: run {
            if (startMillis == dateStartMills) {
                return OutputParams(
                    user = user,
                    startDateError = true,
                    endDateError = false
                )
            }

            if (startMillis == endMillis) {
                return OutputParams(
                    user = user,
                    startDateError = true,
                    endDateError = true
                )
            }

        }

        return OutputParams(
            user = null,
            startDateError = false,
            endDateError = false
        )
    }

}

data class InputParams(
    val userCode: Int,
    val userSeq: Int?,
    val startDateInMillis: Long,
    val endDateInMillis: Long?,
)

data class OutputParams(
    val user: FSTripUser?,
    val startDateError: Boolean,
    val endDateError: Boolean,
)
