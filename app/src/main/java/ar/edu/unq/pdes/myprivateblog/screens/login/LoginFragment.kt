package ar.edu.unq.pdes.myprivateblog.screens.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.screens.posts_listing.PostsListingFragmentDirections
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_posts_listing.*
import java.io.FileOutputStream

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isLoggedIn()) {
            goToPostListing()
        } else {
            viewModel.signOut()
            googleButton.setOnClickListener {
                logingUser()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.login(
            requestCode,
            data,
            { goToPostListing() },
            { showMessage(getString(R.string.error_login)) }
        )
    }

    private fun showMessage(message: String) {
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun goToPostListing() {
        savePassword()
        findNavController().navigate(LoginFragmentDirections.navActionPostListing())
    }

    private fun logingUser() {
        startActivityForResult(viewModel.signInIntent(), viewModel.GOOGLE_SING_IN)
    }

    private fun savePassword(){
        val editText: EditText = getMainActivity().findViewById(R.id.password)
        val data: String = editText.text.toString()
        viewModel.savePassword(data)
    }
}

