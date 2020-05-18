package ar.edu.unq.pdes.myprivateblog.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BlogEntriesRepository(
    private val appDatabase: AppDatabase,
    private val remoteRepository: BlogEntriesRemoteRepository
) {
    private fun syncPosts(posts: Flowable<List<BlogEntry>>) {
        val disposable = Single.fromCallable {
            posts.forEach { list ->
                list.filter { !it.inSync }.forEach {
                    val p = it.synced()
                    remoteRepository.upload(p) {
                        appDatabase.blogEntriesDao().update(p)
                    }
                }
            }
        }

//        remoteRepository.download { list ->
//            list.forEach {
//                Timber.d("${it.uid}")
//            }
//        }
    }

    fun getAllBlogEntries(): LiveData<List<BlogEntry>> {
        val posts = appDatabase.blogEntriesDao().getAll()
        syncPosts(posts)
        return LiveDataReactiveStreams.fromPublisher(posts)
    }

    fun fetchLiveById(entryId: EntityID) =
        LiveDataReactiveStreams.fromPublisher(appDatabase.blogEntriesDao().loadById(entryId))

    fun fetchById(entryId: EntityID) = appDatabase.blogEntriesDao().loadById(entryId)

    fun createBlogEntry(blogEntry: BlogEntry) = appDatabase.blogEntriesDao()
        .insert(blogEntry)
        .subscribeOn(Schedulers.io())

    fun updateBlogEntry(blogEntry: BlogEntry) = appDatabase.blogEntriesDao()
        .update(blogEntry)
        .subscribeOn(Schedulers.io())
}