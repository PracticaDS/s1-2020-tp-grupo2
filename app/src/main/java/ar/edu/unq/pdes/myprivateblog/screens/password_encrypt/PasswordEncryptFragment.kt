package ar.edu.unq.pdes.myprivateblog.screens.password_encrypt

import android.os.Bundle
import android.view.View
import android.widget.EditText
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

        exitApplication()
    }

    private fun goToPostListing() {
        findNavController().navigate(PasswordEncryptFragmentDirections.navActionPostListing())
    }

    private fun savePassword(){
        val editText: EditText = getMainActivity().findViewById(R.id.password)
        val data: String = editText.text.toString()

        if(data.isNotEmpty() && data.length >= 6){
           save(data)
        } else {
            showMessage(getString(R.string.error_pasword))
        }
    }

    private fun goToUserLogin(){
        findNavController().navigate(PasswordEncryptFragmentDirections.navActionLogin())
    }

    private fun save(password: String){
        viewModel.savePassword(password,
            { goToPostListing()},
            {showMessage(getString(R.string.error_pasword_incorrect))},
            {showMessage(getString(R.string.error_firebase_pasword))})
    }
}