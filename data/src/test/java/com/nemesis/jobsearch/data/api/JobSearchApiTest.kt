package com.nemesis.jobsearch.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File


class JobSearchApiTest {
    private val mockEngine = MockEngine {
        respond(
            content = File("src/test/resources/api_response.json").readText(),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/octet-stream")
        )
    }

    @Test
    fun query() = runBlocking {
        val api = JobSearchApi(HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json = Json {
                    prettyPrint = true
                    isLenient = true
                }, contentType = ContentType.Any)
            }
        })

        val result = runCatching { api.query() }

        assertTrue(result.isSuccess)

        val successResult = result.getOrThrow()

        assertEquals(3, successResult.offers.size)

        assertEquals(6, successResult.vacancies.size)

        val firstOffer = successResult.offers.first()
        assertEquals(firstOffer.id, "near_vacancies")
        assertEquals(firstOffer.title, "Вакансии рядом с вами")
        assertEquals(firstOffer.link, "https://hh.ru/")

        val firstVacancy = successResult.vacancies.first()
        assertEquals(firstVacancy.id, "cbf0c984-7c6c-4ada-82da-e29dc698bb50")
        assertEquals(firstVacancy.lookingNumber, 2)
        assertEquals(firstVacancy.address.town, "Минск")
        assertEquals(firstVacancy.experience.previewText, "Опыт от 1 до 3 лет")
        assertFalse(firstVacancy.isFavorite)
    }

}