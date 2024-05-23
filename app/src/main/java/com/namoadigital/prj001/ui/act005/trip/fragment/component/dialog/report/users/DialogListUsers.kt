package com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.namoa_digital.namoa_library.ctls.MKEditTextNM
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.DialogTechnicalAddUserBinding
import com.namoadigital.prj001.extensions.getResourceCode
import com.namoadigital.prj001.model.TranslateResource
import com.namoadigital.prj001.model.trip.FSTrip
import com.namoadigital.prj001.ui.act005.trip.di.enums.UserAction
import com.namoadigital.prj001.ui.act005.trip.di.model.TripUserEdit
import com.namoadigital.prj001.ui.act005.trip.di.usecase.user.OutputParams
import com.namoadigital.prj001.ui.act005.trip.fragment.base.BaseTripDialog
import com.namoadigital.prj001.ui.act005.trip.fragment.component.dialog.report.ReportBottomSheet
import com.namoadigital.prj001.ui.base.BaseDialog

class DialogListUsers constructor(
    context: Context,
    private val trip: FSTrip,
    private val source: List<TripUserEdit>,
    private val onSaveUser: (user: TripUserEdit, action: UserAction, processMessage: Pair<String, String>) -> Unit,
    private val checkUserIntersectionDate: (
        userCode: Int,
        userSeq: Int?,
        startDateMillis: Long,
        endDateMillis: Long?
    ) -> OutputParams
) : BaseTripDialog<DialogTechnicalAddUserBinding>(trip, null) {

    private lateinit var userAdapter: UsersListAdapter
    private lateinit var editDialog: DialogEditUser
    private val hmAuxTranslate = loadTranslation(context)

    init {
        dialog = BaseDialog.Builder(
            context = context,
            contentView = DialogTechnicalAddUserBinding.inflate(LayoutInflater.from(context)),
            margin = true
        ).content { _, binding ->
            this@DialogListUsers.binding = binding;
            initializeViews()
            initializeListeners()

        }.build()
    }

    private fun initializeViews() {
        with(binding) {
            tvTitle.text = hmAuxTranslate[DIALOG_TITLE]
            edittextFilterLayout.hint = hmAuxTranslate[DIALOG_FILTER_HINT]
            tvEmptyList.text = hmAuxTranslate[DIALOG_EMPTY_LIST]

            if (source.isEmpty()) {
                recyclerView.visibility = View.GONE
                tvEmptyList.visibility = View.VISIBLE
                return
            }



            userAdapter = UsersListAdapter(
                source = source.toMutableList(),
                selectUser = { user ->
                    dialog.dismiss()
                    editDialog = DialogEditUser(
                        context = dialog.dialogContext,
                        trip = trip,
                        user = user,
                        onSaveUser = onSaveUser,
                        checkUserIntersectionDate = checkUserIntersectionDate
                    )
                    editDialog.show()
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
                dialog.dismiss()
            }
        }
    }

    override fun show() {
        dialog.show()
    }

    override fun dismiss() {
        dialog.dismiss()
        if(!this::editDialog.isInitialized) return
        editDialog.dismiss()
    }

    override fun errorSendData() {
        editDialog.errorSendData()
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
                mResoure_code = context.getResourceCode(ReportBottomSheet.RESOURCE_DIALOG_EVENT_TRIP)
            )::setLanguage
        )
    }
}