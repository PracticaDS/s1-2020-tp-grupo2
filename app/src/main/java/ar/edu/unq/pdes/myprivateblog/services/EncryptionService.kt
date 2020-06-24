package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import android.util.Base64
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

class EncryptionService @Inject constructor(val context: Context) {

    private val keySpecAlgorithm: String = "AES"
    private val keyFactoryAlgorithm = "PBKDF2WithHmacSHA1"
    private val transformations: String = "AES/CBC/PKCS5Padding"
    private val keySpecIterationCount = 65536
    private val keySpecKeyLength = 256
    private val cipher = Cipher.getInstance(transformations)
    private val fileName = "password"

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


   private fun getPassword() : String{
        var  password = ""
        context.openFileInput(fileName).use{
                it -> password = it.bufferedReader().use{
            it.readText() }
        }
       return password
    }

    fun savePassword(password: String){
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use{
                output ->
            if (output != null) {
                output.write(password.toByteArray())
            }
        }
    }
    @ExperimentalStdlibApi
    fun encryptString(string: String): String{
        val inputStream = ByteArrayInputStream(string.encodeToByteArray())
        val outputStream = ByteArrayOutputStream()
        encrypt(inputStream, outputStream)
        val encodeString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        return encodeString
    }

    @ExperimentalStdlibApi
    fun decrytString(string: String): String {
        val decodedBytes = Base64.decode(string, Base64.DEFAULT)
        val decryptInputStream = ByteArrayInputStream(decodedBytes)
        val decryptOutputStream = ByteArrayOutputStream()
        decrypt(decryptInputStream, decryptOutputStream)
        val decodeString = decryptOutputStream.toByteArray().decodeToString()
        return decodeString
    }

}