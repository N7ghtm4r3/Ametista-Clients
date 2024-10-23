package com.tecknobit.ametista.helpers

import com.tecknobit.equinox.environment.helpers.EquinoxRequester

class AmetistaRequester(
    host: String
) : EquinoxRequester(
    host = host,
    connectionErrorMessage = "No connection" // TODO: TO SET 
) {
}