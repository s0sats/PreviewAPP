package com.namoadigital.prj001.ui.base

//P = Presenter
//B = Binding
//S = State
interface BaseView<S> {

    fun onState(state: S)

    fun initView(
        block: (() -> Unit)? = null
    ) {
        initSetup()
        initTrans()
        initVars()
        block?.invoke()
        initAction()
    }

    fun initSetup()
    fun initTrans()
    fun initVars()
    fun initAction()

}