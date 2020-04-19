package ar.edu.unq.pdes.myprivateblog

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.web.assertion.WebAssertion
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.model.Atoms
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

fun hasItem(matcher: Matcher<View?>) =
    object : BoundedMatcher<View?, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("has item: ")
            matcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val adapter = view.adapter
            return (0 until adapter!!.itemCount).any { position ->
                val type = adapter.getItemViewType(position)
                val holder = adapter.createViewHolder(view, type)
                adapter.onBindViewHolder(holder, position)
                return matcher.matches(holder.itemView)
            }
        }
    }

fun atPosition(position: Int, itemMatcher: Matcher<View?>) =
    object : BoundedMatcher<View?, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position)
                ?: return false // has no item on such position
            return itemMatcher.matches(viewHolder.itemView)
        }
    }

fun withTintColor(expectedColor: Int) =
    object : BoundedMatcher<View?, View>(
        View::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("Checking the matcher on received view: ")
            description.appendText("with expectedStatus=$expectedColor")
        }

        override fun matchesSafely(view: View): Boolean {
            return view.backgroundTintList?.defaultColor == expectedColor
        }
    }

fun withWebViewTextMatcher(expectedText: String): WebAssertion<String> =
    WebViewAssertions.webMatches(
        Atoms.script(
            "return document.documentElement.innerHTML;",
            Atoms.castOrDie(String::class.java)
        ),
        CoreMatchers.`is`(expectedText)
    )


fun withBoldStyle(resourceId: Int): Matcher<View?>?{
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("has Bold Text with resource" );
        }

        override fun matchesSafely(view : View): Boolean {
            val textView: TextView = view.findViewById(resourceId);
            return (textView.getTypeface().getStyle() == Typeface.BOLD);
        }
    };
}

fun hasItemCount(expectedCount: Int): RecyclerViewItemCountAssertion {
    return RecyclerViewItemCountAssertion(expectedCount)
}

class RecyclerViewItemCountAssertion(val amountPostsExpected: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter

        ViewMatchers.assertThat(adapter?.itemCount, CoreMatchers.`is`(amountPostsExpected))
    }
}