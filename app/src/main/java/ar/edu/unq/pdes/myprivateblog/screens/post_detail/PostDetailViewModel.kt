package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.helper.logEventDeletePost
import ar.edu.unq.pdes.myprivateblog.helper.logEventEditPost
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(private val postService: PostService, val context: Context) : ViewModel() {

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
        logEventEditPost(context)
    }

    fun deletePost() {
        val aPost = post.value ?: return
        val disposable = postService.delete(aPost).subscribe {
            state.value = State.DELETED
        }
        logEventDeletePost(context)
    }

    fun cancelDeletePost() {
        val aPost = post.value ?: return
        val disposable = postService.restore(aPost).subscribe()
    }
}