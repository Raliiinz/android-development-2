package com.example.androiddevelopment2.data.local.cache

import com.example.androiddevelopment2.domain.model.RecipeModel
import java.util.Date
import java.util.concurrent.TimeUnit

class RecipeCache {

    data class CacheEntry(
        val data: List<RecipeModel>,
        val timestamp: Long,
        var intermediateQueries: Int = 0
    )

    private val cache = mutableMapOf<String, CacheEntry>()

    fun get(query: String): List<RecipeModel>? {
//        cleanupExpiredEntries() // ✅ Удаляем протухшие записи перед доступом

        val currentTime = Date().time
        val entry = cache[query]

        if (entry == null) {
            incrementAllCounters()
            return null
        }

        val timeDiff = currentTime - entry.timestamp
        val isTimeValid = timeDiff <= CACHE_TIMEOUT_MS
        val isCountValid = entry.intermediateQueries < 3

        return if (isTimeValid && isCountValid) {
            entry.intermediateQueries = 0
            incrementOtherCounters(query)
            entry.data
        } else {
            incrementAllCounters()
            null
        }
    }

    fun updateCache(query: String, data: List<RecipeModel>) {
//        cleanupExpiredEntries()

        val now = Date().time
        cache[query] = CacheEntry(
            data = data,
            timestamp = now
        )
        incrementOtherCounters(query)
    }

    private fun incrementOtherCounters(currentQuery: String) {
        cache.forEach { (query, entry) ->
            if (query != currentQuery) {
                entry.intermediateQueries++
            }
        }
    }

    private fun incrementAllCounters() {
        cache.forEach { (_, entry) ->
            entry.intermediateQueries++
        }
    }
    /**
     * ✅ Удаляем все записи, которые:
     * - устарели по времени
     * - или имеют слишком много промежуточных запросов
     */
    private fun cleanupExpiredEntries() {
        val now = Date().time
        val toRemove = cache.filterValues { entry ->
            val isExpired = now - entry.timestamp > CACHE_TIMEOUT_MS
            val isTooManyQueries = entry.intermediateQueries >= 3
            isExpired || isTooManyQueries
        }.keys

        toRemove.forEach {
            cache.remove(it)
        }
    }

    companion object {
        private val CACHE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5)
    }
}