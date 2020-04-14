package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import kotlinx.android.synthetic.main.fragment_post_edit.*
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.glideloader.GlideImageLoader
import org.wordpress.aztec.glideloader.GlideVideoThumbnailLoader
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import timber.log.Timber

class PostEditFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    private val viewModel by viewModels<PostEditViewModel> { viewModelFactory }

    private val args: PostEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchBlogEntry(args.postId) { renderBlogEntry() }

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                PostEditViewModel.State.ERROR -> {
                    // TODO: manage error states
                }
                PostEditViewModel.State.SUCCESS -> {
                    closeAndGoBack()
                }
                else -> { /* Do nothing, should not happen*/
                }
            }
        })

        viewModel.post.observe(viewLifecycleOwner, Observer {
            updateCardColor(it!!.cardColor)
        })

        title.doOnTextChanged { text, start, count, after ->
            val post = viewModel.post.value!!
            post.title = text.toString()
            viewModel.post.postValue(post)
        }

        body.doOnTextChanged { text, start, count, after ->
            viewModel.bodyText.value = body.toFormattedHtml()
            Timber.d(viewModel.bodyText.value)
        }

        color_picker.onColorSelectionListener = {
            val post = viewModel.post.value!!
            post.cardColor = it
            viewModel.post.postValue(post)
        }

        btn_save.setOnClickListener {
            viewModel.updatePost()
        }

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

        context?.apply {
            Aztec.with(body, source, formatting_toolbar, object : IAztecToolbarClickListener {
                override fun onToolbarCollapseButtonClicked() {
                }

                override fun onToolbarExpandButtonClicked() {
                }

                override fun onToolbarFormatButtonClicked(
                    format: ITextFormat,
                    isKeyboardShortcut: Boolean
                ) {
                }

                override fun onToolbarHeadingButtonClicked() {
                }

                override fun onToolbarHtmlButtonClicked() {
                }

                override fun onToolbarListButtonClicked() {
                }

                override fun onToolbarMediaButtonClicked(): Boolean = false

            })
                .setImageGetter(GlideImageLoader(this))
                .setVideoThumbnailGetter(GlideVideoThumbnailLoader(this))
        }

    }

    private fun renderBlogEntry() {
        val post = viewModel.post.value!!
        updateCardColor(post.cardColor)
        title.setText(post.title)
        body.fromHtml(viewModel.bodyText.value ?: "")
    }

    private fun updateCardColor(cardColor: Int) {
        header_background.setBackgroundColor(cardColor)
        val itemsColor = ColorUtils.findTextColorGivenBackgroundColor(cardColor)
        title.setTextColor(itemsColor)
        btn_save.setColorFilter(itemsColor)
        btn_close.setColorFilter(itemsColor)
        applyStatusBarStyle(cardColor)
    }

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }
}