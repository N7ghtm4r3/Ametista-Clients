package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.helpers.AmetistaEndpointsSet.CHANGE_PRESET_PASSWORD_ENDPOINT
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.ametistacore.helpers.pagination.PaginatedResponse.Companion.PAGE_SIZE_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication
import com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATIONS_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication.APPLICATION_ICON_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication.DESCRIPTION_KEY
import com.tecknobit.ametistacore.models.AmetistaApplication.PLATFORMS_KEY
import com.tecknobit.ametistacore.models.AmetistaMember
import com.tecknobit.ametistacore.models.AmetistaUser.ADMIN_CODE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.EMAIL_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.LANGUAGE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.MEMBERS_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.NAME_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.PASSWORD_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN
import com.tecknobit.ametistacore.models.AmetistaUser.SESSION_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.SURNAME_KEY
import com.tecknobit.ametistacore.models.Platform
import com.tecknobit.apimanager.apis.APIRequest.Params
import com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY
import com.tecknobit.apimanager.formatters.JsonHelper
import com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.SIGN_IN_ENDPOINT
import com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.SIGN_UP_ENDPOINT
import com.tecknobit.equinox.environment.helpers.EquinoxRequester
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File

class AmetistaRequester(
    host: String,
    userId: String?,
    userToken: String?
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    connectionTimeout = 2000,
    enableCertificatesValidation = true,
    debugMode = true,
    connectionErrorMessage = "No connection" // TODO: TO SET 
) {

    fun adminSignUp(
        adminCode: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(ADMIN_CODE_KEY, adminCode)
        payload.addParam(NAME_KEY, name)
        payload.addParam(SURNAME_KEY, surname)
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        payload.addParam(LANGUAGE_KEY, language)
        payload.addParam(ROLE_KEY, ADMIN)
        return execPost(
            endpoint = SIGN_UP_ENDPOINT,
            payload = payload
        )
    }

    fun adminSignIn(
        adminCode: String,
        email: String,
        password: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(ADMIN_CODE_KEY, adminCode)
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        return execPost(
            endpoint = SIGN_IN_ENDPOINT,
            payload = payload
        )
    }

    fun viewerSignIn(
        serverSecret: String,
        email: String,
        password: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(SERVER_SECRET_KEY, serverSecret)
        payload.addParam(EMAIL_KEY, email)
        payload.addParam(PASSWORD_KEY, password)
        return execPost(
            endpoint = SIGN_IN_ENDPOINT,
            payload = payload
        )
    }

    fun getSessionMembers(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE
    ): JSONObject {
        val query = createPaginationQuery(
            page = page,
            pageSize = pageSize
        )
        return execGet(
            endpoint = assembleSessionEndpoint(
                query = query.createQueryString()
            )
        )
    }

    fun addViewer(
        name: String,
        surname: String,
        email: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(NAME_KEY, name)
        payload.addParam(SURNAME_KEY, surname)
        payload.addParam(EMAIL_KEY, email)
        return execPost(
            endpoint = assembleSessionEndpoint(),
            payload = payload
        )
    }

    fun removeMember(
        member: AmetistaMember
    ): JSONObject {
        return execDelete(
            endpoint = assembleSessionEndpoint(
                subEndpoint = member.id
            )
        )
    }

    fun changeViewerPresetPassword(
        password: String
    ): JSONObject {
        val payload = Params()
        payload.addParam(PASSWORD_KEY, password)
        return execPatch(
            endpoint = assembleUsersEndpointPath(
                endpoint = CHANGE_PRESET_PASSWORD_ENDPOINT
            ),
            payload = payload
        )
    }

    private fun assembleSessionEndpoint(
        subEndpoint: String = "",
        query: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = "/$SESSION_KEY/$MEMBERS_KEY",
            subEndpoint = subEndpoint,
            query = query
        )
    }

    fun getApplications(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        name: String = "",
        platforms: List<Platform> = emptyList()
    ): JSONObject {
        val platformsFormatter = StringBuilder()
        if (platforms.isNotEmpty()) {
            platforms.forEach { platform: Platform ->
                platformsFormatter.append(platform.name).append(",")
            }
            platformsFormatter.deleteAt(platformsFormatter.lastIndex)
        }
        val query = createPaginationQuery(
            page = page,
            pageSize = pageSize
        )
        query.addParam(NAME_KEY, name)
        query.addParam(PLATFORMS_KEY, platformsFormatter.toString())
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                query = query.createQueryString()
            )
        )
    }

    fun addApplication(
        icon: String,
        name: String,
        description: String
    ): JSONObject {
        val body = createApplicationPayload(
            icon = icon,
            name = name,
            description = description
        )
        return execMultipartRequest(
            endpoint = assembleApplicationsEndpoint(),
            body = body
        )
    }

    fun editApplication(
        application: AmetistaApplication,
        icon: String,
        name: String,
        description: String
    ): JSONObject {
        println(icon)
        println(application.icon)
        val body = createApplicationPayload(
            icon = if (icon != application.icon)
                icon
            else
                null,
            name = name,
            description = description
        )
        return execMultipartRequest(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = application.id
            ),
            body = body
        )
    }

    private fun createApplicationPayload(
        icon: String?,
        name: String,
        description: String
    ): MultipartBody {
        val payload = MultipartBody.Builder()
        if (icon != null) {
            val iconFile = File(icon)
            payload.addFormDataPart(
                name = APPLICATION_ICON_KEY,
                filename = iconFile.name,
                body = iconFile.readBytes().toRequestBody("*/*".toMediaType())
            )
        }
        payload.addFormDataPart(
            name = NAME_KEY,
            value = name
        )
        payload.addFormDataPart(
            name = DESCRIPTION_KEY,
            value = description
        )
        return payload.build()
    }

    fun deleteApplication(
        application: AmetistaApplication
    ): JSONObject {
        return execDelete(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = application.id
            )
        )
    }

    private fun assembleApplicationsEndpoint(
        subEndpoint: String = "",
        query: String = ""
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = "/$APPLICATIONS_KEY",
            subEndpoint = subEndpoint,
            query = query
        )
    }

    fun <T> sendPaginatedRequest(
        request: AmetistaRequester.() -> JSONObject,
        supplier: (JSONObject) -> T,
        onSuccess: (PaginatedResponse<T>) -> Unit,
        onFailure: (JsonHelper) -> Unit,
        onConnectionError: ((JsonHelper) -> Unit)? = null
    ) {
        sendRequest(
            request = { request.invoke(this) },
            onSuccess = { responsePage ->
                onSuccess.invoke(
                    PaginatedResponse(
                        hPage = responsePage,
                        supplier = supplier
                    )
                )
            },
            onFailure = onFailure,
            onConnectionError = onConnectionError
        )
    }

    protected fun createPaginationQuery(
        page: Int,
        pageSize: Int,
    ): Params {
        val query = Params()
        query.addParam(PAGE_KEY, page.toString())
        query.addParam(PAGE_SIZE_KEY, pageSize.toString())
        return query
    }

}