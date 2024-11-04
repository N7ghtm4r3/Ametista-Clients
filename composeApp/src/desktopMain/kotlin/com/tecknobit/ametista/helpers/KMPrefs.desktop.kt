package com.tecknobit.ametista.helpers

import java.util.prefs.Preferences

actual class KMPrefs actual constructor(
    path: String
) {

    private val preferences = Preferences.userRoot().node(path)

    actual fun storeString(
        key: String,
        value: String?
    ) {
        if (value == null)
            preferences.remove(key)
        else
            preferences.put(key, value)
    }

    actual fun fetchString(
        key: String,
        defValue: String?
    ): String? {
        return preferences.get(key, defValue)
    }

    actual fun removeString(key: String) {
        preferences.remove(key)
    }

    actual fun clearAll() {
        preferences.clear()
    }

}