package com.iium.iium_medioz.model

import android.net.Uri
import retrofit2.http.Url

data class NaverOcrModel (
    val version: String? = null,
    val requestId: String? = null,
    val timestamp: String? = null,
    val lang: String? = null,
    val images: OcrImages
)

data class OcrImages(
    val format: String? = null,
    val name: String? = null,
    val url: String? = null
)