package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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

class PostCreateFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    private val viewModel by viewModels<PostCreateViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                PostCreateViewModel.State.EDITING -> {
                    // TODO
                }

                PostCreateViewModel.State.ERROR -> {
                    // TODO: manage error states
                }

                PostCreateViewModel.State.SUCCESS -> {
                    findNavController().navigate(
                        PostCreateFragmentDirections.navActionSaveNewPost(
                            viewModel.post
                        )
                    )
                }

                else -> { /* Do nothing, should not happen*/
                }
            }
        })

        viewModel.titleText.observe(viewLifecycleOwner, Observer {
            //TODO: use when implementing the edit mode
        })

        viewModel.bodyText.observe(viewLifecycleOwner, Observer {
            //TODO: use when implementing the edit mode
        })

        viewModel.cardColor.observe(viewLifecycleOwner, Observer {
            header_background.setBackgroundColor(it)
            applyStatusBarStyle(it)
            if (ColorUtils.luminance(it) > 0.7) {
                title.setTextColor(context?.getColor(R.color.grey_darken_10) ?: Color.DKGRAY)
            } else {
                title.setTextColor(Color.WHITE)
            }
        })

        title.doOnTextChanged { text, start, count, after ->
            viewModel.titleText.postValue(text.toString())
        }

        body.doOnTextChanged { text, start, count, after ->
            viewModel.bodyText.value = body.toFormattedHtml()
            Timber.d(viewModel.bodyText.value)
        }

        btn_save.setOnClickListener {
            viewModel.createPost()
        }

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

        color_picker.onColorSelectionListener = {
            viewModel.cardColor.postValue(it)
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

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }
}