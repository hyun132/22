package com.iium.iium_medioz.model.pdf

class RPdfGeneratorModel(list: List<RTransaction>, header: String) {

    var list = emptyList<RTransaction>()
    var header = ""
    var totalCredit = ""
    var totalDebit = ""
    var totalProfit = ""

    init {
        this.list = list
        this.header = header
        calculateTotal(list)

    }
    private fun calculateTotal(items: List<RTransaction>) {
        val totalPlus = items.sumOf {
            if (it.transType == RTransactionType.plus) {
                it.totalPrice
            } else {
                0.0
            }
        }

        val totalMinus = items.sumOf {
            if (it.transType == RTransactionType.minus) {
                it.totalPrice
            } else {
                0.0
            }
        }

        val final = totalPlus - totalMinus
        totalDebit = "-$totalMinus"
        totalCredit = totalPlus.toString()
        totalProfit = final.toString()
    }
}