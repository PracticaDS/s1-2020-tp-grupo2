package ar.edu.unq.pdes.myprivateblog.data

import ar.edu.unq.pdes.myprivateblog.services.AuthService
import ar.edu.unq.pdes.myprivateblog.services.FileService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.lang.Exception

data class BlogPair(val blog: BlogEntry?, val body: String){
    constructor() : this(null, "")
}

class BlogEntriesRemoteRepository(
    private val db: FirebaseFirestore,
    private val authService: AuthService,
    private val fileService: FileService
) {

    private fun withPostsCollection(withCollection: (CollectionReference) -> Unit) {
        val user = authService.currentUser() ?: return
        val postsCollection = db.collection("users")
            .document(user.uid).collection("posts")
        withCollection(postsCollection)
    }

    fun upload(blog: BlogEntry, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        withPostsCollection { postsCollection ->
            postsCollection.document(blog.uid.toString()).set(BlogPair(blog, fileService.readBody(blog.bodyPath!!)))
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onError(e) }
        }
    }

    fun download(onSuccess: (List<BlogEntry>) -> Unit) {
        withPostsCollection { postsCollection ->
            postsCollection.get()
                .addOnSuccessListener { result ->
                    val pairs = result.map { it.toObject(BlogPair::class.java) }
                    pairs.forEach { fileService.updateBody(it.blog!!.bodyPath, it.body) }
                    onSuccess(pairs.map { it.blog!! })
                }
                .addOnFailureListener {
                    Timber.d(it, "Error download posts")
                }
        }
    }
}