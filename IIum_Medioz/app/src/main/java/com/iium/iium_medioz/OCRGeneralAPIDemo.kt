package com.iium.iium_medioz

import android.util.Log
import com.iium.iium_medioz.util.`object`.Constant.OCR_SECRET
import com.iium.iium_medioz.util.`object`.Constant.OCR_URL
import com.iium.iium_medioz.util.`object`.Constant.OCR_URL_IMG
import com.iium.iium_medioz.util.`object`.Constant.TAG
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.jvm.JvmStatic

object OCRGeneralAPIDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val apiURL = OCR_URL
        val secretKey = OCR_SECRET
        try {
            val url = URL(apiURL)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.useCaches = false
            con.doInput = true
            con.doOutput = true
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            con.setRequestProperty("X-OCR-SECRET", secretKey)
            val json = JSONObject()
            json.put("version", "V2")
            json.put("requestId", UUID.randomUUID().toString())
            json.put("timestamp", System.currentTimeMillis())
            val image = JSONObject()
            image.put("format", "jpg")
            image.put(
                "url", OCR_URL_IMG
            )
            image.put("name", "demo")
            val images = JSONArray()
            images.put(image)
            json.put("images", images)
            val postParams: String = json.toString()
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode: Int = con.responseCode
            val br: BufferedReader = if (responseCode == 200) {
                BufferedReader(InputStreamReader(con.inputStream))
            } else {
                BufferedReader(InputStreamReader(con.errorStream))
            }
            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            br.close()
            println(response)
            Log.d(TAG,"네이버 테스트 OCR -> $response")
        } catch (e: Exception) {
            println(e)
        }
    }
}