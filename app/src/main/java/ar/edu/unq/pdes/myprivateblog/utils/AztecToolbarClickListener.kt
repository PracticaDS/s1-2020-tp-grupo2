package ar.edu.unq.pdes.myprivateblog.utils

import android.content.Context
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.glideloader.GlideImageLoader
import org.wordpress.aztec.glideloader.GlideVideoThumbnailLoader
import org.wordpress.aztec.source.SourceViewEditText
import org.wordpress.aztec.toolbar.AztecToolbar
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener

fun setAztecToolbarClickListener(
    context: Context,
    visualEditor: AztecText,
    sourceEditor: SourceViewEditText,
    toolbar: AztecToolbar
) {
    Aztec.with(visualEditor, sourceEditor, toolbar, AztecToolbarClickListener())
        .setImageGetter(GlideImageLoader(context))
        .setVideoThumbnailGetter(GlideVideoThumbnailLoader(context))
}

class AztecToolbarClickListener : IAztecToolbarClickListener {
    override fun onToolbarCollapseButtonClicked() {}
    override fun onToolbarExpandButtonClicked() {}
    override fun onToolbarHeadingButtonClicked() {}
    override fun onToolbarHtmlButtonClicked() {}
    override fun onToolbarListButtonClicked() {}
    override fun onToolbarMediaButtonClicked(): Boolean = false
    override fun onToolbarFormatButtonClicked(
        format: ITextFormat,
        isKeyboardShortcut: Boolean
    ) {
    }
}
