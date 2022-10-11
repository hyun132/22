package com.iium.iium_medioz.util.calendar

import com.google.gson.GsonBuilder
import com.iium.iium_medioz.util.`object`.Constant.BASE_URL
import com.iium.iium_medioz.util.log.debugE
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.reflect.KClass

const val USER_ID_PATH_SEGMENT = "__USER_ID_PATH_SEGMENT__"

class ApiFactory @Inject constructor(private val uniqueId: UniqueIdentifier) {
    private val gson = GsonBuilder().create()
    private lateinit var okHttpClient: OkHttpClient

    init {
        configureOkHttpClient()
    }

    private fun configureOkHttpClient() {
        val builder = OkHttpClient.Builder().addInterceptor {
            val baseRequest = it.request()
            val builder = baseRequest.newBuilder()
            val newRequest = builder.url(baseRequest.url.fillUserId()).addHeaders(uniqueId.loadToken())

            try {
                var response = it.proceed(newRequest.build())

                if (response.code == 401 && uniqueId.id != null) {
                    kotlin.runCatching {
                        response.body?.close()
                        val loginRequestBody = gson.toJson(LoginReq(uniqueId.id!!))
                        val loginRequest = newRequest.build().newBuilder().url("$BASE_URL/auth/login")
                            .post(RequestBody.create(MEDIA_TYPE.toMediaTypeOrNull(), loginRequestBody))
                            .removeHeader(HEADER_TOKEN).build()
                        val loginResponse =
                            gson.fromJson(it.proceed(loginRequest).body!!.string(), LoginRes::class.java)
                        uniqueId.saveToken(loginResponse.token)
                        it.proceed(newRequest.removeHeader(HEADER_TOKEN).addHeaders(loginResponse.token).build())
                    }.onSuccess {
                        response = it
                    }.onFailure {
                        debugE("토큰 갱신 실패")
                    }
                }


                response
            } catch (e: Throwable) {
                Response.Builder().code(500).protocol(Protocol.HTTP_1_1).message("Internal Server Error")
                    .request(newRequest.build()).body(ResponseBody.create(null, "{${e}}")).build()
            }
        }
    }

    private fun HttpUrl.fillUserId() = if (toString().contains(USER_ID_PATH_SEGMENT)) {
        val idx = pathSegments.indexOf(USER_ID_PATH_SEGMENT)
        newBuilder().setPathSegment(idx, uniqueId.userId.toString()).build()
    } else {
        this
    }

    private fun Request.Builder.addHeaders(token: String?): Request.Builder {
        addHeader("Content-Type", MEDIA_TYPE).addHeader("Accept", MEDIA_TYPE)
        token?.let {
            addHeader(HEADER_TOKEN, "${uniqueId.loadToken()}")
        }
        return this
    }

    private val apiRetrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
            .build()

    fun <T : Any> createApi(clazz: KClass<T>): T = apiRetrofit.create(clazz.java)

    companion object {
//        private const val BASE_URL = "http://13.125.46.238:8080"
        private const val MEDIA_TYPE = "application/json"
        private const val HEADER_TOKEN = "x-access-token"
    }
}