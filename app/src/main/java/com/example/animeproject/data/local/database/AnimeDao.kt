package com.example.animeproject.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeDao {
    @Query("SELECT * FROM favorite_anime")
    fun getAllFavoritesFlow(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM favorite_anime WHERE id = :id")
    suspend fun getFavoriteById(id: String): AnimeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(anime: AnimeEntity): Long

    @Query("DELETE FROM favorite_anime WHERE id = :id")
    suspend fun deleteFavoriteById(id: String): Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_anime WHERE id = :id)")
    fun isFavoriteFlow(id: String): Flow<Boolean>
}