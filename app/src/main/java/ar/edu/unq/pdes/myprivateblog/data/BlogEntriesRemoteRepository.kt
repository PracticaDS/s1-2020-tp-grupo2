package ar.edu.unq.pdes.myprivateblog.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.lang.Exception

class BlogEntriesRemoteRepository(private val db: FirebaseFirestore) {

    private fun withPostsCollection(withCollection: (CollectionReference) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val postsCollection = db.collection("users")
            .document(user.uid).collection("posts")
        withCollection(postsCollection)
    }

    fun upload(blog: BlogEntry, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        withPostsCollection { postsCollection ->
            postsCollection.document(blog.uid.toString()).set(blog)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onError(e) }
        }
    }

    fun download(onSuccess: (List<BlogEntry>) -> Unit) {
        withPostsCollection { postsCollection ->
            postsCollection.get()
                .addOnSuccessListener { result ->
                    onSuccess(result.map { it.toObject(BlogEntry::class.java) })
                }
                .addOnFailureListener {
                    Timber.d(it, "Error download posts")
                }
        }
    }
}