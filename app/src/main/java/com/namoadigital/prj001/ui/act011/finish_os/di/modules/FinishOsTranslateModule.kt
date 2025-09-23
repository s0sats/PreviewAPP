package com.namoadigital.prj001.ui.act011.finish_os.di.modules

import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.TranslateMap
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_MACHINE_STOPPED_SWITCH_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_MAINTENANCE
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_THIRD_PARTY
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_BACKUP_MACHINE_HINT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_BACKUP_MACHINE_SWITCH_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_BACKUP_MACHINE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DATE_EXCEED_MACHINE_DATE_STOPPED_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_MSG
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_SO_INFO_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_FORM_SO_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EMPTY_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EXCEED_FORM_START_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_AFTER_TITLE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_BTN_CANCEL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_BTN_SAVE
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_EMPTY_VERIFY_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_ELAPSED_TIME_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_END_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_INVALID_START_DATE_MSG
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_FORM_START_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_END_DATE_EXCEEDED_START_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_END_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_LAST_MEASURE_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_INVALID_INITIAL_SERIAL_STATE_DATE_ERROR_MSG
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZED_DATE_INCORRECT_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZED_PARTIAL_EXECUTION_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZE_DECIDE_PLANNING_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_NOT_FINALIZE_INFO_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_MACHINE_EMPTY_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_MACHINE_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_SERIAL_HELP_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.DIALOG_SELECT_BACKUP_SERIAL_HINT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_DATE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_MAINTENANCE_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_NO_STOPPED_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_RESPONSIBLE_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_STOPPED_TTL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_SWITCH_LBL
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_THIRD_PARTY_ERROR_OPT
import com.namoadigital.prj001.ui.act011.finish_os.ui.translate.INITIAL_SERIAL_STATE_TTL
import com.namoadigital.prj001.util.ConstantBaseApp.ACT011
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FinishOsTranslate

@Module
@InstallIn(ViewModelComponent::class)
object FinishOsTranslateModule {


    @Provides
    @FinishOsTranslate
    @ViewModelScoped
    fun providesFinishOsTranslate(
        translateBuild: TranslateBuild
    ): TranslateMap {

        
        val list = mutableListOf(
            DIALOG_FINALIZE_FORM_SO_TTL,
            INITIAL_SERIAL_STATE_TTL,
            INITIAL_SERIAL_STATE_STOPPED_TTL,
            INITIAL_SERIAL_STATE_SWITCH_LBL,
            INITIAL_SERIAL_STATE_DATE_LBL,
            INITIAL_SERIAL_STATE_RESPONSIBLE_LBL,
            INITIAL_SERIAL_STATE_MAINTENANCE_OPT,
            INITIAL_SERIAL_STATE_THIRD_PARTY_ERROR_OPT,
            INITIAL_SERIAL_STATE_NO_STOPPED_OPT,
            DIALOG_FINALIZE_FORM_SO_INFO_LBL,
            DIALOG_SELECT_BACKUP_MACHINE_TTL,
            DIALOG_INVALID_INITIAL_SERIAL_STATE_DATE_ERROR_MSG,
            DIALOG_SELECT_BACKUP_MACHINE_EMPTY_LBL,
            DIALOG_SELECT_BACKUP_SERIAL_HELP_LBL,
            DIALOG_SELECT_BACKUP_SERIAL_HINT,
            DIALOG_FINALIZE_OS_EMPTY_VERIFY_LBL,
            DIALOG_FINALIZE_OS_FORM_START_DATE_LBL,
            DIALOG_FINALIZE_OS_FORM_END_DATE_LBL,
            DIALOG_FINALIZE_OS_FORM_ELAPSED_TIME_LBL,
            DIALOG_FINALIZE_OS_FORM_INVALID_START_DATE_MSG,
            DIALOG_FINALIZE_OS_BTN_SAVE,
            DIALOG_FINALIZE_OS_BTN_CANCEL,
            DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_END_DATE_LBL,
            DIALOG_FINALIZE_OS_INFO_END_DATE_EXCEEDED_START_DATE_LBL,
            FORM_OS_INFO_END_DATE_FUTURE_ERROR_LBL,
            DIALOG_NOT_FINALIZE_INFO_LBL,
            DIALOG_NOT_FINALIZE_DECIDE_PLANNING_LBL,
            DIALOG_NOT_FINALIZED_PARTIAL_EXECUTION_LBL,
            DIALOG_NOT_FINALIZED_DATE_INCORRECT_LBL,
            DIALOG_FINALIZE_OS_AFTER_TITLE_LBL,
            DIALOG_FINALIZED_OS_MACHINE_STOPPED_SWITCH_TTL,
            DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_MAINTENANCE,
            DIALOG_FINALIZED_OS_OPTION_STOPPED_BY_THIRD_PARTY,
            DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EMPTY_ERROR_LBL,
            DIALOG_FINALIZE_INITIAL_MACHINE_DATE_EXCEED_FORM_START_ERROR_LBL,
            DIALOG_FINALIZE_BACKUP_MACHINE_TTL,
            DIALOG_FINALIZE_BACKUP_MACHINE_SWITCH_TTL,
            DIALOG_FINALIZE_BACKUP_MACHINE_HINT,
            DIALOG_FINALIZE_FORM_OS_DATE_EXCEED_MACHINE_DATE_STOPPED_LBL,
            DIALOG_FINALIZE_OS_INFO_START_DATE_EXCEEDED_LAST_MEASURE_DATE_LBL,
            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_TTL,
            DIALOG_FINALIZE_FORM_OS_DIALOG_CLOSE_CONFIRM_MSG,
        )

        return translateBuild
            .resource(ACT011)
            .listVars(list)
            .build()
    }

}