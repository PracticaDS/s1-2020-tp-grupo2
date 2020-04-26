package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class PostsListingViewModel @Inject constructor(private val postService: PostService) :
    ViewModel() {
    val posts: LiveData<List<BlogEntry>> by lazy { postService.getAllBlogEntries() }
}
