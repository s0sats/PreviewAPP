package com.namoadigital.prj001.core.translate

import android.content.Context
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf

typealias TranslateVar = String
typealias ListTranslateVars = List<TranslateVar>
typealias TranslateMap = Map<String, String>

fun TranslateMap.translate(key: String): String = this[key] ?: "../$key"


class TranslateBuild(private val context: Context) {

    private var moduleCode: String = ConstantBaseApp.APP_MODULE
    private var resourceName: String? = null
    private var listTranslateVars: ListTranslateVars = emptyList()

    fun resource(name: String): TranslateBuild {
        this.resourceName = name
        return this
    }

    fun listVars(vars: List<String>): TranslateBuild {
        this.listTranslateVars = vars
        return this
    }

    fun listVars(vararg vars: String): TranslateBuild {
        this.listTranslateVars = vars.toList()
        return this
    }

    fun build(): TranslateMap {
        val resourceCode = ToolBox_Inf.getResourceCode(
            context,
            moduleCode,
            resourceName ?: throw NullPointerException("resourceName has not been defined")
        )

        val result = TranslateResource(
            context,
            moduleCode,
            resourceCode
        ).setLanguage(listTranslateVars)

        return result.filter { listTranslateVars.contains(it.key) || it.key.contains("sys_")}
    }
}