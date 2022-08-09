package com.iium.iium_medioz.model.rest.login

data class PutUser(
    val pickscore: List<String?>? = null,
    val videoscore: List<String?>? = null,
    val keywordscore: List<String?>? = null,
    val sensitivityscore: List<String?>? = null,
    val allscore: Int? = null
)