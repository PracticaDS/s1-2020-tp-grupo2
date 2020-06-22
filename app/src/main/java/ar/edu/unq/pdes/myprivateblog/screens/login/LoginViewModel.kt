package ar.edu.unq.pdes.myprivateblog.screens.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.services.AuthService
import ar.edu.unq.pdes.myprivateblog.services.EncryptionService
import java.net.PasswordAuthentication
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val context: Context,
    private val authService: AuthService,
    private val encryptionService: EncryptionService
) : ViewModel() {
    val GOOGLE_SING_IN = 100

    fun isLoggedIn() = authService.isLoggedIn()
    fun signOut() {
        authService.signOut()
    }

    fun signInIntent() = authService.signInIntent()

    fun login(
        requestCode: Int,
        data: Intent?,
        onSuccess: () -> Unit,
        onError: (Exception?) -> Unit
    ) {
        if (requestCode != GOOGLE_SING_IN) return
        authService.login(data, onSuccess, onError)
    }

    fun savePassword(password: String){
       // encryptionService.savePassword(password)
    }

}
