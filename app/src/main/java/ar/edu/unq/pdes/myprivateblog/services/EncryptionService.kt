package ar.edu.unq.pdes.myprivateblog.services


import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import ar.edu.unq.pdes.myprivateblog.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptionService @Inject constructor(
    val context: Context
) {
    private val charset = Charsets.UTF_8
    private val SALT_SIZE = 16
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun generateSecretKey(): SecretKey? {
        val secureRandom = SecureRandom()
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator?.init(256, secureRandom)
        return keyGenerator?.generateKey()
    }

    private fun encodeSecretKey(secretKey: SecretKey): String = Base64.encodeToString(secretKey.encoded, Base64.NO_WRAP)

    private fun decodeSecretKey(key: String): SecretKey {
        val decodedKey = Base64.decode(key, Base64.NO_WRAP)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
    }

    fun generateSalt(): ByteArray {
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        val salt = ByteArray(SALT_SIZE)
        secureRandom.nextBytes(salt)
        return salt
    }

    fun concatWithSalt(salt: ByteArray, encryptedData: ByteArray): ByteArray = salt + encryptedData

    fun getSaltAndEncryptedData(data: ByteArray): Pair<ByteArray, ByteArray> {
        val salt = data.copyOfRange(0, SALT_SIZE)
        val encryptedData = data.copyOfRange(SALT_SIZE, data.size)
        return Pair(encryptedData, salt)
    }

    fun encrypt(yourKey: SecretKey, plainText: String, salt: ByteArray): ByteArray {
        val plainTextBase64 = plainText.toByteArray(charset)
        val data = yourKey.encoded
        val skeySpec = SecretKeySpec(data, 0, data.size, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(salt))
        return cipher.doFinal(plainTextBase64)
    }

    fun decrypt(yourKey: SecretKey, encryptedData: ByteArray, salt: ByteArray): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, yourKey, IvParameterSpec(salt))
        val decrypted = cipher.doFinal(encryptedData)
        return decrypted.toString(charset)
    }

    fun storeSecretKey(secretKey: SecretKey) {
        val encodedSecretKey = encodeSecretKey(secretKey)
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit {
            this.putString(context.getString(R.string.secret_key_key), encodedSecretKey)
            this.commit()
        }
    }

    fun retrieveSecretKey(): SecretKey? {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        val encodedSecretKey = sharedPreferences.getString(context.getString(R.string.secret_key_key), null)
        return if (encodedSecretKey != null) {
            decodeSecretKey(encodedSecretKey)
        } else {
            null
        }
    }

    fun clearSecretKey() {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit {
            this.putString(context.getString(R.string.secret_key_key), null)
            this.commit()
        }
    }

    fun uploadSecretKey(secretKey: SecretKey): Task<Void>? {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userUid = user.uid
            val encodedSecretKey = encodeSecretKey(secretKey)
            val encodedSecretKeyObject = mapOf<String, String>( "token" to encodedSecretKey )
            return db.document("users/$userUid").set(encodedSecretKeyObject, SetOptions.merge())
        }
        return null
    }

    fun downloadSecretKey(callback: (SecretKey?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userUid = user.uid
            db.document("users/$userUid").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                val encodedSecretKey = documentSnapshot?.getString("token")
                val secretKey = if (encodedSecretKey != null) decodeSecretKey(encodedSecretKey) else null
                callback(secretKey)
            }
        }
    }
}