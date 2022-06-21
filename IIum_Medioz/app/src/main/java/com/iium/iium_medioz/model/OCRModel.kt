package com.iium.iium_medioz.model

import java.lang.reflect.Array

data class OCRModel (
    val version: String? = null,
    val requestId: String? = null,
    val timestamp: String? = null,
    val images: Array? = null,
)