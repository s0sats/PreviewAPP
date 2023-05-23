package com.namoadigital.prj001.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.namoadigital.prj001.databinding.Act005DialogSupportBinding
import com.namoadigital.prj001.model.SupportDialogFields

class SupportDialog(
    context: Context,
    val labels: SupportDialogFields,
    val onSendSupport: (String, String) -> Unit
): Dialog(context) {

    private var _binding: Act005DialogSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        _binding = Act005DialogSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        setLabels()
        //
        iniActions()
    }

    private fun iniActions() {
        binding.apply {
            act005DialogSupportBtnCancel.setOnClickListener {
                dismiss()
            }
            act005DialogSupportBtnOk.setOnClickListener {

                if (act005DialogSupportEtMsg.getText().toString().trim { it <= ' ' }.length > 0) {
                    if (act005DialogSupportEtContact.getText().toString().trim { it <= ' ' }.length > 0) {
                        onSendSupport(
                            act005DialogSupportEtMsg.getText().toString().trim { it <= ' ' },
                            act005DialogSupportEtContact.getText().toString().trim { it <= ' ' })
                        //
                        dismiss()
                    } else {
                        act005DialogSupportEtContact.setText("")
                        act005DialogSupportEtContact.findFocus()
                        //
                        Toast.makeText(
                            context,
                            labels.alert_support_empty_contact,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    act005DialogSupportEtMsg.setText("")
                    act005DialogSupportEtMsg.findFocus()
                    //
                    Toast.makeText(
                        context,
                        labels.alert_support_empty_msg,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setLabels() {
        binding.apply {
            act005DialogSupportTvTtl.setText(labels.alert_support_ttl)
            act005DialogSupportTvContect.setText(labels.alert_support_contact)
            act005DialogSupportTvMsg.setText(labels.alert_support_msg)
            act005DialogSupportEtContact.setHint(labels.alert_support_contact_hint)
            act005DialogSupportEtMsg.setHint(labels.alert_support_hint)
        }
        //
        setTitle(labels.alert_support_ttl)

    }

}