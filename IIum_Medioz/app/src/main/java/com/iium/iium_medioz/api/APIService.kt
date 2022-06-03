package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.base.AppPolicy
import com.iium.iium_medioz.model.rest.base.AutoLogin
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.base.Verification
import com.iium.iium_medioz.model.rest.login.*
import com.iium.iium_medioz.model.ui.CounGet
import com.iium.iium_medioz.model.ui.CounPost
import com.iium.iium_medioz.model.ui.NoticeModel
import com.iium.iium_medioz.model.upload.ChangeModel
import com.iium.iium_medioz.model.upload.CreateMedical
import com.iium.iium_medioz.model.upload.NormalModel
import com.iium.iium_medioz.model.upload.VideoModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
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

    // 로그아웃 API
    @DELETE
    fun getLogOut(@Header("Accesstoken")accesstoken: String?): Call<LogOut>

    // 문자 인증 API
    @POST("v1/user/sms")
    fun getSMS(@Body loginSMS: LoginSMS): Call<LoginSMS>

    // 나의 의료데이터 작성
    @Multipart
    @POST("v1/datalist/textImg")
    fun getCreate(@Header("Accesstoken")accesstoken: String?,
                  @Part textimg : MutableList<MultipartBody.Part?>,
                  @PartMap data: HashMap<String, RequestBody>): Call<CreateMedical>

    // 나의 의료데이터 작성(일반 이미지)
    @Multipart
    @POST("v1/datalist/Img")
    fun getNormal(@Header("Accesstoken")accesstoken: String?,
                  @Part Img : MutableList<MultipartBody.Part?>,
                  @PartMap data: HashMap<String, RequestBody>): Call<NormalModel>

    // 나의 의료데이터 작성(영상)
    @Multipart
    @POST("v1/datalist/video")
    fun getVideo(@Header("Accesstoken")accesstoken: String?,
                 @Part video : MutableList<MultipartBody.Part?>,
                 @PartMap data: HashMap<String, RequestBody>): Call<VideoModel>

    // 나의 의료데이터 조회
    @GET("v1/datalist/my")
    fun getCreateGet(@Header("Accesstoken") accesstoken: String?): Call<MedicalData>


    //나의 의료데이터 텍스트 이미지 받기API
    @GET("v1/datalist/textImg")
    fun getImg(@Query("textimg") textimg: String?,
               @Header("Accesstoken") accesstoken: String?) : Call<ResponseBody>


    //나의 의료데이터 일반 이미지 받기API
    @GET("v1/datalist/normalImg")
    fun getCallNorImg(@Query("norImg") norImg: String?,
               @Header("Accesstoken") accesstoken: String?) : Call<ResponseBody>

    //나의 의료데이터 영상 받기API
    @GET("v1/datalist/video")
    fun getCallVideo(@Query("video") video: String?,
               @Header("Accesstoken") accesstoken: String?) : Call<ResponseBody>

    //나의 의료데이터 수정
    @PUT("v1/datalist/:id")
    fun getChange(@Body changeModel: ChangeModel,
                  @Header("Accesstoken") accesstoken: String?) : Call<ChangeModel>

    // 나의 의료데이터 검색
    @GET("v1/datalist")
    fun getSearch(@Query("name")name: String?,
                      @Header("Accesstoken")accesstoken: String?) : Call<CreateName>

    // 공지사항 모두조회 API
    @GET("v1/center/announcement")
    fun getNoti(@Header("Accesstoken")accesstoken: String?) : Call<NoticeModel>

    // 1:1문의 조회
    @GET("v1/center/request")
    fun getCounRequest(@Header("Accesstoken")accesstoken: String?) : Call<CounGet>

    // 1:1문의 작성
    @POST("v1/center/request")
    fun getCounPost(@Header("Accesstoken")accesstoken: String?,
                    @Body counPost: CounPost) : Call<CounPost>

}