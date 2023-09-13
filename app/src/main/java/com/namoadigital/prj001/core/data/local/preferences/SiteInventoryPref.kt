package com.namoadigital.prj001.core.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences

class SiteInventoryPref constructor(
    private val pref: SharedPreferences
) : ModelPreferences<SiteInventory> {
    override fun write(model: SiteInventory) {
        with(pref) {
            edit()
                .putInt(SITE_CODE, model.site_code)
                .putString(SITE_DESC, model.site_desc)
                .apply()
        }
    }

    override fun read(): SiteInventory {
        with(pref) {
            return SiteInventory(
                site_code = getInt(SITE_CODE, -1),
                site_desc = getString(SITE_DESC, "") ?: ""
            )
        }
    }


    companion object {

        const val SITE_CODE = "site_code"
        const val SITE_DESC = "site_desc"

        fun instance(context: Context): SiteInventoryPref {
            return SiteInventoryPref(
                context.getSharedPreferences("site_inventory", Base_Activity.MODE_PRIVATE)
            )
        }
    }
}