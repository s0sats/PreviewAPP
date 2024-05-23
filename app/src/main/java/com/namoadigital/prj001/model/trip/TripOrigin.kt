package com.namoadigital.prj001.model.trip

enum class TripOrigin(val type:String) {
    GPS("GPS"),
    SITE("SITE");
    override fun toString(): String {
        return type 
    }
}
