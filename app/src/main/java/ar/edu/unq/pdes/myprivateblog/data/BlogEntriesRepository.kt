package ar.edu.unq.pdes.myprivateblog.data

import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.OffsetDateTime

class BlogEntriesRepository(
    private val blogEntriesDao: BlogEntriesDao,
    private val remoteRepository: BlogEntriesRemoteRepository
) {
    private var lastSync = OffsetDateTime.now().minusDays(1)

    private fun syncPosts(posts: List<BlogEntry>) {
        if (lastSync.plusMinutes(5).isAfter(OffsetDateTime.now())) return
        lastSync = OffsetDateTime.now()
        val disposable = Flowable.just(posts)
            .flatMap { Flowable.fromIterable(it) }
            .filter { !it.inSync }
            .flatMap { remoteRepositoryUploadFlowable(it.asSynced()) }
            .flatMapCompletable {
                blogEntriesDao
                    .update(it)
                    .subscribeOn(Schedulers.io())
            }.doOnComplete {
                remoteRepository.download {
                    blogEntriesDao
                        .insertAll(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe()
                }
            }.subscribe()
    }

    fun getAllBlogEntries() = LiveDataReactiveStreams.fromPublisher(
        blogEntriesDao
            .getAll()
            .subscribeOn(Schedulers.io())
            .doOnNext { syncPosts(it) }
            .map { posts -> posts.filter { !it.deleted } }
    )

    fun fetchLiveById(entryId: EntityID) =
        LiveDataReactiveStreams.fromPublisher(blogEntriesDao.loadById(entryId))

    fun fetchById(entryId: EntityID) = blogEntriesDao.loadById(entryId)

    fun createBlogEntry(blogEntry: BlogEntry) = blogEntriesDao
        .insert(blogEntry)
        .subscribeOn(Schedulers.io())

    fun updateBlogEntry(blogEntry: BlogEntry) = blogEntriesDao
        .update(blogEntry.asNotSynced())
        .subscribeOn(Schedulers.io())

    private fun remoteRepositoryUploadFlowable(it: BlogEntry): Flowable<BlogEntry> {
        return Flowable.create({ emitter ->
            remoteRepository.upload(it, {
                emitter.onNext(it)
                emitter.onComplete()
            }, { e -> emitter.onError(e) })
        }, BackpressureStrategy.BUFFER)
    }
}