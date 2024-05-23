package com.namoadigital.prj001.ui.act093.data.repository

import android.content.Context
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.error
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.core.IResult.Companion.success
import com.namoadigital.prj001.dao.MD_Product_SerialDao
import com.namoadigital.prj001.dao.MD_Product_Serial_Tp_DeviceDao
import com.namoadigital.prj001.dao.MdDeviceTpDao
import com.namoadigital.prj001.dao.MeMeasureTpDao
import com.namoadigital.prj001.extensions.coroutines.namoaCatch
import com.namoadigital.prj001.model.MD_Product_Serial
import com.namoadigital.prj001.model.MdDeviceTp
import com.namoadigital.prj001.sql.MD_Product_Serial_Sql_002
import com.namoadigital.prj001.sql.MeMeasureTpSql_001
import com.namoadigital.prj001.ui.act092.data.local.preferences.FilterParamPreferences
import com.namoadigital.prj001.ui.act092.model.SerialModel
import com.namoadigital.prj001.ui.act093.model.DeviceTpModel
import com.namoadigital.prj001.ui.base.NamoaFactory
import com.namoadigital.prj001.util.Constant
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import java.io.IOException


class IInfoSerialRepository constructor(
    private val context: Context,
    private val serialDao: MD_Product_SerialDao,
    private val measureTpDao: MeMeasureTpDao,
    private val serialTpDeviceDao: MD_Product_Serial_Tp_DeviceDao,
    private val deviceTpDao: MdDeviceTpDao,
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

        }.namoaCatch(IInfoSerialRepository::class.java.simpleName)
    }

    override suspend fun geMeasureRestrictionDecimal(
        customerCode: Long,
        measureTpCode: Int
    ): Int {
        val meMeasureTp = measureTpDao.getByString(
            MeMeasureTpSql_001(
                customerCode,
                measureTpCode
            ).toSqlQuery()
        )
        return meMeasureTp?.let{
            it.restrictionDecimal?:ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
        }?: ConstantBaseApp.FORM_OS_MEASURE_DECIMAL_DEFAULT
    }


    override suspend fun getPreferences(): Flow<IResult<SerialModel>> {
        return flow {
            emit(success(prefs))
        }.namoaCatch(IInfoSerialRepository::class.java.simpleName)
    }

    override suspend fun getValueSuffixProduct(
        customerCode: Long,
        code: Int
    ): Flow<IResult<String?>> {
        return flow {

            val suffix = measureTpDao.getByString(
                MeMeasureTpSql_001(
                    customerCode,
                    code
                ).toSqlQuery()
            )
            emit(success(suffix?.valueSufix))

        }.namoaCatch(IInfoSerialRepository::class.java.simpleName)
    }


    override suspend fun getListItems(): Flow<IResult<MutableList<DeviceTpModel>>> {
        var serial: MD_Product_Serial? = null
        getInfoSerial().last().isSuccess { product_serial ->
            serial = product_serial
        }

        return flow {

            emit(loading(true))

            val serialTpDevices = serialTpDeviceDao.getDeviceForSerialInfo(
                serial?.customer_code ?: -1L,
                serial?.product_code ?: -1L,
                serial?.serial_code ?: -1L
            )

            emit(loading(false))
            emit(success(serialTpDevices.toMutableList()))

        }.namoaCatch(IInfoSerialRepository::class.java.simpleName)
    }


    override suspend fun getListDeviceTp(): Flow<IResult<List<MdDeviceTp>>> {

        return flow {

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
                    MD_Product_Serial_Tp_DeviceDao(
                        context,
                        ToolBox_Con.customDBPath(ToolBox_Con.getPreference_Customer_Code(context)),
                        Constant.DB_VERSION_CUSTOM
                    ),
                    MdDeviceTpDao(
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