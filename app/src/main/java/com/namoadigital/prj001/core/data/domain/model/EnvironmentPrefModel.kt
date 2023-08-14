package com.namoadigital.prj001.core.data.domain.model

data class EnvironmentPrefModel(
    val environment: EnvironmentType
)

fun String?.toEnvironmentType(): EnvironmentType {
    return when (this) {
        EnvironmentType.DEVELOPMENT.string -> EnvironmentType.DEVELOPMENT
        EnvironmentType.HOMOLOG.string -> EnvironmentType.HOMOLOG
        else -> EnvironmentType.NULL
    }
}


enum class EnvironmentType(val string: String) {

    DEVELOPMENT("dev"),
    HOMOLOG("homolog"),
    NULL("")

}
