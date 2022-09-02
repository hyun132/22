package com.iium.iium_medioz.model.di

import kotlinx.coroutines.delay

class MainRepository() {

    private val list = arrayListOf(
        "범프리카 인생치킨",
        "더꼬치다",
        "치킨대가",
        "처갓집",
        "오 순살",
        "후라이드참잘하는집",
        "박옥녀",
        "치킨더홈",
        "꾸브라꼬숯불두마리치킨",
        "갓튀긴후라이드",
        "쌀통닭",
        "21세기굽는치킨",
        "림스치킨",
        "치킨신드롬",
        "철인7호",
        "동키치킨",
        "굽네치킨",
        "땅땅치킨",
        "썬더치킨",
        "BBQ",
        "Bhc",
        "장수통닭",
        "동근이숯불두마리치킨",
        "갓튀긴후라이드"
    )

    suspend fun search(text: String?): List<String> {
        delay(100) // 실제 Api 호출인것처럼 하기위해 딜레이 넣음
        return list.filter { it.contains(text ?: "") }
    }
}