package com.namoadigital.prj001.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.namoadigital.prj001.R
import com.namoadigital.prj001.ui.act005.trip.fragment.base.TripBaseFragment

inline fun  <reified T: Fragment> Fragment.getFragment(): T? {
    return if (this.childFragmentManager.fragments[0] is T) {
        this.childFragmentManager.fragments[0] as T
    } else null
}

inline fun  <reified T: Fragment> Fragment.navigateTo(resId: Int) {
    NavHostFragment.findNavController(this).navigate(resId)
}


fun Fragment.popBackStack(fragmentId: Int, inclusive: Boolean = false){
    NavHostFragment.findNavController(this).popBackStack(fragmentId, inclusive)
}
