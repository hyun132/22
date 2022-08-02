package com.iium.iium_medioz.util.keyword

import com.iium.iium_medioz.model.upload.KeywordEntity
import com.iium.iium_medioz.util.extensions.ExceedException
import com.iium.iium_medioz.util.extensions.OutOfMatchException
import com.iium.iium_medioz.util.extensions.RegisteredException
import com.iium.iium_medioz.util.extensions.ShortException
import java.util.regex.Pattern

object KeywordChecker {
    private const val KEYWORD_MIN_LEN = 2
    private const val KEYWORD_MAX_CNT = 10
    private var ps = Pattern.compile("^[0-9ㄱ-ㅎ가-힣]+$");

    fun check(keyword: String, registeredKeywordList: List<KeywordEntity>) {
        if(isShort(keyword)) throw ShortException()
        if(isExceedLimit(registeredKeywordList.size)) throw ExceedException()
        if(isOutOfMatch(keyword)) throw OutOfMatchException()
        if(isRegistered(keyword, registeredKeywordList)) throw RegisteredException()
    }

    fun searchCheck(keyword: String) {
        if(isShort(keyword)) throw ShortException()
        if(isOutOfMatch(keyword)) throw OutOfMatchException()
    }

    private fun isShort(keyword: String): Boolean {
        return keyword.length < KEYWORD_MIN_LEN
    }

    private fun isExceedLimit(count: Int): Boolean {
        return count > KEYWORD_MAX_CNT
    }

    private fun isOutOfMatch(keyword: String): Boolean {
        return !ps.matcher(keyword).matches()
    }

    private fun isRegistered(keyword: String, registeredKeywordList: List<KeywordEntity>): Boolean {
        return registeredKeywordList.any { it.keyword == keyword }
    }
}