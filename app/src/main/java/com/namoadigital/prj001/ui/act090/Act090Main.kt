package com.namoadigital.prj001.ui.act090

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.R

class Act090Main : Base_Activity(), Act090MainContract.IView {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act090_main)
    }
}