package com.namoadigital.prj001.util;

import android.content.Context
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.dao.*
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.model.MD_Schedule_Exec
import com.namoadigital.prj001.sql.GE_Custom_Form_Blob_Sql_001
import com.namoadigital.prj001.sql.GE_Custom_Form_Local_Sql_002
import com.namoadigital.prj001.sql.GE_Custom_Form_Sql_001_TT
import com.namoadigital.prj001.sql.Sql_Act011_002
import java.util.*

class ScheduleFormFatory {

   fun buildInitialScheduleFormLocal(
        context: Context,
        scheduleExec: MD_Schedule_Exec,
        custom_formDao: GE_Custom_FormDao,
        custom_form_fieldDao: GE_Custom_Form_FieldDao,
        custom_form_field_LocalDao: GE_Custom_Form_Field_LocalDao,
        custom_form_blob_localDao: GE_Custom_Form_Blob_LocalDao,
        formLocalDao: GE_Custom_Form_LocalDao
    ): GE_Custom_Form_Local? {
        //region Implementação2
        var daoObjReturn = DaoObjReturn()
        val nextFormData = custom_formDao.getByStringHM(
            GE_Custom_Form_Local_Sql_002(
                scheduleExec.customer_code.toString(),
                scheduleExec.custom_form_type.toString(),
                scheduleExec.custom_form_code.toString(),
                scheduleExec.custom_form_version.toString()
            ).toSqlQuery().toLowerCase()
        )
        //
        if (nextFormData != null && nextFormData.size > 0 && nextFormData.hasConsistentValue("id")) {
            val customerGMT = ToolBox_Con.getPreference_Customer_TMZ(context)
            val customForm = custom_formDao.getByString(
                GE_Custom_Form_Sql_001_TT(
                    scheduleExec.customer_code.toString(),
                    scheduleExec.custom_form_type.toString(),
                    scheduleExec.custom_form_code.toString(),
                    scheduleExec.custom_form_version.toString()
                ).toSqlQuery().toLowerCase()
            )
            //LUCHE - 15/05/2020 - Comentado pois só era usado para definir url_locla o icone do produto.
            //Add metodo que verifica se img existe local e definir valor.
            //MD_Product productInfo = getProduct(ToolBox_Inf.convertStringToInt(item.get(MD_Schedule_ExecDao.PRODUCT_CODE)));
            val customFormLocal = GE_Custom_Form_Local()
            //
            customFormLocal.customer_code = customForm.customer_code
            customFormLocal.custom_form_type = customForm.custom_form_type
            customFormLocal.custom_form_code = customForm.custom_form_code
            customFormLocal.custom_form_version = customForm.custom_form_version
            customFormLocal.custom_form_data = nextFormData["id"]!!.toLong()
            customFormLocal.custom_form_pre = ToolBox_Inf.getPrefix(context)
            customFormLocal.custom_form_status = ConstantBaseApp.SYS_STATUS_SCHEDULE
            customFormLocal.custom_product_code = scheduleExec.product_code
            customFormLocal.custom_product_desc = scheduleExec.product_desc
            customFormLocal.custom_product_id = scheduleExec.product_id
            customFormLocal.custom_form_desc = scheduleExec.custom_form_desc
            customFormLocal.serial_id = scheduleExec.serial_id
            customFormLocal.require_signature = customForm.require_signature
            customFormLocal.automatic_fill = customForm.automatic_fill
            customFormLocal.schedule_date_start_format = "${scheduleExec.date_start} $customerGMT"
            customFormLocal.schedule_date_end_format = "${scheduleExec.date_end} $customerGMT"
            customFormLocal.schedule_date_start_format_ms =
                ToolBox_Inf.dateToMilliseconds("${scheduleExec.date_start} $customerGMT")
            customFormLocal.schedule_date_end_format_ms =
                ToolBox_Inf.dateToMilliseconds("${scheduleExec.date_end} $customerGMT")
            customFormLocal.require_location = customForm.require_location
            customFormLocal.require_serial_done = customForm.require_serial_done
            customFormLocal.schedule_comments = scheduleExec.comments
            customFormLocal.schedule_prefix = scheduleExec.schedule_prefix
            customFormLocal.schedule_code = scheduleExec.schedule_code
            customFormLocal.schedule_exec = scheduleExec.schedule_exec
            customFormLocal.site_code = scheduleExec.site_code
            customFormLocal.site_id = scheduleExec.site_id
            customFormLocal.site_desc = scheduleExec.site_desc
            //LUCHE - 29/04/2020
            //Após alteração onde o servidor manda "tabelas" temporarias com as infos relacionais
            //do agendamento, agora a informação DEVE ser setado na criação do form.
            customFormLocal.allow_new_serial_cl = scheduleExec.allow_new_serial_cl
            customFormLocal.require_serial = scheduleExec.require_serial
            customFormLocal.serial_rule = scheduleExec.serial_rule
            customFormLocal.serial_max_length = scheduleExec.serial_max_length
            customFormLocal.serial_min_length = scheduleExec.serial_min_length
            customFormLocal.local_control = scheduleExec.local_control
            customFormLocal.product_io_control = scheduleExec.io_control
            customFormLocal.site_restriction = scheduleExec.site_restriction
            customFormLocal.custom_product_icon_name = scheduleExec.product_icon_name
            customFormLocal.custom_product_icon_url = scheduleExec.product_icon_url
            customFormLocal.custom_product_icon_url_local =
                getProductIconLocalPath(scheduleExec.product_icon_name?.toLowerCase(Locale.getDefault()))
            customFormLocal.require_location = scheduleExec.require_location
            //
            customFormLocal.tag_operational_code = scheduleExec.tag_operational_code
            customFormLocal.tag_operational_id = scheduleExec.tag_operational_id
            customFormLocal.tag_operational_desc = scheduleExec.tag_operational_desc
            //
            //LUCHE -  14/03/2019
            //Alteração Dao de insert com exception NOVO METODO DAO
            //custom_form_LocalDao.addUpdate(customFormLocal);
            daoObjReturn = formLocalDao.addUpdateThrowException(customFormLocal)
            //
            if (!daoObjReturn.hasError()) {
                //
                val items = custom_form_fieldDao.query_HM(
                    Sql_Act011_002(
                        customFormLocal.customer_code.toString(),
                        customFormLocal.custom_form_type.toString(),
                        customFormLocal.custom_form_code.toString(),
                        customFormLocal.custom_form_version.toString(),
                        ToolBox_Con.getPreference_Translate_Code(context),
                        customFormLocal.custom_form_data.toString()
                    ).toSqlQuery().toLowerCase(Locale.getDefault())
                ) as ArrayList<HMAux>
                //
                custom_form_field_LocalDao.addUpdate(items)
                //
                custom_form_blob_localDao.addUpdate(
                    custom_form_blob_localDao.query(
                        GE_Custom_Form_Blob_Sql_001(
                            customFormLocal.customer_code.toString(),
                            customFormLocal.custom_form_type.toString(),
                            customFormLocal.custom_form_code.toString(),
                            customFormLocal.custom_form_version.toString()
                        ).toSqlQuery().toLowerCase(Locale.getDefault())
                    ),
                    false
                )
                return customFormLocal
            }
        }
        return null
    }

    /**
     * Metodo que verifica se o icone do produto existe e se sim retorno o url_local
     * @param product_icon_name
     * @return
     */
    private fun getProductIconLocalPath(productIconName: String?): String? {
        if (productIconName != null && ToolBox_Inf.verifyDownloadFileInf(
                productIconName,
                Constant.CACHE_PATH
            )
        ) {
            return productIconName
        }
        return null
    }
}