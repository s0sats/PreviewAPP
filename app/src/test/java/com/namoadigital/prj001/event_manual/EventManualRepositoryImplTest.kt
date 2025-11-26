package com.namoadigital.prj001.event_manual

import android.content.Context
import app.cash.turbine.test
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.dao.GE_FileDao
import com.namoadigital.prj001.dao.event.EventManualDao
import com.namoadigital.prj001.dao.trip.FSEventTypeDao
import com.namoadigital.prj001.model.DaoObjReturn
import com.namoadigital.prj001.model.event.local.EventManual
import com.namoadigital.prj001.model.event.local.EventManual.Photo
import com.namoadigital.prj001.ui.act005.trip.di.enums.EventStatus
import com.namoadigital.prj001.ui.act095.event_manual.data.EventManualRepositoryImpl
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Con.isOnline
import com.namoadigital.prj001.util.ToolBox_Inf
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mockStatic

@RunWith(JUnit4::class)
class EventManualRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var typeDao: FSEventTypeDao
    private lateinit var eventDao: EventManualDao
    private lateinit var fileDao: GE_FileDao
    private lateinit var repository: EventManualRepositoryImpl

    private val fakeEvent = EventManual(
        appId = "app-xyz",
        user = 10,
        eventDay = 20251013,
        typeCode = 5,
        comments = "teste online",
        photo = Photo(local = "tmp/photo.jpg", isChanged = true),
        dateStart = "2025-10-13 12:00",
        status = EventStatus.DONE
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        context = mockk(relaxed = true)
        typeDao = mockk(relaxed = true)
        eventDao = mockk(relaxed = true)
        fileDao = mockk(relaxed = true)
        repository = EventManualRepositoryImpl(context, typeDao, eventDao, fileDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `should emit success when saving event offline`() = runBlocking {
        val toolboxConMock = mockStatic(ToolBox_Con::class.java)
        val toolboxInfMock = mockStatic(ToolBox_Inf::class.java)

        // Força modo offline
        toolboxConMock.`when`<Boolean> { isOnline(context) }
            .thenReturn(false)

        // Mocka retorno DAO
        val daoReturn = DaoObjReturn().apply {
            setError(false)
            action = DaoObjReturn.INSERT
            actionReturn = 1
        }

        coEvery { eventDao.saveEvent(any()) } returns daoReturn

        // Executa o flow
        val flow = repository.saveEvent(fakeEvent)

        flow.test {
            val loading1 = awaitItem()
            assert(loading1 is IResult.isLoading)

            val loading2 = awaitItem()
            assert(loading2 is IResult.isLoading)

            val success = awaitItem()
            assert(success is IResult.isSuccess)
            assert((success as IResult.isSuccess).response == fakeEvent)

            awaitComplete()
        }

        toolboxConMock.close()
        toolboxInfMock.close()
    }

    @Test
    fun `should emit failed when dao returns error`() = runBlocking {
        val toolboxConMock = mockStatic(ToolBox_Con::class.java)
        val toolboxInfMock = mockStatic(ToolBox_Inf::class.java)

        // Força modo offline
        toolboxConMock.`when`<Boolean> { isOnline(context) }.thenReturn(false)

        // Mocka retorno DAO com erro
        val daoError = DaoObjReturn().apply {
            setError(true)
            description = "Falha ao salvar evento manual"
        }

        coEvery { eventDao.saveEvent(any()) } returns daoError

        val flow = repository.saveEvent(fakeEvent)

        flow.test {
            val loading1 = awaitItem()
            assert(loading1 is IResult.isLoading)

            val loading2 = awaitItem()
            assert(loading2 is IResult.isLoading)

            val failed = awaitItem()
            assert(failed is IResult.isFailed)
            assert((failed as IResult.isFailed).message?.contains("Falha") == true)

            awaitComplete()
        }

        toolboxConMock.close()
        toolboxInfMock.close()
    }

}
