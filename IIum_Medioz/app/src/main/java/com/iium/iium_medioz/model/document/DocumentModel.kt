package com.iium.iium_medioz.model.document

data class DocumentModel(
//    val documentList : List<DocumentList>
    val doname: String? = null,
    val address: String? = null,
    val address_city: String? = null,
    val address_district: String? = null,
    val address_location: String? = null,
    val call: String? = null,
    val username: String? = null,
    val usernumber: String? = null,
    val usercall: String? = null,
    val userreqdocument: String? = null,
    val imgUrl: String? = null,
    val inquiry_first: String? = null,
    val inquiry_second: String? = null,
    val inquiry_document: String? = null,
    val timestamp: String? = null
)

//data class DocumentList(
//    val doname: String? = null,
//    val address: String? = null,
//    val call: String? = null,
//    val username: String? = null,
//    val usernumber: String? = null,
//    val usercall: String? = null,
//    val userreqdocument: String? = null
//)