package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import kotlinx.android.synthetic.main.fragment_post_edit.*

class PostEditFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    private val viewModel by viewModels<PostEditViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO!!!

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

    }

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }
}