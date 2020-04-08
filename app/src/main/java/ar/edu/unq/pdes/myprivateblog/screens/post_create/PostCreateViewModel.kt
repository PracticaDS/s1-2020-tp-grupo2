package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Flowable
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject


class PostCreateViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    var post = 0

    fun createPost() {
        // TODO: extract this to some BlogEntryService or BlogEntryActions or some other super meaningful name...

        val disposable = Flowable.fromCallable {

            val fileName = UUID.randomUUID().toString() + ".body"
            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
            outputStreamWriter.use { it.write(bodyText.value) }
            fileName

        }.flatMapSingle {

            blogEntriesRepository.createBlogEntry(
                BlogEntry(
                    title = titleText.value.toString(),
                    bodyPath = it
                )
            )

        }.compose(RxSchedulers.flowableAsync()).subscribe {
            post = it.toInt()
            state.value = State.SUCCESS
        }

    }

}
