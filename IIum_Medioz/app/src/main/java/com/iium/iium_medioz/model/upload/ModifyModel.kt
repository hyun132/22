package com.iium.iium_medioz.model.upload

import com.iium.iium_medioz.model.send.DataSend

data class ModifyModel (
    val updatedObit : List<ModifyList>? =null,
)

data class ModifyList(
    val title : String? = null,
    val keyword  : String? = null,
)
