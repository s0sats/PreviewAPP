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

abstract class BaseTripDialog<BINDING : ViewBinding>(
    private val trip: FSTrip,
    protected val getDestinationThresholds: GetDestinationThresholds? = null,
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

    abstract fun show()
    abstract fun dismiss()
    abstract fun errorSendData()

    fun getOdometerPhotoLabel(isRequiredFleetData:Boolean, label:String): String {
        return if(isRequiredFleetData){
            """$label *"""
        }else{
            label
        }
    }

    fun updatePhoto(): Bitmap? {
        if (lastDateOriginFile != camtest.lastModified()) {
            lastDateOriginFile = camtest.lastModified()
            isNewPhoto = true
        }
        return getBitmapImage()
    }

    fun getPhoto(): Bitmap? {
        return getBitmapImage()
    }

    fun isNewPhotoInt() = if (isNewPhoto) 1 else 0

    private fun getBitmapImage(): Bitmap? {
        File(tempPath.path).let { file ->
            if (!file.exists()) return null

            return BitmapFactory.decodeFile(tempPath.path)
        }
    }


    fun saveImage() {
        if(!this::tempPath.isInitialized) return
        if (tempPath.exists()) {
            ToolBox_Inf.copyFiles(tempPath.path, originPath.path)
            tempPath.delete()
        }
    }


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
            it.isFile && it.name.startsWith(TripViewModel.TEMP_PREFIX + TripViewModel.FS_PREFIX)
        } == true
    }

    private fun File.deleteIfStartsWith(prefix: String): Boolean {
        return exists() && isFile && name.startsWith(prefix) && delete()
    }


    private fun saveImageFromDownload(image: Bitmap, imageFile: File) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val downloadImage = CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    imageFile.outputStream().use {
                        image.compress(Bitmap.CompressFormat.JPEG, 100, it)
                        it.close()
                    }
                }.onFailure {
                    _statePhotoPath.value = IResult.failed(it)
                    ToolBox_Inf.registerException(this::class.java.name, it as Exception)
                }
            }

            downloadImage.invokeOnCompletion {
                createTempPath(imageFile.name)
                _statePhotoPath.value = IResult.success(imageFile.name)
            }

            downloadImage.join()
        }
    }

    fun handlePhoto(context: Context, fleetPhoto: FSTripPhoto, newPathFile: String) {
        val (pathLocal, pathRemote, photoUrl) = fleetPhoto

        when {
            //Exibir
            photoUrl.isEmpty() && !pathLocal.isNullOrEmpty() -> {
                createTempPath(pathLocal)
                _statePhotoPath.value = IResult.success(pathLocal)
            }

            //Download
            photoUrl.isNotEmpty() && pathLocal.isNullOrEmpty() -> {
                File("${ConstantBase.CACHE_PATH_PHOTO}/$pathRemote").let { file ->
                    if (file.exists()) {
                        createTempPath(file.name)
                        _statePhotoPath.value = IResult.success(pathRemote)
                        return
                    }

                    downloadAndSaveImage(context, photoUrl, pathRemote)
                }
            }

            //Comparar
            photoUrl.isNotEmpty() && !pathLocal.isNullOrEmpty() -> {
                File("${ConstantBase.CACHE_PATH_PHOTO}/$pathRemote").let { file ->
                    if (pathLocal != pathRemote) {
                        downloadAndSaveImage(context, photoUrl, pathRemote)
                    } else {
                        if (!file.exists()) {
                            downloadAndSaveImage(context, photoUrl, pathRemote)
                            return
                        }
                        createTempPath(pathLocal)
                        _statePhotoPath.value = IResult.success(pathLocal)
                    }
                }

            }

            //Foto nova
            else -> {
                createTempPath(newPathFile)
                _statePhotoPath.value = IResult.success(newPathFile)
            }
        }

    }

    private fun createTempPath(path: String) {
        File("${ConstantBase.CACHE_PATH_PHOTO}/$path").let { file ->
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
    }

    private fun downloadAndSaveImage(context: Context, url: String?, fileName: String?) {
        Glide.with(context)
            .load(url)
            .listener(@SuppressLint("CheckResult")
            object : RequestListener<Drawable> {
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
                    val path = File(ConstantBase.CACHE_PATH_PHOTO + "/" + fileName + JPG_EXTENSION)
                    saveImageFromDownload(image, path)
                    return true
                }

            }).submit()
    }


    fun generateNewFilePath(context: Context, prefix: Int, code: Int): String {
        val format = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())
        return "${TripViewModel.FS_PREFIX}${context.getCustomerCode()}_${prefix}_${code}_${(0..999).random()}_$format${TripViewModel.JPG_EXTENSION}"
    }

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

            return dateStart?.let { start ->
                dateEnd?.let { end ->
                    comparator(start, end)
                }
            } ?: false
        } catch (e: Exception) {
            ToolBox_Inf.registerException(this::class.java.name, e)
            false
        }
    }

    fun dateIsFuture(userSelectDate: String): Boolean {
        return compareDateToCurrent(userSelectDate) { userDate, dialogDate ->
            userDate.after(
                dialogDate
            )
        }
    }

    fun dateBeforeTrip(userSelectDate: String): Boolean {
        return trip.originDate?.let { date ->
            compareDates(
                userSelectDate,
                date,
                ConstantBaseApp.FULL_TIMESTAMP_TZ_FORMAT_GMT
            ) { userDate, tripDate -> userDate.before(tripDate) }
        } ?: false
    }

    fun isDateBefore(userSelectDate: String, thresholdDate: String?): Boolean {

        return thresholdDate?.let { date ->
            compareDates(
                userSelectDate,
                date
            ) { userDate, tripDate -> userDate.before(tripDate) || userDate == tripDate }
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
                dialogDate?.let { dialog ->
                    comparator(user, dialog)
                }
            } ?: false
        } catch (e: Exception) {
            ToolBox_Inf.registerException(this::class.java.name, e)
            false
        }
    }

    fun photoPathLoading() {
        _statePhotoPath.value = IResult.loading(true)
    }

     fun isInputOdometerValid(
        destination: FsTripDestination?,
        value: Long,
        hmAuxTranslate: HMAux,
        type: GetDestinationForThresholdValidationUseCase.TripDestinationValidationType = GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.BOTH
    ): String {
        var result = ""
        getDestinationThresholds?.let { invoke ->
            val (previousDestination, nextDestination) = invoke(
                trip.customerCode,
                trip.tripPrefix,
                trip.tripCode,
                destination?.destinationSeq ?: -1,
                type
            )
            //
            val minimumValue =
                previousDestination?.arrivedFleetOdometer ?: kotlin.run {
                    if(GetDestinationForThresholdValidationUseCase.TripDestinationValidationType.ODOMETER_NEXT == type ){
                        0L
                    }else{
                        trip.fleetStartOdometer ?: 0L
                    }
                }
            //
            val maxValue = nextDestination?.arrivedFleetOdometer ?: Long.MAX_VALUE
            //
            if (value <= minimumValue) {
                result =
                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_HIGHER_THAN_LBL] + ": $minimumValue"
            }
            if (value >= maxValue) {
                result =
                    hmAuxTranslate[TranslateInfoDialogs.DIALOG_VALUE_SHOULD_BE_LOWER_THAN_LBL] + ": $maxValue"
            }
            //
        }
        return result
    }

    fun setOdometerErrorLayout(
        context: Context,
        layout: TextInputLayout,
        layoutOdometerInvalid: LinearLayout,
        tvOdometerInvalid: TextView,
        inputOdometerValidErrorMsg: String,
        visible: Int
    ) {
        layoutOdometerInvalid.visibility = visible
        if(visible == View.VISIBLE) {
            layout.setBoxStrokeColorState(context, R.drawable.edittext_error)
            layout.setHintTextColor(context, R.drawable.edittext_error)
            tvOdometerInvalid.text = "$inputOdometerValidErrorMsg"
        }
    }

    companion object {
        private const val TEMP_PREFIX = "temp_"
        private const val JPG_EXTENSION = ".jpg"
    }
}