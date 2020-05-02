package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.helper.logEventSavePost
import ar.edu.unq.pdes.myprivateblog.utils.setAztecToolbarClickListener
import kotlinx.android.synthetic.main.fragment_post_edit.*

class PostCreateFragment : BaseFragment(R.layout.fragment_post_edit) {
    private val viewModel by viewModels<PostCreateViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                PostCreateViewModel.State.ERROR -> {
                    // TODO: manage error states
                }
                PostCreateViewModel.State.SUCCESS -> {
                    findNavController().navigate(
                        PostCreateFragmentDirections.navActionSaveNewPost(viewModel.post)
                    )
                }
                else -> {
                }
            }
        })

        viewModel.cardColor.observe(viewLifecycleOwner, Observer {
            header_background.setBackgroundColor(it)
            val itemsColor = ColorUtils.findTextColorGivenBackgroundColor(it)
            title.setTextColor(itemsColor)
            title.setHintTextColor(itemsColor)
            btn_save.setColorFilter(itemsColor)
            btn_close.setColorFilter(itemsColor)

            applyStatusBarStyle(it)
        })

        title.doOnTextChanged { text, start, count, after ->
            viewModel.titleText.postValue(text.toString())
        }
        body.doOnTextChanged { text, start, count, after ->
            viewModel.bodyText.value = body.toFormattedHtml()
        }
        btn_save.setOnClickListener {
            logEventSavePost(getMainActivity())
            viewModel.createPost() }
        btn_close.setOnClickListener { closeAndGoBack() }
        color_picker.onColorSelectionListener = {
            viewModel.cardColor.postValue(it)
        }

        context?.apply { setAztecToolbarClickListener(this, body, source, formatting_toolbar) }
    }
}