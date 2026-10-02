package com.example.data.local

import androidx.room.*
import com.example.data.model.Album
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.SecurityLog
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND isDecoy = :isDecoy ORDER BY dateAdded DESC")
    fun getActiveMedia(isDecoy: Boolean): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND isDecoy = :isDecoy AND mediaType = :mediaType ORDER BY dateAdded DESC")
    fun getMediaByType(isDecoy: Boolean, mediaType: MediaType): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND isDecoy = :isDecoy AND isFavorite = 1 ORDER BY dateAdded DESC")
    fun getFavorites(isDecoy: Boolean): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND isDecoy = :isDecoy AND albumId = :albumId ORDER BY dateAdded DESC")
    fun getMediaByAlbum(isDecoy: Boolean, albumId: String): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE isDeleted = 1 AND isDecoy = :isDecoy ORDER BY deletedTimestamp DESC")
    fun getTrashMedia(isDecoy: Boolean): Flow<List<MediaItem>>

    @Query("SELECT * FROM media_items WHERE id = :id LIMIT 1")
    suspend fun getMediaById(id: String): MediaItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedia(media: MediaItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaList: List<MediaItem>)

    @Update
    suspend fun updateMedia(media: MediaItem)

    @Delete
    suspend fun deleteMedia(media: MediaItem)

    @Query("DELETE FROM media_items WHERE isDeleted = 1 AND isDecoy = :isDecoy")
    suspend fun clearTrash(isDecoy: Boolean)

    @Query("SELECT COUNT(*) FROM media_items WHERE isDeleted = 0 AND isDecoy = :isDecoy AND albumId = :albumId")
    suspend fun getCountInAlbum(isDecoy: Boolean, albumId: String): Int

    @Query("SELECT SUM(fileSize) FROM media_items WHERE isDecoy = :isDecoy")
    suspend fun getTotalStorageUsed(isDecoy: Boolean): Long?

    @Query("SELECT COUNT(*) FROM media_items")
    suspend fun getTotalMediaCount(): Int
}

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums WHERE isDecoy = :isDecoy ORDER BY createdAt ASC")
    fun getAlbums(isDecoy: Boolean): Flow<List<Album>>

    @Query("SELECT COUNT(*) FROM albums")
    suspend fun getTotalAlbumsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: Album)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAlbums(albums: List<Album>)

    @Delete
    suspend fun deleteAlbum(album: Album)
}

@Dao
interface SecurityLogDao {
    @Query("SELECT * FROM security_logs ORDER BY timestamp DESC LIMIT 50")
    fun getSecurityLogs(): Flow<List<SecurityLog>>

    @Query("SELECT COUNT(*) FROM security_logs")
    suspend fun getTotalLogsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: SecurityLog)

    @Query("DELETE FROM security_logs")
    suspend fun clearLogs()
}
