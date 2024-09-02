package com.namoadigital.prj001.ui.act005.trip.fragment.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.textfield.TextInputLayout
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.core.IResult
import com.namoadigital.prj001.core.trip.domain.usecase.destination.GetDestinationForThresholdValidationUseCase
import com.namoadigital.prj001.extensions.date.getCurrentDateApi
import com.namoadigital.prj001.extensions.getCustomerCode
import com.namoadigital.prj001.extensions.setBoxStrokeColorState
import com.namoadigital.prj001.extensions.setHintTextColor
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.model.trip.FSTripPhoto
import com.namoadigital.prj001.model.trip.FsTripDestination
import com.namoadigital.prj001.ui.act005.trip.TripViewModel
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.info.util.TranslateInfoDialogs
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.NetworkConnectionException
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

typealias GetDestinationThresholds = (Long, Int, Int, Int, GetDestinationForThresholdValidationUseCase.TripDestinationValidationType) -> Pair<FsTripDestination?, FsTripDestination?>

/**
 * Classe base para diálogos relacionados a viagens.
 *
 * @param BINDING Tipo de ViewBinding utilizado para a interface do diálogo.
 * @param trip Objeto de viagem associado ao diálogo.
 * @param getDestinationThresholds Função opcional para obter os limites de destino.
 */
abstract class BaseTripDialog<BINDING : ViewBinding>(
    private val trip: FSTrip,
    protected val getDestinationThresholds: GetDestinationThresholds? = null
) {
    lateinit var binding: BINDING
    lateinit var dialog: BaseDialog<BINDING>
    lateinit var originPath: File
    private var job: Job? = null
    private lateinit var tempPath: File
    private lateinit var camtest: File
    lateinit var tempPathName: String
    private var lastDateOriginFile: Long? = null
    private var fileImageExist: Boolean = false
    var isNewPhoto = false
    private val _statePhotoPath = MutableStateFlow<IResult<String>?>(null)
    val statePhotoPath = _statePhotoPath.asStateFlow()

    /**
     * Exibe o diálogo.
     */
    abstract fun show()

    /**
     * Fecha o diálogo.
     */
    abstract fun dismiss()

    /**
     * Trata o erro ao enviar dados.
     */
    abstract fun errorSendData()

    /**
     * Retorna o rótulo para a foto do odômetro, incluindo um asterisco se os dados da frota forem obrigatórios.
     *
     * @param isRequiredFleetData Indica se os dados da frota são obrigatórios.
     * @param label O rótulo do odômetro.
     * @return O rótulo com ou sem asterisco.
     */
    fun getOdometerPhotoLabel(isRequiredFleetData: Boolean, label: String): String {
        return if (isRequiredFleetData) "$label *" else label
    }

    /**
     * Atualiza a foto, se houver uma foto nova.
     *
     * @return A imagem em bitmap ou nula se a foto não foi atualizada.
     */
    fun updatePhoto(): Bitmap? {
        if (lastDateOriginFile != camtest.lastModified()) {
            lastDateOriginFile = camtest.lastModified()
            isNewPhoto = true
        }
        return getBitmapImage(true)
    }

    /**
     * Obtém a foto atual.
     *
     * @return A imagem em bitmap ou nula se a foto não existir.
     */
    fun getPhoto(): Bitmap? {
        return getBitmapImage()
    }

    /**
     * Retorna 1 se a foto for nova, 0 caso contrário.
     *
     * @return 1 ou 0 baseado no estado da foto.
     */
    fun isNewPhotoInt() = if (isNewPhoto) 1 else 0

    private fun getBitmapImage(isUpdatePhoto: Boolean = false): Bitmap? {
        val file = File(tempPath.path)
        if (!file.exists()) {
            if (isUpdatePhoto) isNewPhoto = true
            return null
        }
        return BitmapFactory.decodeFile(tempPath.path)
    }

    /**
     * Salva a imagem temporária no caminho de origem e remove a imagem temporária.
     */
    fun saveImage() {
        if (!this::tempPath.isInitialized) return
        if (tempPath.exists()) {
            ToolBox_Inf.copyFiles(tempPath.path, originPath.path)
            tempPath.delete()
        }
    }

    /**
     * Remove arquivos temporários no diretório de cache.
     */
    fun removeTempPath() {
        if (containsTempFilesInDirectory()) {
            File(ConstantBase.CACHE_PATH_PHOTO).listFiles()?.forEach {
                it.deleteIfStartsWith("${TripViewModel.TEMP_PREFIX}${TripViewModel.FS_PREFIX}")
            }
        }
    }

    private fun containsTempFilesInDirectory(): Boolean {
        val directory = File(ConstantBase.CACHE_PATH_PHOTO)
        return directory.exists() && directory.isDirectory && directory.listFiles()?.any {
            it.isFile && it.name.startsWith("${TripViewModel.TEMP_PREFIX}${TripViewModel.FS_PREFIX}")
        } == true
    }

    private fun File.deleteIfStartsWith(prefix: String): Boolean {
        return exists() && isFile && name.startsWith(prefix) && delete()
    }

    /**
     * Salva a imagem após o download e atualiza o caminho temporário.
     *
     * @param image A imagem a ser salva.
     * @param imageFile O arquivo onde a imagem será salva.
     */
    private fun saveImageFromDownload(image: Bitmap, imageFile: File) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                imageFile.outputStream().use {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }.onFailure {
                _statePhotoPath.value = IResult.failed(it)
                ToolBox_Inf.registerException(this::class.java.name, it as Exception)
            }.onSuccess {
                createTempPath(imageFile.name)
                _statePhotoPath.value = IResult.success(imageFile.name)
            }
        }
    }

    /**
     * Verifica se a foto já existe no dispositivo para exibir, caso contrário, baixa e salva a imagem.
     * Ou compara para saber se precisa só exibir ou baixar-la
     *
     * @param context O contexto da aplicação.
     * @param fleetPhoto Detalhes da foto do veículo.
     * @param newPathFile Caminho para o novo arquivo de foto.
     */
    fun handlePhoto(context: Context, fleetPhoto: FSTripPhoto, newPathFile: String) {
        val (pathLocal, pathRemote, photoUrl) = fleetPhoto

        when {
            photoUrl.isEmpty() && !pathLocal.isNullOrEmpty() -> {
                createTempPath(pathLocal)
                _statePhotoPath.value = IResult.success(pathLocal)
            }
            photoUrl.isNotEmpty() && pathLocal.isNullOrEmpty() -> {
                val file = File("${ConstantBase.CACHE_PATH_PHOTO}/$pathRemote")
                if (file.exists()) {
                    createTempPath(file.name)
                    _statePhotoPath.value = IResult.success(pathRemote)
                } else {
                    downloadAndSaveImage(context, photoUrl, pathRemote)
                }
            }
            photoUrl.isNotEmpty() && !pathLocal.isNullOrEmpty() -> {
                val file = File("${ConstantBase.CACHE_PATH_PHOTO}/$pathRemote")
                if (pathLocal != pathRemote || !file.exists()) {
                    downloadAndSaveImage(context, photoUrl, pathRemote)
                } else {
                    createTempPath(pathLocal)
                    _statePhotoPath.value = IResult.success(pathLocal)
                }
            }
            else -> {
                createTempPath(newPathFile)
                _statePhotoPath.value = IResult.error(newPathFile)
            }
        }
    }

    private fun createTempPath(path: String) {
        val file = File("${ConstantBase.CACHE_PATH_PHOTO}/$path")
        originPath = file
        tempPath = File("${ConstantBase.CACHE_PATH_PHOTO}/$TEMP_PREFIX${originPath.name}")
        camtest = File("${ConstantBaseApp.CAM_TEST_PATH}/img$JPG_EXTENSION")
        tempPathName = tempPath.name
        if (file.exists()) {
            ToolBox_Inf.copyFiles(file.path, tempPath.path)
            ToolBox_Inf.copyFiles(file.path, camtest.path)
            lastDateOriginFile = camtest.lastModified()
            isNewPhoto = lastDateOriginFile != camtest.lastModified()
            fileImageExist = true
        } else {
            fileImageExist = false
        }
    }

    private fun downloadAndSaveImage(context: Context, url: String?, fileName: String?) {
        if (ToolBox_Con.isOnline(context)) {
            Glide.with(context)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        ToolBox_Inf.registerException(this::class.java.name, e)
                        _statePhotoPath.value = IResult.failed(e?.cause!!)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        val image = (resource as BitmapDrawable).bitmap
                        val path = File("${ConstantBase.CACHE_PATH_PHOTO}/$fileName$JPG_EXTENSION")
                        saveImageFromDownload(image, path)
                        return true
                    }
                }).submit()
        } else {
            // Para não quebrar o código, define um caminho padrão e um erro de conexão.
            originPath = File("${ConstantBase.CACHE_PATH_PHOTO}/$fileName$JPG_EXTENSION")
            _statePhotoPath.value = IResult.failed(NetworkConnectionException("No connection with ethernet"))
        }
    }

    /**
     * Gera um novo caminho de arquivo com base no contexto e parâmetros fornecidos.
     *
     * @param context O contexto da aplicação.
     * @param prefix Prefixo para o caminho do arquivo.
     * @param code Código associado ao arquivo.
     * @return O novo caminho de arquivo gerado.
     */
    fun generateNewFilePath(context: Context, prefix: Int, code: Int): String {
        val format = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        return "${TripViewModel.FS_PREFIX}${context.getCustomerCode()}_${prefix}_${code}_${(0..999).random()}_$format${TripViewModel.JPG_EXTENSION}"
    }

    /**
     * Compara duas datas usando um comparador fornecido.
     *
     * @param startDate A data de início.
     * @param endDate A data de término.
     * @param dateFormat O formato das datas.
     * @param comparator Função de comparação entre as datas.
     * @return Verdadeiro se as datas atenderem ao critério do comparador.
     */
    fun compareDates(
        startDate: String,
        endDate: String,
        dateFormat: String = "dd/MM/yy HH:mm",
        comparator: (Date, Date) -> Boolean
    ): Boolean {
        val startDateFormat = SimpleDateFormat("dd/MM/yy HH:mm")
        val endDateFormat = SimpleDateFormat(dateFormat)
        return try {
            if (startDate.trim().isEmpty() || endDate.trim().isEmpty()) return false
            val dateStart = startDateFormat.parse(startDate)
            val dateEnd = endDateFormat.parse(endDate)
            dateStart?.let { start ->
                dateEnd?.let { end ->
                    comparator(start, end)
                }
            } ?: false
        } catch (e: Exception) {
            ToolBox_Inf.registerException(this::class.java.name, e)
            false
        }
    }

    /**
     * Verifica se a data selecionada pelo usuário está no futuro em relação à data atual.
     *
     * @param userSelectDate Data selecionada pelo usuário.
     * @return Verdadeiro se a data estiver no futuro.
     */
    fun dateIsFuture(userSelectDate: String): Boolean {
        return compareDateToCurrent(userSelectDate) { userDate, currentDate ->
            userDate.after(currentDate)
        }
    }

    /**
     * Verifica se a data selecionada pelo usuário é anterior à data da viagem.
     *
     * @param userSelectDate Data selecionada pelo usuário.
     * @return Verdadeiro se a data for anterior à data da viagem.
     */
    fun dateBeforeTrip(userSelectDate: String): Boolean {
        return trip.originDate?.let { date ->
            compareDates(
                userSelectDate,
                date,
                ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
            ) { userDate, tripDate -> userDate.before(tripDate) }
        } ?: false
    }

    /**
     * Verifica se a data selecionada pelo usuário é anterior ou igual à data limite.
     *
     * @param userSelectDate Data selecionada pelo usuário.
     * @param thresholdDate Data limite para comparação.
     * @return Verdadeiro se a data selecionada for anterior ou igual à data limite.
     */
    fun isDateBefore(userSelectDate: String, thresholdDate: String?): Boolean {
        return thresholdDate?.let { date ->
            compareDates(
                userSelectDate,
                date
            ) { userDate, threshold -> userDate.before(threshold) || userDate == threshold }
        } ?: false
    }

    private fun compareDateToCurrent(
        userSelectDate: String,
        comparator: (Date, Date) -> Boolean
    ): Boolean {
        val simpleDate = SimpleDateFormat("dd/MM/yy HH:mm")
        val currentSimpleDate = SimpleDateFormat(ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT)
        return try {
            if (userSelectDate.trim().isEmpty()) return false
            val userDate = simpleDate.parse(userSelectDate)
            val dialogDate = currentSimpleDate.parse(getCurrentDateApi())
            userDate?.let { user ->
                dialogDate?.let { current ->
                    comparator(user, current)
                }
            } ?: false
        } catch (e: Exception) {
            ToolBox_Inf.registerException(this::class.java.name, e)
            false
        }
    }

    /**
     * Define o estado de carregamento do caminho da foto.
     */
    fun photoPathLoading() {
        _statePhotoPath.value = IResult.loading(true)
    }

    /**
     * Valida o odômetro de entrada com base nos limites de destino.
     *
     * @param destination Destino atual da viagem.
     * @param value Valor do odômetro a ser validado.
     * @param hmAuxTranslate Mapa de traduções para mensagens de erro.
     * @param type Tipo de validação de destino.
     * @return Mensagem de erro se o valor não estiver dentro dos limites.
     */
    fun isInputOdometerValid(
        destination: FsTripDestination?,
        value: Long,
        hmAuxTranslate: HMAux,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType = GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.BOTH
    ): String {
        var result = ""
        getDestinationThresholds?.let { thresholds ->
            val (previousDestination, nextDestination) = thresholds(
                trip.customerCode,
                trip.tripPrefix,
                trip.tripCode,
                destination?.destinationSeq ?: -1,
                type
            )

            val minimumValue = previousDestination?.arrivedFleetOdometer ?: run {
                if (type == GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_NEXT) {
                    0L
                } else {
                    trip.fleetStartOdometer ?: 0L
                }
            }

            val maxValue = nextDestination?.arrivedFleetOdometer ?: Long.MAX_VALUE

            if (value <= minimumValue) {
                result = "${hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_LBL]}: $minimumValue"
            }
            if (value >= maxValue) {
                result = "${hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_LOWER_THAN_LBL]}: $maxValue"
            }
        }
        return result
    }

    /**
     * Configura a exibição do layout de erro do odômetro.
     *
     * @param context O contexto da aplicação.
     * @param layout O layout do campo do odômetro.
     * @param layoutOdometerInvalid Layout para mostrar a mensagem de erro do odômetro.
     * @param tvOdometerInvalid TextView para mostrar a mensagem de erro do odômetro.
     * @param inputOdometerValidErrorMsg Mensagem de erro para o odômetro.
     * @param visible Visibilidade do layout de erro.
     */
    fun setOdometerErrorLayout(
        context: Context,
        layout: TextInputLayout,
        layoutOdometerInvalid: LinearLayout,
        tvOdometerInvalid: TextView,
        inputOdometerValidErrorMsg: String,
        visible: Int
    ) {
        layoutOdometerInvalid.visibility = visible
        if (visible == View.VISIBLE) {
            layout.setBoxStrokeColorState(context, R.drawable.edittext_error)
            layout.setHintTextColor(context, R.drawable.edittext_error)
            tvOdometerInvalid.text = inputOdometerValidErrorMsg
        }
    }

    companion object {
        private const val TEMP_PREFIX = "temp_"
        private const val JPG_EXTENSION = ".jpg"
    }
}
