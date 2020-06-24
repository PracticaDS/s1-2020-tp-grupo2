package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject

class PostService @Inject constructor(
    private val blogRepository: BlogEntriesRepository,
    val context: Context,
    val encrypService: EncryptionService
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

    private fun readBody(bodyPath: String): String {
        val file = File(context.filesDir, bodyPath)
        return if (file.exists()) file.readText() else ""
    }

    @ExperimentalStdlibApi
    fun getById(id: EntityID): Flowable<Pair<BlogEntry, String>> = blogRepository
        .fetchById(id)
        .map { Pair(decrytBlog(it), readBody(decrytBlog(it).bodyPath!!)) }
        .compose(RxSchedulers.flowableAsync())

    @ExperimentalStdlibApi
    fun update(post: BlogEntry, bodyText: String): Completable =
        Flowable.fromCallable {
            updateBody(post.bodyPath!!, bodyText)
            post
        }
            .flatMapCompletable { blogRepository.updateBlogEntry(encryptBlog(it)) }
            .compose(RxSchedulers.completableAsync())

    @ExperimentalStdlibApi
    fun create(title: String, bodyText: String, cardColor: Int): Flowable<Long> =
        Flowable.fromCallable {
            saveBody(bodyText)
        }.flatMapSingle {
            blogRepository.createBlogEntry(
                encryptBlog(BlogEntry(title = title, bodyPath = it, cardColor = cardColor))
            )
        }.compose(RxSchedulers.flowableAsync())

    fun delete(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(post.asDeleted())
        .compose(RxSchedulers.completableAsync())

    fun restore(post: BlogEntry): Completable = blogRepository
        .updateBlogEntry(post.asRestored())
        .compose(RxSchedulers.completableAsync())

    @ExperimentalStdlibApi
    fun getAllBlogEntries() =
        blogRepository.getAllBlogEntries().map { posts ->
            posts.map {
                decrytBlog(it)
            }
        }


    @ExperimentalStdlibApi
    private fun decrytBlog(blogEntry : BlogEntry): BlogEntry {
        val title = encrypService.decrytString(blogEntry.title)
        return BlogEntry(blogEntry.uid,title, blogEntry.bodyPath,
            blogEntry.imagePath, blogEntry.deleted,
            blogEntry.date, blogEntry.cardColor, blogEntry.inSync)
    }
    @ExperimentalStdlibApi
    private fun encryptBlog(blogEntry : BlogEntry): BlogEntry {
        val encodeTitle = encrypService.encryptString(blogEntry.title)
        return BlogEntry(title = encodeTitle, bodyPath = blogEntry.bodyPath, cardColor = blogEntry.cardColor)
    }
}