package ar.edu.unq.pdes.myprivateblog.data

import ar.edu.unq.pdes.myprivateblog.services.FileService
import timber.log.Timber

data class BlogPair(val blog: BlogEntry?, val body: String) {
    constructor() : this(null, "")
}

class BlogEntriesRemoteRepository(
    private val firebaseRepository: FirebaseRepository,
    private val fileService: FileService
) {

    fun upload(blog: BlogEntry, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        firebaseRepository.withPostsCollection { postsCollection ->
            postsCollection.document(blog.uid.toString())
                .set(BlogPair(blog, fileService.readBody(blog.bodyPath!!)))
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onError(e) }
        }
    }

    fun download(onSuccess: (List<BlogEntry>) -> Unit) {
        firebaseRepository.withPostsCollection { postsCollection ->
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