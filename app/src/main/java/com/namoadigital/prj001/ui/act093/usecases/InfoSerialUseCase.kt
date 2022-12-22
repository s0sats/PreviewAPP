package com.namoadigital.prj001.ui.act093.usecases

import android.content.Context
import com.namoadigital.prj001.ui.act093.data.repository.IInfoSerialRepository.Companion.InfoSerialRepositoryFactory
import com.namoadigital.prj001.ui.base.NamoaFactory

data class InfoSerialUseCase(
    val getInfoSerial: GetInfoSerialUseCase,
    val getDeviceList: GetListDeviceUseCase
) {

    companion object {
        class InfoSerialUseCasesFactory constructor(
            private val context: Context
        ) : NamoaFactory<InfoSerialUseCase>() {
            override fun build(): InfoSerialUseCase {

                val repository = InfoSerialRepositoryFactory(context).build()

                return InfoSerialUseCase(
                    getInfoSerial = GetInfoSerialUseCase(repository, context),
                    getDeviceList = GetListDeviceUseCase(repository)
                )
            }
        }
    }

}
