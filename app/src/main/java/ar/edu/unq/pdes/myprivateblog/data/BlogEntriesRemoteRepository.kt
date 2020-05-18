package ar.edu.unq.pdes.myprivateblog.data

import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class BlogEntriesRemoteRepository(db: FirebaseFirestore) {

    private val postsCollection = db.collection("posts")

    fun upload(blog: BlogEntry, onSuccess: () -> Unit) {
        postsCollection.document(blog.uid.toString()).set(blog)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e ->
                Timber.w(e, "Error uploading post")
            }
    }

    fun download(onSuccess: (List<BlogEntry>) -> Unit) {
        postsCollection.get()
            .addOnSuccessListener { result ->
                onSuccess(result.map { it.toObject(BlogEntry::class.java) })
            }
            .addOnFailureListener {
                Timber.d(it, "Error download posts")
            }
    }
}