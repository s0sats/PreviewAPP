package com.namoadigital.prj001.ui.base

//P = Presenter
//B = Binding
//E = Event
interface BaseView<E> {

    fun onEvent(event: E)

    fun initView(
        block: (() -> Unit)? = null
    ) {
        initSetup()
        initTrans()
        block?.invoke()
        initVars()
        initAction()
    }

    fun initSetup()
    fun initTrans()
    fun initVars()
    fun initAction()

}