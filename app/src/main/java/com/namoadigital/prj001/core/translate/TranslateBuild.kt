package com.namoadigital.prj001.core.translate

import android.content.Context
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

typealias TranslateVar = String
typealias ListTranslateVars = List<TranslateVar>

/**
 * Mapa de traduções, normalmente algo como Map<String, String>
 * Exemplo:  "login_success" -> "Login realizado com sucesso"
 */
typealias TranslateMap = Map<String, String>

/**
 * Obtém o texto traduzido usando uma chave simples.
 *
 * @param key Chave da tradução (string).
 * @return Texto traduzido ou "../key" se não encontrado.
 */
fun TranslateMap.textOf(key: String): String =
    this[key] ?: "../$key"

/**
 * Obtém o texto traduzido usando uma chave tipada (`TranslateKey`).
 *
 * @param key Objeto que contém a chave da tradução.
 * @return Texto traduzido ou "../key" se não encontrado.
 */
fun TranslateMap.textOf(key: TranslateKey): String =
    this[key.key] ?: "../${key.key}"

/**
 * Obtém o texto traduzido e substitui placeholders por valores.
 * Caso não exista placeholders ou args seja null/empty, retorna o texto base.
 *
 * Exemplo:
 *  Chave no map: "order_message" = "Pedido #{{id}} confirmado com sucesso."
 *  textOf(OrderMessage, mapOf("id" to "123")) -> "Pedido #123 confirmado com sucesso."
 *
 * @param value Chave de tradução com texto base.
 * @param args Mapa onde chave = nome do placeholder, valor = substituição.
 * @return Texto com placeholders substituídos.
 */
fun TranslateMap.textOf(value: TranslateKey, args: Map<String, String>? = null): String {
    val base = this[value.key] ?: "../${value.key}"
    if (args.isNullOrEmpty()) return base

    var result = base
    args.forEach { (key, replacement) ->
        result = result.replace("{{$key}}", replacement)
    }
    return result
}

/**
 * Obtém o texto traduzido usando uma chave com placeholders,
 * substituindo automaticamente pela ordem dos valores.
 *
 * Exemplo:
 *   key.placeholders = ["name", "date"]
 *   values = ["UserName", "14/03"]
 *
 *   "Olá {{name}}, seu registro é de {{date}}."
 *   -> "Olá UserName, seu registro é de 14/03."
 *
 * @param key Chave de tradução com placeholders definidos.
 * @param values Lista contendo os valores que substituem os placeholders na ordem definida.
 *
 * @throws IllegalArgumentException caso o número de valores não seja igual ao número de placeholders.
 *
 * @return Texto traduzido com placeholders substituídos.
 */
fun TranslateMap.textOf(key: TranslateWildCard, values: List<String>): String {
    if (key.placeholders.isEmpty()) return textOf(key)

/*    require(values.size == key.placeholders.size) {
        "Key '${key.key}' requires ${key.placeholders.size} args: ${key.placeholders}, but received ${values.size}"
    }*/

    val args = key.placeholders.zip(values).toMap()
    return textOf(key, args)
}

class TranslateBuild @Inject constructor(
    @ApplicationContext private val context: Context
) {

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

    fun listVars(block: () -> List<String>): TranslateBuild {
        this.listTranslateVars = block()
        return this
    }

    fun listVarsKeys(block: () -> List<TranslateKey>): TranslateBuild {
        this.listTranslateVars = block().map { it.key }
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

        return result.filter { listTranslateVars.contains(it.key) || it.key.contains("sys_") }
    }
}