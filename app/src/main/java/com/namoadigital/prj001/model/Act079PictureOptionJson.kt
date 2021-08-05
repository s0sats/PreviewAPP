package com.namoadigital.prj001.model

import com.google.gson.GsonBuilder

class Act079PictureOptionJson{
    fun getJson(pictureLines: String, pictureColumns: String, pictureColor: String, pictureUrl: String) : String{
        val gson = GsonBuilder().serializeNulls().create()
        return gson.toJson(
                PictureOption(
                    arrayListOf(
                        PictureOptionContent(
                            pictureLines,
                            pictureColumns,
                            pictureColor,
                            pictureUrl
                        )
                    )
                )
            )
    }

    private inner class PictureOption(
        private val CONTENT: ArrayList<PictureOptionContent>
    )

    private inner class PictureOptionContent(
        private val LINES: String,
        private val COLUMNS: String,
        private val COLOR: String,
        private val VALUE: String
    )
}

