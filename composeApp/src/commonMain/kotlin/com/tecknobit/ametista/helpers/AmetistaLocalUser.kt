package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.models.AmetistaUser.Role
import com.tecknobit.equinox.environment.records.EquinoxLocalUser
import com.tecknobit.equinox.environment.records.EquinoxUser

class AmetistaLocalUser : EquinoxLocalUser() {

    /**
     * Method to store and set a preference
     *
     * @param key   :   the key of the preference
     * @param value : the value of the preference
     */
    override fun setPreference(key: String, value: String) {
    }

    /**
     * Method to get a stored preference
     *
     * @param key : the key of the preference to get
     * @return the preference stored as [String]
     */
    override fun getPreference(key: String): String {
        return ""
    }

    /**
     * Method to clear the current local user session <br></br>
     * No-any params required
     */
    override fun clear() {
    }

    // TODO: TO REMOVE
    override fun getCompleteName(): String {
        return "John Doe"
    }

    // TODO: TO REMOVE
    override fun getProfilePic(): String {
        return "https://d.newsweek.com/en/full/2316078/astronaut-space-laptop.jpg"
    }

    // TODO: TO REMOVE 
    override fun getEmail(): String {
        return "john_doe@email.com"
    }

    // TODO: TO REMOVE
    override fun getLanguage(): String {
        return "en"
    }

    // TODO: TO REMOVE
    override fun getTheme(): EquinoxUser.ApplicationTheme {
        return EquinoxUser.ApplicationTheme.Auto
    }

    /* TODO: TO USE CORRECTLY */
    fun getRole(): Role {
        return Role.VIEWER
    }

    // TODO: IMPLEMENT THE IS_ADMIN OR IS_VIEWER METHODS 
    
}
