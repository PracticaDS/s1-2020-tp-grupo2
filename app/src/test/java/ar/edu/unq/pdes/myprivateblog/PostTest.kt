package ar.edu.unq.pdes.myprivateblog

import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import org.junit.Assert.*
import org.junit.Test

class PostTest {
    @Test
    fun can_be_deleted() {
        val blog = BlogEntry().asDeleted()
        assertTrue(blog.deleted)
    }

    @Test
    fun can_be_restored() {
        val blog = BlogEntry().asRestored()
        assertFalse(blog.deleted)
    }

    @Test
    fun can_be_synced() {
        val blog = BlogEntry().asSynced()
        assertTrue(blog.inSync)
    }

    @Test
    fun can_be_not_synced() {
        val blog = BlogEntry().asNotSynced()
        assertFalse(blog.inSync)
    }

}

