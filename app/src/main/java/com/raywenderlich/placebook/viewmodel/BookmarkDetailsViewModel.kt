package com.raywenderlich.placebook.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.raywenderlich.placebook.R
import com.raywenderlich.placebook.model.Bookmark
import com.raywenderlich.placebook.repository.BookmarkRepo
import com.raywenderlich.placebook.util.ImageUtils
import com.raywenderlich.placebook.util.ImageUtils.decodeFileToSize
import com.raywenderlich.placebook.util.ImageUtils.rotateImageIfRequired
import com.raywenderlich.placebook.util.ImageUtils.saveBitmapToFile
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookmarkDetailsViewModel(application: Application) :
AndroidViewModel(application) {
    private val bookmarkRepo = BookmarkRepo(getApplication())
    private var bookmarkDetailsView: LiveData<BookmarkDetailsView>? = null


    data class BookmarkDetailsView(var id: Long? = null,
                                   var name: String = "",
                                   var phone: String = "",
                                   var address: String = "",
                                   var notes: String = "",
                                   var category: String = "") {

        fun getImage(context: Context) = id?.let {
            ImageUtils.loadBitmapFromFile(context,
                Bookmark.generateImageFilename(it))
        }
        fun setImage(context: Context, image: Bitmap) {
            id?.let {
                ImageUtils.saveBitmapToFile(context, image,
                    Bookmark.generateImageFilename(it))
            }
        }
    }
    private fun bookmarkToBookmarkView(bookmark: Bookmark):
            BookmarkDetailsView {
        return BookmarkDetailsView(
            bookmark.id,
            bookmark.name,
            bookmark.phone,
            bookmark.address,
            bookmark.notes,
            bookmark.category
        )
    }
    private fun mapBookmarksToBookmarkView(bookmarkId: Long) {
        val bookmark = bookmarkRepo.getLiveBookmark(bookmarkId)
        bookmarkDetailsView = Transformations.map(bookmark)
        { repoBookmark ->
            bookmarkToBookmarkView(repoBookmark)
        }
    }
    fun getBookmarkViews(bookmarkId: Long):
            LiveData<BookmarkDetailsView>? {
        if (bookmarkDetailsView == null) {
            mapBookmarksToBookmarkView(bookmarkId)
        }
        return bookmarkDetailsView
    }
    private fun bookmarkViewToBookmark(bookmarkView:
                                       BookmarkDetailsView): Bookmark? {
        val bookmark = bookmarkView.id?.let {
            bookmarkRepo.getBookmark(it)
        }
        if (bookmark != null) {
            bookmark.id = bookmarkView.id
            bookmark.name = bookmarkView.name
            bookmark.phone = bookmarkView.phone
            bookmark.address = bookmarkView.address
            bookmark.notes = bookmarkView.notes
            bookmark.category = bookmarkDetailsView.category
        }
        return bookmark
    }
    fun updateBookmark(bookmarkView: BookmarkDetailsView) {

        GlobalScope.launch {

            val bookmark = bookmarkViewToBookmark(bookmarkView)

            bookmark?.let { bookmarkRepo.updateBookmark(it) }
        }
    }
    fun getCategoryResourceId(category: String): Int? {
        return bookmarkRepo.getCategoryResourceId(category)
    }
    fun getCategories(): List<String> {
        return bookmarkRepo.categories
    }


}