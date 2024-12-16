package com.namoadigital.prj001.ui.act011.finish_os

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import com.namoa_digital.namoa_library.util.HMAux
import com.namoadigital.prj001.R
import com.namoadigital.prj001.databinding.DialogFinishFormOsBinding
import com.namoadigital.prj001.design.compose.NamoaTheme
import com.namoadigital.prj001.extensions.getIntArguments
import com.namoadigital.prj001.extensions.getLongArguments
import com.namoadigital.prj001.ui.act011.finish_os.ui.FinishOSScreen
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishOSCloseDialog
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishScreenArguments
import com.namoadigital.prj001.ui.act011.finish_os.ui.utils.FinishScreenArguments.ARGUMENTS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogFinishOSFragment constructor(
    private val hmAuxLib: HMAux,
    private val onDialogClose: FinishOSCloseDialog
) : DialogFragment() {

    private lateinit var binding: DialogFinishFormOsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_FRAME, R.style.AppTheme_NoActionBar)
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFinishFormOsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initScreen()
    }

    private fun initScreen() {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                NamoaTheme(false) {
                    FinishOSScreen(
                        translateMapLib = hmAuxLib,
                        arguments = FinishScreenArguments(
                            typeCode = arguments.getIntArguments(ARGUMENTS.TYPE_CODE),
                            code = arguments.getIntArguments(ARGUMENTS.CODE),
                            versionCode = arguments.getIntArguments(ARGUMENTS.VERSION_CODE),
                            formData = arguments.getLongArguments(ARGUMENTS.FORM_DATA)
                        ),
                        onDialogDismiss = { dismiss() },
                        onBarcodeMkEditText = { mkeditText ->
                            onDialogClose.setBackupMachineSerialRecovery(
                                mkeditText
                            )
                        },
                        onBackupMachineWsProcess = { backupMachineList ->
                            onDialogClose.setWsProcess(
                                backupMachineList
                            )
                        },
                        onDialogClose = {
                            dismiss()
                            onDialogClose.action()
                        }
                    )
                }
            }
        }
    }
}
