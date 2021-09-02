package com.namoadigital.prj001.model

import java.io.Serializable


class AcessoryFormView(
    val acessoryName: String,
    val inspections: List<InspectionCell>
): Serializable {
}