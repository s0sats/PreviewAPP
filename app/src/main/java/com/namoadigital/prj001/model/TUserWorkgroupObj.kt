package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TUserWorkgroupObj(
    @SerializedName("user_code")
    var userCode: Int,
    @SerializedName("user_nick")
    var userNick: String,
    @SerializedName("user_name")
    var userName: String,
    @SerializedName("email_p")
    var emailP: String?,
    @SerializedName("user_image")
    var userImage: String?,
    @SerializedName("erp_code")
    var erpCode: String?
) : Serializable{
    fun getAllFieldForFilter() : String{
        return  "$userCode|" +
                "$userNick|" +
                "$userName|" +
                "$emailP|" +
                "$erpCode|"
                    .replace("null|","")
                    .replace("null","")
    }
}
