package ar.edu.unq.pdes.myprivateblog

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ar.edu.unq.pdes.myprivateblog.screens.posts_listing.PostsListingFragment
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by viewModels<MainActivityViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        RxJavaPlugins.setErrorHandler { Timber.e(it) }

        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    fun navigateToPostsListing() = navigateTo(PostsListingFragment())

    private fun navigateTo(fragment: BaseFragment, backStackTag: String? = null) {
//        Timber.d("Navigated to ${fragment.javaClass.canonicalName}")
//
//        val transaction = supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, fragment)
//
//        if (backStackTag != null) {
//            transaction.addToBackStack(backStackTag)
//        }
//
//        transaction.commit()
    }

}
