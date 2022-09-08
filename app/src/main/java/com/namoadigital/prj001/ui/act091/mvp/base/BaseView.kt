package com.namoadigital.prj001.ui.act091.mvp.base

//P = Presenter
//B = Binding
//S = State
interface BaseView<P, B, S> {


    val presenter: P

    val binding: B

    fun onState(state: S)

    fun initView(
        block: () -> Unit
    ){
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