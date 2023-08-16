package com.namoadigital.prj001.design.list

interface OnRememberListState<T> {
    fun dataChanged(list: ArrayList<T>?)

}