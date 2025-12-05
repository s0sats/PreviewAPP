package com.namoadigital.prj001.util.preferences

import android.content.Context
import com.namoadigital.prj001.model.big_file.BigFile
import com.namoadigital.prj001.worker.big_file.utils.BigFileStatus

class BigFilePreferenceManager(context: Context, val fileType:String) {
    private val repository = SharedPreferencesRepository(
        context = context,
        keyStrategy = TypeBasedKeyStrategy(),
        "${fileType}_big_file_prefs"
    )

    fun saveBigFile(bigFile: BigFile) {
        repository.save(bigFile)
    }

    fun getBigFile(): BigFile {
        val key = "bigfile_$fileType"
        val bigFile = repository.get(key, BigFile::class.java)
        if(bigFile!= null){
            return bigFile
        }
        //
        return BigFile(
            fileType = fileType,
            fileStatus = BigFileStatus.NO_VALUE.name,
        )
    }

    fun removeBigFile(bigFile: BigFile) {
        repository.remove(bigFile)
    }

    fun bigFileExists(bigFile: BigFile): Boolean {
        return repository.exists(bigFile)
    }

    fun clearAll() {
        repository.clear()
    }

    fun initializeStatus() {
//        val structurePreference = getBigFile(FILE_TYPE_SERIAL_STRUCTURE)
//        startBigFilePreference(structurePreference)
//        val ticketPreference = getBigFile(FILE_TYPE_TICKET)
//        startBigFilePreference(ticketPreference)
    }

    private fun startBigFilePreference(preference: BigFile) {
//        if (preference.fileStatus != null && (preference.fileStatus == BigFileStatus.NO_VALUE.name
//                    || preference.fileStatus == BigFileStatus.DONE.name
//                    || preference.fileStatus == BigFileStatus.NOT_FOUND.name)
//        ) {
//            preference.fileStatus = BigFileStatus.PENDING.name
//            saveBigFile(preference)
//        }
    }

    fun initializePreference() {
//        initializeBigFileProcess(FILE_TYPE_SERIAL_STRUCTURE)
//        initializeBigFileProcess(FILE_TYPE_TICKET)
    }

    fun initializeBigFileProcess(){
        val bigFile = getBigFile()
        bigFile.fileStatus = BigFileStatus.NO_VALUE.name
        bigFile.fileUrl = null
        bigFile.fileMd5 = null
        bigFile.fileCode = null
        saveBigFile(bigFile)
    }

    companion object{
        const val FILE_TYPE_SERIAL_STRUCTURE = "SERIAL_STRUCTURE"
        const val FILE_TYPE_TICKET = "TICKET"

    }
}