package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import javax.inject.Inject


class PostEditViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")

    fun editPost() {
        // TODO: extract this to some BlogEntryService or BlogEntryActions or some other super meaningful name...

//        val disposable = Flowable.fromCallable {
//
//            val fileName = UUID.randomUUID().toString() + ".body"
//            val outputStreamWriter =
//                OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
//            outputStreamWriter.use { it.write(bodyText.value) }
//            fileName
//
//        }.flatMapCompletable {
//            blogEntriesRepository.createBlogEntry(
//                BlogEntry(
//                    title = titleText.value.toString(),
//                    bodyPath = it
//                )
//            ).compose(RxSchedulers.completableAsync())
//        }.subscribe {
//            state.value = State.SUCCESS
//        }

    }

}
