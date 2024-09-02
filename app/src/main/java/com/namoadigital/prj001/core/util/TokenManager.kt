package com.namoadigital.prj001.core.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.namoadigital.prj001.util.ConstantBaseApp
import com.namoadigital.prj001.util.ToolBox_Inf
import java.io.File


data class TokenModel(
    @Expose @SerializedName("token") var token: String?,
    @Expose @SerializedName("parameters") var parameters: Any?,
)
class TokenManager <T> constructor(
    private val context: Context
) {

    private val filePath = "${ConstantBaseApp.DB_PATH}/token/request_token.json"
    private val gson = Gson()
    private val model = loadModel()
    private fun loadModel(): TokenModel {
        File(filePath).let { file ->
            return if(file.exists()){
                val data = file.readText()
                return gson.fromJson(data, TokenModel::class.java) ?: TokenModel(null, null)
            }else{
                TokenModel(null, null)
            }
        }
    }

    private fun saveModel(){
        File(filePath).let { file ->
            if(!file.exists()){
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            file.writeText(gson.toJson(model))
        }
    }

    private fun generateToken() = ToolBox_Inf.getToken(context)

    fun getToken(params: T, getLastToken: Boolean = false): String {
        if(getLastToken){
            return model.token.orEmpty()
        }
        //
        if(model.parameters == params){
            return model.token.orEmpty()
        }
        //
        val newToken = generateToken()
        model.token = newToken
        model.parameters = params
        saveModel()
        return newToken
    }

    fun deleteToken() {
        File(filePath).delete()
    }

}