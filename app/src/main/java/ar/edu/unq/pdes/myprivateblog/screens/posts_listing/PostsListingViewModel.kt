package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.services.AuthService
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class PostsListingViewModel @Inject constructor(
    private val postService: PostService,
    private val authService: AuthService
) :
    ViewModel() {
    fun isLoggedIn() = authService.isLoggedIn()
    fun logout() = authService.logout()

    @ExperimentalStdlibApi
    val posts: LiveData<List<BlogEntry>> by lazy { postService.getAllBlogEntries() }
}
