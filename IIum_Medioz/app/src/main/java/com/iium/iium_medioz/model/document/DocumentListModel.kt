package com.iium.iium_medioz.model.document

data class DocumentListModel (
    val documentList: List<DocumentList>
)

data class DocumentList(
    val doname: String? = null,
    val address: String? = null,
    val call: String? = null,
    val username: String? = null,
    val usernumber: String? = null,
    val usercall: String? = null,
    val userreqdocument: String? = null,
    val imgUrl: String? = null,
    val timestamp: String? = null
)