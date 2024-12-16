package com.namoadigital.prj001.ui.act011.finish_os.di.model

import com.namoadigital.prj001.ui.act011.finish_os.di.model.ResponsibleStop.NO_STOPPED
import com.namoadigital.prj001.util.ToolBox_Inf


data class FinishOsData(
    val showBalloonVerify: Boolean = false,
    val showOptionsStopped: Boolean = false,
    val showBkupMachine: Boolean = false,
    val infoOs: FinishFormField.ExpectedTimeOS? = null,
    val machineOsInitial: FinishFormField.MachineOSInitial = FinishFormField.MachineOSInitial(),
    val machineOsFinal: FinishFormField.MachineOSFinal = FinishFormField.MachineOSFinal(),
    val backupMachine: FinishFormField.BackupMachine? = null,
    val backupMachineListState: FinishFormBackupMachineList = FinishFormBackupMachineList(
        false,
        null
    ),
    var hasNewService: FinishFormField.HasNewService = FinishFormField.HasNewService()
) {
}

sealed interface FinishFormField {

    data class BackupMachine(
        val hasBackupMachine: Boolean = false,
        val productCode: Int?,
        val productId: String?,
        val productDesc: String?,
        val serialCode: Int?,
        val serialId: String?,
    ) : FinishFormField {
        val isValid = (!serialId.isNullOrEmpty() && productCode != null)
    }

    data class MachineOSInitial(
        val date: String? = null,
        val responsibleStop: ResponsibleStop? = NO_STOPPED,
        val isSerialStopped: Boolean? = false
    ) : FinishFormField

    data class MachineOSFinal(
        val option: ResponsibleStop? = NO_STOPPED
    ) : FinishFormField

    data class HasNewService(val option: NewServiceChoose = NewServiceChoose.FINALIZED) :
        FinishFormField

    data class ExpectedTimeOS(
        val dateStart: String,
        val dateEnd: String?,
        val elapsedTime: String? = null,
        val isEditDate: Boolean? = null,
        val partitionMinDate: String? = null,
        val lastMeasureDate: String? = null,
    ) : FinishFormField {

        companion object {

            fun elapsedTime(dateStart: String?, dateEnd: String?): String? {
                if (dateStart == null) return null
                if (dateEnd != null) return ToolBox_Inf.getDateDiferenceInHHMM(dateStart, dateEnd)
                return null
            }

        }

    }
}

sealed class NewServiceChoose(val value: Int) {
    data object PLANNING : NewServiceChoose(0)
    data class RETURN(val date: String) : NewServiceChoose(-1)
    data object FINALIZED : NewServiceChoose(1)
}

enum class ResponsibleStop(val isStopped: Int, val value: String?) {
    NO_STOPPED(0, null),
    MAINTENANCE(1, "MAINTENANCE"),
    THIRD_PARTY(1, "THIRD_PARTY"),

    /***
     * enum para estado intermediario onde é respondido que houve parada, o value neste caso nao sera
     * usado em nenhum lugar
     */
    STOPPED(1, "STOPPED");

    companion object {
        fun isMachineStopped(value: ResponsibleStop) =
            listOf(MAINTENANCE, THIRD_PARTY, STOPPED).contains(value)
    }

}