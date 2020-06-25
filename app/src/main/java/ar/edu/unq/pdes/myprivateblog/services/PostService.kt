package ar.edu.unq.pdes.myprivateblog.services

import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class PostService @Inject constructor(
    private val blogRepository: BlogEntriesRepository,
    private val fileService: FileService
) {

    fun getById(id: EntityID): Flowable<Pair<BlogEntry, String>> = blogRepository
        .fetchById(id)
        .map { Pair(it, fileService.readBody(it.bodyPath!!)) }
        .compose(RxSchedulers.flowableAsync())

    fun update(post: BlogEntry, bodyText: String): Completable =
        Flowable.fromCallable {
            fileService.updateBody(post.bodyPath!!, bodyText)
            post
        }
            .flatMapCompletable { blogRepository.updateBlogEntry(it) }
            .compose(RxSchedulers.completableAsync())

    fun create(title: String, bodyText: String, cardColor: Int): Flowable<Long> =
        Flowable.fromCallable {
            fileService.saveBody(bodyText)
        }.flatMapSingle {
            blogRepository.createBlogEntry(
                BlogEntry(title = title, bodyPath = it, cardColor = cardColor)
            )
        }.compose(RxSchedulers.flowableAsync())

    fun delete(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(post.asDeleted())
        .compose(RxSchedulers.completableAsync())

    fun restore(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(post.asRestored())
        .compose(RxSchedulers.completableAsync())

    fun getAllBlogEntries() = blogRepository.getAllBlogEntries()
}