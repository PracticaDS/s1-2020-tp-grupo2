package ar.edu.unq.pdes.myprivateblog

import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun withTintColor(expectedColor: Int): Matcher<View?>? {
    return object : BoundedMatcher<View?, View>(View::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("Checking the matcher on received view: ")
            description.appendText("with expectedStatus=$expectedColor")
        }

        override fun matchesSafely(view: View): Boolean {
            return view.backgroundTintList?.defaultColor == expectedColor
        }
    }
}

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