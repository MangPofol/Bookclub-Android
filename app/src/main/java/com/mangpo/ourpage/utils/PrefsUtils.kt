package com.mangpo.ourpage.utils

import com.mangpo.ourpage.ApplicationClass.Companion.prefs

object PrefsUtils {
    fun setTempRecord(record: String) {
        with(prefs.edit()) {
            putString("TEMP_RECORD", record)
            apply()
        }
    }

    fun getTempRecord(): String = prefs.getString("TEMP_RECORD", "").toString()
}