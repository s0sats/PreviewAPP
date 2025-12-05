package com.namoadigital.prj001.ui.act011.group_verification.domain.model

import com.namoadigital.prj001.ui.act011.group_verification.composable.components.badge.model.NamoaBadges

data class VerificationGroup(
    val vgCode: Int?,
    val title: String? = null, // Inspeção 260h
    val expired: Boolean = false, // Item expirado
    val predictedDate: String? = null, // Data prevista de conclusão
    val inExecution: Boolean = false, // Em execução
    val ticket: String? = null, // Ticket ID.CODE
    val user: String? = null, // USER PARTIÇÃO
    val alerts: List<NamoaBadges> = emptyList(), // ALERTAS
    val requiredByTickets: Int? = null, // Itens obrigatorios pela execucao do ticket.
    var isActive: Boolean = false, // AUTO-SELECT
    val canToggle: Boolean = true,// USUARIO PODE MUDAR SWITCH ?,
){


}
