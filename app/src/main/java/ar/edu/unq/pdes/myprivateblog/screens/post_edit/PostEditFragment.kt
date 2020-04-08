package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import kotlinx.android.synthetic.main.fragment_post_edit.*

class PostEditFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    private val viewModel by viewModels<PostEditViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {
                PostEditViewModel.State.EDITING -> {
                    // TODO
                }

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

        viewModel.titleText.observe(viewLifecycleOwner, Observer {
            //TODO: use when implementing the edit mode
        })

        viewModel.bodyText.observe(viewLifecycleOwner, Observer {
            //TODO: use when implementing the edit mode
        })

        title.doOnTextChanged { text, start, count, after ->
            viewModel.titleText.value = text.toString()
        }

        body.doOnTextChanged { text, start, count, after ->
            viewModel.bodyText.value = text.toString()
        }

        btn_save.setOnClickListener {
            viewModel.editPost()
        }

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

    }

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }
}