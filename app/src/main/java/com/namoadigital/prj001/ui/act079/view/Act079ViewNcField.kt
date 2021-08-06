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

    private fun configFrameViewComponent() {
        when(nc.getCustomFormDataType()){
            TkTicketOriginNc.PHOTO -> configDataTypePhoto()
            TkTicketOriginNc.PICTURE -> configDataTypePicture()
            TkTicketOriginNc.RATINGIMAGE -> configDataTypeRatingImage()
            else -> configDataTypeOthers()

        }
    }

    private fun configDataTypeOthers() {
        binding.act079ViewNcFieldFrame.addView(
            getAnswerTextView(
                nc.getDataValueTxt()?:""
            )
        )
    }

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

    private fun configDataTypeRatingImage() {
        val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val imageView = ImageView(context)
        imageView.layoutParams = layoutParams
        when(nc.getDataValue()){
            "GREEN" -> {imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_happy))}
            "YELLOW" -> {imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_neutro))}
            "RED" -> {imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_face_sad))}
            "NA" -> {imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_na))}
        }
        binding.act079ViewNcFieldFrame.addView(imageView)
    }

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

    private fun getmOptionJson(): String {
        val json = Act079PictureOptionJson().getJson(
            nc.getPictureLines().toString(),
            nc.getPictureColumns().toString(),
            nc.getPictureColor() ?: "",
            nc.getPictureUrlLocal() ?: ""
        )
        //
        return json;
    }

    private fun convertPictureDataValueToJson() = ToolBox.converterToJson(nc.getDataValue()?.replace("|", "#"))

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

    private fun reportPositionAndHighlightItem() {
        isHighlighted = true
        onFieldClick?.onFieldClick(mSequence)
        applyHighlight()
    }

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

    private fun getPhotoFullPath(photoLocal: String) = "${ConstantBaseApp.CACHE_PATH_PHOTO}/$photoLocal"

    private fun getWaitingImgDownloadIv(params: ViewGroup.LayoutParams): ImageView {
        return ImageView(context).apply {
            layoutParams = params
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sand_watch_transp))
        }
    }

    private fun getLayoutParamWithPercentWindowMetric(wPercent: Double, hPercent: Double): ViewGroup.LayoutParams {
        val screenMetrics = ToolBox_Inf.getPercentageWidthAndHeight(context, wPercent, hPercent)
        val layoutParams = ViewGroup.LayoutParams(screenMetrics[0], screenMetrics[1])
        return layoutParams
    }

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
                    onFieldClick?.onGalleryClick(images)
                }
            }
        }
    }

    private fun getGalleryPhotoName(id: String): String {
        return "${ConstantBaseApp.TK_TICKET_NC_PREX_IMG}_${nc.getCustomerCode()}_${nc.getTicketPrefix()}_${nc.getTicketCode()}_${nc.getPage()}_${nc.getCustomFormOrder()}_${id}"
    }

    private fun getPhotosName(images: String, photo :String): String {
        if (images.isNullOrEmpty()) {
            return photo
        } else {
            return "$images#$photo"
        }
    }

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

    private fun getFieldDesc(): String {
        return "${nc.getPage()}.${nc.getCustomFormOrder()} - ${nc.getDescription()}"
    }


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