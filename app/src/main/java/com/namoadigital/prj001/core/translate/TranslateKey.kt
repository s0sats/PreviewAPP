package com.namoadigital.prj001.core.translate

/**
 * Representa uma chave simples de tradução.
 * Exemplo:
 *   object LoginSuccess : TranslateKey { override val key = "login_success" }
 */
interface TranslateKey {
    val key: String
}

/**
 * Representa uma chave de tradução que contém placeholders (curingas).
 * Exemplo:
 *   object WelcomeUser : TranslateWildCard {
 *      override val key = "welcome_user"
 *      override val placeholders = listOf("username")
 *   }
 *
 * No arquivo de tradução:
 *   "welcome_user" = "Bem-vindo, {{username}}!"
 */
interface TranslateWildCard : TranslateKey {
    val placeholders: List<String>
}

