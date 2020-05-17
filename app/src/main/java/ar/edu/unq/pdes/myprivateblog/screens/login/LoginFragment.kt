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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    /* Request code used to invoke sign in user interactions. */
    private val GOOGLE_SING_IN = 100
    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMainActivity().hideKeyboard()

        googleButton.setOnClickListener{

            //configuration
          val googleConf=  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(
              R.string.default_web_client_id)).requestEmail().build()

            val googleClient = GoogleSignIn.getClient(this.getMainActivity(), googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SING_IN)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                findNavController().navigate(LoginFragmentDirections.navActionPostListing())
                            } else {
                                Toast.makeText(getMainActivity(), getString(R.string.error_login),
                                    Toast.LENGTH_LONG).show();
                            }
                        }
                }
            }catch (e:ApiException){
                Toast.makeText(getMainActivity(), getString(R.string.error_login),
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}
