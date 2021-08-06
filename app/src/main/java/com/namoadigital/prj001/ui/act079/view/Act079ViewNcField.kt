package com.namoadigital.prj001.ui.act079.view

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.namoa_digital.namoa_library.ctls.ImageViewCR
import com.namoa_digital.namoa_library.ctls.PictureFF
import com.namoa_digital.namoa_library.util.ConstantBase
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoa_digital.namoa_library.view.Camera_Activity
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act079ViewNcFieldBinding
import com.namoadigital.prj001.model.Act079PictureOptionJson
import com.namoadigital.prj001.model.TkTicketOriginNc
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class Act079ViewNcField(
    context: Context,
    nc: TkTicketOriginNc
) : Act079ViewNcBase(
    context,
    nc
){
    private val binding: Act079ViewNcFieldBinding by lazy {
        Act079ViewNcFieldBinding.inflate(LayoutInflater.from(context),this,true)
    }
    var onFieldClick: onFieldClickListener? = null
    var emptyAnswerLabel: String? = null
    var isHighlighted: Boolean = false

    private fun initialize() {
        bindData()
        applyHighlight()
    }

    private fun bindData() {
        with(binding){
            act079ViewNcFieldTvDesc.text = getFieldDesc()
            configFrameViewComponent()
            configComment()
            configDotsPicIcon()
        }
    }

    /**
     * Fun que define o conteudo do framelayout baseado nodata type do campo
     */
    private fun configFrameViewComponent() {
        when(nc.getCustomFormDataType()){
            TkTicketOriginNc.PHOTO -> configDataTypePhoto()
            TkTicketOriginNc.PICTURE -> configDataTypePicture()
            TkTicketOriginNc.RATINGIMAGE -> configDataTypeRatingImage()
            else -> configDataTypeOthers()

        }
    }

    /**
     * Fun datatype de texto
     */
    private fun configDataTypeOthers() {
        binding.act079ViewNcFieldFrame.addView(
            getAnswerTextView(
                nc.getDataValueTxt()?:""
            )
        )
    }

    /**
     * Fun gera o campo text com o valor da resposta ou label traduzivel rosa e em italico para sem resposta
     */
    private fun getAnswerTextView(answer: String): TextView {
        val lParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return TextView(context).apply {
            layoutParams = lParams
            if (answer.isNullOrEmpty()) {
                text = emptyAnswerLabel
                setTextColor(ContextCompat.getColor(context, R.color.namoa_status_not_executed))
                setTypeface(null,Typeface.ITALIC)
            } else {
                text = answer
                setTextColor(ContextCompat.getColor(context, R.color.namoa_status_pending))
            }
            setPadding(18,0,0,0)
        }
    }

    /**
     * Fun datatype ratingImage
     * Add imageView no frame layout
     */
    private fun configDataTypeRatingImage() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val imageView = ImageView(context).apply {
            this.layoutParams = layoutParams
            //
            when(nc.getDataValue()){
                TkTicketOriginNc.RATINGIMAGE_GREEN -> {setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_happy))}
                TkTicketOriginNc.RATINGIMAGE_YELLOW -> {setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_neutro))}
                TkTicketOriginNc.RATINGIMAGE_RED -> {setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_sad))}
                TkTicketOriginNc.RATINGIMAGE_NA -> {setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_na))}
            }
            setPadding(18,0,0,0)
        }

        binding.act079ViewNcFieldFrame.addView(imageView)
    }

    /**
     * Fun datatype Picture
     * Seta PictureFF se foto baixada
     * Seta ImageView com ampulheta se foto ainda não baixada
     * Seta textView com label sem resposta caso não tenha imagem
     */
    private fun configDataTypePicture() {
        if(!nc.getPictureUrl().isNullOrEmpty()){
            if(!nc.getPictureUrlLocal().isNullOrEmpty()) {
                //Transform o valor de data_value no json de mValue
                val mValue = convertPictureDataValueToJson()
                //Construi o json usado pelo mOption baseado nas propriedades de line, column, e color
                val mOption: String = getmOptionJson()
                val pictureFF = PictureFF(context).apply {
                    id = View.generateViewId()
                    setmLabel(null)
                    setmOrder(nc.getCustomFormOrder())
                    setmType(nc.getCustomFormDataType())
                    setmOption(mOption)
                    setmRequired(false)
                    setmValue(mValue)
                    setmEnabled(false)
                    setmFName(nc.getPictureUrlLocal())
                    //remove icone do dots
                    setmIv_Dots(0)
                    //remove linha abaixo.
                    setmV_Line(0)
                }
                //
                pictureFF.findViewById<ImageViewCR>(R.id.pictureff_icr_value)?.isClickable = false
                pictureFF.findViewById<TextView>(R.id.customff_tv_label)?.visibility = if(!pictureFF.getmLabel().isNullOrEmpty()) VISIBLE else GONE
                //
                binding.act079ViewNcFieldFrame.apply {
                    setOnClickListener {
                        reportPositionAndHighlightItem()
                        pictureFF.findViewById<ImageViewCR>(R.id.pictureff_icr_value)
                            ?.performClick()
                    }
                    addView(pictureFF)
                }
            }else{
                binding.act079ViewNcFieldFrame.addView(
                    getWaitingImgDownloadIv(getLayoutParamWithPercentWindowMetric(0.8,0.3))
                )
            }
        }else{
            configDataTypeOthers()
        }
    }

    /**
     * Fun que gera json usado no mOption do pictureFF
     */
    private fun getmOptionJson(): String {
        val json = Act079PictureOptionJson().getJson(
            nc.getPictureLines().toString(),
            nc.getPictureColumns().toString(),
            nc.getPictureColor() ?: "",
            nc.getPictureUrlLocal() ?: ""
        )
        //
        return json
    }

    /**
     * Fun que transforma resposta do PictureFF em mValue
     */
    private fun convertPictureDataValueToJson() = ToolBox.converterToJson(nc.getDataValue()?.replace("|", "#")?:"")

    /**
     * Fun datatype Photo
     * Seta foto se ja baixada
     * Seta amplheta se foto nao baixada
     * Seta texto de sem resposta, se não houver foto
     */
    private fun configDataTypePhoto() {
        val layoutParams = getLayoutParamWithPercentWindowMetric(0.8,0.3)
        val imageView = getWaitingImgDownloadIv(layoutParams)
        if(!nc.getDataValue().isNullOrEmpty()){
            if(!nc.getDataValueLocal().isNullOrEmpty()){
                val photoFullPath = getPhotoFullPath(nc.getDataValueLocal() ?: "")
                imageView.apply{
                    setImageBitmap(BitmapFactory.decodeFile(photoFullPath))
                    setOnClickListener {
                        reportPositionAndHighlightItem()
                        callCameraAct(photoFullPath)
                    }
                }
            }
            //
            binding.act079ViewNcFieldFrame.addView(imageView)
        }else{
            configDataTypeOthers()
        }
    }

    /**
     * Fun que aciona interface quando o field é clicado
     */
    private fun reportPositionAndHighlightItem() {
        onFieldClick?.onFieldClick(mSequence)
    }

    /**
     * Fun que aplica ou remove o fundo colorido
     */
    private fun applyHighlight() {
        if(isHighlighted) {
            binding.act079ViewNcFieldClMain.background = ContextCompat.getDrawable(
                context,
                R.drawable.namoa_cell_default_blue_states
            )
        }else{
            binding.act079ViewNcFieldClMain.background = null
        }
    }

    /**
     * Fun que retorna o nome completo da photo
     */
    private fun getPhotoFullPath(photoLocal: String) = "${ConstantBaseApp.CACHE_PATH_PHOTO}/$photoLocal"

    /**
     * Fun que gera imageView com ampulheta
     */
    private fun getWaitingImgDownloadIv(params: ViewGroup.LayoutParams): ImageView {
        return ImageView(context).apply {
            layoutParams = params
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sand_watch_transp))
        }
    }

    /**
     * Fun que retorna layoutParam com W e H baseado no tamanho da tela e porcentagem
     */
    private fun getLayoutParamWithPercentWindowMetric(wPercent: Double, hPercent: Double): ViewGroup.LayoutParams {
        val screenMetrics = ToolBox_Inf.getPercentageWidthAndHeight(context, wPercent, hPercent)
        val layoutParams = ViewGroup.LayoutParams(screenMetrics[0], screenMetrics[1])
        return layoutParams
    }

    /**
     * Fun que trata o icone de foto dos dots e gera nome das foto para galeria
     */
    private fun configDotsPicIcon() {
        if( nc.getDataPhoto1Url().isNullOrEmpty()
            && nc.getDataPhoto2Url().isNullOrEmpty()
            && nc.getDataPhoto3Url().isNullOrEmpty()
            && nc.getDataPhoto4Url().isNullOrEmpty()
        ){
            binding.act079ViewNcFieldIvPicDots.visibility = View.GONE
        }else{
            binding.act079ViewNcFieldIvPicDots.apply {
                visibility = View.VISIBLE
                var images = ""

                images = if(nc.getDataPhoto1UrlLocal() != null){
                    getPhotosName(images, nc.getDataPhoto1UrlLocal()!!)
                }else{
                    if(nc.getDataPhoto1Url() != null) {
                        getPhotosName(images, getGalleryPhotoName("1"))
                    }else{
                        images
                    }
                }

                images = if(nc.getDataPhoto2UrlLocal() != null){
                    getPhotosName(images, nc.getDataPhoto2UrlLocal()!!)
                }else{
                    if(nc.getDataPhoto2Url() != null) {
                        getPhotosName(images, getGalleryPhotoName("2"))
                    }else{
                        images
                    }
                }

                images = if(nc.getDataPhoto3UrlLocal() != null){
                    getPhotosName(images, nc.getDataPhoto3UrlLocal()!!)
                }else{
                    if(nc.getDataPhoto3Url() != null) {
                        getPhotosName(images, getGalleryPhotoName("3"))
                    }else{
                        images
                    }
                }

                images = if(nc.getDataPhoto4UrlLocal() != null){
                    getPhotosName(images, nc.getDataPhoto4UrlLocal()!!)
                }else{
                    if(nc.getDataPhoto4Url() != null) {
                        getPhotosName(images, getGalleryPhotoName("4"))
                    }else{
                        images
                    }
                }

                setOnClickListener {
                    onFieldClick?.onGalleryClick(mSequence,images)
                }
            }
        }
    }

    /**
     * Fun que gera nome das fotos para galeria
     */
    private fun getGalleryPhotoName(id: String): String {
        return "${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_${nc.getCustomerCode()}_${nc.getTicketPrefix()}_${nc.getTicketCode()}_${nc.getPage()}_${nc.getCustomFormOrder()}_${id}"
    }

    /**
     * Fun que concatena foto a lista de fotos
     */
    private fun getPhotosName(images: String, photo :String): String {
        if (images.isNullOrEmpty()) {
            return photo
        } else {
            return "$images#$photo"
        }
    }

    /**
     * Fun que configura o comentario
     */
    private fun configComment() {
        if(nc.getDataComment().isNullOrEmpty()){
            binding.act079ViewNcFieldTvComment.apply{
                visibility = GONE
            }
        }else{
            binding.act079ViewNcFieldTvComment.apply{
                text = nc.getDataComment()
                visibility = VISIBLE
            }
        }
    }

    /**
     * Fun que configura a descricao do Field
     */
    private fun getFieldDesc(): String {
        return "${nc.getPage()}.${nc.getCustomFormOrder()} - ${nc.getDescription()}"
    }


    /**
     * Fun que chama cameraACt passando a foto como param
     */
    private fun callCameraAct(photoFullPath: String) {
        val sFile =  File(photoFullPath)
        if (!sFile.exists()) {
            return
        }
        //
        val bundle = Bundle()
        bundle.putInt(ConstantBase.PID, id)
        bundle.putInt(ConstantBase.PTYPE, 1)
        bundle.putString(ConstantBase.PPATH, sFile.name)
        //
        bundle.putBoolean(ConstantBase.PEDIT, false)
        bundle.putBoolean(ConstantBase.PENABLED, false)
        bundle.putBoolean(ConstantBase.P_ALLOW_GALLERY, false)
        bundle.putBoolean(ConstantBase.P_ALLOW_HIGH_RESOLUTION, false)
        bundle.putString(ConstantBase.FILE_AUTHORITIES, ConstantBase.AUTHORITIES_FOR_PROVIDER)
        //
        val mIntent = Intent(context, Camera_Activity::class.java)
        mIntent.putExtras(bundle)
        //
        context.startActivity(mIntent)
    }

    /**
     * Fun usada pela act para refreshar o layout do highlight
     */
    fun forceHighlight(){
        applyHighlight()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initialize()
    }

//
//    override fun onSaveInstanceState(): Parcelable? {
//        val parcelable = super.onSaveInstanceState()
//        parcelable?.let { it ->
//            val ss = SavedState(it)
//            ss._isHighlighted = isHighlighted
//            return ss
//        }
//        //
//        return parcelable
//    }
//
//    override fun onRestoreInstanceState(state: Parcelable?) {
//        super.onRestoreInstanceState(state)
//        state?.let {
//            if(state is SavedState){
//                isHighlighted = state._isHighlighted
//            }
//        }
//    }
//
//    internal class SavedState : View.BaseSavedState {
//        var _isHighlighted = false
//
//        constructor(superState: Parcelable) : super(superState)
//
//        constructor(source: Parcel) : super(source) {
//            _isHighlighted = source.readInt() != 0
//        }
//
//        override fun writeToParcel(out: Parcel?, flags: Int) {
//            super.writeToParcel(out, flags)
//            out?.writeInt(if(_isHighlighted) 1 else 0)
//        }
//
//        companion object CREATOR : Parcelable.Creator<SavedState> {
//            override fun createFromParcel(parcel: Parcel): SavedState {
//                return SavedState(parcel)
//            }
//
//            override fun newArray(size: Int): Array<SavedState?> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
}