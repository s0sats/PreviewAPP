package com.namoadigital.prj001.extensions.keyboard

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/* ---------------------------
   MOSTRAR TECLADO
---------------------------- */

fun View.showKeyboard() {
    requestFocus()

    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.showKeyboardDelayed() {
    post { showKeyboard() }
}

/* ---------------------------
   ESCONDER TECLADO
---------------------------- */

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
    clearFocus()
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

/* ---------------------------
   VERSÃO MODERNA (WindowInsets)
   Melhor para Android 11+
---------------------------- */

fun View.showKeyboardCompat() {
    requestFocus()
    ViewCompat.getWindowInsetsController(this)
        ?.show(WindowInsetsCompat.Type.ime())
}

fun View.hideKeyboardCompat() {
    ViewCompat.getWindowInsetsController(this)
        ?.hide(WindowInsetsCompat.Type.ime())
}