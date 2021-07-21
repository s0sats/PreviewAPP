package com.namoadigital.prj001.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TUser_Search_Rec(
    @SerializedName("app")
    @Expose
    val app:String,
    @SerializedName("validation")
    @Expose
    val validation:String,
    @SerializedName("link_url")
    @Expose
    val link_url:String,
    @SerializedName("error_msg")
    @Expose
    val error_msg:String,
    @SerializedName("record")
    @Expose
    val record: List<TUserWorkgroupObj>?,
    @SerializedName("record_count")
    @Expose
    val usersCount: Int?,
    @SerializedName("record_page")
    @Expose
    val recordPage: Int?
    ){
    fun getAct085UserModel(): Act085UserModel{
        return Act085UserModel(record?: mutableListOf(), usersCount ?: 0, recordPage ?: 0)
    }
}