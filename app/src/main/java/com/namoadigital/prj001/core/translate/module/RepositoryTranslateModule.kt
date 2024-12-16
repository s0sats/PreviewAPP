package com.namoadigital.prj001.core.translate.module

import android.content.Context
import com.namoadigital.prj001.core.DB_GET_DATA_ERROR_LBL
import com.namoadigital.prj001.core.DB_TRANSACTION_ERROR_LBL
import com.namoadigital.prj001.core.NETWORK_GENERIC_ERROR
import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.core.translate.TranslateMap
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RepositoryTranslate

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryTranslateModule {


    @Provides
    @RepositoryTranslate
    fun provideRepositoryTranslate(
        @ApplicationContext context: Context
    ): TranslateMap {
        listOf(
            NETWORK_GENERIC_ERROR,
            DB_TRANSACTION_ERROR_LBL,
            DB_GET_DATA_ERROR_LBL
        ).let {
            return TranslateBuild(context)
                .listVars(it)
                .resource("0")
                .build()
        }
    }


}