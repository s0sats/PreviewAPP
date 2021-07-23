package com.namoadigital.prj001.model.VH_models


import android.graphics.drawable.Drawable
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.Act085UserListFrgCellBinding
import com.namoadigital.prj001.model.TUserWorkgroupObj

class Act085UserVH(
    val binding: Act085UserListFrgCellBinding,
    val onItemClick: (user: TUserWorkgroupObj) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun onBindData(user: TUserWorkgroupObj) {
        user.emailP?.let {
            if (!it.isEmpty()) {
                binding.act085UserListFrgCellTvEmail.text = it
            }
        }
        //
        user.erpCode?.let {
            if (!it.isEmpty()) {
                binding.act085UserListFrgCellTvErp.text = it
            }
        }
        //
        if (!user.userName.isEmpty()) {
            binding.act085UserListFrgCellTvName.text = user.userName
        }
        //
        if (!user.userNick.isEmpty()) {
            binding.act085UserListFrgCellTvNick.text = """(${user.userCode}) ${user.userNick}"""
        }
        //
        user.userImage?.let {
            if (!it.isEmpty()) {
                //
                Glide
                    .with(binding.root.context)
                    .load(it)
                    .placeholder(R.drawable.ic_baseline_account_box_24)
                    .into(binding.act085UserListFrgCellIvPhoto)
            }
        }?: binding.act085UserListFrgCellIvPhoto.apply {
            setImageDrawable(context.resources.getDrawable(R.drawable.ic_baseline_account_box_24, context.theme ))
        }

        binding.root.setOnClickListener {
            onItemClick(user)
        }
    }
}