package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context
) : ViewModel() {

    enum class State {
        VIEW, DELETED
    }

    val state = MutableLiveData(State.VIEW)

    var post = MutableLiveData<BlogEntry?>()

    fun fetchBlogEntry(id: EntityID) {

        val disposable = blogEntriesRepository
            .fetchById(id)
            .compose(RxSchedulers.flowableAsync())
            .subscribe {
                post.value = it
            }
    }

    fun deletePost() {
        blogEntriesRepository.deleteBlogEntry(post.value!!).blockingAwait()
        state.value = State.DELETED
    }
}