package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.Role
import com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN
import com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.annotations.CustomParametersOrder
import com.tecknobit.equinox.environment.records.EquinoxLocalUser

class AmetistaLocalUser : EquinoxLocalUser() {

    private val kmpPrefs = KMPrefs("Ametista")

    private var role: Role?

    init {
        role = getRole()
        initLocalUser()
    }

    @CustomParametersOrder(order = [ROLE_KEY])
    override fun insertNewUser(
        hostAddress: String?,
        name: String?,
        surname: String?,
        email: String?,
        password: String?,
        language: String?,
        hResponse: JsonHelper?,
        vararg custom: Any?
    ) {
        setRole(
            role = (custom[0] as Array<Any>)[0] as Role
        )
        super.insertNewUser(
            hostAddress,
            name,
            surname,
            email,
            password,
            language,
            hResponse,
            *custom
        )
    }

    /**
     * Method to store and set a preference
     *
     * @param key   :   the key of the preference
     * @param value : the value of the preference
     */
    override fun setPreference(
        key: String,
        value: String
    ) {
        kmpPrefs.storeString(
            key = key,
            value = value
        )
    }

    /**
     * Method to get a stored preference
     *
     * @param key : the key of the preference to get
     * @return the preference stored as [String]
     */
    override fun getPreference(
        key: String
    ): String? {
        return kmpPrefs.fetchString(
            key = key
        )
    }

    /**
     * Method to clear the current local user session <br></br>
     * No-any params required
     */
    override fun clear() {
        kmpPrefs.clearAll()
    }

    fun setRole(
        role: Role
    ) {
        kmpPrefs.storeString(
            key = ROLE_KEY,
            value = role.name
        )
    }

    fun getRole(): Role? {
        val value = kmpPrefs.fetchString(
            key = ROLE_KEY
        )
        return if (value == null)
            null
        else {
            Role.valueOf(
                value = value
            )
        }
    }

    fun isAdmin(): Boolean {
        return role == ADMIN
    }

    fun isViewer(): Boolean {
        return role == VIEWER
    }

}
