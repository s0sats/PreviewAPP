package com.namoadigital.prj001.extensions

import android.content.Context
import com.namoadigital.prj001.dao.trip.FSTripDao

fun Context.isCurrentTrip() = FSTripDao(this).getTrip() != null