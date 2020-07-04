package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.services.AnalyticsService
import ar.edu.unq.pdes.myprivateblog.services.PostService
import ar.edu.unq.pdes.myprivateblog.services.TypeEventAnalytics
import javax.inject.Inject

class PostCreateViewModel @Inject constructor(private val postService: PostService, val analytics: AnalyticsService) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    val cardColor = MutableLiveData<Int>(Color.LTGRAY)

    var post = 0

    fun createPost(onError: () -> Unit) {
      if(isPostEmpty()){
          onError()
      } else {
          create()
      }
    }

    private  fun isPostEmpty() : Boolean{
        return titleText.value.toString().isEmpty() && bodyText.value.toString().isEmpty()
    }

    private fun  create(){
        val disposable = postService.create(
            title = titleText.value.toString(),
            bodyText = bodyText.value.toString(),
            cardColor = cardColor.value!!
        ).subscribe {
            post = it.toInt()
            state.value = State.SUCCESS
        }
        analytics.logEvent(TypeEventAnalytics.CREATE_NEW_POST)
    }
}
