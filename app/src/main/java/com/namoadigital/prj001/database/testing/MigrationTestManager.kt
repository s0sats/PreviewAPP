package com.namoadigital.prj001.database.testing

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction
import com.namoadigital.prj001.core.database.base.BaseDaoWithReturn
import org.junit.Assert.assertTrue

/**
 * Classe gestora genérica e reutilizável que gerencia o ciclo de vida completo
 * de qualquer banco de dados (que herde de SQLiteOpenHelper) em testes de migração.
 *
 * Esta classe abstrai a criação, o upgrade e a limpeza do banco de dados,
 * permitindo que os testes se concentrem apenas na lógica da migração.
 *
 * @param T O tipo da classe de banco de dados a ser testada (ex: DatabaseHelperMulti).
 * @param context O contexto da aplicação ou do teste de instrumentação.
 * @param dbHelperFactory Uma função "fábrica" que ensina o Manager a criar uma instância
 * do seu SQLiteOpenHelper. Isso elimina a necessidade de reflection, tornando o
 * processo mais seguro e performático. Ex: `{ ctx, name, version -> DatabaseHelperMulti(ctx, name, version) }`
 */
class MigrationTestManager<T : SQLiteOpenHelper>(
    private val context: Context,
    private val dbHelperFactory: (ctx: Context, name: String, version: Int) -> T
) {

    private lateinit var db: SQLiteDatabase
    private lateinit var dbHelper: T
    private val dbName = "migration_test.db"

    /**
     * Prepara o ambiente para um teste limpo, garantindo que qualquer banco de dados
     * de uma execução anterior seja excluído.
     *
     * Deve ser chamado no bloco `@Before` do seu teste.
     */
    fun setup() {
        context.deleteDatabase(dbName)
    }

    /**
     * Cria uma nova instância do banco de dados em uma versão de schema específica.
     * Este método é usado para configurar o estado "antigo" do banco antes da migração.
     *
     * @param version A versão do schema que o banco de dados deve ter ao ser criado.
     */
    fun createDatabaseAtVersion(version: Int) {
        dbHelper = dbHelperFactory(context, dbName, version)
        db = dbHelper.writableDatabase
    }

    /**
     * Dispara o processo de migração do banco de dados.
     * Ele fecha a conexão com a versão antiga e abre uma nova com a versão mais recente,
     * o que força o Android a chamar o método `onUpgrade` do seu Helper.
     *
     * @param newVersion A versão de destino para a qual o banco de dados deve ser migrado.
     */
    fun runMigrationTo(newVersion: Int) {
        dbHelper.close()
        dbHelper = dbHelperFactory(context, dbName, newVersion)
        db = dbHelper.writableDatabase
    }

    /**
     * Insere um modelo de dados no banco de forma type-safe, reutilizando a lógica
     * de um DAO moderno que herda de `BaseDaoWithReturn`.
     *
     * @param M O tipo do modelo de dados (ex: MDVerificationGroup).
     * @param D O tipo do DAO (ex: MDVerificationGroupDao).
     * @param dao A instância do DAO a ser usada para obter o nome da tabela e a lógica de mapeamento.
     * @param model O objeto de modelo a ser inserido.
     */
    fun <M, D : BaseDaoWithReturn<M>> insert(dao: D, model: M) {
        db.transaction {
            val tableName = dao.tableName
            val values = dao.modelToContentValues(model, ContentValues())
            insertOrThrow(tableName, null, values)
        }
    }

    /**
     * Insere um modelo de dados no banco, recebendo o nome da tabela e uma função "mapper".
     *
     * É a solução ideal para DAOs legados (que implementam `Dao<T>`) ou casos customizados
     * onde a lógica de mapeamento não é pública ou padronizada.
     *
     * @param M O tipo do modelo de dados (ex: GE_Custom_Form_Blob).
     * @param tableName O nome exato da tabela no banco.
     * @param model O objeto de modelo a ser inserido.
     * @param mapper Uma função lambda que ensina como converter o objeto `model` em `ContentValues`.
     */
    fun <M> insert(
        tableName: String,
        model: M,
        mapper: (M) -> ContentValues
    ) {
        db.transaction {
            val values = mapper(model)
            insertOrThrow(tableName, null, values)
        }
    }

    /**
     * Retorna a instância do banco de dados.
     */
    fun getDB(): SQLiteDatabase {
        return db
    }

    /**
     * Executa uma query SQL e permite processar o resultado dentro de um bloco seguro,
     * garantindo que o Cursor seja fechado automaticamente.
     *
     * @param R O tipo de retorno esperado do bloco de processamento.
     * @param sql A instrução SQL a ser executada.
     * @param selectionArgs Argumentos para a cláusula WHERE (substitui os '?').
     * @param block A função lambda que recebe o Cursor e processa o resultado.
     * @return O resultado processado pelo bloco.
     */
    fun <R> query(sql: String, selectionArgs: Array<String>? = null, block: (Cursor) -> R): R {
        val cursor = db.rawQuery(sql, selectionArgs)
        return cursor.use(block) // `use` garante o fechamento do cursor
    }

    /**
     * Executa uma query que deve retornar um único valor de qualquer tipo primitivo.
     * A função é 'inline' e o tipo 'T' é 'reified', o que permite verificar
     * o tipo em tempo de execução e chamar o método 'get' apropriado do Cursor.
     *
     * @param T O tipo de dado esperado (ex: String::class, Int::class).
     * @return O valor encontrado, ou nulo se a query não retornar resultados.
     * @throws IllegalArgumentException se o tipo T não for suportado.
     */
    inline fun <reified T> querySingleValue(sql: String, selectionArgs: Array<String>? = null): T? {
        return query(sql, selectionArgs) { cursor ->
            if (cursor.moveToFirst()) {
                when (T::class) {
                    String::class -> cursor.getString(0) as T?
                    Int::class -> cursor.getInt(0) as T?
                    Long::class -> cursor.getLong(0) as T?
                    Double::class -> cursor.getDouble(0) as T?
                    Float::class -> cursor.getFloat(0) as T?
                    Short::class -> cursor.getShort(0) as T?
                    else -> throw IllegalArgumentException("Tipo não suportado ${T::class.java.name}")
                }
            } else {
                null
            }
        }
    }

    /**
     * Verifica se uma tabela específica existe no banco de dados.
     * Lança um erro de asserção (AssertionError) se a tabela não for encontrada.
     *
     * @param tableName O nome da tabela a ser verificada.
     */
    fun assertTableExists(tableName: String) {
        val exists =
            query("SELECT name FROM sqlite_master WHERE type='table' AND name='$tableName'") { cursor ->
                cursor.count > 0
            }
        assertTrue("A tabela '$tableName' deveria existir no banco de dados.", exists)
    }

    /**
     * Verifica se uma coluna específica existe em uma determinada tabela.
     * Lança um erro de asserção (AssertionError) se a coluna não for encontrada.
     *
     * @param tableName O nome da tabela onde a coluna deveria existir.
     * @param columnName O nome da coluna a ser verificada.
     */
    fun assertColumnExists(tableName: String, columnName: String) {
        var columnFound = false
        query("PRAGMA table_info($tableName)") { cursor ->
            val nameColumnIndex = cursor.getColumnIndex("name")
            if (nameColumnIndex == -1) return@query

            while (cursor.moveToNext()) {
                val currentColumnName = cursor.getString(nameColumnIndex)
                if (currentColumnName.equals(columnName, ignoreCase = true)) {
                    columnFound = true
                    break
                }
            }
        }
        assertTrue("A coluna '$columnName' deveria existir na tabela '$tableName'.", columnFound)
    }

    /**
     * Limpa os recursos e fecha a conexão com o banco de dados.
     *
     * Deve ser chamado no bloco `@After` do seu teste para evitar vazamentos.
     */
    fun tearDown() {
        if (::db.isInitialized && db.isOpen) {
            db.close()
        }
    }
}
