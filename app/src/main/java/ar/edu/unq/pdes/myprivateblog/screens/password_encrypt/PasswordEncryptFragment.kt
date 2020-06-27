package ar.edu.unq.pdes.myprivateblog.screens.password_encrypt

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import kotlinx.android.synthetic.main.fragment_password_encrypt.*

class PasswordEncryptFragment : BaseFragment(R.layout.fragment_password_encrypt) {
    private val viewModel by viewModels<PasswordEncryptViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewModel.isLoggedIn()){
            goToUserLogin()
        }

        buttonEncrypt.setOnClickListener {
            savePassword()
        }
    }

    private fun goToPostListing() {
        findNavController().navigate(PasswordEncryptFragmentDirections.navActionPostListing())
    }

    private fun savePassword(){
        val editText: EditText = getMainActivity().findViewById(R.id.password)
        val data: String = editText.text.toString()

        if(data.isNotEmpty() && data.length >= 6){
            viewModel.savePassword(data)
            goToPostListing()
        } else {
            showMessage(getString(R.string.error_pasword))
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun goToUserLogin(){
        findNavController().navigate(PasswordEncryptFragmentDirections.navActionLogin())
    }
}