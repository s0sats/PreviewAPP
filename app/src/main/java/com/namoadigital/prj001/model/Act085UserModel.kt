package com.namoadigital.prj001.model

import java.io.Serializable

data class Act085UserModel(
    val users: List<TUserWorkgroupObj>,
    val usersCount: Int,
    val recordPage: Int
    ): Serializable
