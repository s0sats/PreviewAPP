package com.namoadigital.prj001.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.databinding.M3DialogBinding
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Con
import com.namoadigital.prj001.util.ToolBox_Inf


class WarningFormPending(
    context: Context,
    @DrawableRes private val icon: Int,
    private val isBlocked: Boolean,
    private val interact: Interact,

    ) : AlertDialog(context) {
    private var resourceName: String = ""

    init {
        resourceName = if (isBlocked) {
            "dialog_warning_form_block"
        } else {
            "dialog_warning_form_pending"
        }
    }

    private lateinit var hmAux: HMAux

    private val binding by lazy {
        M3DialogBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadTranslation()

        setCancelable(false)

        setLabels()
        setActions()

    }

    private fun loadTranslation() {

        listOf(
            DIALOG_TITLE,
            DIALOG_DESC,
            DIALOG_OK,
            DIALOG_CANCEL
        ).let { list ->

            ToolBox_Inf.getResourceCode(
                context,
                ConstantBaseApp.APP_MODULE,
                resourceName
            ).let { code ->
                hmAux = ToolBox_Inf.setLanguage(
                    context,
                    ConstantBaseApp.APP_MODULE,
                    code,
                    ToolBox_Con.getPreference_Translate_Code(context),
                    list
                )
            }


        }

    }


    private fun setLabels() {
        with(binding) {
            dialogIcon.setImageDrawable(AppCompatResources.getDrawable(context, icon))
            dialogTitle.text = hmAux[DIALOG_TITLE]
            dialogDescription.text = hmAux[DIALOG_DESC]
            dialogAction1.text = "Enviar / Continuar"//hmAux[DIALOG_OK]
            dialogAction2.text = "Voltar"//hmAux[DIALOG_CANCEL]

            dialogAction2.visibility = if (isBlocked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun setActions() {
        binding.dialogAction1.setOnClickListener { interact.onClickOk(this@WarningFormPending) }
        binding.dialogAction2.setOnClickListener { interact.onClickCancel(this@WarningFormPending) }
    }

    companion object {

        private const val DIALOG_TITLE = "dialog_pending_form_warning_ttl"
        private const val DIALOG_DESC = "dialog_pending_form_warning_desc"
        private const val DIALOG_OK = "dialog_pending_form_warning_ok"
        private const val DIALOG_CANCEL = "dialog_pending_form_warning_cancel"

    }

    interface Interact {

        fun onClickOk(dialog: AlertDialog)
        fun onClickCancel(dialog: AlertDialog)

    }
}