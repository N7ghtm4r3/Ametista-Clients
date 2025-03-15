package com.tecknobit.ametista.ui.screens.session.data

import com.tecknobit.ametistacore.enums.Role
import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmetistaMember(
    val id: String,
    @SerialName(PROFILE_PIC_KEY)
    val profilePic: String,
    val name: String,
    val surname: String,
    val email: String,
    val role: Role,
) {

    val completeName: String
        get() = "$name $surname"

}
