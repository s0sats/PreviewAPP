package com.namoadigital.prj001.model.VH_models

import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.namoadigital.prj001.databinding.Act086PhotoItemBinding
import com.namoadigital.prj001.util.ConstantBaseApp

class Act086PhotoVH(
    private val binding: Act086PhotoItemBinding,
    private val onPhotoClick: (photoName: String, position: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(photoName: String){
        val bitmap = BitmapFactory.decodeFile("${ConstantBaseApp.CACHE_PATH_PHOTO}/$photoName")
        binding.act086PhotoItemIvPhoto.apply {
            setImageBitmap(bitmap)
        }
        //
        binding.root.setOnClickListener {
            onPhotoClick(photoName,adapterPosition)
        }
    }
}