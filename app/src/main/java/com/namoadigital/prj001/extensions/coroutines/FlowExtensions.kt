package com.namoadigital.prj001.extensions.coroutines

import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.IResult.Companion.failed
import com.namoadigital.prj001.core.IResult.Companion.isError
import com.namoadigital.prj001.core.IResult.Companion.isSuccess
import com.namoadigital.prj001.core.IResult.Companion.loading
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

/**
 * ------------------------------------------------------------
 * 🧩 FlowSafe Extensions — Fluxos seguros com IResult<T>
 * ------------------------------------------------------------
 *
 * Conjunto de extensões utilitárias para simplificar o uso de
 * [Flow] com resultados do tipo [IResult].
 *
 * Recursos principais:
 * - Execução em contexto configurável (ex: [Dispatchers.IO]);
 * - Tratamento automático de exceções;
 * - Registro de erros via [ToolBox_Inf];
 * - Conversão segura para estados de [IResult].
 *
 * Ideal para uso em camadas de **Repository** ou **UseCase**,
 * onde falhas devem ser convertidas em resultados previsíveis.
 *
 * ---
 * **Exemplo:**
 * ```kotlin
 * fun loadUser(): Flow<IResult<User>> = flowSafe(Dispatchers.IO, "UserFlow") {
 *     emit(IResult.loading(true, "Carregando usuário..."))
 *     val user = repository.getUser()
 *     emit(IResult.success(user))
 * }.namoaCatch("UserFlow").dispatchersIO()
 * ```
 */

// ------------------------------------------------------------
// flowSafe — cria um fluxo seguro de IResult<T>
// ------------------------------------------------------------

/**
 * Executa um bloco suspenso dentro de um [Flow] protegido.
 *
 * - Aplica o contexto fornecido ([ioScope]);
 * - Captura exceções e registra via [ToolBox_Inf];
 * - Emite automaticamente um [IResult.isError] em caso de falha.
 *
 * @param ioScope Contexto de execução (geralmente [Dispatchers.IO]).
 * @param tag Identificador usado para logs e rastreamento.
 * @param block Bloco de emissão de [IResult].
 */
inline fun <T> flowSafe(
    ioScope: CoroutineContext = Dispatchers.IO,
    tag: String = "Unknown",
    crossinline block: suspend FlowCollector<IResult<T>>.() -> Unit,
): Flow<IResult<T>> = flow {
    try {
        block()
    } catch (e: Exception) {
        ToolBox_Inf.registerException(tag, e)
        emit(failed(exceptionError = e))
    }
}.namoaCatch(tag).flowOn(ioScope)

// ------------------------------------------------------------
// flowSafeGeneric — versão sem IResult<T>
// ------------------------------------------------------------

/**
 * Variante genérica de [flowSafe] para fluxos sem [IResult].
 *
 * - Executa o bloco em [ioScope];
 * - Captura e registra exceções;
 * - Repropaga o erro após o log.
 */
inline fun <T> flowSafeGeneric(
    ioScope: CoroutineContext = Dispatchers.IO,
    tag: String = "Unknown",
    crossinline block: suspend FlowCollector<T>.() -> Unit,
): Flow<T> = flow {
    try {
        block()
    } catch (e: Exception) {
        ToolBox_Inf.registerException(tag, IOException(e.message))
        throw e
    }
}.flowOn(ioScope)

// ------------------------------------------------------------
// safeIO — adiciona tratamento a um Flow<IResult<T>> existente
// ------------------------------------------------------------

/**
 * Adiciona captura de erros a um [Flow] que já emite [IResult].
 *
 * - Converte exceções em [IResult.isError];
 * - Registra o erro com [ToolBox_Inf];
 * - Mantém o fluxo ativo.
 */
fun <T> Flow<IResult<T>>.safeIO(
    ioScope: CoroutineContext = Dispatchers.IO,
    tag: String = "Unknown",
): Flow<IResult<T>> = this
    .namoaCatch(tag)
    .flowOn(ioScope)

// ------------------------------------------------------------
// mapToResultSafe — converte Flow<T> → Flow<IResult<T>>
// ------------------------------------------------------------

/**
 * Converte um [Flow] genérico em um fluxo de [IResult].
 *
 * - Emite [IResult.isSuccess] para cada valor;
 * - Converte exceções em [IResult.isError];
 * - Registra erros automaticamente.
 */
fun <T> Flow<T>.mapToResultSafe(
    ioScope: CoroutineContext = Dispatchers.IO,
    tag: String = "Unknown",
): Flow<IResult<T>> = flow {
    try {
        collect { value -> emit(IResult.success(value)) }
    } catch (e: Exception) {
        ToolBox_Inf.registerException(tag, IOException(e.message))
        emit(IResult.error("${e.message}", e))
    }
}.flowOn(ioScope)

// ------------------------------------------------------------
// safeFlowExecution — executa operação simples com segurança
// ------------------------------------------------------------

/**
 * Executa uma operação suspensa com captura de erro.
 *
 * Retorna:
 * - [IResult.isSuccess] em caso de sucesso;
 * - [IResult.isError] se ocorrer exceção.
 */
suspend inline fun <T> safeFlowExecution(
    tag: String = "Unknown",
    crossinline operation: suspend () -> T,
): IResult<T> = try {
    IResult.success(operation())
} catch (e: Exception) {
    ToolBox_Inf.registerException(tag, IOException(e.message))
    IResult.error("${e.message}", e)
}

// ------------------------------------------------------------
// namoaCatch — captura e registra erros em Flow<IResult<T>>
// ------------------------------------------------------------

/**
 * Captura exceções em um fluxo de [IResult] e registra com [ToolBox_Inf].
 *
 * Após o erro:
 * - Emite `loading(false)`;
 * - Emite `failed(e)`;
 *
 * Ideal para uso em repositórios.
 */
fun <T> Flow<IResult<T>>.namoaCatch(local: String) = apply {
    this.catch { e ->
        ToolBox_Inf.registerException(local, IOException(e.message))
        emit(loading(false))
        emit(failed(e))
    }
}

/**
 * Variante de [namoaCatch] que identifica o fluxo pela classe de origem.
 */
fun <T> Flow<IResult<T>>.namoaCatch(clazz: KClass<*>) = apply {
    this.catch { e ->
        ToolBox_Inf.registerException(clazz.simpleName, IOException(e.message))
        emit(loading(false))
        emit(failed(e))
    }
}

// ------------------------------------------------------------
// flowCatch — alternativa funcional para captura inline
// ------------------------------------------------------------

/**
 * Variante funcional de [namoaCatch] para encadeamento direto.
 *
 * Pode ser usada como parte de uma pipeline de fluxo.
 */
fun <T> Flow<IResult<T>>.flowCatch(local: String) = this.catch { e ->
    ToolBox_Inf.registerException(local, IOException(e.message))
    emit(loading(false))
    emit(failed(e))
}

// ------------------------------------------------------------
// dispatchersIO — define o fluxo para execução em IO
// ------------------------------------------------------------

/**
 * Define [Dispatchers.IO] como contexto de execução do fluxo.
 *
 * Simplifica encadeamentos, por exemplo:
 * ```kotlin
 * flowSafe(...).namoaCatch("tag").dispatchersIO()
 * ```
 */
fun <T> Flow<IResult<T>>.dispatchersIO() = this.flowOn(Dispatchers.IO)

/**
 * Transforma apenas os valores de sucesso ([IResult.isSuccess]) dentro do fluxo.
 *
 * Essa extensão permite aplicar uma transformação **somente** quando o estado atual
 * for sucesso, preservando todos os outros estados ([isError], [isLoading]) inalterados.
 *
 * É especialmente útil para adaptar dados de retorno sem precisar quebrar a cadeia
 * de `Flow<IResult<T>>`, mantendo o tratamento unificado de resultados.
 *
 * ---
 * **Exemplo:**
 * ```kotlin
 * flowSafe { emit(IResult.success(User("Ana"))) }
 *     .mapSuccess { user -> user.name.uppercase() }
 *     .collect { result ->
 *         when {
 *             result.isSuccess() -> println("Usuário: ${result.response}")
 *             result.isError() -> println("Erro: ${result.message}")
 *         }
 *     }
 * ```
 *
 * @param transform Função aplicada somente quando o resultado for sucesso.
 * @return Um novo [Flow] emitindo [IResult] com a transformação aplicada.
 */
inline fun <T, R> Flow<IResult<T>>.mapSuccess(
    crossinline transform: suspend (T) -> R,
): Flow<IResult<R>> = map { result ->
    when (result) {
        is IResult.isSuccess -> IResult.success(transform(result.response))
        is IResult.isError -> IResult.error(result.message, result.exceptionError)
        is IResult.isFailed -> failed(result.exceptionError)
        is IResult.isLoading -> loading(result.isLoading, result.message)
    }
}