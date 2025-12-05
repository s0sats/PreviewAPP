package com.namoadigital.prj001.util.preferences

import com.namoadigital.prj001.model.big_file.BigFile

class TypeBasedKeyStrategy : KeyStrategy<BigFile> {
    override fun getKey(item: BigFile): String = "bigfile_${item.fileType}"
}