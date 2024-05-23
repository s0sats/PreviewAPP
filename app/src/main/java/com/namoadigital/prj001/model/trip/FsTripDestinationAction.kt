package com.namoadigital.prj001.model.trip

import com.google.gson.annotations.SerializedName

data class FsTripDestinationAction(
    @SerializedName("actionSeq") var actionSeq: Int,
    @SerializedName("siteCode") var siteCode : Int,
    @SerializedName("siteDesc") var siteDesc : String,
    @SerializedName("regionCode") var regionCode : Int?,
    @SerializedName("regionDesc") var regionDesc : String?,
    @SerializedName("actType") var actType : String, //Foi acordado com Jhon que so sera exibido actType == "FORM" ou resto deve ser ignorado.
    @SerializedName("actDesc") var actDesc : String?,
    @SerializedName("actPDFLocal") var actPDFLocal : String?,
    @SerializedName("actPDFName") var actPDFName : String?,
    @SerializedName("actPDFUrl") var actPDFUrl : String?,
    @SerializedName("productCode") var productCode : Int,
    @SerializedName("productDesc") var productDesc : String,
    @SerializedName("serialCode") var serialCode : Int,
    @SerializedName("serialId") var serialId : String,
    @SerializedName("serialInf1") var serialInf1 : String?,
    @SerializedName("brandDesc") var brandDesc : String?,
    @SerializedName("modelDesc") var modelDesc : String?,
    @SerializedName("dateStart") var dateStart : String,
    @SerializedName("dateEnd") var dateEnd : String,
    @SerializedName("processType") var processType : String?,
    @SerializedName("ticketPrefix") var ticketPrefix : Int?,
    @SerializedName("ticketCode") var ticketCode : Int?,
    @SerializedName("ticketId") var ticketId : String?,
    @SerializedName("kanbanData") var kanbanData : String?,
    @SerializedName("customFormType") var customFormType : Int?,
    @SerializedName("customFormCode") var customFormCode : Int?,
    @SerializedName("customFormVersion") var customFormVersion : Int?,
    @SerializedName("customFormData") var customFormData : Int?,
) {
    @SerializedName("customerCode") var customerCode: Long = -1
    @SerializedName("tripPrefix") var tripPrefix: Int= -1
    @SerializedName("tripCode") var tripCode: Int= -1
    @SerializedName("destinationSeq") var destinationSeq: Int= -1
    constructor(
        customerCode: Long,
        tripPrefix: Int,
        tripCode: Int,
        destinationSeq: Int,
        actionSeq: Int,
        siteCode : Int,
        siteDesc : String,
        regionCode : Int?,
        regionDesc : String?,
        actType : String,
        actDesc : String?,
        productCode : Int,
        productDesc : String,
        serialCode : Int,
        serialId : String,
        serialInf1 : String?,
        brandDesc : String?,
        modelDesc : String?,
        dateStart : String,
        dateEnd : String,
        processType : String?,
        ticketPrefix : Int?,
        ticketCode : Int?,
        ticketId : String?,
        kanbanData : String?,
        actPDFLocal : String?,
        actPDFName : String?,
        actPDFUrl : String?,
        customFormType : Int?,
        customFormCode : Int?,
        customFormVersion : Int?,
        customFormData : Int?,
    ): this(
        actionSeq,
        siteCode,
        siteDesc,
        regionCode,
        regionDesc,
        actType,
        actDesc,
        actPDFLocal,
        actPDFName,
        actPDFUrl,
        productCode,
        productDesc,
        serialCode,
        serialId,
        serialInf1,
        brandDesc,
        modelDesc,
        dateStart,
        dateEnd,
        processType,
        ticketPrefix,
        ticketCode,
        ticketId,
        kanbanData,
        customFormType,
        customFormCode,
        customFormVersion,
        customFormData,
    ){
        this.customerCode = customerCode
        this.tripPrefix = tripPrefix
        this.tripCode = tripCode
        this.destinationSeq = destinationSeq
    }

    fun setPk(fsTripDestination: FsTripDestination) {
        this.customerCode = fsTripDestination.customerCode
        this.tripPrefix = fsTripDestination.tripPrefix
        this.tripCode = fsTripDestination.tripCode
        this.destinationSeq = fsTripDestination.destinationSeq
    }
}