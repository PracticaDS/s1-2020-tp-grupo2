package ar.edu.unq.pdes.myprivateblog.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.core.view.children
import ar.edu.unq.pdes.myprivateblog.R


class ColorPicker @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    private var selectedView: View? = null
    var onColorSelectionListener: (Int) -> Unit = {}

    init {
        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.view_color_picker, this, true)

        view.findViewById<LinearLayout>(R.id.color_container).children.forEach {
            it.setOnClickListener { colorView ->
                selectedView = colorView
                onColorSelectionListener(colorView.backgroundTintList?.defaultColor ?: Color.GREEN)
            }
        }
    }

}