package ar.edu.unq.pdes.myprivateblog

import android.content.Context
import ar.edu.unq.pdes.myprivateblog.services.EncryptionService
import ar.edu.unq.pdes.myprivateblog.services.FakeAuthService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

class EncryptionServiceTest {

    private lateinit var encryptionService: EncryptionService
    private lateinit var context: Context

    @Before
    fun setup() {
        context = mock(Context::class.java)
        encryptionService = EncryptionService(context,FakeAuthService())
    }

    @ExperimentalStdlibApi
    @Test
    fun whenEncryptingAString_itShouldBeTheSameAfterDecrypting() {
        val someString = "Prueba"
        val password = "Password"
        //  encryptionService.savePassword(password)

        val inputStream = ByteArrayInputStream(
            someString.toByteArray()
        )

        val outputStream = ByteArrayOutputStream()

        encryptionService.encrypt(inputStream, outputStream)
        val encodeString = outputStream.toByteArray()
        val encodedString: String = Base64.getEncoder().encodeToString(encodeString)
        val decodedBytes = Base64.getDecoder().decode(encodedString)
        val decryptInputStream = ByteArrayInputStream(decodedBytes)

        val decryptOutputStream = ByteArrayOutputStream()

        encryptionService.decrypt(decryptInputStream, decryptOutputStream)

        val decodeString = decryptOutputStream.toByteArray().decodeToString()

        assertEquals(someString, decodeString)
    }
}