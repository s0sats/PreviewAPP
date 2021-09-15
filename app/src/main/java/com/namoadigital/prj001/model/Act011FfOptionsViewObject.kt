package com.namoadigital.prj001.model

import java.io.Serializable

class Act011FfOptionsViewObject(
    val formDesc: String,
    var tabs: List<Act011FormTab>,
    var tabSelected: Int,
    val formStatus: String,
    val signature: String,
    val automatic: Boolean,
    val isTicketForm: Boolean,
    val isSOForm: Boolean
): Serializable
