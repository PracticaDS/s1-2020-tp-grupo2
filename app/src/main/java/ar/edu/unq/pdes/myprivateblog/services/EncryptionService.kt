package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import android.util.Base64
import ar.edu.unq.pdes.myprivateblog.data.FirebaseRepository
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptionService @Inject constructor(val context: Context,val authService: AuthService, val firebaseRepository: FirebaseRepository) {

    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"
    private val keySpecIterationCount = 1536
    private val keySpecKeyLength = 256
    private val cipher = Cipher.getInstance(transformations)

    fun encrypt(plainText: InputStream, outputStream: OutputStream) {

        val salt = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(salt)

        val iv = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(iv)

        val password = getPassword()

        val skeySpec = getSecretKeySpec(password, salt)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(iv))
        val output = CipherOutputStream(outputStream,cipher)

        outputStream.use {
            it.write(salt)
            it.write(iv)
        }

        output.use {
            plainText.use {
                it.copyTo(output)
            }
        }
    }

    fun decrypt(encryptedInput: InputStream, outputStream: OutputStream) {

        val password = getPassword()
        val salt = ByteArray(cipher.blockSize)
        val iv = ByteArray(cipher.blockSize)
        encryptedInput.use {
            it.read(salt,0,cipher.blockSize)
            it.read(iv,0,cipher.blockSize)
        }

        val skeySpec = getSecretKeySpec(password, salt)

        cipher.init(Cipher.DECRYPT_MODE, skeySpec, IvParameterSpec(iv))
        val cipherInput = CipherInputStream(encryptedInput,cipher)

        cipherInput.use {
            it.copyTo(outputStream)
        }
    }

    private fun getSecretKeySpec(password: String, salt: ByteArray): SecretKeySpec {
        val spec: KeySpec = PBEKeySpec(
            password.toCharArray(),
            salt,
            keySpecIterationCount,
            keySpecKeyLength
        )
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(keyFactoryAlgorithm)
        val secretKey: SecretKey = factory.generateSecret(spec)
        return SecretKeySpec(secretKey.encoded, keySpecAlgorithm)
    }


    private fun getPassword() = authService.getPassword()


    @OptIn(ExperimentalStdlibApi::class)
    fun encryptString(string: String): String{
        val inputStream = ByteArrayInputStream(string.encodeToByteArray())
        val outputStream = ByteArrayOutputStream()
        encrypt(inputStream, outputStream)
        val encodeString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return encodeString
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun decrytString(string: String): String {
        val decodedBytes = Base64.decode(string, Base64.DEFAULT)
        val decryptInputStream = ByteArrayInputStream(decodedBytes)
        val decryptOutputStream = ByteArrayOutputStream()
        decrypt(decryptInputStream, decryptOutputStream)
        val decodeString = decryptOutputStream.toByteArray().decodeToString()
        return decodeString
    }

    fun savePassword(password: String,  onSuccess: () -> Unit, onErrorPasswordInvalid: () -> Unit, onErrorGetPassord: () -> Unit) {
        val hasPassword = password.hashCode().toString()
        firebaseRepository.getPassword({result->savePasswordOnCellphone(hasPassword, result, onSuccess, onErrorPasswordInvalid, onErrorGetPassord)}, {onErrorGetPassord()})
    }

    private fun savePasswordOnCellphone(passwordUser: String, passwordSave: String?,  onSuccess: () -> Unit, onErrorPasswordInvalid: () -> Unit, onErrorGetPassord: () -> Unit){
        if(passwordSave.isNullOrEmpty()){
            savePasswordOnFirebase(passwordUser, onSuccess, onErrorGetPassord)
        } else if (passwordUser.equals(passwordSave)){
            save(passwordUser, onSuccess)
        } else {
            onErrorPasswordInvalid()
        }
    }

    private fun savePasswordOnFirebase(password: String, onSuccess: () -> Unit, onErrorGetPassord: () -> Unit){
        firebaseRepository.savePassword(password, {save(password, onSuccess)}, {onErrorGetPassord()} )
    }

    private fun save(password: String, onSuccess: () -> Unit){
        authService.savePassword(password)
        onSuccess()
    }
}