package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.iium.iium_medioz.databinding.FragmentAddressDialogBinding
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPX
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPY
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.*

class AddressDialog : DialogFragment() {

    private var _binding: FragmentAddressDialogBinding? = null
    private val binding get() = _binding!!
    val clinent_id = "BQwUhf6Ll2GKA8WIPgkj"
    val client_secret = "hIT8qlMzxS"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddressDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        // 레이아웃 배경을 투명하게 해줌, 필수 아님
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
        return view
    }

    private fun initView() {
        binding.okBtn.setOnClickListener {
           Thread {
               initAPI(binding.etSearch.text.toString())
           }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClearClick(v: View) {
        binding.etSearch.text = null
    }

    private fun initAPI(string: String) {

        val text: String?
        try {
            text = URLEncoder.encode(string, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }

        val apiURL = "https://openapi.naver.com/v1/search/local?query=" + text!!    // json 결과
        val requestHeaders : HashMap<String, String> = HashMap()
        requestHeaders["X-Naver-Client-Id"] = clinent_id
        requestHeaders["X-Naver-Client-Secret"] = client_secret
        val responseBody = get(apiURL, requestHeaders)

        parseData(responseBody)

    }

    private operator fun get(apiUrl: String, requestHeaders: Map<String, String>): String {
        val con = connect(apiUrl)
        try {
            con.requestMethod = "GET"
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }

            val responseCode = con.responseCode
            return if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                readBody(con.inputStream)
            } else { // 에러 발생
                readBody(con.errorStream)
            }

        } catch (e: IOException) {
            throw RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }

    private fun connect(apiUrl: String): HttpURLConnection {
        try {
            val url = URL(apiUrl)
            return url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }

    }

    private fun readBody(body: InputStream): String {
        val streamReader = InputStreamReader(body)

        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()

                var line: String? = lineReader.readLine()
                while (line != null) {
                    responseBody.append(line)
                    line = lineReader.readLine()
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }

    private fun parseData(responseBody: String) {
        var mapx: String?
        var mapy: String?
        val jsonObject: JSONObject?

        try {
            jsonObject = JSONObject(responseBody)
            val jsonArray = jsonObject.getJSONArray("items")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                mapx = item.getString("mapx")
                mapy = item.getString("mapy")
                println("TITLE : $mapx,$mapy")

                val intent = Intent(context,AddressActivity::class.java)
                intent.putExtra(NAVER_MAPX, mapx)
                intent.putExtra(NAVER_MAPY, mapy)
                startActivity(intent)
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}