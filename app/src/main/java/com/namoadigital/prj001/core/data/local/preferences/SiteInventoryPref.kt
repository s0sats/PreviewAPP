package com.namoadigital.prj001.core.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.namoa_digital.namoa_library.view.Base_Activity
import com.namoadigital.prj001.core.data.domain.model.SiteInventory
import com.namoadigital.prj001.core.data.domain.preference.ModelPreferences
import com.namoadigital.prj001.util.ToolBox_Inf

class SiteInventoryPref constructor(
    private val pref: SharedPreferences
) : ModelPreferences<SiteInventory> {
    override fun write(model: SiteInventory) {
        with(pref) {
            edit()
                .putInt(SITE_CODE, model.site_code)
                .putString(SITE_DESC, model.site_desc)
                .putBoolean(REFRESH, model.refresh)
                .apply()
        }
    }

    override fun read(): SiteInventory {
        with(pref) {
            return SiteInventory(
                site_code = getInt(SITE_CODE, -1),
                site_desc = getString(SITE_DESC, "") ?: "",
                refresh = getBoolean(REFRESH, false)
            )
        }
    }

    fun edit(hashMap: HashMap<String, Any>) {
        with(pref) {
            hashMap.map { map ->
                try {
                    edit {
                        when (map.key) {
                            SITE_CODE -> putInt(SITE_CODE, map.value as Int)
                            SITE_DESC -> putString(SITE_DESC, map.value as String)
                            REFRESH -> putBoolean(REFRESH, map.value as Boolean)
                            else -> {
                                throw NoSuchFieldException(
                                    "${map.key} not found."
                                )
                            }

                        }
                    }
                } catch (e: Exception) {
                    ToolBox_Inf.registerException(javaClass.name, e)
                }
            }
        }
    }


    companion object {

        const val SITE_CODE = "site_code"
        const val SITE_DESC = "site_desc"
        const val REFRESH = "refresh"

        fun instance(context: Context): SiteInventoryPref {
            return SiteInventoryPref(
                context.getSharedPreferences("site_inventory", Base_Activity.MODE_PRIVATE)
            )
        }
    }
}