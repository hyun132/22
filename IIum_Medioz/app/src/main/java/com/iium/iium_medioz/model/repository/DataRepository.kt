package com.iium.iium_medioz.model.repository

import com.iium.iium_medioz.model.OCRModel
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.login.GetUser
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

/**
 * 추가
 */
interface DataRepository {
    suspend fun getProfileImg2(profile:String,token:String): Result<ResponseBody>
    suspend fun getCreateGet2(token:String): Result<MedicalData>
    suspend fun getUser2(token:String): Result<GetUser>
    suspend fun getCreate2(token:String,datalist : MutableList<MultipartBody.Part?>,data: HashMap<String, RequestBody>): Result<CreateMedical>
    suspend fun getImg2(textimg: String,token:String): Result<ResponseBody>
    suspend fun getChange2(token:String,dataSend: DataSend): Result<SendModel>
    suspend fun getSend2(token:String): Result<SendTestModel>
    suspend fun getDataDelete2(token:String,id : String): Result<DeleteModel>
    suspend fun putData2(token:String,id:String,modifyList: ModifyList): Result<ModifyList>
    suspend fun getSendDelete2(token:String,id:String): Result<DeleteModel>
    suspend fun getSearch2(name: String,token:String): Result<CreateName>
    suspend fun getCounRequest2(token:String): Result<CounGet>
    suspend fun postOCR2(token:String,ocrModel: OCRModel): Result<OCRModel>
    suspend fun getMap2(token:String,value: String): Result<MapMarker>
    suspend fun postDocument2(token:String,documentModel: DocumentModel): Result<DocumentModel>
    suspend fun getDocument2(token:String): Result<DocumentListModel>
    suspend fun getFeel2(token:String): Result<CalendarModel>
    suspend fun deleteFeel2(token:String,id : String): Result<DeleteModel>

}