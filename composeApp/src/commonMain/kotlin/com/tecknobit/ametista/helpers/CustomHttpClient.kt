package com.tecknobit.ametista.helpers

import io.ktor.client.HttpClient

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.ametista.imageLoader] instance
 */
expect fun customHttpClient(): HttpClient