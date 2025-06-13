package com.example.androiddevelopment2.data.local.cache

import com.example.androiddevelopment2.domain.model.RecipeModel
import java.util.concurrent.TimeUnit

class RecipeCache {

    data class CacheEntry(
        val data: List<RecipeModel>,
        val timestamp: Long,
        var intermediateQueries: Int = 0
    )

    private val cache = mutableMapOf<String, CacheEntry>()

    fun get(query: String): List<RecipeModel>? {
//        cleanupExpiredEntries() //Удаляем протухшие записи перед доступом

        val currentTime = System.currentTimeMillis()
        val entry = cache[query] ?: run {
            incrementAllCounters()
            return null
        }

        return when {
            isValidEntry(entry, currentTime) -> {
                entry.intermediateQueries = 0
                incrementOtherCounters(query)
                entry.data
            }
            else -> {
                incrementAllCounters()
                null
            }
        }
    }

    private fun isValidEntry(entry: CacheEntry, currentTime: Long): Boolean {
        val timeDiff = currentTime - entry.timestamp
        return timeDiff <= CACHE_TIMEOUT_MS &&
                entry.intermediateQueries < INTERMEDIATE_MAX_QUERIES
    }

    fun updateCache(query: String, data: List<RecipeModel>) {
//        cleanupExpiredEntries()
        cache[query] = CacheEntry(
            data = data,
            timestamp = System.currentTimeMillis()
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
        cache.values.forEach { it.intermediateQueries++ }
    }

    /**
     * Удаляем все записи, которые:
     * - устарели по времени
     * - или имеют слишком много промежуточных запросов
     */
//    private fun cleanupExpiredEntries() {
//        val now = Date().time
//        val toRemove = cache.filterValues { entry ->
//            val isExpired = now - entry.timestamp > CACHE_TIMEOUT_MS
//            val isTooManyQueries = entry.intermediateQueries >= 3
//            isExpired || isTooManyQueries
//        }.keys
//
//        toRemove.forEach {
//            cache.remove(it)
//        }
//    }

    companion object {
        private val CACHE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5)
        private const val INTERMEDIATE_MAX_QUERIES = 3
    }
}
