package com.iium.iium_medioz.util.calendar

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginReq(
    val uuid: String
)

data class LoginRes(
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String,
    @SerializedName("message") val message: String
)

interface AuthAPI {
    @POST("auth/login")
    suspend fun login(@Body body: LoginReq): LoginRes
}


@Parcelize
data class User(
    @SerializedName("id") val id: Int, @SerializedName("nickname") val nickname: String
) : Parcelable