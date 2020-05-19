package ar.edu.unq.pdes.myprivateblog.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import javax.inject.Inject

class LoginViewModel @Inject constructor( val context: Context) : ViewModel() {

    val GOOGLE_SING_IN = 100
    lateinit var googleConf: GoogleSignInOptions
    lateinit var googleClient: GoogleSignInClient

    fun configureGoogleSignIn() {
       googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString( R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleClient = GoogleSignIn.getClient(context, googleConf)
        googleClient.signOut()
    }
}
