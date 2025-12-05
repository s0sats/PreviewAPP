package com.namoadigital.prj001.worker.big_file

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.extensions.util.debugBigFile
import com.namoadigital.prj001.model.T_MD_Product_Serial_Structure_Env
import com.namoadigital.prj001.model.big_file.create_file.BigFileRec
import com.namoadigital.prj001.model.big_file.create_file.BigFileStructureCreationEnv
import com.namoadigital.prj001.sql.MDProductSerialSql020
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager
import com.namoadigital.prj001.util.preferences.BigFilePreferenceManager.Companion.FILE_TYPE_SERIAL_STRUCTURE
import com.namoadigital.prj001.worker.BaseRemoteBigFileUseCase
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus

class StructureBigFileCreationUseCase (
    context: Context,
) : BaseRemoteBigFileUseCase(context) {

    private val gson: Gson = GsonBuilder().serializeNulls().create()
    //
    private val serialDao: MD_Product_SerialDao by lazy {
        MD_Product_SerialDao(
            context,
            ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
            ConstantBaseApp.DB_VERSION_CUSTOM
        )
    }
    //


    //
    override suspend fun invoke(inputData: DataInput): DataOutput {
        val fileStatus = inputData.bigFile.fileStatus
        debugBigFile("StructureBigFileCreationUseCase", inputData.bigFile)

        if(ToolBox_Con.getPreference_Customer_Code(context) == -1L
            || ToolBox_Con.getPreference_User_Code(context).isBlank()
        ){
            return formatErrorMsg("Worker Finalizado por Sessao")
        }
        //
        if(fileStatus == null
            || fileStatus != BigFileStatus.PENDING.name) {
            return formatErrorMsg("Worker Finalizado por fileStatus: $fileStatus")
        }

        val structureList = getSerialStructureForBigFile(context)
        if (structureList.isNotEmpty()){
            val env = BigFileStructureCreationEnv(
                FILE_TYPE_SERIAL_STRUCTURE,
                structureList
            )
            env.app_code = Constant.PRJ001_CODE
            env.app_version = Constant.PRJ001_VERSION
            env.session_app = ToolBox_Con.getPreference_Session_App(context)
            env.app_type = Constant.PKG_APP_TYPE_DEFAULT
            //
            try {
                val resultado = ToolBox_Con.connWebService(
                    Constant.WS_PRODUCT_SERIAL_STRUCTURE_SEARCH_SYNC,
                    gson.toJson(env),
                )
                //
                val rec = gson.fromJson(
                    resultado,
                    BigFileRec::class.java
                )
                //
                if (checkValidation(rec.validation)) {
                    val bigFilePreferenceManager = BigFilePreferenceManager(context, FILE_TYPE_SERIAL_STRUCTURE)
                    val structureBigFile = bigFilePreferenceManager.getBigFile()
                    val bigFileResult = structureBigFile.copy(
                        fileType = rec.file.fileType,
                        fileStatus = BigFileStatus.PROCESS.name,
                        fileCode = rec.file.fileCode
                    )
                    bigFilePreferenceManager.saveBigFile(bigFileResult)
                    return  DataOutput.Success(bigFileResult)
                } else {
                    return formatErrorMsg("Worker Finalizado por fileStatus: $fileStatus")
                }
            }catch (httpException: NetworkConnectionException){
                ToolBox_Inf.registerException(javaClass.name, httpException)
                return formatErrorMsg("${httpException.message}")
            }catch (exception: Exception){
                ToolBox_Inf.registerException(javaClass.name, exception)
                return formatErrorMsg("${exception.message}")
            }
        }else{
            val resultBigFile = updateBigFileStatus(FILE_TYPE_SERIAL_STRUCTURE, BigFileStatus.DONE)
            return DataOutput.Success(resultBigFile)
        }
    }

    private fun getSerialStructureForBigFile(context: Context):ArrayList<T_MD_Product_Serial_Structure_Env> {
        val search = mutableListOf<T_MD_Product_Serial_Structure_Env>()
        val query = serialDao.query(
            MDProductSerialSql020(
                ToolBox_Con.getPreference_Customer_Code(context),
                ToolBox_Con.getPreference_Site_Code(context).toString()
            ).toSqlQuery()
        )
        query.forEach {
            search.add(
                T_MD_Product_Serial_Structure_Env(
                    it.customer_code,
                    it.product_code,
                    it.serial_code,
                    it.scn_item_check
                )
            )
        }
        return search as java.util.ArrayList<T_MD_Product_Serial_Structure_Env>

    }

    private fun debugWithNotification(msg: String) {
//        ToolBox.registerException("BIG_FILE", Exception(msg))
    }


}