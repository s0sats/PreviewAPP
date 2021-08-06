package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TkTicketOriginNc(
    @SerializedName("customer_code")
    private var customerCode: Int =-1,
    @SerializedName("ticket_prefix")
    private var ticketPrefix: Int=-1,
    @SerializedName("ticket_code")
    private var ticketCode: Int=-1,
    @SerializedName("page")
    private var page: Int,
    @SerializedName("custom_form_data_type")
    private var customFormDataType: String,
    @SerializedName("custom_form_order")
    private var customFormOrder: Int,
    @SerializedName("description")
    private var description: String,
    @SerializedName("data_value")
    private var dataValue: String?,
    @SerializedName("data_value_txt")
    private var dataValueTxt: String?,
    @SerializedName("data_value_local")
    private var dataValueLocal: String?,
    @SerializedName("data_photo1_url")
    private var dataPhoto1Url: String?,
    @SerializedName("data_photo1_url_local")
    private var dataPhoto1UrlLocal: String?,
    @SerializedName("data_photo2_url")
    private var dataPhoto2Url: String?,
    @SerializedName("data_photo2_url_local")
    private var dataPhoto2UrlLocal: String?,
    @SerializedName("data_photo3_url")
    private var dataPhoto3Url: String?,
    @SerializedName("data_photo3_url_local")
    private var dataPhoto3UrlLocal: String?,
    @SerializedName("data_photo4_url")
    private var dataPhoto4Url: String?,
    @SerializedName("data_photo4_url_local")
    private var dataPhoto4UrlLocal: String?,
    @SerializedName("data_comment")
    private var dataComment: String?,
    @SerializedName("picture_lines")
    private var pictureLines: Int?,
    @SerializedName("picture_columns")
    private var pictureColumns: Int?,
    @SerializedName("picture_color")
    private var pictureColor: String?,
    @SerializedName("picture_url")
    private var pictureUrl: String?,
    @SerializedName("picture_url_local")
    private var pictureUrlLocal: String?
): Serializable{

    fun setPk(tkTicket: TK_Ticket){
        customerCode = tkTicket.customer_code.toInt()
        ticketPrefix = tkTicket.ticket_prefix
        ticketCode = tkTicket.ticket_code
    }

    fun getCustomerCode() = customerCode
    fun setCustomerCode(customerCode:Int){this.customerCode = customerCode}

    fun getTicketPrefix() = ticketPrefix
    fun setTicketPrefix(ticketPrefix:Int){this.ticketPrefix = ticketPrefix}

    fun getTicketCode() = ticketCode
    fun setTicketCode(ticketCode:Int){this.ticketCode = ticketCode}

    fun getPage() = page
    fun setPage(page:Int){
        this.page = page
    }

    fun getCustomFormDataType() = customFormDataType
    fun setCustomFormDataType(customFormDataType: String) {
        this.customFormDataType = customFormDataType
    }

    fun getCustomFormOrder() = customFormOrder
    fun setCustomFormOrder(customFormOrder: Int) {this.customFormOrder = customFormOrder}

    fun getDescription() = description
    fun setDescription(description: String) {this.description = description }

    fun getDataValue() = dataValue
    fun setDataValue(dataValue: String){this.dataValue = dataValue }

    fun getDataValueTxt() = dataValueTxt
    fun setDataValueTxt(dataValueTxt: String){this.dataValueTxt = dataValueTxt }

    fun getDataValueLocal() = dataValueLocal
    fun setDataValueLocal(dataValueLocal: String?) { this.dataValueLocal = dataValueLocal }

    fun getDataPhoto1Url() = dataPhoto1Url
    fun setDataPhoto1Url(dataPhoto1Url: String?) { this.dataPhoto1Url = dataPhoto1Url }

    fun getDataPhoto1UrlLocal() = dataPhoto1UrlLocal
    fun setDataPhoto1UrlLocal(dataPhoto1UrlLocal: String?) { this.dataPhoto1UrlLocal = dataPhoto1UrlLocal }

    fun getDataPhoto2Url() = dataPhoto2Url
    fun setDataPhoto2Url(dataPhoto2Url: String?) { this.dataPhoto2Url = dataPhoto2Url }

    fun getDataPhoto2UrlLocal() = dataPhoto2UrlLocal
    fun setDataPhoto2UrlLocal(dataPhoto2UrlLocal: String?) { this.dataPhoto2UrlLocal = dataPhoto2UrlLocal }

    fun getDataPhoto3Url() = dataPhoto3Url
    fun setDataPhoto3Url(dataPhoto3Url: String?) { this.dataPhoto3Url = dataPhoto3Url }

    fun getDataPhoto3UrlLocal() = dataPhoto3UrlLocal
    fun setDataPhoto3UrlLocal(dataPhoto3UrlLocal: String?) { this.dataPhoto3UrlLocal = dataPhoto3UrlLocal }

    fun getDataPhoto4Url() = dataPhoto4Url
    fun setDataPhoto4Url(dataPhoto4Url: String?) { this.dataPhoto4Url = dataPhoto4Url }

    fun getDataPhoto4UrlLocal() = dataPhoto4UrlLocal
    fun setDataPhoto4UrlLocal(dataPhoto4UrlLocal: String?) { this.dataPhoto4UrlLocal = dataPhoto4UrlLocal }

    fun getDataComment() = dataComment
    fun getDataComment(dataComment: String?) { this.dataComment = dataComment}

    fun getPictureLines() = pictureLines
    fun setPictureLines(pictureLines: Int?) { this. pictureLines = pictureLines}

    fun getPictureColumns() = pictureColumns
    fun setPictureColumns(pictureColumns: Int?) {this.pictureColumns = pictureColumns }

    fun getPictureColor() = pictureColor
    fun setPictureColor(pictureColor: String?) {this.pictureColor = pictureColor }

    fun getPictureUrl() = pictureUrl
    fun setPictureUrl(pictureUrl: String?) {this.pictureUrl = pictureUrl}

    fun getPictureUrlLocal() = pictureUrlLocal
    fun setPictureUrlLocal(pictureUrlLocal: String?) { this.pictureUrlLocal = pictureUrlLocal }
    
    override fun toString(): String {
        return "TkTicketOriginNc(customerCode=$customerCode, \nticketPrefix=$ticketPrefix, \nticketCode=$ticketCode, \npage=$page, \ncustomFormDataType='$customFormDataType', \ncustomFormOrder=$customFormOrder, \ndescription='$description', \ndataValue='$dataValue', \ndataValueTxt='$dataValueTxt', \ndataValueLocal=$dataValueLocal, \ndataPhoto1Url=$dataPhoto1Url, \ndataPhoto1UrlLocal=$dataPhoto1UrlLocal, \ndataPhoto2Url=$dataPhoto2Url, \ndataPhoto2UrlLocal=$dataPhoto2UrlLocal, \ndataPhoto3Url=$dataPhoto3Url, \ndataPhoto3UrlLocal=$dataPhoto3UrlLocal, \ndataPhoto4Url=$dataPhoto4Url, \ndataPhoto4UrlLocal=$dataPhoto4UrlLocal, \ndataComment=$dataComment, \npictureLines=$pictureLines, \npictureColumns=$pictureColumns, \npictureColor=$pictureColor, \npictureUrl=$pictureUrl, \npictureUrlLocal=$pictureUrlLocal)"
    }

    companion object{
        const val TAB = "TAB"
        const val CHAR = "CHAR"
        const val CHECKBOX = "CHECKBOX"
        const val COMBOBOX = "COMBOBOX"
        const val DATE = "DATE"
        const val HOUR = "HOUR"
        const val LABEL = "LABEL"
        const val NUMBER = "NUMBER"
        const val PHOTO = "PHOTO"
        const val PICTURE = "PICTURE"
        const val RATINGBAR = "RATINGBAR"
        const val RATINGIMAGE = "RATINGIMAGE"
        const val RATINGIMAGE_GREEN = "GREEN"
        const val RATINGIMAGE_YELLOW = "YELLOW"
        const val RATINGIMAGE_RED = "RED"
        const val RATINGIMAGE_NA = "NA"

    }

}



