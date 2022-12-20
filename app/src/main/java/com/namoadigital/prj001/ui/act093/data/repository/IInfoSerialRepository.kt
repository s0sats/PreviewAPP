package com.namoadigital.prj001.ui.act093.data.repository

import android.content.Context
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002
import com.namoadigital.prj001.sql.MeMeasureTpSql_001
import com.namoadigital.prj001.ui.act092.data.local.preferences.FilterParamPreferences
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException

class IInfoSerialRepository constructor(
    private val context: Context,
    private val serialDao: MD_Product_SerialDao,
    private val measureTpDao: MeMeasureTpDao,
    private val sharedActionSerial: FilterParamPreferences
) : InfoSerialRepository {

    val prefs = sharedActionSerial.read()

    override suspend fun getInfoSerial(
    ): Flow<IResult<MD_Product_Serial>> {
        return flow {

            emit(loading(true))

            serialDao.getByString(
                MD_Product_Serial_Sql_002(
                    ToolBox_Con.getPreference_Customer_Code(context),
                    prefs.productCode?.toLong() ?: -1,
                    prefs.serialId
                ).toSqlQuery()
            )?.let {

                emit(loading(false))
                emit(success(it))

            } ?: emit(failed(InfoSerialRepositoryException("MD_Product_Serial not found")))

        }
    }


    override suspend fun getPreferences(): Flow<IResult<SerialModel>> {
        return flow {
            emit(success(prefs))
        }
    }

    override suspend fun getValueSuffixProduct(
        customerCode: Long,
        code: Int
    ): Flow<IResult<String?>> {
        return flow {
            emit(loading(true))

            val suffix = measureTpDao.getByString(
                MeMeasureTpSql_001(
                    customerCode,
                    code
                ).toSqlQuery()
            )
            emit(loading(false))
            emit(success(suffix?.valueSufix))
        }.catch { e ->
            ToolBox_Inf.registerException(
                IInfoSerialRepository::class.java.simpleName,
                e as java.lang.Exception
            )
            emit(failed(e))
        }
    }

    companion object {

        class InfoSerialRepositoryFactory(private val context: Context) :
            NamoaFactory<InfoSerialRepository>() {
            override fun build(): InfoSerialRepository =
                IInfoSerialRepository(
                    context,
                    MD_Product_SerialDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MeMeasureTpDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    FilterParamPreferences(
                        context.getSharedPreferences("act092_filter", Base_Activity.MODE_PRIVATE)
                    )
                )
        }
    }
}

class InfoSerialRepositoryException(override val message: String) : IOException(message)