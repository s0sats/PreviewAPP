package com.namoadigital.prj001.core.trip.domain.model.blockchain

/**
 * Representa o resultado da tentativa de localizar um bloco da timeline
 * que contenha completamente um intervalo [startDate, endDate].
 *
 * Cada variante indica um tipo específico de cenário encontrado durante a busca.
 */
sealed class FindBlockResult {

    /**
     * Retornado quando o intervalo informado está COMPLETAMENTE contido
     * dentro de exatamente um bloco da timeline.
     *
     * Exemplos:
     * - startDate/endDate estão dentro dos limites do bloco.
     * - startDate == block.startDate e endDate == block.endDate (match perfeito).
     */
    data class Success(val block: TripTimelineBlock) : FindBlockResult()

    /**
     * Retornado quando o intervalo NÃO pertence a nenhum bloco
     * e também NÃO se sobrepõe a nenhum bloco.
     *
     * Esse caso indica "lacuna" na timeline.
     *
     * @param previous Último bloco cuja data de fim ocorre ANTES de startDate,
     *                 ou null caso o intervalo esteja antes de toda a timeline.
     *
     * @param next Primeiro bloco cuja data de início ocorre APÓS endDate,
     *             ou null caso o intervalo esteja depois de toda a timeline.
     *
     * Ambos são úteis para diagnosticar erros, validar fluxos
     * ou sugerir blocos próximos.
     */
    data class NotFound(
        val previous: TripTimelineBlock?,
        val next: TripTimelineBlock?
    ) : FindBlockResult()

    /**
     * Retornado quando o intervalo informado gera sobreposição com
     * mais de um bloco OU quando há blocos da timeline sobrepostos entre si.
     *
     * Esse é um erro de integridade da timeline — significa que o intervalo
     * colide com um conjunto inválido de blocos.
     *
     * @param overlapStart startDate do primeiro bloco que participa da sobreposição.
     * @param overlapEnd endDate do último bloco sobreposto (pode ser null se o bloco for aberto).
     *
     * Esses valores servem para reportar qual região da timeline está inconsistente.
     */
    data class OverlapError(
        val overlapStart: TripTimelineBlock? = null,
        val overlapEnd: TripTimelineBlock? = null
    ) : FindBlockResult()

    /**
     * Retornado quando o intervalo fornecido é inválido:
     * - startDate >= endDate
     * - datas inconsistentes ou inversas
     *
     * Esse caso sempre é verificado antes de qualquer operação de timeline.
     */
    object InvalidDateRange : FindBlockResult()
}
