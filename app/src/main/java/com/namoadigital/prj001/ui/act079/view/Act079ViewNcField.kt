package com.namoadigital.prj001.ui.act079.view

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.namoa_digital.namoa_library.ctls.ImageViewCR
import com.namoa_digital.namoa_library.ctls.PictureFF
import com.namoa_digital.namoa_library.util.ToolBox
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act079ViewNcFieldBinding
import com.namoadigital.prj001.model.Act079PictureOptionJson
import com.namoadigital.prj001.model.TkTicketOriginNc
import com.namoadigital.prj001.util.ToolBox_Inf

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

    init {
        initialize()
    }

    private fun initialize() {
        bindData()
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
                nc.getDataValueTxt().toString()
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
                text = "Vazio"
                setTextColor(ContextCompat.getColor(context, R.color.namoa_status_not_executed))
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
        if(!nc.getDataValue().isNullOrEmpty()){
            val mValeu = convertPictureDataValueToJson()
            val mOption: String = getmOptionJson()
            val pictureFF = PictureFF(context).apply {
                id = View.generateViewId()
                setmLabel(null)
                setmOrder(nc.getCustomFormOrder())
                setmSequence(mSequence)
                setmType(nc.getCustomFormDataType())
                setmOption(mOption)
                setmRequired(false)
                setmValue(mValeu)
                setmEnabled(false)
//                setmFName(nc.getPictureUrlLocal())
                setmFName("picture_1_8_1_4_13.jpg")
                setmIv_Dots(0)
                setmV_Line(0)
            }
            pictureFF.findViewById<ImageViewCR>(R.id.pictureff_icr_value)?.isClickable = false
            //
            binding.act079ViewNcFieldFrame.apply {
                setOnClickListener {
                    binding.root.background = ContextCompat.getDrawable(context, R.drawable.namoa_cell_default_blue_states)
                    pictureFF.findViewById<ImageViewCR>(R.id.pictureff_icr_value)?.performClick()
                }
                addView(pictureFF)
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
        val imageView = ImageView(context)
        imageView.layoutParams = layoutParams

        if(!nc.getDataValue().isNullOrEmpty()){
            if(nc.getDataValueLocal().isNullOrEmpty()){
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.sand_watch_transp))
            }else{
                imageView.setImageBitmap(BitmapFactory.decodeFile(nc.getDataValueLocal()))
            }
        }else{
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_delete_empty))
        }
        binding.act079ViewNcFieldFrame.addView(imageView)
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
            binding.act079ViewNcFieldIvPicDots.visibility = View.VISIBLE
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
}