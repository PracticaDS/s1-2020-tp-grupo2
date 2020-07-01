package ar.edu.unq.pdes.myprivateblog.services

import androidx.lifecycle.map
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class PostService @Inject constructor(
    private val blogRepository: BlogEntriesRepository,
    private val encryptService: EncryptionService,
    private val fileService: FileService
) {

    fun getById(id: EntityID): Flowable<Pair<BlogEntry, String>> = blogRepository
        .fetchById(id)
        .map {
            Pair(decrytBlog(it), encryptService.decrytString(fileService.readBody(it.bodyPath!!)))
        }
        .compose(RxSchedulers.flowableAsync())

    fun update(post: BlogEntry, bodyText: String): Completable =
        Flowable.fromCallable {
            fileService.updateBody(post.bodyPath!!, encryptService.encryptString(bodyText))
            post
        }.flatMapCompletable {
            blogRepository.updateBlogEntry(encryptBlog(it))
        }.compose(RxSchedulers.completableAsync())

    fun create(title: String, bodyText: String, cardColor: Int): Flowable<Long> =
        Flowable.fromCallable {
            fileService.saveBody(encryptService.encryptString(bodyText))
        }.flatMapSingle {
            blogRepository.createBlogEntry(
                encryptBlog(BlogEntry(title = title, bodyPath = it, cardColor = cardColor))
            )
        }.compose(RxSchedulers.flowableAsync())

    fun delete(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(encryptBlog(post.asDeleted()))
        .compose(RxSchedulers.completableAsync())

    fun restore(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(encryptBlog(post.asRestored()))
        .compose(RxSchedulers.completableAsync())


    fun getAllBlogEntries() = blogRepository.getAllBlogEntries().map { posts ->
        posts.map { decrytBlog(it) }
    }

    private fun decrytBlog(blogEntry: BlogEntry): BlogEntry {
        val decodedTitle = encryptService.decrytString(blogEntry.title)
        return blogEntry.copy(title = decodedTitle)
    }

    private fun encryptBlog(blogEntry: BlogEntry): BlogEntry {
        val encodeTitle = encryptService.encryptString(blogEntry.title)
        return blogEntry.copy(title = encodeTitle)
    }
}