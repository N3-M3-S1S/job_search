package com.nemesis.jobsearch.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

internal class JobSearchApi(private val ktorClient: HttpClient) {
    private val jobsApiUrl =
        "https://drive.usercontent.google.com/u/0/uc?id=1z4TbeDkbfXkvgpoJprXbN85uCcD7f00r&export=download"

    suspend fun query(): JobSearchApiResponse {
        val response = ktorClient.get(jobsApiUrl)
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            throw JobSearchApiException(message = response.status.description)
        }
    }

}