package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.Role
import com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN
import com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.annotations.CustomParametersOrder
import com.tecknobit.equinox.environment.records.EquinoxLocalUser

/**
 * The {@code AmetistaLocalUser} class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class AmetistaLocalUser : EquinoxLocalUser() {

    companion object {

        /**
         * **PREF_NAME** -> the name of the preferences pathname file
         */
        private const val PREF_NAME = "Ametista"

    }

    /**
     * **kmpPrefs** -> the local preferences manager
     */
    private val kmpPrefs = KMPrefs(PREF_NAME)

    /**
     * **role** -> the role of the current user
     */
    private var role: Role? = null

    init {
        initLocalUser()
    }

    /**
     * Method to init the local user session
     */
    override fun initLocalUser() {
        super.initLocalUser()
        role = getRole()
    }

    /**
     * Method to insert and init a new local user
     *
     * @param hostAddress: the host address which the user communicate
     * @param name:        the name of the user
     * @param surname:     the surname of the user
     * @param email:       the email of the user
     * @param password:    the password of the user
     * @param language:    the language of the user
     * @param hResponse:   the payload response received from an authentication request
     * @param custom: the custom parameters added in a customization of the {@link EquinoxUser}
     */
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
            role = (custom[0] as Array<*>)[0] as Role
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
     * Method to set the [role] instance
     *
     * @param role: the role of the user
     */
    fun setRole(
        role: Role
    ) {
        kmpPrefs.storeString(
            key = ROLE_KEY,
            value = role.name
        )
    }

    /**
     * Method to get the [role] instance
     *
     * @return role of the user as [Role]
     */
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

    /**
     * Method to check whether the current user is an [ADMIN]
     *
     * @return whether the current user is an [ADMIN] as [Boolean]
     */
    fun isAdmin(): Boolean {
        return role == ADMIN
    }

    /**
     * Method to check whether the current user is an [VIEWER]
     *
     * @return whether the current user is an [VIEWER] as [Boolean]
     */
    fun isViewer(): Boolean {
        return role == VIEWER
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
        initLocalUser()
    }

}
