package com.namoadigital.prj001.ui.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

class BaseDialog<T : ViewBinding> private constructor(
    context: Context,
    contentView: T,
    isCancelable: Boolean,
    margin: Boolean = false,
    content: ((Dialog, T) -> Unit?)?
) {
    private var dialog: Dialog
    var dialogContext: Context


    init {
        Dialog(context).apply {
            setContentView(contentView.root)
            setCancelable(isCancelable)
            dialogContext = this.context
        }.let { dialogView ->
            dialog = dialogView
            val dm = context.resources.displayMetrics
            val dmW = dm.widthPixels.toFloat() * 0.9f
            val dmH = dm.heightPixels.toFloat() * 0.95f
            dialogView.window?.apply {
                setLayout(
                    dmW.toInt(),
                    if (margin) dmH.toInt() else ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            content?.invoke(dialog, contentView)
        }
    }

    fun show(): BaseDialog<T> {
        dialog.show()
        return this
    }

    fun dismiss(): BaseDialog<T> {
        dialog.dismiss()
        return this
    }

    fun onDismissListener(dismiss: (DialogInterface) -> Unit): BaseDialog<T> {
        dialog.setOnDismissListener { dismiss(it) }
        return this
    }

    fun onShowListener(showListener: (DialogInterface) -> Unit): BaseDialog<T> {
        dialog.setOnShowListener { showListener(it) }
        return this
    }

    class Builder<T : ViewBinding>(
        private val context: Context,
        private val contentView: T,
        private val margin: Boolean = false
    ) {
        private var isCancelable = false
        private var content: ((Dialog, T) -> Unit?)? = null

        fun content(content: (dialog: Dialog, binding: T) -> Unit): Builder<T> {
            this.content = content
            return this
        }

        fun isCancelable(isCancelable: Boolean): Builder<T> {
            this.isCancelable = isCancelable
            return this
        }

        fun build(): BaseDialog<T> {
            return BaseDialog(context, contentView, isCancelable, margin, content)
        }
    }
}