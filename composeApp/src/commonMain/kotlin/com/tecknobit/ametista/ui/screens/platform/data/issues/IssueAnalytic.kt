package com.tecknobit.ametista.ui.screens.platform.data.issues

import com.tecknobit.ametista.ui.screens.applications.data.AmetistaDevice
import com.tecknobit.ametista.ui.screens.platform.data.AmetistaAnalytic
import com.tecknobit.ametista.ui.screens.shared.data.AmetistaItem
import com.tecknobit.ametistacore.APP_VERSION_KEY
import com.tecknobit.ametistacore.CREATION_DATE_KEY
import com.tecknobit.ametistacore.enums.Platform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [IssueAnalytic] is used represent an issue of the `Ametista` system
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see AmetistaItem
 * @see AmetistaAnalytic
 */
interface IssueAnalytic : AmetistaAnalytic {

    /**
     * `issue` -> the issue details
     */
    val issue: String

    /**
     * `device` -> the device where the issue occurred
     */
    val device: AmetistaDevice

}

/**
 * The [IssueAnalyticImpl] is the implementation of the [IssueAnalytic] interface
 *
 * @property id The identifier of the issue
 * @property name The name of the issue
 * @property creationDate The date when the issue occurred
 * @property appVersion The platform of the issue
 * @property platform The description of the issue
 * @property issue The issue details
 * @property device The device where the issue occurred
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see AmetistaItem
 * @see AmetistaAnalytic
 * @see IssueAnalytic
 */
@Serializable
data class IssueAnalyticImpl(
    override val id: String,
    override val name: String,
    @SerialName(CREATION_DATE_KEY)
    override val creationDate: Long,
    @SerialName(APP_VERSION_KEY)
    override val appVersion: String,
    override val platform: Platform,
    override val issue: String,
    override val device: AmetistaDevice,
) : IssueAnalytic
