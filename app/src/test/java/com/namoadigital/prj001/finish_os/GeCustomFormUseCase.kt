package com.namoadigital.prj001.finish_os

import app.cash.turbine.test
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.model.GE_Custom_Form_Local
import com.namoadigital.prj001.ui.act011.finish_os.data.repository.ge_custom_form.GeCustomFormRepository
import com.namoadigital.prj001.ui.act011.finish_os.di.usecase.ge_custom.GetCustomFormLocalByIdUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub


@RunWith(JUnit4::class)
class GeCustomFormUseCase {

    private val repository = mock<GeCustomFormRepository>()


    @Test
    fun `should return the normal form`() = runBlocking {

        repository.stub {
            onBlocking {
                getGeCustomFormLocalById(
                    formCode = 101.toString(),
                    formVersionCode = 1.toString(),
                    formTypeCode = 1,
                    formData = 9876543210L.toString(),
                    productCode = 202,
                    serialId = "S12345"
                )
            } doReturn flowOf(IResult.success(customFormMocked))
        }

        val flow = GetCustomFormLocalByIdUseCase(repository).invoke(
            GetCustomFormLocalByIdUseCase.Param(
                formCode = 101,
                formVersionCode = 1,
                formTypeCode = 1,
                formData = 9876543210L,
                productCode = 202,
                serialId = "S12345"
            )
        )

        flow.test {
            assertEquals(IResult.isSuccess(customFormMocked), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `should return the ticket form`() = runBlocking {

        repository.stub {
            onBlocking {
                getCustomFormLocalTicketById(
                    formCode = 101.toString(),
                    formVersionCode = 1.toString(),
                    formTypeCode = 1,
                    formData = 9876543210L.toString(),
                    productCode = 202,
                    serialId = "S12345",
                    ticketPrefix = 601,
                    ticketCode = 701,
                    ticketSeq = 1101,
                    ticketSeqTmp = 1201,
                    stepCode = 1301
                )
            } doReturn flowOf(IResult.success(customFormMocked))
        }

        val flow = GetCustomFormLocalByIdUseCase(repository).invoke(
            GetCustomFormLocalByIdUseCase.Param(
                formCode = 101,
                formVersionCode = 1,
                formTypeCode = 1,
                formData = 9876543210L,
                productCode = 202,
                serialId = "S12345"
            )
        )

        flow.test {
            assertEquals(IResult.isSuccess(customFormTicketMocked), awaitItem())
            awaitComplete()
        }
    }


    private val customFormMocked = GE_Custom_Form_Local(
    ).apply {
        this.customer_code = 1234567890L
        this.custom_form_type = 1
        this.custom_form_code = 101
        this.custom_form_version = 1
        this.custom_form_data = 9876543210L
        this.custom_form_pre = "PreData"
        this.custom_form_status = "Active"
        this.require_signature = 1
        this.require_location = 1
        this.require_serial_done = 1
        this.automatic_fill = "Yes"
        this.custom_product_code = 202
        this.custom_product_desc = "Product Description"
        this.custom_product_id = "P12345"
        this.custom_product_icon_name = "icon_name"
        this.custom_product_icon_url = "http://example.com/icon.png"
        this.custom_product_icon_url_local = "/local/icon.png"
        this.custom_form_desc = "Form Description"
        this.serial_id = "S12345"
        this.schedule_date_start_format = "2024-08-01T00:00:00Z"
        this.schedule_date_end_format = "2024-08-31T23:59:59Z"
        this.schedule_date_start_format_ms = 1690886400000L
        this.schedule_date_end_format_ms = 1693518399000L
        this.require_serial = 1
        this.allow_new_serial_cl = 1
        this.all_site = 1
        this.all_operation = 1
        this.all_product = 1
        this.site_code = 301
        this.site_id = "Site123"
        this.site_desc = "Site Description"
        this.zone_code = 401
        this.zone_id = "Zone123"
        this.zone_desc = "Zone Description"
        this.io_control = 1
        this.inbound_auto_create = 1
        this.operation_code = 501
        this.operation_id = "Op123"
        this.operation_desc = "Operation Description"
        this.local_control = 1
        this.product_io_control = 1
        this.site_restriction = 1
        this.serial_rule = "Rule"
        this.serial_min_length = 6
        this.serial_max_length = 12
        this.schedule_comments = "Comments"
        this.schedule_prefix = 601
        this.schedule_code = 701
        this.schedule_exec = 801
        this.tag_operational_code = 1401
        this.tag_operational_id = "Tag123"
        this.tag_operational_desc = "Tag Description"
        this.is_so = 1
        this.so_edit_start_end = 1
        this.so_order_type_code_default = 1501
        this.so_allow_change_order_type = 1
        this.so_allow_backup = 1
        this.so_optional_justify_problem = 1
        this.nc_recognize_email_in_comment = 1
    }

    private val customFormTicketMocked = GE_Custom_Form_Local(
    ).apply {
        this.customer_code = 1234567890L
        this.custom_form_type = 1
        this.custom_form_code = 101
        this.custom_form_version = 1
        this.custom_form_data = 9876543210L
        this.custom_form_pre = "PreData"
        this.custom_form_status = "Active"
        this.require_signature = 1
        this.require_location = 1
        this.require_serial_done = 1
        this.automatic_fill = "Yes"
        this.custom_product_code = 202
        this.custom_product_desc = "Product Description"
        this.custom_product_id = "P12345"
        this.custom_product_icon_name = "icon_name"
        this.custom_product_icon_url = "http://example.com/icon.png"
        this.custom_product_icon_url_local = "/local/icon.png"
        this.custom_form_desc = "Form Description"
        this.serial_id = "S12345"
        this.schedule_date_start_format = "2024-08-01T00:00:00Z"
        this.schedule_date_end_format = "2024-08-31T23:59:59Z"
        this.schedule_date_start_format_ms = 1690886400000L
        this.schedule_date_end_format_ms = 1693518399000L
        this.require_serial = 1
        this.allow_new_serial_cl = 1
        this.all_site = 1
        this.all_operation = 1
        this.all_product = 1
        this.site_code = 301
        this.site_id = "Site123"
        this.site_desc = "Site Description"
        this.zone_code = 401
        this.zone_id = "Zone123"
        this.zone_desc = "Zone Description"
        this.io_control = 1
        this.inbound_auto_create = 1
        this.operation_code = 501
        this.operation_id = "Op123"
        this.operation_desc = "Operation Description"
        this.local_control = 1
        this.product_io_control = 1
        this.site_restriction = 1
        this.serial_rule = "Rule"
        this.serial_min_length = 6
        this.serial_max_length = 12
        this.schedule_comments = "Comments"
        this.schedule_prefix = 601
        this.schedule_code = 701
        this.schedule_exec = 801
        this.ticket_prefix = 901
        this.ticket_code = 1001
        this.ticket_seq = 1101
        this.ticket_seq_tmp = 1201
        this.step_code = 1301
        this.tag_operational_code = 1401
        this.tag_operational_id = "Tag123"
        this.tag_operational_desc = "Tag Description"
        this.is_so = 1
        this.so_edit_start_end = 1
        this.so_order_type_code_default = 1501
        this.so_allow_change_order_type = 1
        this.so_allow_backup = 1
        this.so_optional_justify_problem = 1
        this.nc_recognize_email_in_comment = 1
    }
}