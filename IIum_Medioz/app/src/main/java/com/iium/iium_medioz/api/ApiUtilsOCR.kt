package com.iium.iium_medioz.api

import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.OCR_URL

object ApiUtilsOCR {
    val ocrapi: OCRAPI
        get() = RetrofitClient.getClient(OCR_URL).create(OCRAPI::class.java)
}
