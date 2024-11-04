package com.tecknobit.ametista.helpers

import com.tecknobit.ametistacore.models.AmetistaUser.ADMIN_CODE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.EMAIL_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.LANGUAGE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.NAME_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.PASSWORD_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.ROLE_KEY
import com.tecknobit.ametistacore.models.AmetistaUser.Role.ADMIN
import com.tecknobit.ametistacore.models.AmetistaUser.SURNAME_KEY
import com.tecknobit.apimanager.apis.APIRequest.Params
import com.tecknobit.apimanager.apis.ServerProtector.SERVER_SECRET_KEY
import com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.SIGN_IN_ENDPOINT
import com.tecknobit.equinox.environment.helpers.EquinoxBaseEndpointsSet.SIGN_UP_ENDPOINT
import com.tecknobit.equinox.environment.helpers.EquinoxRequester
import org.json.JSONObject

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


}