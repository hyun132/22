package com.iium.iium_medioz.model.pdf

enum class RTransactionType { plus, minus }

class RTransaction {

    var pdf_index: String? = null
    var pdf_name: String? = null
    var pdf_address: String? = null
    var pdf_gender: String? = null
    var pdf_data:  String? = null
    var pdf_sortation: String? = null
    var transType: RTransactionType = RTransactionType.plus
    var totalPrice: Double = 0.0
}
