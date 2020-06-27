package ar.edu.unq.pdes.myprivateblog.screens.password_encrypt

import android.content.Context
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.services.AuthService
import ar.edu.unq.pdes.myprivateblog.services.EncryptionService
import javax.inject.Inject

class PasswordEncryptViewModel @Inject constructor(
    val context: Context,
    private val authService: AuthService,
    private val encryptionService: EncryptionService
) : ViewModel() {

    fun isLoggedIn() = authService.isLoggedIn()

    fun savePassword(password: String){
        encryptionService.savePassword(password)
    }
}