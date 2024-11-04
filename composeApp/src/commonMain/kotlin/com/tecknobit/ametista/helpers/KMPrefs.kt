package com.tecknobit.ametista.helpers

expect class KMPrefs(
    path: String
) {

    fun storeString(
        key: String,
        value: String?
    )

    fun fetchString(
        key: String,
        defValue: String? = null
    ): String?

    fun removeString(
        key: String
    )

    fun clearAll()

}