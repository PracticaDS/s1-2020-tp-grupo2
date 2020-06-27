package ar.edu.unq.pdes.myprivateblog.data

import ar.edu.unq.pdes.myprivateblog.services.AuthService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class FirebaseRepository(
    private val db: FirebaseFirestore,
    private val authService: AuthService
) {

    private fun getUserDocument(): DocumentReference? {
        val user = authService.currentUser() ?: return null
        return db.collection("users")
            .document(user.uid)
    }

    fun withPostsCollection(withCollection: (CollectionReference) -> Unit) {
        val userDocument = getUserDocument() ?: return
        withCollection(userDocument.collection("posts"))
    }

    private val PASSWORD_FIELD = "password"

    fun savePassword(password: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val userDocument = getUserDocument() ?: return
        userDocument.set(hashMapOf(PASSWORD_FIELD to password))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e) }
    }

    fun getPassword(onSuccess: (String?) -> Unit, onError: (Exception) -> Unit) {
        val userDocument = getUserDocument() ?: return
        userDocument.get()
            .addOnSuccessListener { result -> onSuccess(result.getString(PASSWORD_FIELD)) }
            .addOnFailureListener { e -> onError(e) }
    }

//    fun asd(){
//        savePassword("asdasd", {  }, { e -> e.message })
//        getPassword({ password -> password == }, { e -> })
//    }
}