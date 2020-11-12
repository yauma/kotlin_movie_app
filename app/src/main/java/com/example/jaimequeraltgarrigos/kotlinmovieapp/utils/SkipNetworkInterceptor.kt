package com.example.jaimequeraltgarrigos.kotlinmovieapp.utils

import com.example.jaimequeraltgarrigos.kotlinmovieapp.network.MovieNetworkEntity
import com.google.gson.Gson
import okhttp3.*

/**
 * A list of fake results to return.
 */
private val FAKE_RESULTS = listOf(
    MovieNetworkEntity("Suicide Squad", "/86L8wqGMDbwURPni2t7FQ0nDjsH.jpg", 1),
    MovieNetworkEntity("Jason Bourne", "/4lWr2j3ZSEe8qlt3W3ma8TiiMQB.jpg", 2),
    MovieNetworkEntity("Now You See Me 2", "/wu1uilmhM4TdluKi2ytfz8gidHf.jpg", 3),
    MovieNetworkEntity("The Boy Next Door", "/xoqr4dMbRJnzuhsWDF3XNHQwJ9x.jpg", 4),
    MovieNetworkEntity("The Jungle Book", "/5UkzNSOK561c2QRy2Zr4AkADzLT.jpg", 5)
)

/**
 * This class will return fake [Response] objects to Retrofit, without actually using the network.
 */
class SkipNetworkInterceptor : Interceptor {
    private val gson = Gson()

    private var attempts = 0

    /**
     * Return true iff this request should error.
     */
    private fun wantRandomError() = attempts++ % 5 == 0

    /**
     * Stop the request from actually going out to the network.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()
        return if (wantRandomError()) {
            makeErrorResult(chain.request())
            //makeOkResult(chain.request())
        } else {
            makeOkResult(chain.request())
        }
    }

    /**
     * Pretend to "block" interacting with the network.
     *
     * Really: sleep for 500ms.
     */
    private fun pretendToBlockForNetworkRequest() = Thread.sleep(500)

    /**
     * Generate an error result.
     *
     * ```
     * HTTP/1.1 500 Bad server day
     * Content-type: application/json
     *
     * {"cause": "not sure"}
     * ```
     */
    private fun makeErrorResult(request: Request): Response {
        return Response.Builder()
            .code(500)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("Bad server day")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(mapOf("cause" to "not sure"))
                )
            )
            .build()
    }

    /**
     * Generate a success response.
     *
     * ```
     * HTTP/1.1 200 OK
     * Content-type: application/json
     *
     * "$random_string"
     * ```
     */
    private fun makeOkResult(request: Request): Response {
        val nextResult = FAKE_RESULTS
        return Response.Builder()
            .code(200)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                ResponseBody.create(
                    MediaType.get("application/json"),
                    gson.toJson(nextResult)
                )
            )
            .build()
    }
}