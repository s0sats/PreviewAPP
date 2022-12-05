package com.namoadigital.prj001.ui.act092.usecases

import com.namoadigital.prj001.ui.act092.data.repository.ActionSerialRepository
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File

class CheckFileExistsUseCase constructor(
    private val repository: ActionSerialRepository
) {

    operator fun invoke(productCode: Int, serialCode: Long): Boolean {
        val fileName = ToolBox_Inf.getOtherActionFileName(productCode, serialCode)
        val file = File(ConstantBaseApp.OTHER_ACTIONS_JSON_PATH, fileName)
        return file.exists()
    }

}
