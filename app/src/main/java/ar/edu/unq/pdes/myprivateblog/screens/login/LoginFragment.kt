package ar.edu.unq.pdes.myprivateblog.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        if(firebaseAuth.currentUser != null){
            goToPostListing()
        }
        else {
            viewModel.configureGoogleSignIn()
            googleButton.setOnClickListener {
                logingUser()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== viewModel.GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                goToPostListing()
                            } else {
                                showMessage(getString(R.string.error_login))
                            }
                        }
                }
            }catch (e:ApiException){
                showMessage(getString(R.string.error_login))
            }
        }
    }
    private fun showMessage(message: String){
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun goToPostListing(){
        findNavController().navigate(LoginFragmentDirections.navActionPostListing())
    }

    private fun logingUser() {
        val signInIntent = viewModel.googleClient.signInIntent
        startActivityForResult(signInIntent, viewModel.GOOGLE_SING_IN)
    }

}
