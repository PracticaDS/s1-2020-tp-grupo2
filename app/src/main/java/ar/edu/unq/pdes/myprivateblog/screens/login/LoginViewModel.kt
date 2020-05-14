package ar.edu.unq.pdes.myprivateblog.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.services.PostService
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val postService: PostService) :
    ViewModel() {

}
