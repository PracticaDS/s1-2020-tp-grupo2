package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val blogRepository: BlogEntriesRepository,
    val context: Context
) {

    private fun updateBody(bodyPath: String, bodyText: String) {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(bodyPath, Context.MODE_PRIVATE))
        outputStreamWriter.use { it.write(bodyText) }
    }

    private fun saveBody(bodyText: String): String {
        val fileName = UUID.randomUUID().toString() + ".body"
        updateBody(fileName, bodyText)
        return fileName
    }

    private fun readBody(bodyPath: String) = File(context.filesDir, bodyPath).readText()

    fun getById(id: EntityID): Flowable<Pair<BlogEntry, String>> = blogRepository
        .fetchById(id)
        .map { Pair(it, readBody(it.bodyPath!!)) }
        .compose(RxSchedulers.flowableAsync())

    fun update(post: BlogEntry, bodyText: String): Completable =
        Flowable.fromCallable {
            updateBody(post.bodyPath!!, bodyText)
            post
        }
            .flatMapCompletable { blogRepository.updateBlogEntry(it) }
            .compose(RxSchedulers.completableAsync())

    fun create(title: String, bodyText: String, cardColor: Int): Flowable<Long> =
        Flowable.fromCallable {
            saveBody(bodyText)
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