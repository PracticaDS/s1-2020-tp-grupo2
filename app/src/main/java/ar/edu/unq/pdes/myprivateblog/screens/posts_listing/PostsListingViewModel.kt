package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import javax.inject.Inject

class PostsListingViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository
) : ViewModel() {

    val posts: LiveData<List<BlogEntry>> by lazy {
        blogEntriesRepository.getAllBlogEntries()
    }


}
