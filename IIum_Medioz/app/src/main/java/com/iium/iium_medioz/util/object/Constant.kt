package com.iium.iium_medioz.util.`object`

import android.Manifest
import android.os.Handler
import android.os.Looper
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds

object  Constant {
    const val TAG = "MEDIOZ"
    const val BASE_URL="http://49.50.161.198:8080/"
    const val OCR_URL="https://2ihmtg25yc.apigw.ntruss.com/custom/v1/16697/bca7590ad614a464052b8d2bc1fe99f16edeb31677cd0e7e4863c7611d01238b/general"

    const val PREF_KEY_USER_TOKEN = "PREF_KEY_USER_TOKEN"
    const val NAVER_API_URL = "https://openapi.naver.com/"
    const val KAKAO_API_URL = "https://dapi.kakao.com/"
    const val PREF_KEY_APP_TOKEN = "myAppToken"
    const val PREF_KEY_ENCTYPT_IV = "myEncryptIv"
    const val PREF_KEY_ENCTYPT_KEY = "myEncryptKey"
    const val PREF_KEY_AUTH_TOKEN ="myAuthToken"
    const val PREF_KEY_LANG = "myLang"
    const val PREF_KEY_LANG_CODE = "myLangCode"
    const val PREF_HASH_KEY_TOKEN = "myHashToken"
    const val PREF_PERMISSION_GRANTED = "PREF_PERMISSION_GRANTED"
    const val PREF_PHONE = "myphone"
    const val PREF_PW = "myPW"
    const val PREF_AUTO_LOGIN = "PREF_AUTO_LOGIN"
    const val PREF_KEY_LANGUAGE = "PREF_KEY_LANGUAGE"
    const val PREF_KEY_LANGUAGE_CODE = "PREF_KEY_LANGUAGE_CODE"
    const val PREF_SMS = "PREF_SMS"
    const val PREF_KEY_ENC_KEY = "PREF_KEY_ENC_KEY"
    const val PREF_KEY_ENC_IV = "PREF_KEY_ENC_IV"
    const val PREF_TERMS = "PREF_TERMS"
    const val PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN"
    const val PREF_NEW_ACCESS_TOKEN = "PREF_NEW_ACCESS_TOKEN"
    const val ONE_PERMISSION_REQUEST_CODE = 1
    const val SECOND_PERMISSION_REQUEST_CODE = 2
    const val THRID_PERMISSION_REQUEST_CODE = 3

    const val TABLE_NAME = "notification"


    const val ALL_PERMISSION_REQUEST_CODE = 2
    const val BACKPRESS_CLOSE_TIME = 1500
    const val DEFATULT_TIMEOUT = 20000

    const val CAMERA_REQUEST_CODE = 101
    const val VIDEO_REQUEST_CODE = 202
    const val ALBUM_REQUEST_CODE = 302
    const val PERM_STROAGE = 100

    const val GPS_ENABLE_REQUEST_CODE = 2001
    const val PERMISSION_REQUEST_CODE = 1000



    //????????? ??????
    const val LOGIN_PHONE = "LOGIN_PHONE"
    const val LOGIN_SEX = "LOGIN_SEX"

    //DATA ?????????
    const val DATA_TITLE = "DATA_TITLE"
    const val DATA_KEYWORD = "DATA_KEYWORD"
    const val DATA_TIMESTAMP = "DATA_TIMESTAMP"
    const val DATA_TEXTIMG = "DATA_TEXTIMG"
    const val DATA_NORMAL = "DATA_NORMAL"
    const val DATA_VIDEOFILE = "DATA_VIDEOFILE"
    const val DATA_ID = "DATA_ID"
    const val DATA_SEND_CODE = "DATA_SEND_CODE"
    const val DATA_DEFAULT_CODE = "DATA_DEFAULT_CODE"
    const val DATA_KEYWORD_SCORE = "DATA_KEYWORD_SCORE"
    const val DATA_PICK_SCORE = "DATA_PICK_SCORE"
    const val DATA_VIDEO_SCORE = "DATA_VIDEO_SCORE"
    const val DATA_SENSITIVITY_SCORE = "DATA_SENSITIVITY_SCORE"


    //Search ?????????
    const val SEARCH_TITLE = "SEARCH_TITLE"
    const val SEARCH_KEYWORD = "SEARCH_KEYWORD"
    const val SEARCH_TIME_STAMP = "SEARCH_TIME_STAMP"
    const val SEARCH_TEXTIMG = "SEARCH_TEXTIMG"
    const val SEARCH_NORMAL = "SEARCH_NORMAL"
    const val SEARCH_VIDEO = "SEARCH_VIDEO"
    const val SEARCH_ID = "SEARCH_ID"

    //?????? ?????????
    const val SEND_TITLE = "SEND_TITLE"
    const val SEND_KEYWORD = "SEND_KEYWORD"
    const val SEND_TIME_STAMP = "SEND_TIME_STAMP"
    const val SEND_TEXTIMG = "SEND_TEXTIMG"
    const val SEND_NORMAL = "SEND_NORMAL"
    const val SEND_VIDEO = "SEND_VIDEO"
    const val SEND_ID = "SEND_ID"

    const val SEND_DETAIL_TITLE = "SEND_DETAIL_TITLE"
    const val SEND_DETAIL_KEYWORD = "SEND_DETAIL_KEYWORD"
    const val SEND_DETAIL_TIME_STAMP = "SEND_DETAIL_TIME_STAMP"
    const val SEND_DETAIL_TEXTIMG = "SEND_DETAIL_TEXTIMG"
    const val SEND_DETAIL_NORMAL = "SEND_DETAIL_NORMAL"
    const val SEND_DETAIL_VIDEO = "SEND_DETAIL_VIDEO"
    const val SEND_DETAIL_ID = "SEND_DETAIL_ID"
    const val SEND_DETAIL_DEFAULT = "SEND_DETAIL_DEFAULT"
    const val SEND_DETAIL_SENSITIVITY = "SEND_DETAIL_SENSITIVITY"
    const val SEND_DETAIL_SEND_CODE = "SEND_DETAIL_SEND_CODE"


    const val SEND_CODE_TRUE = "true"
    const val SEND_CODE_FALSE = "false"

    const val DEFAULT_CODE_TRUE = "true"
    const val DEFAULT_CODE_FALSE = "false"

    //????????? ??????
    const val DATA_MODIFY_TITLE = "DATA_MODIFY_TITLE"
    const val DATA_MODIFY_KEYWORD = "DATA_MODIFY_KEYWORD"
    const val DATA_MODIFY_TEXTIMG = "DATA_MODIFY_TEXTIMG"
    const val DATA_MODIFY_NORMAL = "DATA_MODIFY_NORMAL"
    const val DATA_MODIFY_VIDEO = "DATA_MODIFY_VIDEO"
    const val DATA_MODIFY_ID = "DATA_MODIFY_ID"


    //Notice
    const val NOTICE_TITLE = "NOTICE_TITLE"
    const val NOTICE_CONTENT = "NOTICE_CONTENT"
    const val NOTICE_CREATED = "NOTICE_CREATED"

    //Coun
    const val COUN_NAME = "COUN_NAME"
    const val COUN_TITLE = "COUN_TITLE"
    const val COUN_CONTENT = "COUN_CONTENT"
    const val COUN_CREATED = "COUN_CREATED"

    //????????????
    const val NUM_TABS = 2

    const val ONE_PICK_PERMISSION_REQUEST_CODE = 1

    //????????? OCR
    const val CONTENT_TYPE = "application/json"
    const val OCR_SECRET = "UXVwQ05wZU91VG1qTHdQam1ocmpncVhySlRPZ2NwSWQ="
    const val OCR_VERSION = "V2"
    const val OCR_REQUESTID = "string"
    const val OCR_TIMESTAMP = "0"
    const val OCR_LANG = "ko"
    const val OCR_FORMAT = "png"
    const val OCR_NAME = "test 1"
    const val OCR_URL_IMG = "http://49.50.161.198:8080/v1/datalist/datalist?textimg=1655700097940_20220216_140748.jpg"

    //???????????? ??????
    const val DOCUMENT_NAME = "DOCUMENT_NAME"
    const val DOCUMENT_ADDRESS = "DOCUMENT_ADDRESS"
    const val DOCUMENT_CALL = "DOCUMENT_CALL"
    const val DOCUMENT_IMGURL_FIRST = "DOCUMENT_IMGURL_FIRST"
    const val DOCUMENT_IMGURL_SECOND = "DOCUMENT_IMGURL_SECOND"
    const val DOCUMENT_IMGURL_THIRD = "DOCUMENT_IMGURL_THIRD"
    const val DOCUMENT_IMGURL_FOUR = "DOCUMENT_IMGURL_FOUR"
    const val DOCUMENT_IMGURL_FIVE = "DOCUMENT_IMGURL_FIVE"
    const val DOCUMENT_IMGURL = "DOCUMENT_IMGURL"
    const val DOCUMENT_WEBSITE = "DOCUMENT_WEBSITE"
    const val DOCUMENT_TIMESTAMP = "DOCUMENT_TIMESTAMP"

    // ???????????? ?????? ??????
    const val SUNDAY_TIME_START = "SUNDAY_TIME_START"
    const val SUNDAY_TIME_END = "SUNDAY_TIME_END"
    const val SUNDAY_DAY_OFF = "SUNDAY_DAY_OFF"

    const val MONDAY_TIME_START = "MONDAY_TIME_START"
    const val MONDAY_TIME_END = "MONDAY_TIME_END"
    const val MONDAY_DAY_OFF = "MONDAY_DAY_OFF"

    const val TUESDAY_TIME_START = "TUESDAY_TIME_START"
    const val TUESDAY_TIME_END = "TUESDAY_TIME_END"
    const val TUESDAY_DAY_OFF = "TUESDAY_DAY_OFF"

    const val WEDNESDAY_TIME_START = "WEDNESDAY_TIME_START"
    const val WEDNESDAY_TIME_END = "WEDNESDAY_TIME_END"
    const val WEDNESDAY_DAY_OFF = "WEDNESDAY_DAY_OFF"

    const val THURSDAY_TIME_START = "THURSDAY_TIME_START"
    const val THURSDAY_TIME_END = "THURSDAY_TIME_END"
    const val THURSDAY_DAY_OFF = "THURSDAY_DAY_OFF"

    const val FRIDAY_TIME_START = "FRIDAY_TIME_START"
    const val FRIDAY_TIME_END = "FRIDAY_TIME_END"
    const val FRIDAY_DAY_OFF = "FRIDAY_DAY_OFF"

    const val SATURDAY_TIME_START = "SATURDAY_TIME_START"
    const val SATURDAY_TIME_END = "SATURDAY_TIME_END"
    const val SATURDAY_DAY_OFF = "SATURDAY_DAY_OFF"

    const val DETAIL_WORDS = "DETAIL_WORDS"

    //?????? ??????
    const val PAYMENT_NAME = "PAYMENT_NAME"
    const val PAYMENT_ADDRESS = "PAYMENT_ADDRESS"
    const val PAYMENT_CALL = "PAYMENT_CALL"

    //PDF
    const val PDF_HEADER = "????????????"

    //????????? ?????? ??????
    const val NAVER_MAPX = "NAVER_MAPX"
    const val NAVER_MAPY = "NAVER_MAPY"

    const val KAKAO_MAPX = "KAKAO_MAPX"
    const val KAKAO_MAPY = "KAKAO_MAPY"

    const val MAP_AUTOZOOM_MAX_ZOOM_LEVEL = 20
    const val MAP_MIN_ZOOM_LEVEL = 12
    const val MAP_MAX_ZOOM_LEVEL = 21.0

    private val notrhEast = LatLng(39.788312, 132.893671)
    private val southWest = LatLng(30.664915, 122.628502)
    val DEFAULT_MAP_BOUND = LatLngBounds(southWest, notrhEast)


    // Splash
    const val SPLASH_WAIT = 3500
    val MUTILE_PERMISSION: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            add(Manifest.permission.CAMERA)
        }
    }

    val CAMERA = arrayOf(Manifest.permission.CAMERA)

    val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun delayRun(r: Runnable?, delay: Int) {
        val loop = Looper.myLooper()
        if (loop != null) {
            val handler = Handler(loop)
            handler.postDelayed(r!!, delay.toLong())
        }
    }

    val LIST_PERMISSION_LOCATION: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add(Manifest.permission.ACCESS_FINE_LOCATION)
            add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }

    val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
        , Manifest.permission.ACCESS_COARSE_LOCATION
    )

    const val LOCATION_PERMISSION_REQUEST_CODE = 1000

    //????????? ??????
    const val INTENT_NOTICE_URL = "INTENT_NOTICE_URL"
    const val INTENT_NOTICE_END_DATE = "INTENT_NOTICE_END_DATE"
    const val IMAGE_TIMEOUT = 10000
    const val PROGRESS_TIMEOUT = 15000
    const val PREF_MAIN_NOTICE_END_DATE = "PREF_MAIN_NOTICE_END_DATE"

    const val EVENT_IMG_ENABLE_YN = "EVENT_IMG_ENABLE_YN"
    const val EVENT_IMG_TIME_END = "EVENT_IMG_TIME_END"
    const val EVENT_IMG_URL = "EVENT_IMG_URL"

    const val IMAGE_BANNER = 5


}