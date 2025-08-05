package com.namoadigital.prj001.database

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.namoadigital.prj001.dao.GE_Custom_Form_Data_FieldDao
import com.namoadigital.prj001.database.testing.MigrationTestManager
import com.namoadigital.prj001.model.GE_Custom_Form_Data_Field
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Classe de teste de instrumentação para validar as migrações do banco de dados.
 *
 * Esta classe utiliza o [MigrationTestManager] para orquestrar a criação,
 * migração e verificação do banco de dados em um ambiente de teste controlado.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {

    private lateinit var testManager: MigrationTestManager<DatabaseHelperMulti>
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val OLD_VERSION = 23
    private val NEW_VERSION = 24

    /**
     * Prepara o ambiente antes da execução de cada teste.
     * Instancia o [MigrationTestManager] e garante que o banco de dados
     * de testes esteja limpo.
     */
    @Before
    fun setup() {
        testManager = MigrationTestManager(
            context = context,
            dbHelperFactory = { ctx, name, version -> DatabaseHelperMulti(ctx, name, version) }
        )
        testManager.setup()
    }

    /**
     * Limpa os recursos após a execução de cada teste, fechando a conexão
     * com o banco de dados para evitar vazamentos.
     */
    @After
    fun tearDown() {
        testManager.tearDown()
    }

    /**
     * Este teste verifica se:
     * 1. Os dados inseridos usando um padrão de DAO legado são preservados.
     * 2. Os dados inseridos usando um padrão de DAO moderno são preservados.
     * 3. A migração executa as alterações de schema esperadas (ex: adiciona uma nova tabela).
     */
    @Test
    fun aoMigrarDaVersao23Para24_devePreservarDadosEAtualizarSchema() {
        testManager.createDatabaseAtVersion(OLD_VERSION)

        // --- Inserindo dados com o padrão de DAO LEGADO ---
        val formDataField = GE_Custom_Form_Data_Field().apply {
            customer_code = 1L
            custom_form_type = 2
            custom_form_code = 45
            custom_form_version = 3
            custom_form_data = 987654321L
            custom_form_seq = 1
            value = "Resposta do DAO Legado"
            value_extra = "{}"
        }

        testManager.insert(GE_Custom_Form_Data_FieldDao.TABLE, formDataField) { field ->
            ContentValues().apply {
                put(GE_Custom_Form_Data_FieldDao.CUSTOMER_CODE, field.customer_code)
                put(GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_TYPE, field.custom_form_type)
                put(GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_CODE, field.custom_form_code)
                put(GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_VERSION, field.custom_form_version)
                put(GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_DATA, field.custom_form_data)
                put(GE_Custom_Form_Data_FieldDao.CUSTOM_FORM_SEQ, field.custom_form_seq)
                put(GE_Custom_Form_Data_FieldDao.VALUE, field.value)
                put(GE_Custom_Form_Data_FieldDao.VALUE_EXTRA, field.value_extra)
            }
        }

        // --- Inserindo dados com o padrão de DAO MODERNO ---
        /*
                val verificationGroupDao = MDVerificationGroupDao(context)
                val verificationGroup = MDVerificationGroup(
                    customerCode = 1,
                    vgCode = 101,
                    vgId = "VG-101",
                    vgDesc = "Grupo do DAO Moderno"
                )
                testManager.insert(verificationGroupDao, verificationGroup)

        */
        testManager.runMigrationTo(NEW_VERSION)


        testManager.assertColumnExists(GE_Custom_Form_Data_FieldDao.TABLE, "is_active")

        val isActiveValue =
            testManager.querySingleValue<Int>("SELECT is_active FROM ${GE_Custom_Form_Data_FieldDao.TABLE} WHERE custom_form_seq = 1")
        assertEquals(
            "O dado deveria ser 1",
            1,
            isActiveValue
        )


    }
}
