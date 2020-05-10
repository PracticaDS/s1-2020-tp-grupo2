package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.logger.AnalyticsLogger
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class PostCreateViewModel @Inject constructor(private val postService: PostService, val analytics: AnalyticsLogger) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    val cardColor = MutableLiveData<Int>(Color.LTGRAY)

    var post = 0

    fun createPost() {
        val disposable = postService.create(
            title = titleText.value.toString(),
            bodyText = bodyText.value.toString(),
            cardColor = cardColor.value!!
        ).subscribe {
            post = it.toInt()
            state.value = State.SUCCESS
        }
        analytics.logEventCreateNewPost()
    }
}
