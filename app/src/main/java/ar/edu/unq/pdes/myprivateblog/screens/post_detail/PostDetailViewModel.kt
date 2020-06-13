package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.services.AnalyticsService
import ar.edu.unq.pdes.myprivateblog.services.PostService
import ar.edu.unq.pdes.myprivateblog.services.TypeEventAnalytics
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(private val postService: PostService, val analytics: AnalyticsService) : ViewModel() {

    enum class State {
        VIEW, DELETED
    }

    val state = MutableLiveData(State.VIEW)
    var post = MutableLiveData<BlogEntry?>()
    val bodyText = MutableLiveData<String?>()

    fun fetchBlogEntry(id: EntityID) {
        val disposable = postService.getById(id).subscribe {
            post.value = it.first
            bodyText.value = it.second
        }
        analytics.logEvent(TypeEventAnalytics.VIEW_POST)
    }

    fun deletePost() {
        val aPost = post.value ?: return
        val disposable = postService.delete(aPost).subscribe {
            state.value = State.DELETED
        }
        analytics.logEvent(TypeEventAnalytics.DELETE_POST)
    }

    fun cancelDeletePost() {
        val aPost = post.value ?: return
        val disposable = postService.restore(aPost).subscribe()
        analytics.logEvent(TypeEventAnalytics.CANCEL_EDIT_POST)
    }
}