package com.namoadigital.prj001.core.translate.di

import com.namoadigital.prj001.core.translate.TranslateBuild
import com.namoadigital.prj001.ui.act095.event_manual.translate.EventManualKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ServiceComponent
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EventTranslate

@Module
@InstallIn(ActivityRetainedComponent::class)
object TranslateModule {

    @Provides
    @EventTranslate
    fun provideEventManualTranslate(translateBuild: TranslateBuild): TranslateBuild {
        val eventResource = "event_manual_resource"
        return translateBuild
            .resource(eventResource)
            .listVarsKeys { EventManualKey.entries }
    }


}

@Module
@InstallIn(ServiceComponent::class)
object TranslateServiceModule {

    @Provides
    @EventTranslate
    fun provideEventManualTranslate(translateBuild: TranslateBuild): TranslateBuild {
        val eventResource = "event_manual_resource"
        return translateBuild
            .resource(eventResource)
            .listVarsKeys { EventManualKey.entries }
    }


}