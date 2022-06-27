package com.iium.iium_medioz.model.pdf

enum class RTransactionType { plus, minus }

class RTransaction {

    var pdf_index: String = ""
    var pdf_name: String = ""
    var pdf_address:  String = ""
    var pdf_gender: String = ""
    var pdf_data:  String = ""
    var pdf_sortation: String = ""
    var transType: RTransactionType = RTransactionType.plus
    var totalPrice: Double = 0.0
}
