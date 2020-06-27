package ar.edu.unq.pdes.myprivateblog.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.isLoggedIn()) {
            goToPostListing()
        } else {
            viewModel.signOut()
            googleButton.setOnClickListener {
                loginUser()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.login(
            requestCode,
            data,
            { goToPasswordEncryptOrPostListing() },
            { showMessage(getString(R.string.error_login)) }
        )
    }

    private fun showMessage(message: String) {
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun goToPostListing() {
        findNavController().navigate(LoginFragmentDirections.navActionPostListing())
    }

    private fun goToPasswordEncrypt() {
        findNavController().navigate(LoginFragmentDirections.navActionPasswordEncrypt())
    }

    private fun goToPasswordEncryptOrPostListing(){
        if (viewModel.isPasswordSave()){
            goToPostListing()
        } else {
            goToPasswordEncrypt()
        }
    }

    private fun loginUser() {
        startActivityForResult(viewModel.signInIntent(), viewModel.GOOGLE_SING_IN)
    }
}

