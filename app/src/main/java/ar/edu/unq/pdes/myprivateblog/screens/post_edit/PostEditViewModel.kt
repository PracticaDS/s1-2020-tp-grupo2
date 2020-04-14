package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Flowable
import java.io.File
import java.io.OutputStreamWriter
import javax.inject.Inject

class PostEditViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    var post = MutableLiveData<BlogEntry?>()
    val bodyText = MutableLiveData<String?>()

    fun fetchBlogEntry(id: EntityID, onNext: (BlogEntry) -> Unit) {
        val disposable = blogEntriesRepository
            .fetchById(id)
            .compose(RxSchedulers.flowableAsync())
            .subscribe {
                post.value = it
                val content = File(context.filesDir, it.bodyPath!!).readText()
                bodyText.value = content
                onNext(it)
            }
    }

    fun updatePost() {
        val disposable = Flowable.fromCallable {
            val outputStreamWriter =
                OutputStreamWriter(
                    context.openFileOutput(
                        post.value!!.bodyPath,
                        Context.MODE_PRIVATE
                    )
                )
            outputStreamWriter.use { it.write(bodyText.value!!) }
        }.compose(RxSchedulers.flowableAsync()).subscribe {
            blogEntriesRepository.updateBlogEntry(post.value!!).blockingAwait()
            state.value = State.SUCCESS
        }
    }
}
