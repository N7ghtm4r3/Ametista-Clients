package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.ADMIN_CODE_KEY
import com.tecknobit.ametistacore.APPLICATIONS_KEY
import com.tecknobit.ametistacore.APPLICATION_ICON_KEY
import com.tecknobit.ametistacore.DESCRIPTION_KEY
import com.tecknobit.ametistacore.FILTERS_KEY
import com.tecknobit.ametistacore.ISSUES_KEY
import com.tecknobit.ametistacore.MEMBERS_KEY
import com.tecknobit.ametistacore.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistacore.PLATFORMS_KEY
import com.tecknobit.ametistacore.PLATFORM_KEY
import com.tecknobit.ametistacore.ROLE_KEY
import com.tecknobit.ametistacore.SESSION_KEY
import com.tecknobit.ametistacore.VERSION_FILTERS_KEY
import com.tecknobit.ametistacore.enums.PerformanceAnalyticType
import com.tecknobit.ametistacore.enums.Platform
import com.tecknobit.ametistacore.enums.Role
import com.tecknobit.ametistacore.enums.Role.ADMIN
import com.tecknobit.ametistacore.helpers.AmetistaEndpointsSet.CHANGE_PRESET_PASSWORD_ENDPOINT
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcore.annotations.Assembler
import com.tecknobit.equinoxcore.annotations.RequestPath
import com.tecknobit.equinoxcore.helpers.EMAIL_KEY
import com.tecknobit.equinoxcore.helpers.LANGUAGE_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.helpers.PASSWORD_KEY
import com.tecknobit.equinoxcore.helpers.SERVER_SECRET_KEY
import com.tecknobit.equinoxcore.helpers.SURNAME_KEY
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.SIGN_IN_ENDPOINT
import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.SIGN_UP_ENDPOINT
import com.tecknobit.equinoxcore.network.RequestMethod.*
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_KEY
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.PAGE_SIZE_KEY
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.content.PartData
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * The `AmetistaRequester` class is useful to communicate with the Ametista's backend
 *
 * @param host The host where is running the Nova's backend
 * @param userId The user identifier
 * @param userToken The user token
 * @param debugMode Whether the requester is still in development and who is developing needs the log of the requester's
 * workflow, if it is enabled all the details of the requests sent and the errors occurred will be printed in the console
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxRequester
 */
class AmetistaRequester(
    host: String,
    userId: String?,
    userToken: String?,
    debugMode: Boolean = false
) : EquinoxRequester(
    host = host,
    userId = userId,
    userToken = userToken,
    connectionTimeout = 2000,
    byPassSSLValidation = true,
    debugMode = debugMode,
    connectionErrorMessage = "No internet connection"
) {

    /**
     * Method to sign-up as an [ADMIN]
     *
     * @param adminCode The admin code Ametista's backend
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param password The password of the user
     * @param language The language of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
    suspend fun adminSignUp(
        adminCode: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(ADMIN_CODE_KEY, adminCode)
            put(NAME_KEY, name)
            put(SURNAME_KEY, surname)
            put(EMAIL_KEY, email)
            put(PASSWORD_KEY, password)
            put(LANGUAGE_KEY, language)
            put(ROLE_KEY, ADMIN.name)
        }
        return execPost(
            endpoint = SIGN_UP_ENDPOINT,
            payload = payload
        )
    }

    /**
     * Method to sign-in as an [ADMIN]
     *
     * @param adminCode The admin code Ametista's backend
     * @param email The email of the user
     * @param password The password of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
    suspend fun adminSignIn(
        adminCode: String,
        email: String,
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(ADMIN_CODE_KEY, adminCode)
            put(EMAIL_KEY, email)
            put(PASSWORD_KEY, password)
        }
        return execPost(
            endpoint = SIGN_IN_ENDPOINT,
            payload = payload
        )
    }

    /**
     * Method to sign-in as an [ADMIN]
     *
     * @param serverSecret The secret of the personal Ametista's backend
     * @param email The email of the user
     * @param password The password of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
    suspend fun viewerSignIn(
        serverSecret: String,
        email: String,
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(SERVER_SECRET_KEY, serverSecret)
            put(EMAIL_KEY, email)
            put(PASSWORD_KEY, password)
        }
        return execPost(
            endpoint = SIGN_IN_ENDPOINT,
            payload = payload
        )
    }

    /**
     * Method to get the members registered in the current session
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     */
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = GET)
    suspend fun getSessionMembers(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
    ): JsonObject {
        val query = createPaginationQuery(
            page = page,
            pageSize = pageSize
        )
        return execGet(
            endpoint = assembleSessionEndpoint(),
            query = query
        )
    }

    /**
     * Method to add a new [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] in the system
     *
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = POST)
    suspend fun addViewer(
        name: String,
        surname: String,
        email: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(NAME_KEY, name)
            put(SURNAME_KEY, surname)
            put(EMAIL_KEY, email)
        }
        return execPost(
            endpoint = assembleSessionEndpoint(),
            payload = payload
        )
    }

    /**
     * Method to remove an [com.tecknobit.ametistacore.models.AmetistaMember] from the system
     *
     * @param member The member to remove
     *
     * @return the result of the request as [JsonObject]
     *
     */
    // TODO: TO EDIT 
    /*@RequestPath(path = "/api/v1/users/{user_id}/session/members/{member_id}", method = DELETE)
    suspend fun removeMember(
        member: AmetistaMember,
    ): JsonObject {
        return execDelete(
            endpoint = assembleSessionEndpoint(
                subEndpoint = member.id
            )
        )
    }*/

    /**
     * Method to change the preset password by a [com.tecknobit.ametistacore.enums.Role.VIEWER]
     *
     * @param password The password choose by the [com.tecknobit.ametistacore.enums.Role.VIEWER]
     *
     * @return the result of the request as [JsonObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/changePresetPassword", method = PATCH)
    suspend fun changeViewerPresetPassword(
        password: String,
    ): JsonObject {
        val payload = buildJsonObject {
            put(PASSWORD_KEY, password)
        }
        return execPatch(
            endpoint = assembleUsersEndpointPath(
                endpoint = CHANGE_PRESET_PASSWORD_ENDPOINT
            ),
            payload = payload
        )
    }

    /**
     * Method to assemble the endpoint to make the request to the users controller
     *
     * @param subEndpoint The endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleSessionEndpoint(
        subEndpoint: String = "",
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = "/$SESSION_KEY/$MEMBERS_KEY",
            subEndpoint = subEndpoint
        )
    }

    /**
     * Method to get the applications list
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param name The name to use as filter
     * @param platforms The platforms to use as filter
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications", method = GET)
    suspend fun getApplications(
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        name: String = "",
        platforms: List<Platform> = emptyList(),
    ): JsonObject {
        val platformsFormatter = StringBuilder()
        if (platforms.isNotEmpty()) {
            platforms.forEach { platform: Platform ->
                platformsFormatter.append(platform.name).append(",")
            }
            platformsFormatter.deleteAt(platformsFormatter.lastIndex)
        }
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(NAME_KEY, name)
            put(PLATFORMS_KEY, platformsFormatter.toString())
        }
        return execGet(
            endpoint = assembleApplicationsEndpoint(),
            query = query
        )
    }

    /**
     * Method to add a new application to the system
     *
     * @param iconBytes The bytes of the icon of the application
     * @param iconName The name of the icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications", method = POST)
    suspend fun addApplication(
        iconBytes: ByteArray?,
        iconName: String?,
        name: String,
        description: String,
    ): JsonObject {
        val payload = createApplicationPayload(
            iconBytes = iconBytes,
            iconName = iconName,
            name = name,
            description = description
        )
        return execMultipartRequest(
            endpoint = assembleApplicationsEndpoint(),
            payload = payload
        )
    }

    /**
     * Method to edit an existing application
     *
     * @param iconBytes The bytes of the icon of the application
     * @param iconName The name of the icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return an endpoint to make the request as [String]
     */
    // TODO: TO EDIT 
    /*@RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = POST)
    suspend fun editApplication(
        application: AmetistaApplication,
        iconBytes: ByteArray?,
        iconName: String?,
        name: String,
        description: String,
    ): JsonObject {
        val payload = createApplicationPayload(
            iconBytes = iconBytes,
            iconName = iconName,
            name = name,
            description = description
        )
        return execMultipartRequest(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = application.id
            ),
            payload = payload
        )
    }*/

    /**
     * Method to create the payload for the [addApplication] or [editApplication] requests
     *
     * @param iconBytes The bytes of the icon of the application
     * @param iconName The name of the icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return the application payload as [List] of [PartData]
     */
    @Assembler
    private fun createApplicationPayload(
        iconBytes: ByteArray?,
        iconName: String?,
        name: String,
        description: String,
    ): List<PartData> {
        return formData {
            iconBytes?.let {
                append(APPLICATION_ICON_KEY, iconBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/*")
                    append(HttpHeaders.ContentDisposition, "filename=\"$iconName\"")
                })
            }
            append(NAME_KEY, name)
            append(DESCRIPTION_KEY, description)
        }
    }

    /**
     * Method to get an existing application
     *
     * @param applicationId The identifier of the application to get
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = GET)
    suspend fun getApplication(
        applicationId: String,
    ): JsonObject {
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = applicationId
            )
        )
    }

    /**
     * Method to get the issues list
     *
     * @param applicationId The identifier of the application from fetch the related issues list
     * @param platform The platform from fetch the issues
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     * @param filters The filters value to filter the issues selection
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/applications/{application_id}/issues",
        queryParameters = "?platform={platform}",
        method = GET
    )
    suspend fun getIssues(
        applicationId: String,
        platform: Platform,
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: HashSet<String> = HashSet(),
    ): JsonObject {
        val filtersFormatter = StringBuilder()
        if (filters.isNotEmpty()) {
            filters.forEach { filter: String ->
                filtersFormatter.append(filter).append(",")
            }
            filtersFormatter.deleteAt(filtersFormatter.lastIndex)
        }
        val query = buildJsonObject {
            put(PAGE_KEY, page)
            put(PAGE_SIZE_KEY, pageSize)
            put(PLATFORM_KEY, platform.name)
            put(FILTERS_KEY, filtersFormatter.toString())
        }
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$ISSUES_KEY"
            ),
            query = query
        )
    }

    /**
     * Method to get the performance data of an [AmetistaApplication]
     *
     * @param applicationId The identifier of the application from fetch the related performance data
     * @param platform The platform from fetch the performance data
     * @param performanceDataFilters The filters value to use to filter the data
     *
     * @return an endpoint to make the request as [String]
     *//*
    @RequestPath(
        path = "/api/v1/users/{user_id}/applications/{application_id}/performance",
        queryParameters = "?platform={platform}",
        method = POST
    )
    // TODO: TO SET 
    suspend fun getPerformanceData(
        applicationId: String,
        platform: Platform,
        performanceDataFilters: PerformanceDataFilters,
    ): JsonObject {
        val query = buildJsonObject {
            put(PLATFORM_KEY, platform.name)
        }
        val payload = buildJsonObject {
            put(FILTERS_KEY, performanceDataFilters.toPayload())
        }
        return execPost(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$PERFORMANCES_KEY"
            ),
            query = query,
            payload = payload
        )
    }*/

    /**
     * Method to get the version samples for each analytic
     *
     * @param applicationId The identifier of the application from fetch the version samples
     * @param platform The platform from fetch the version samples
     * @param analyticType The type of the analytic from fetch the version samples
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/applications/{application_id}/versions",
        queryParameters = "?platform={platform}&performance_analytic_type={performance_analytic_type}",
        method = GET
    )
    suspend fun getVersionSamples(
        applicationId: String,
        platform: Platform,
        analyticType: PerformanceAnalyticType,
    ): JsonObject {
        val query = buildJsonObject {
            put(PLATFORM_KEY, platform.name)
            put(PERFORMANCE_ANALYTIC_TYPE_KEY, analyticType.name)
        }
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$VERSION_FILTERS_KEY"
            ),
            query = query
        )
    }

    /**
     * Method to delete an [AmetistaApplication]
     *
     * @param application The application to delete
     *
     * @return an endpoint to make the request as [String]
     */
    // TODO: TO SET 
    /*@RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = DELETE)
    suspend fun deleteApplication(
        application: AmetistaApplication,
    ): JsonObject {
        return execDelete(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = application.id
            )
        )
    }*/

    /**
     * Method to assemble the endpoint to make the request to the applications controller
     *
     * @param subEndpoint The endpoint path of the url
     *
     * @return an endpoint to make the request as [String]
     */
    @Assembler
    private fun assembleApplicationsEndpoint(
        subEndpoint: String = "",
    ): String {
        return assembleCustomEndpointPath(
            customEndpoint = "/$APPLICATIONS_KEY",
            subEndpoint = subEndpoint
        )
    }

}