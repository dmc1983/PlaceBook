package com.raywenderlich.placebook.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.raywenderlich.placebook.model.Bookmark
import java.nio.charset.CodingErrorAction.REPLACE

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM Bookmark")
    fun loadAll(): LiveData<List<Bookmark>>

    @Query("SELECT * FROM Bookmark WHERE id = :bookmarkId")
    fun loadBookmark(bookmarkId: Long): Bookmark

    @Query("SELECT * FROM Bookmark WHERE id = :bookmarkId")
    fun loadLiveBookmark(bookmarkId: Long): LiveData<Bookmark>

    @Insert(onConflict = IGNORE)
    fun insertBookmark(bookmark: Bookmark): Long

    @Update(onConflict = REPLACE)
    fun updateBookmark(bookmark: Bookmark)

    @Delete
    fun deleteBookmark(bookmark: Bookmark)
}