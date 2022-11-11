package com.namoadigital.prj001.ui.base

//P = Presenter
//B = Binding
//S = State
interface BaseView<S> {


    fun onState(state: S)

    fun initView(
        block: () -> Unit
    ) {
        initSetup()
        initTrans()
        initVars()
        initAction()
        block()
    }

    fun initSetup()
    fun initTrans()
    fun initVars()
    fun initAction()

}