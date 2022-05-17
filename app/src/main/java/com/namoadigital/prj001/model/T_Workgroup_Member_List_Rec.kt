package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class T_Workgroup_Member_List_Rec: Main_Header_Rec(){
    @SerializedName("data") var data : ArrayList<TWorkgroupObj> = arrayListOf()
}
