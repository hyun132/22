package com.iium.iium_medioz.model.repository

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.model.OCRModel
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.model.rest.login.UserGet
import com.iium.iium_medioz.model.send.DataSend
import com.iium.iium_medioz.model.send.SendModel
import com.iium.iium_medioz.model.send.SendTestModel
import com.iium.iium_medioz.model.ui.CounGet
import com.iium.iium_medioz.model.upload.CreateMedical
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.model.upload.ModifyList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * 추가
 */
class DataRepositoryImpl(private val service: APIService) : DataRepository {
    override suspend fun getProfileImg2(profile: String, token: String): Result<ResponseBody> {
        return handleApi { service.getProfileImg2(profile,token) }
    }

    override suspend fun getCreateGet2(token: String): Result<MedicalData> {
        return handleApi { service.getCreateGet2(token) }
    }

    override suspend fun getUser2(token: String): Result<GetUser> {
        return handleApi { service.getUser2(token) }
    }

    override suspend fun getCreate2(
        token: String,
        datalist: MutableList<MultipartBody.Part?>,
        data: HashMap<String, RequestBody>
    ): Result<CreateMedical> {
        return handleApi { service.getCreate2(token,datalist,data) }
    }

    override suspend fun getImg2(textimg: String, token: String): Result<ResponseBody> {
        return handleApi { service.getImg2(textimg,token) }
    }

    override suspend fun getChange2(token: String, dataSend: DataSend): Result<SendModel> {
        return handleApi { service.getChange2(token,dataSend) }
    }

    override suspend fun getSend2(token: String): Result<SendTestModel> {
        return handleApi { service.getSend2(token) }
    }

    override suspend fun getDataDelete2(token: String, id: String): Result<DeleteModel> {
        return handleApi { service.getDataDelete2(token,id) }
    }

    override suspend fun putData2(token: String,id: String, modifyList: ModifyList): Result<ModifyList> {
        return handleApi { service.putData2(token,id,modifyList) }
    }

    override suspend fun getSendDelete2(token: String, id: String): Result<DeleteModel> {
        return handleApi { service.getSendDelete2(token,id) }
    }

    override suspend fun getSearch2(name: String, token: String): Result<CreateName> {
        return handleApi { service.getSearch2(name,token) }
    }

    override suspend fun getCounRequest2(token: String): Result<CounGet> {
        return handleApi { service.getCounRequest2(token) }
    }

    override suspend fun postOCR2(token: String, ocrModel: OCRModel): Result<OCRModel> {
        return handleApi { service.postOCR2(token, ocrModel) }
    }

    override suspend fun getMap2(token: String, value: String): Result<MapMarker> {
        return handleApi { service.getMap2(token, value) }
    }

    override suspend fun postDocument2(
        token: String,
        documentModel: DocumentModel
    ): Result<DocumentModel> {
        return handleApi { service.postDocument2(token, documentModel) }
    }

    override suspend fun getDocument2(token: String): Result<DocumentListModel> {
        return handleApi { service.getDocument2(token) }
    }

    override suspend fun getFeel2(token: String): Result<CalendarModel> {
        return handleApi { service.getFeel2(token) }
    }

    override suspend fun deleteFeel2(token: String, id: String): Result<DeleteModel> {
        return handleApi { service.deleteFeel2(token,id) }
    }

    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            Result.Error(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            Result.Exception(e)
        }
    }
}