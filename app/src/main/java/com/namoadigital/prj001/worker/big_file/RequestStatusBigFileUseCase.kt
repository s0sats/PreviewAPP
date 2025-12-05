package com.namoadigital.prj001.worker.big_file

import android.content.Context
import com.google.gson.GsonBuilder
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.model.big_file.create_file.BigFileRec
import com.namoadigital.prj001.model.big_file.create_file.BigFileStatusRequestEnv
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.Constant.WS_BIG_FILE_STATUS
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.worker.BaseRemoteBigFileUseCase
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class RequestStatusBigFileUseCase(
    context: Context,
) : BaseRemoteBigFileUseCase(context) {
    //
    private val mutex = Mutex()
    private val gson = GsonBuilder().serializeNulls().create()
    //
    override suspend fun invoke(inputData: DataInput): DataOutput {
        val fileType = inputData.bigFile.fileType
        val fileCode = inputData.bigFile.fileCode ?: -1
        //
        mutex.withLock {
            try {
                debugBigFile("RequestStatusBigFileUseCase", inputData.bigFile)

                if (checkUserLoginOrWorkerStopped()
                    || fileType == null
                    || fileCode == -1
                ) {

                    return formatErrorMsg("Worker Finalizado por Sessao")
                } else {

                    val env = BigFileStatusRequestEnv(
                        fileType,
                        fileCode,
                    )
                    env.app_code = Constant.PRJ001_CODE
                    env.app_version = Constant.PRJ001_VERSION
                    env.session_app = ToolBox_Con.getPreference_Session_App(context)
                    env.app_type = Constant.PKG_APP_TYPE_DEFAULT
                    //

                    val response = ToolBox_Con.connWebService(
                        WS_BIG_FILE_STATUS,
                        gson.toJson(env)
                    )
                    //
                    val rec = gson.fromJson(
                        response,
                        BigFileRec::class.java
                    )
                    if (checkValidation(rec.validation)) {
                        when (rec.file.fileStatus) {
                            BigFileStatus.NOT_FOUND.name -> {
                                return  DataOutput.Success(setBigFilePending(fileType))
                            }

                            BigFileStatus.DONE.name -> {
                                val bigFilePreferenceManager =
                                    BigFilePreferenceManager(context, fileType)
                                val bigFile =
                                    bigFilePreferenceManager.getBigFile()
                                bigFile.fileStatus = BigFileStatus.DOWNLOAD.name
                                bigFile.fileUrl = rec.file.file_url
                                bigFile.fileMd5 = rec.file.file_md5
                                bigFilePreferenceManager.saveBigFile(bigFile)

                                return DataOutput.Success(bigFile)
                            }

                            BigFileStatus.PROCESS.name -> {
                                return formatErrorMsg("Arquivo em PROCESS")
                            }

                            else -> {
                                return formatErrorMsg("Falha no processamento")
                            }
                        }
                    } else {
                        //
                        return formatErrorMsg("Erro! \n Validation: ${rec.validation}")
                    }
                    //
                }
            } catch (httpException: NetworkConnectionException) {
                ToolBox_Inf.registerException(javaClass.name, httpException)
            } catch (exception: Exception) {
                ToolBox.registerException(javaClass.name, exception)
            }
            return formatErrorMsg("Worker Finalizado por Exception")
        }
    }



    private fun setBigFilePending(fileType: String?): BigFile {
        val bigFilePreferenceManager =
            BigFilePreferenceManager(context, fileType!!)
        val bigFile =
            bigFilePreferenceManager.getBigFile()
        bigFile.fileCode = null
        bigFile.fileStatus = BigFileStatus.PENDING.name
        bigFilePreferenceManager.saveBigFile(bigFile)
        return bigFile
    }

    private fun checkUserLoginOrWorkerStopped():
            Boolean = (ToolBox_Con.getPreference_Customer_Code(context) == -1L
            || ToolBox_Con.getPreference_User_Code(context).isBlank())


    private fun debugWithNotification(msg: String) {
//        ToolBox.registerException("BIG_FILE", Exception(msg))
    }

}