package com.namoadigital.prj001.util.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferencesRepository <T>(
    private val context: Context,
    private val keyStrategy: KeyStrategy<T>,
    private val prefName: String
) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    // Salva um objeto
    fun save(item: T) {
        val key = keyStrategy.getKey(item)
        val json = gson.toJson(item)
        sharedPreferences.edit().putString(key, json).apply()
    }

    // Recupera um objeto baseado na chave
    fun get(key: String, clazz: Class<T>): T? {
        val json = sharedPreferences.getString(key, null) ?: return null
        return gson.fromJson(json, clazz)
    }

    // Recupera um objeto usando a estratégia de chave
    fun getByItem(item: T, clazz: Class<T>): T? {
        val key = keyStrategy.getKey(item)
        return get(key, clazz)
    }

    // Remove um objeto
    fun remove(item: T) {
        val key = keyStrategy.getKey(item)
        sharedPreferences.edit().remove(key).apply()
    }

    // Verifica se existe
    fun exists(item: T): Boolean {
        val key = keyStrategy.getKey(item)
        return sharedPreferences.contains(key)
    }

    // Limpa todos os dados
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}