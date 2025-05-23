package com.namoadigital.prj001.extensions

import android.widget.CompoundButton


/**
 * Fun que altera valor de isChecked porem, pula a animação.
 */
fun CompoundButton.setCheckedJumpingAnimation(checked: Boolean){
    if(checked != this.isChecked) {
        this.isChecked = checked
        jumpDrawablesToCurrentState()
    }
}