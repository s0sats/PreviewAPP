package com.namoadigital.prj001.ui.act047.component.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.DialogTechnicalAddUserBinding
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.next_os.ListReservedUserRec
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users.DialogListUsers
import com.namoadigital.prj001.ui.act047.component.adapter.ReservedUserListAdapter
import com.namoadigital.prj001.ui.base.BaseDialog
import com.namoadigital.prj001.util.Constant

class DialogReservedUserList(
    private val context: Context,
    private val source: List<ListReservedUserRec.ResultRec.Users>,
    private val onClickUser: (user: ListReservedUserRec.ResultRec.Users) -> Unit,
    private val onClose: () -> Unit
) {

    private lateinit var binding: DialogTechnicalAddUserBinding
    private lateinit var userAdapter: ReservedUserListAdapter

    private val hmAuxTranslate = loadTranslation(context)

    private val dialog by lazy {
        BaseDialog.Builder(
            context = context,
            contentView = DialogTechnicalAddUserBinding.inflate(LayoutInflater.from(context)),
            margin = true
        ).content { _, binding ->
            this@DialogReservedUserList.binding = binding
            initializeViews()
            initializeListeners()

        }.build()
    }

    private fun initializeViews() {
        with(binding) {
            tvTitle.text = hmAuxTranslate[DialogListUsers.DIALOG_TITLE]
            edittextFilterLayout.hint = hmAuxTranslate[DialogListUsers.DIALOG_FILTER_HINT]
            tvEmptyList.text = hmAuxTranslate[DialogListUsers.DIALOG_EMPTY_LIST]

            if (source.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
                return
            }

            userAdapter = ReservedUserListAdapter(
                source = source.toMutableList(),
                selectUser = { user ->
                    dialog.dismiss()
                    onClickUser(user)
                },
                updateSizeList = {
                    if(it == 0) {
                        recyclerView.visibility = View.GONE
                        tvEmptyList.visibility = View.VISIBLE
                    }else{
                        recyclerView.visibility = View.VISIBLE
                        tvEmptyList.visibility = View.GONE
                    }
                }
            )

            edittextFilter.setOnReportTextChangeListner(object : MKEditTextNM.IMKEditTextChangeText{
                override fun reportTextChange(text: String?) {
                }

                override fun reportTextChange(text: String?, p1: Boolean) {
                    userAdapter.filter(text ?: "")
                }

            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                visibility = View.VISIBLE
                adapter = userAdapter
            }
        }
    }

    private fun initializeListeners() {
        with(binding) {
            closeDialog.setOnClickListener {
                dismiss()
            }
        }
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
        onClose()
    }



    companion object TRANSLATE {

        const val DIALOG_TITLE = "list_user_title_ttl"
        const val DIALOG_FILTER_HINT = "list_user_filter_hint"
        const val DIALOG_EMPTY_LIST = "list_user_empty_list"



        fun loadTranslation(context: Context): HMAux = listOf(
            DIALOG_TITLE,
            DIALOG_FILTER_HINT,
            DIALOG_EMPTY_LIST,
        ).let(
            TranslateResource(
                context = context,
                mResoure_code = context.getResourceCode(Constant.ACT047)
            )::setLanguage
        )
    }

}