package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.OCRModel
import com.iium.iium_medioz.model.calendar.CalendarFeel
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.model.map.NaverSearchModel
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.base.AppPolicy
import com.iium.iium_medioz.model.rest.base.AutoLogin
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.base.Verification
import com.iium.iium_medioz.model.rest.login.*
import com.iium.iium_medioz.model.send.DataSend
import com.iium.iium_medioz.model.send.SendModel
import com.iium.iium_medioz.model.send.SendTestModel
import com.iium.iium_medioz.model.ui.CounGet
import com.iium.iium_medioz.model.ui.CounPost
import com.iium.iium_medioz.model.ui.NoticeModel
import com.iium.iium_medioz.model.upload.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface APIService {
    // 검증 API
    @GET("v1/app/verification")
    fun getVerification(@Header("abcd-ef")abcdef: String?): Call<Verification>

    // 정책 API
    @GET("v1/app/policy")
    fun getPolicy(): Call<AppPolicy>

    // 자동 로그인 API
    @PUT("v1/user/auto")
    fun getautoLogin(@Header("Accesstoken")accesstoken: String?): Call<AutoLogin>

    // 로그인 API
    @PUT("v1/user")
    fun getLogin(@Body loginSend: LoginSend):Call<LoginSend>

    // 회원가입 API
    @Multipart
    @POST("v1/user")
    fun getSignUp(@Part profile: MutableList<MultipartBody.Part?>,
                  @PartMap data: HashMap<String, RequestBody>):Call<LoginResult>

    // 회원정보 API
    @GET("v1/user")
    fun getUser(@Header("Accesstoken")accesstoken: String?): Call<GetUser>

    /**
     * 수정
     */
    @GET("v1/user")
    suspend fun getUser2(@Header("Accesstoken")accesstoken: String?): Response<GetUser>

    // 프로필 이미지 받기API
    @GET("v1/user/image")
    @Streaming
    fun getProfileImg(@Query("profile") profile: String?,
                      @Header("Accesstoken") accesstoken: String?) : Call<ResponseBody>

    /**
     * 수정
     */
    @GET("v1/user/image")
    @Streaming
    suspend fun getProfileImg2(@Query("profile") profile: String?,
                               @Header("Accesstoken") accesstoken: String?) : Response<ResponseBody>

    // 로그아웃 API
    @DELETE
    fun getLogOut(@Header("Accesstoken")accesstoken: String?): Call<LogOut>

    // 문자 인증 API
    @POST("v1/user/sms")
    fun getSMS(@Body loginSMS: LoginSMS): Call<LoginSMS>

    // 나의 의료데이터 작성
    @Multipart
    @POST("v1/datalist/datalist")
    fun getCreate(@Header("Accesstoken")accesstoken: String?,
                  @Part datalist : MutableList<MultipartBody.Part?>,
                  @PartMap data: HashMap<String, RequestBody>): Call<CreateMedical>

    // 나의 의료데이터 작성
    @Multipart
    @POST("v1/datalist/datalist")
    suspend fun getCreate2(@Header("Accesstoken")accesstoken: String?,
                           @Part datalist : MutableList<MultipartBody.Part?>,
                           @PartMap data: HashMap<String, RequestBody>): Response<CreateMedical>

    // 나의 의료데이터 조회
    @GET("v1/datalist/my")
    fun getCreateGet(@Header("Accesstoken") accesstoken: String?): Call<MedicalData>

    /**
     * 수정
     */
    // 나의 의료데이터 조회
    @GET("v1/datalist/my")
    suspend fun getCreateGet2(@Header("Accesstoken") accesstoken: String?): Response<MedicalData>


    //나의 의료데이터 텍스트 이미지 받기API
    @GET("v1/datalist/datalist")
    fun getImg(@Query("textimg") textimg: String?,
               @Header("Accesstoken") accesstoken: String?) : Call<ResponseBody>

    /**
     * 추가
     */
    @GET("v1/datalist/datalist")
    suspend fun getImg2(@Query("textimg") textimg: String?,
                        @Header("Accesstoken") accesstoken: String?) : Response<ResponseBody>

    //나의 의료데이터 판매 전환
    @POST("v1/datasend")
    fun getChange(@Header("Accesstoken") accesstoken: String?,
                  @Body dataSend: DataSend) : Call<SendModel>

    /**
     * 추가
     */
    @POST("v1/datasend")
    suspend fun getChange2(@Header("Accesstoken") accesstoken: String?,
                           @Body dataSend: DataSend) : Response<SendModel>

    //나의 의료데이터 판매 조회
    @GET("v1/datasend/my")
    fun getSend(@Header("Accesstoken") accesstoken: String?) : Call<SendTestModel>

    /**
     * 추가
     */
    @GET("v1/datasend/my")
    suspend fun getSend2(@Header("Accesstoken") accesstoken: String?) : Response<SendTestModel>

    // 나의 의료데이터 삭제
    @DELETE("v1/datalist")
    fun getDataDelete(@Header("Accesstoken")accesstoken: String?,
                      @Query("id") id : String?) : Call<DeleteModel>

    /**
     * 추가
     */
    @DELETE("v1/datalist")
    suspend fun getDataDelete2(@Header("Accesstoken")accesstoken: String?,
                               @Query("id") id : String?) : Response<DeleteModel>

    // 나의 의료데이터 수정
    @PUT("v1/datalist")
    fun putData(@Header("Accesstoken")accesstoken: String?,
                @Query("id") id : String?,
                @Body modifyList: ModifyList) : Call<ModifyModel>

    /**
     * 추가
     */
    @PUT("v1/datalist")
    suspend fun putData2(@Header("Accesstoken")accesstoken: String?,
                         @Query("id") id : String?,
                         @Body modifyList: ModifyList) : Response<ModifyList>

    // 나의 판매 데이터 해제
    @DELETE("v1/datasend")
    fun getSendDelete(@Header("Accesstoken")accesstoken: String?,
                      @Query("id") id : String?) : Call<DeleteModel>

    /**
     * 추가
     */
    @DELETE("v1/datasend")
    suspend fun getSendDelete2(@Header("Accesstoken")accesstoken: String?,
                               @Query("id") id : String?) : Response<DeleteModel>

    // 나의 의료데이터 검색
    @GET("v1/datalist")
    fun getSearch(@Query("name")name: String?,
                  @Header("Accesstoken")accesstoken: String?) : Call<CreateName>

    /**
     * 추가
     */
    @GET("v1/datalist")
    suspend fun getSearch2(@Query("name")name: String?,
                           @Header("Accesstoken")accesstoken: String?) : Response<CreateName>

    // 공지사항 모두조회 API
    @GET("v1/center/announcement")
    fun getNoti(@Header("Accesstoken")accesstoken: String?) : Call<NoticeModel>

    // 1:1문의 조회
    @GET("v1/center/request")
    fun getCounRequest(@Header("Accesstoken")accesstoken: String?) : Call<CounGet>

    /**
     * 추가
     */
    @GET("v1/center/request")
    suspend fun getCounRequest2(@Header("Accesstoken")accesstoken: String?) : Response<CounGet>

    // 1:1문의 작성
    @POST("v1/center/request")
    fun getCounPost(@Header("Accesstoken")accesstoken: String?,
                    @Body counPost: CounPost) : Call<CounPost>

    // 네이버 JSON 보내기
    @POST("v1/dataocr/dataocr")
    fun postOCR(@Header("Accesstoken")accesstoken: String?,
                @Body ocrModel: OCRModel) : Call<OCRModel>

    /**
     * 추가
     */
    @POST("v1/dataocr/dataocr")
    suspend fun postOCR2(@Header("Accesstoken")accesstoken: String?,
                         @Body ocrModel: OCRModel) : Response<OCRModel>

    // 제휴병원 좌표
    @GET("v1/map/map/search")
    fun getMap(@Header("Accesstoken")accesstoken: String?,
               @Query("name")value: String?): Call<MapMarker>

    /**
     * 추가
     */
    @GET("v1/map/map/search")
    suspend fun getMap2(@Header("Accesstoken")accesstoken: String?,
                        @Query("name")value: String?): Response<MapMarker>

    //제휴병원 서류 등록
    @POST("v1/document/document")
    fun postDocument(@Header("Accesstoken")accesstoken: String?,
                     @Body documentModel: DocumentModel): Call<DocumentModel>

    /**
     * 추가
     */
    @POST("v1/document/document")
    suspend fun postDocument2(@Header("Accesstoken")accesstoken: String?,
                              @Body documentModel: DocumentModel): Response<DocumentModel>

    // 제휴병원 서류 조회
    @GET("v1/document/document/my")
    fun getDocument(@Header("Accesstoken")accesstoken: String?): Call<DocumentListModel>

    /**
     * 추가
     */
    @GET("v1/document/document/my")
    suspend fun getDocument2(@Header("Accesstoken")accesstoken: String?): Response<DocumentListModel>

    //제휴병원 검색

    // 캘린더 등록
    @POST("v1/calendar/feel")
    fun postFeel(@Header("Accesstoken")accesstoken: String?,
                 @Body calendarFeel: CalendarFeel): Call<CalendarModel>

    // 캘린더 조회
    @GET("v1/calendar/my")
    fun getFeel(@Header("Accesstoken")accesstoken: String?): Call<CalendarModel>

    /**
     * 추가
     */
    @GET("v1/calendar/my")
    suspend fun getFeel2(@Header("Accesstoken")accesstoken: String?): Response<CalendarModel>

    // 캘린더 삭제
    @DELETE("v1/calendar")
    fun deleteFeel(@Header("Accesstoken")accesstoken: String?,
                   @Query("id") id : String?): Call<DeleteModel>

    /**
     * 추가
     */
    @DELETE("v1/calendar")
    suspend fun deleteFeel2(@Header("Accesstoken")accesstoken: String?,
                            @Query("id") id : String?): Response<DeleteModel>
}