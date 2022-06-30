package com.namoadigital.prj001.model

import com.google.gson.annotations.SerializedName

class MdJustifyItem(
    @SerializedName("customer_code") var customerCode: Long,
    @SerializedName("justify_group_code") var justifyGroupCode: Int,
    @SerializedName("justify_item_code") var justifyItemCode: Int,
    @SerializedName("justify_item_id") var justifyItemId: String,
    @SerializedName("justify_item_desc") var justifyItemDesc: String,
    @SerializedName("required_comment") var requiredComment: Int,
    @SerializedName("reschedule") var reschedule: Int
)