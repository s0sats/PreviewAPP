package com.namoadigital.prj001.extensions.nservices

import com.namoadigital.prj001.model.SO_Next_Orders_Obj

fun ArrayList<SO_Next_Orders_Obj>.queryPk(soPrefix:String, soCode:String):SO_Next_Orders_Obj?{

    return this.firstOrNull  {
        it.so_prefix == soPrefix
                && it.so_code == soCode
    }
}