package com.namoadigital.prj001.model.auxiliar

import org.json.JSONObject

class GeCustomFormDataFieldExtraPhoto(jsonExtraValue: String?) {
    var photo1: String?
    var photo2: String?
    var photo3: String?
    var photo4: String?

    init {
        val rz: JSONObject = JSONObject(jsonExtraValue)
        val ja = rz.getJSONArray("CONTENT")

        photo1 = ja.getJSONObject(0).getString("PHOTO1")
        photo2 = ja.getJSONObject(0).getString("PHOTO2")
        photo3 = ja.getJSONObject(0).getString("PHOTO3")
        photo4 = ja.getJSONObject(0).getString("PHOTO4")
    }
}