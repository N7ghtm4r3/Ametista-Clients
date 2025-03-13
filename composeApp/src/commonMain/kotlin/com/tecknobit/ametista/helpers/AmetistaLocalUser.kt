package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.ROLE_KEY
import com.tecknobit.ametistacore.enums.Role
import com.tecknobit.ametistacore.enums.Role.ADMIN
import com.tecknobit.ametistacore.enums.Role.VIEWER
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import kotlinx.serialization.json.JsonObject

/**
 * The `AmetistaLocalUser` class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class AmetistaLocalUser : EquinoxLocalUser(
    localStoragePath = "Ametista"
) {

    /**
     * `role` -> the role of the current user
     */
    private var role: Role? = null
        set(value) {
            if (field != value) {
                setPreference(
                    key = ROLE_KEY,
                    value = value?.name
                )
                field = value
            }
        }

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
     * @param hostAddress The host address which the user communicate
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param language The language of the user
     * @param response The payload response received from an authentication request
     * @param custom The custom parameters added in a customization of the {@link EquinoxUser}
     */
    @CustomParametersOrder(order = [ROLE_KEY])
    override fun insertNewUser(
        hostAddress: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String,
        response: JsonObject,
        vararg custom: Any?,
    ) {
        role = custom.extractsCustomValue(
            itemPosition = 0
        )
        super.insertNewUser(
            hostAddress,
            name,
            surname,
            email,
            password,
            language,
            response,
            *custom
        )
    }

    /**
     * Method to get the [role] instance
     *
     * @return role of the user as [Role]
     */
    fun getRole(): Role? {
        val role = getPreference(
            key = ROLE_KEY
        )
        return if (role == null)
            null
        else {
            Role.valueOf(
                value = role
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

}
