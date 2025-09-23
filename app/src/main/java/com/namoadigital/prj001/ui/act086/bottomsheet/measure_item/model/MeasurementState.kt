package com.namoadigital.prj001.ui.act086.bottomsheet.measure_item.model

data class MeasurementState(
    val initialValue: Int? = null,
    val initialId: String? = null,
    val afterValue: Int? = null,
    val afterId: String? = null,
    val isEditingInitial: Boolean = false,
    val isEditingAfter: Boolean = false
) {
    // Helper functions para verificar estados
    fun hasInitialMeasurement() = initialValue != null
    fun hasAfterMeasurement() = afterValue != null

    // Função para atualizar medição inicial
    fun withInitialMeasurement(value: Int, id: String): MeasurementState {
        return this.copy(
            initialValue = value,
            initialId = id,
            isEditingInitial = false
        )
    }

    // Função para atualizar medição final
    fun withAfterMeasurement(value: Int, id: String): MeasurementState {
        return this.copy(
            afterValue = value,
            afterId = id,
            isEditingAfter = false
        )
    }

    // Funções para alternar modo de edição
    fun withEditingInitial(editing: Boolean): MeasurementState {
        return this.copy(isEditingInitial = editing)
    }

    fun withEditingAfter(editing: Boolean): MeasurementState {
        return this.copy(isEditingAfter = editing)
    }
}