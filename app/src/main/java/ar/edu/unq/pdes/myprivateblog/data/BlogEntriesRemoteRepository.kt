package ar.edu.unq.pdes.myprivateblog.data

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber


enum class ForeverStartLifecycleOwner : LifecycleOwner {
    INSTANCE;

    private val mLifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    init {
        mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }
}

class BlogEntriesRemoteRepository(blogRepository: BlogEntriesRepository) {
    init {
        val database: DatabaseReference = Firebase.database.reference
        val postsRemote = database.child("posts")

        postsRemote.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Timber.d(error.toException(), "loadPost:onCancelled")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val post = it.getValue<BlogEntry>()
                    blogRepository.updateBlogEntry(post!!)
                }
            }
        })

        val posts: LiveData<List<BlogEntry>> = blogRepository.getAllBlogEntries()
        posts.observe(ForeverStartLifecycleOwner.INSTANCE, Observer { postList ->
            database.updateChildren(mapOf("posts" to postList))
        })
    }
}