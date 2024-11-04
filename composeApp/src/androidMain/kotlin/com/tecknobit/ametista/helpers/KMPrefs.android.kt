package com.tecknobit.ametista.helpers

import android.content.Context
import com.tecknobit.equinoxcompose.helpers.utils.AppContext

actual class KMPrefs actual constructor(
    path: String
) {

    private val sharedPreferences = AppContext.get().getSharedPreferences(
        path,
        Context.MODE_PRIVATE
    )

    actual fun storeString(
        key: String,
        value: String?
    ) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    actual fun fetchString(
        key: String,
        defValue: String?
    ): String? {
        return sharedPreferences.getString(key, defValue)
    }

    actual fun removeString(
        key: String
    ) {
        sharedPreferences.edit().remove(key).apply()
    }

    actual fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

}