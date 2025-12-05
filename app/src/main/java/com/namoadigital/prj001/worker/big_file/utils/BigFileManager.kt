package com.namoadigital.prj001.worker.big_file.utils

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_SERIAL_STRUCTURE
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_TICKET
import com.namoadigital.prj001.worker.BaseRemoteBigFileUseCase
import com.namoadigital.prj001.worker.big_file.DownloadBigFileUseCase
import com.namoadigital.prj001.worker.big_file.FinishBigFileFCMUseCase
import com.namoadigital.prj001.worker.big_file.RequestStatusBigFileUseCase
import com.namoadigital.prj001.worker.big_file.StructureBigFileCreationUseCase
import com.namoadigital.prj001.worker.big_file.TicketBigFileCreationUseCase
import com.namoadigital.prj001.worker.big_file.WorkCheckBigFile
import java.util.concurrent.TimeUnit

class BigFileManager(private val context: Context) {
    val bigFileStructurePreferenceManager = BigFilePreferenceManager(context, FILE_TYPE_SERIAL_STRUCTURE)
    val bigFileTicketPreferenceManager = BigFilePreferenceManager(context, FILE_TYPE_TICKET)

    suspend fun invoke() {
//        Log.d("BIG_FILE_PROCESS", "---------------BigFileWorkerManager---------------")

        val bigFileExecutionList = mutableListOf<BigFile>()
        var isStructureFinish = false
        var isTicketFinish = false

        bigFileStructurePreferenceManager.getBigFile().let{
            isStructureFinish = it.fileStatus == BigFileStatus.NO_VALUE.name
            bigFileExecutionList.add(it)
        }
        bigFileTicketPreferenceManager.getBigFile().let{
            isTicketFinish = it.fileStatus == BigFileStatus.NO_VALUE.name
            bigFileExecutionList.add(it)
        }

//        Log.d("BIG_FILE_PROCESS", "isStructureDone: $isStructureFinish")
//        Log.d("BIG_FILE_PROCESS", "isTicketDone: $isTicketFinish")

        if(isStructureFinish
            && isTicketFinish){
//            Log.d("BIG_FILE_PROCESS", "Processo encerrado")
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(WorkCheckBigFile.WORKER_TAG)
            debugBigFile("cancel ${WorkCheckBigFile.WORKER_TAG}", null)
        }else {
            bigFileExecutionList.forEach {
//                Log.d("BIG_FILE_PROCESS", "execute: $it")
                execute(it)
            }
        }
    }

    suspend fun executeStructure() {
//        Log.d("BIG_FILE_PROCESS", "Structure\nInstância: ${this@BigFileWorkerManager.hashCode()}, Thread: ${Thread.currentThread().name}")
        execute(bigFileStructurePreferenceManager.getBigFile())
    }

    suspend fun executeTicket() {
//        Log.d("BIG_FILE_PROCESS", "Ticket\nInstância: ${this@BigFileWorkerManager.hashCode()}, Thread: ${Thread.currentThread().name}")
        execute(bigFileTicketPreferenceManager.getBigFile())
    }
    @Throws(Exception::class)
    suspend fun execute(bigFile: BigFile) {
        debugBigFile("BigFileWorkerManager execute", bigFile)
        //
        when {
            checkBigFileStatus(bigFile, BigFileStatus.PENDING, FILE_TYPE_SERIAL_STRUCTURE) -> {
                callBigFileStructureCreation(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.PENDING, FILE_TYPE_TICKET) -> {
                callBigFileTicketCreation(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.PROCESS, FILE_TYPE_SERIAL_STRUCTURE) -> {
                callBigFileStructureRequestStatus(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.PROCESS, FILE_TYPE_TICKET) -> {
                callBigFileTicketRequestStatus(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.DOWNLOAD, FILE_TYPE_SERIAL_STRUCTURE) -> {
                callBigFileStructureDownload(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.DOWNLOAD, FILE_TYPE_TICKET) -> {
                callBigFileTicketDownload(bigFile)
            }
            checkBigFileStatus(bigFile, BigFileStatus.DONE, FILE_TYPE_TICKET)
                    || checkBigFileStatus(bigFile, BigFileStatus.DONE, FILE_TYPE_SERIAL_STRUCTURE) -> {
                callBigFileDoneDownload(bigFile)
                checkDoneDownload()
            }
            checkBigFileStatus(bigFile, BigFileStatus.NO_VALUE, FILE_TYPE_TICKET)
                    || checkBigFileStatus(bigFile, BigFileStatus.NO_VALUE, FILE_TYPE_SERIAL_STRUCTURE) -> {
                checkDoneDownload()
            }
        }
    }

    private suspend fun callBigFileDoneDownload(bigFile: BigFile) {
        val doneBigFileUseCase = FinishBigFileFCMUseCase(context)
        doneBigFileUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
    }

    private fun checkDoneDownload() {
        var isStructureDone = false
        var isTicketDone = false
        //
        bigFileStructurePreferenceManager.getBigFile().let {
            isStructureDone = it.fileStatus == BigFileStatus.NO_VALUE.name
        }
        //
        bigFileTicketPreferenceManager.getBigFile().let {
            isTicketDone = it.fileStatus == BigFileStatus.NO_VALUE.name
        }
        //
        if (isStructureDone
            && isTicketDone
        ) {
//            Log.d("BIG_FILE_PROCESS", "Processo encerrado")
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(WorkCheckBigFile.WORKER_TAG)
            debugBigFile("cancel ${WorkCheckBigFile.WORKER_TAG}", null)
        }
    }






    @Throws(Exception::class)
    private suspend fun callBigFileTicketRequestStatus(bigFile: BigFile) {
        val requestStatusBigFileUseCase = RequestStatusBigFileUseCase(context)
        val result = requestStatusBigFileUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        //
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success) {
            if(result.bigFile.fileStatus.equals(BigFileStatus.DOWNLOAD.name)){
                callBigFileTicketDownload(result.bigFile)
            } else if(result.bigFile.fileStatus.equals(BigFileStatus.PENDING.name)){
                callBigFileTicketCreation(result.bigFile)
            }
        }
    }
    @Throws(Exception::class)
    private suspend fun callBigFileStructureRequestStatus(bigFile: BigFile) {
        //
        val requestStatusBigFileUseCase = RequestStatusBigFileUseCase(context)
        val result = requestStatusBigFileUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        //
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success) {
            if(result.bigFile.fileStatus.equals(BigFileStatus.DOWNLOAD.name)){
                callBigFileStructureDownload(result.bigFile)
            } else if(result.bigFile.fileStatus.equals(BigFileStatus.PENDING.name)){
                callBigFileStructureCreation(result.bigFile)
            }
        }
    }

    private fun checkBigFileStatus(bigFile: BigFile, status: BigFileStatus, type: String): Boolean =
        BigFileStatus.fromStringOrDefault(bigFile.fileStatus) == status && bigFile.fileType == type

    @Throws(Exception::class)
    private suspend fun callBigFileTicketCreation(bigFile: BigFile) {
        val ticketBigFileCreationUseCase = TicketBigFileCreationUseCase(context)
        val result = ticketBigFileCreationUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success
            && result.bigFile.fileStatus == BigFileStatus.DONE.name) {
            callBigFileDoneDownload(result.bigFile)
        }
    }
    @Throws(Exception::class)
    private suspend fun callBigFileStructureCreation(bigFile: BigFile) {
        val structureBigFileCreationUseCase = StructureBigFileCreationUseCase(context)
        val result = structureBigFileCreationUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success
            && result.bigFile.fileStatus == BigFileStatus.DONE.name) {
            callBigFileDoneDownload(result.bigFile)
        }
    }

    @Throws(Exception::class)
    private suspend fun callBigFileTicketDownload(bigFile: BigFile) {
        //
        val downloadBigFileUseCase = DownloadBigFileUseCase(context)
        val result = downloadBigFileUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        //
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success) {
            callBigFileTicketCreation(result.bigFile)
        }
    }
    @Throws(Exception::class)
    private suspend fun callBigFileStructureDownload(bigFile: BigFile) {
        //
        val downloadBigFileUseCase = DownloadBigFileUseCase(context)
        val result = downloadBigFileUseCase.invoke(
            BaseRemoteBigFileUseCase.DataInput(bigFile)
        )
        //
        if(result is BaseRemoteBigFileUseCase.DataOutput.Success) {
            callBigFileStructureCreation(result.bigFile)
        }
    }

    fun getWorkCheckBigFileRequest() {

        val workCheckBigFile =
            PeriodicWorkRequest
                .Builder(
                    WorkCheckBigFile::class.java,
                    15,
                    TimeUnit.MINUTES
                )
                .setInitialDelay(5, TimeUnit.MINUTES)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    1,
                    TimeUnit.MINUTES
                )
                .setConstraints(getWorkerConstraints())
                .build()

        //
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                WorkCheckBigFile.WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                workCheckBigFile
            )
    }

    private fun getWorkerConstraints(): Constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

}
