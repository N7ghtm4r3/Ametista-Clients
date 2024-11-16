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
import com.tecknobit.ametistacore.models.AmetistaApplication.FILTERS_KEY
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
import com.tecknobit.ametistacore.models.analytics.AmetistaAnalytic.PLATFORM_KEY
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.ISSUES_KEY
import com.tecknobit.ametistacore.models.analytics.issues.IssueAnalytic.VERSION_FILTERS_KEY
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCES_KEY
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PERFORMANCE_ANALYTIC_TYPE_KEY
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceAnalytic.PerformanceAnalyticType
import com.tecknobit.ametistacore.models.analytics.performance.PerformanceDataFilters
import com.tecknobit.apimanager.annotations.RequestPath
import com.tecknobit.apimanager.apis.APIRequest.Params
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.DELETE
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.GET
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.PATCH
import com.tecknobit.apimanager.apis.APIRequest.RequestMethod.POST
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

/**
 * The **AmetistaRequester** class is useful to communicate with the Ametista's backend
 *
 * @param host: the host where is running the Nova's backend
 * @param userId: the user identifier
 * @param userToken: the user token
 * @param debugMode: whether the requester is still in development and who is developing needs the log of the requester's
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
    enableCertificatesValidation = true,
    debugMode = debugMode,
    connectionErrorMessage = "No internet connection"
) {

    /**
     * Method to sign-up as an [ADMIN]
     *
     * @param adminCode: the admin code Ametista's backend
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param email: the email of the user
     * @param password: the password of the user
     * @param language: the language of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signUp", method = POST)
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

    /**
     * Method to sign-in as an [ADMIN]
     *
     * @param adminCode: the admin code Ametista's backend
     * @param email: the email of the user
     * @param password: the password of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
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

    /**
     * Method to sign-in as an [ADMIN]
     *
     * @param serverSecret: the secret of the personal Ametista's backend
     * @param email: the email of the user
     * @param password: the password of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/signIn", method = POST)
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

    /**
     * Method to get the members registered in the current session
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     */
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = GET)
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

    /**
     * Method to add a new [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER] in the system
     *
     * @param name: the name of the user
     * @param surname: the surname of the user
     * @param email: the email of the user
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/session/members", method = POST)
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

    /**
     * Method to remove an [com.tecknobit.ametistacore.models.AmetistaMember] from the system
     *
     * @param member: the member to remove
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/session/members/{member_id}", method = DELETE)
    fun removeMember(
        member: AmetistaMember
    ): JSONObject {
        return execDelete(
            endpoint = assembleSessionEndpoint(
                subEndpoint = member.id
            )
        )
    }

    /**
     * Method to change the preset password by a [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER]
     *
     * @param password: the password choose by the [com.tecknobit.ametistacore.models.AmetistaUser.Role.VIEWER]
     *
     * @return the result of the request as [JSONObject]
     *
     */
    @RequestPath(path = "/api/v1/users/{user_id}/changePresetPassword", method = PATCH)
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

    /**
     * Method to assemble the endpoint to make the request to the users controller
     *
     * @param subEndpoint The endpoint path of the url
     * @param query The query parameters
     *
     * @return an endpoint to make the request as [String]
     */
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

    /**
     * Method to add a new [AmetistaApplication] to the system
     *
     * @param icon The icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications", method = POST)
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

    /**
     * Method to edit an existing [AmetistaApplication]
     *
     * @param application The application to edit
     * @param icon The icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = POST)
    fun editApplication(
        application: AmetistaApplication,
        icon: String,
        name: String,
        description: String
    ): JSONObject {
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

    /**
     * Method to create the payload for the [addApplication] or [editApplication] requests
     *
     * @param icon The icon of the application
     * @param name The name of the application
     * @param description The description of the application
     *
     * @return the application payload as [MultipartBody]
     */
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

    /**
     * Method to get an existing [AmetistaApplication]
     *
     * @param applicationId The identifier of the application to get
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = GET)
    fun getApplication(
        applicationId: String
    ): JSONObject {
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
        query_parameters = "?platform={platform}",
        method = GET
    )
    fun getIssues(
        applicationId: String,
        platform: Platform,
        page: Int = DEFAULT_PAGE,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        filters: HashSet<String> = HashSet()
    ): JSONObject {
        val filtersFormatter = StringBuilder()
        if (filters.isNotEmpty()) {
            filters.forEach { filter: String ->
                filtersFormatter.append(filter).append(",")
            }
            filtersFormatter.deleteAt(filtersFormatter.lastIndex)
        }
        val query = createPaginationQuery(
            page = page,
            pageSize = pageSize
        )
        query.addParam(PLATFORM_KEY, platform.name)
        query.addParam(FILTERS_KEY, filtersFormatter.toString())
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$ISSUES_KEY",
                query = query.createQueryString()
            )
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
     */
    @RequestPath(
        path = "/api/v1/users/{user_id}/applications/{application_id}/performance",
        query_parameters = "?platform={platform}",
        method = POST
    )
    fun getPerformanceData(
        applicationId: String,
        platform: Platform,
        performanceDataFilters: PerformanceDataFilters
    ): JSONObject {
        val query = Params()
        query.addParam(PLATFORM_KEY, platform.name)
        val payload = Params()
        payload.addParam(FILTERS_KEY, performanceDataFilters.toPayload())
        return execPost(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$PERFORMANCES_KEY",
                query = query.createQueryString()
            ),
            payload = payload
        )
    }

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
        query_parameters = "?platform={platform}&performance_analytic_type={performance_analytic_type}",
        method = GET
    )
    fun getVersionSamples(
        applicationId: String,
        platform: Platform,
        analyticType: PerformanceAnalyticType
    ): JSONObject {
        val query = Params()
        query.addParam(PLATFORM_KEY, platform.name)
        query.addParam(PERFORMANCE_ANALYTIC_TYPE_KEY, analyticType.name)
        return execGet(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = "$applicationId/$VERSION_FILTERS_KEY",
                query = query.createQueryString()
            )
        )
    }

    /**
     * Method to delete an [AmetistaApplication]
     *
     * @param application The application to delete
     *
     * @return an endpoint to make the request as [String]
     */
    @RequestPath(path = "/api/v1/users/{user_id}/applications/{application_id}", method = DELETE)
    fun deleteApplication(
        application: AmetistaApplication
    ): JSONObject {
        return execDelete(
            endpoint = assembleApplicationsEndpoint(
                subEndpoint = application.id
            )
        )
    }

    /**
     * Method to assemble the endpoint to make the request to the applications controller
     *
     * @param subEndpoint The endpoint path of the url
     * @param query The query parameters
     *
     * @return an endpoint to make the request as [String]
     */
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

    /**
     * Method to execute and manage the paginated response of a request
     *
     * @param request The request to execute
     * @param supplier The supplier function to instantiate a [T] item
     * @param onSuccess The action to execute if the request has been successful
     * @param onFailure The action to execute if the request has been failed
     * @param onConnectionError The action to execute if the request has been failed for a connection error
     *
     * @param T generic type of the items in the page response
     */
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

    /**
     * Method to create the query with the pagination parameters
     *
     * @param page The number of the page to request to the backend
     * @param pageSize The size of the result for the page
     *
     * @return the paginated query as [Params]
     */
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