package com.namoadigital.prj001.core.trip.domain.model.blockchain

import com.namoadigital.prj001.core.translate.TranslateWildCard

/**
 * Representa o resultado da validação de um intervalo de tempo contra a linha do tempo da viagem.
 */
sealed class ValidationResult {
    /**
     * Indica que o intervalo de tempo é válido e não conflita com nenhum bloco existente.
     */
    object Success : ValidationResult() {
        override var parameters: Map<String, String> = emptyMap()

        override fun addPlaceholders(placeholders: Map<String, String>): ValidationResult = this
    }

    /**
     * Indica que o intervalo de tempo proposto entra em conflito com um bloco existente.
     *
     * @param conflictingBlock O bloco da linha do tempo que causou a colisão. Pode ser nulo
     * se o conflito for mais geral (ex: sobreposição de múltiplos blocos).
     */
    data class Conflict(
        val conflictingBlock: TripTimelineBlock?,
        val message: TranslateWildCard
    ) : ValidationResult() {

        override var parameters: Map<String, String> = emptyMap()

        override fun addPlaceholders(placeholders: Map<String, String>) : ValidationResult {
            parameters = placeholders
            return this
        }

    }

    abstract var parameters: Map<String, String>

    abstract fun addPlaceholders(placeholders: Map<String, String>) : ValidationResult

}