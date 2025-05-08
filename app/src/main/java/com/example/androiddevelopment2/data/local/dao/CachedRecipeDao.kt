//package com.example.androiddevelopment2.data.local.dao
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.androiddevelopment2.data.local.entity.CachedRecipe
//
//@Dao
//interface CachedRecipeDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(cachedRecipe: CachedRecipe)
//
//    @Query("SELECT * FROM cached_recipes WHERE query = :query")
//    suspend fun getByQuery(query: String): CachedRecipe?
//
//    @Query("DELETE FROM cached_recipes")
//    suspend fun clearAll()
//}