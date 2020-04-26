package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_post_detail.*

class PostDetailFragment : BaseFragment(R.layout.fragment_post_detail) {
    private val viewModel by viewModels<PostDetailViewModel> { viewModelFactory }

    private val args: PostDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeBodyWebView()

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                PostDetailViewModel.State.DELETED -> {
                    closeAndGoBack()
                }
                else -> {
                }
            }
        })

        viewModel.fetchBlogEntry(args.postId)

        viewModel.post.observe(viewLifecycleOwner, Observer {
            if (it != null) renderBlogEntry(it)
        })
        viewModel.bodyText.observe(viewLifecycleOwner, Observer {
            if (it != null) renderBodyText(it)
        })
        btn_back.setOnClickListener { closeAndGoBack() }
        btn_edit.setOnClickListener {
            findNavController().navigate(PostDetailFragmentDirections.navActionEditPost(args.postId))
        }
        btn_delete.setOnClickListener {
            val title = viewModel.post.value?.title ?: ""
            viewModel.deletePost()
            Snackbar.make(it, getString(R.string.deleted_post, title), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo) { viewModel.cancelDeletePost() }
                .show()
        }
    }

    private fun renderBlogEntry(post: BlogEntry) {
        title.text = post.title
        renderCardColor(post.cardColor)
    }

    private fun renderBodyText(bodyText: String) {
        body.loadData(bodyText, "text/html", "UTF-8")
    }

    private fun renderCardColor(cardColor: Int) {
        header_background.setBackgroundColor(cardColor)
        applyStatusBarStyle(cardColor)
        val itemsColor = ColorUtils.findTextColorGivenBackgroundColor(cardColor)
        title.setTextColor(itemsColor)
        btn_edit.setColorFilter(itemsColor)
        btn_back.setColorFilter(itemsColor)
        btn_delete.setColorFilter(itemsColor)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeBodyWebView() {
        body.settings.javaScriptEnabled = true
        body.settings.setAppCacheEnabled(true)
        body.settings.cacheMode = WebSettings.LOAD_DEFAULT
        body.webViewClient = WebViewClient()
    }
}