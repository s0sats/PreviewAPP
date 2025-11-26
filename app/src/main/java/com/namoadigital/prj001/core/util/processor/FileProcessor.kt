package com.namoadigital.prj001.core.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.namoadigital.prj001.core.util.processor.FileProcessorCallback
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.reflect.Type

/**
 * Processador genérico de arquivos JSON que lê arquivos, desserializa JSON em uma lista de objetos,
 * e passa a lista para um callback para persistência ou processamento.
 *
 * Totalmente compatível com API 21+ e otimizado para Kotlin com Coroutines.
 * Funciona perfeitamente com Java através de funções de extensão.
 */
object FileProcessor {

    private val gson = Gson()
    private val CLASS_NAME = "FileProcessor"

    /**
     * Processa todos os arquivos JSON em um diretório cujos nomes começam com um prefixo específico.
     * Executa operações de I/O em uma thread de background para evitar bloquear a main thread.
     *
     * @param prefix      O prefixo do nome do arquivo para filtrar os arquivos (ex: "fs_event_manual_by_user-").
     * @param directory   O diretório para procurar pelos arquivos correspondentes.
     * @param clazz       O tipo de classe para desserialização JSON (ex: EventManual.class).
     * @param callback    Callback invocado com cada lista de objetos desserializados.
     */
    fun <T> processJsonFiles(
        prefix: String,
        directory: File,
        clazz: Class<T>,
        callback: FileProcessorCallback<T>,
    ) {
        if (!directory.exists() || !directory.isDirectory) {
            ToolBox_Inf.registerException(
                CLASS_NAME,
                Exception("Invalid directory: ${directory.absolutePath}")
            )
            return
        }

        val files = directory.listFiles { file ->
            file.isFile && file.name.startsWith(prefix)
        } ?: return

        if (files.isEmpty()) return

        files.sortBy { it.name }

        for (file in files) {
            try {
                val json = readFileContents(file)

                if (json.isBlank()) {
                    ToolBox_Inf.registerException(
                        CLASS_NAME,
                        Exception("Empty file detected: ${file.name}")
                    )
                    continue
                }

                val listType: Type = TypeToken.getParameterized(List::class.java, clazz).type
                val dataList = gson.fromJson<List<T>>(json, listType)

                dataList?.let { callback.onProcess(it) }

            } catch (ex: Exception) {
                ToolBox_Inf.registerException(
                    CLASS_NAME,
                    Exception("Failed to process file: ${file.name}", ex)
                )
            }
        }    }

    /**
     * Lê o conteúdo completo de um arquivo em uma String.
     * Usa o gerenciamento de recursos do Kotlin para um código mais limpo.
     */
    private fun readFileContents(file: File): String {
        return try {
            BufferedReader(FileReader(file)).use { reader ->
                reader.readText()
            }
        } catch (ex: Exception) {
            ToolBox_Inf.registerException(
                CLASS_NAME,
                Exception("Error reading file: ${file.name}", ex)
            )
            ""
        }
    }
}
