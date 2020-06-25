package ar.edu.unq.pdes.myprivateblog.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import ar.edu.unq.pdes.myprivateblog.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

interface AuthService {
    fun currentUser(): FirebaseUser?
    fun isLoggedIn(): Boolean
    fun signOut()
    fun signInIntent(): Intent?
    fun login(data: Intent?, onSuccess: () -> Unit, onError: (Exception?) -> Unit)
    fun logout()
    fun getMail(): String?
    fun getPassword():String
    fun savePassword(password:String)
}

class FakeAuthService : AuthService {
    override fun currentUser(): FirebaseUser? = null
    override fun getMail(): String? = "a@a.com"
    override fun getPassword(): String = "123456"
    override fun savePassword(password: String) {
    }

    override fun isLoggedIn() = true
    override fun signOut() {}
    override fun signInIntent(): Intent? = null
    override fun login(data: Intent?, onSuccess: () -> Unit, onError: (Exception?) -> Unit) {}
    override fun logout() {}
}


class FirebaseAuthService @Inject constructor(val context: Context) : AuthService {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail().build()
    private val googleClient = GoogleSignIn.getClient(context, googleConf)

    override fun currentUser() = firebaseAuth.currentUser
    override fun getMail(): String? = firebaseAuth.currentUser!!.email

    override fun getPassword(): String {
        val sharedPreferences =context.getSharedPreferences("SP", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(this.getMail(),null).toString();
    }

    override fun savePassword(password: String) {
        val  editor= context.getSharedPreferences("SP",Activity.MODE_PRIVATE).edit();
        editor.putString(this.getMail(),password);
        editor.apply();
    }

    override fun isLoggedIn() = currentUser() != null

    override fun signOut() {
        googleClient.signOut()
    }

    override fun signInIntent() = googleClient.signInIntent

    override fun login(
        data: Intent?,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val token = task.getResult(ApiException::class.java)?.idToken ?: return
            val credential = GoogleAuthProvider.getCredential(token, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) onSuccess() else onError(it.exception)
                }
        } catch (e: ApiException) {
            onError(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}