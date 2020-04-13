package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class PostsListingTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun whenTappingOnNewPostFab_postCreationScreenShouldOpen() {

        onView(withId(R.id.create_new_post))
            .perform(click())


        onView(withId(R.id.title))
            .check(matches(withHint(R.string.hint_post_title)))
    }

    @Test
    fun whenCreatingPost_shouldNavigateToPostDetail() {
        onView(withId(R.id.create_new_post))
            .perform(click())

        val postTitle = "post1"

        onView(withId(R.id.title))
            .perform(typeText(postTitle))

        val bodyText = "This is the body"
        onView(withId(R.id.body))
            .perform(typeText(bodyText))

        val pickedColor = Color.parseColor("#b39ddb")

        onView(withTintColor(pickedColor))
            .perform(click())

        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_save))
            .perform(click())


        onView(withId(R.id.title))
            .check(matches(withText(postTitle)))

//        onView(withId(R.id.body)).check(
//            matches(withWebViewTextMatcher(bodyText))
//        )

    }


}

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
//
//fun withWebViewTextMatcher(expectedText: String): Matcher<View?>? {
//    return object : BoundedMatcher<View?, WebView>(WebView::class.java) {
//
//        override fun describeTo(description: Description) {
//            description.appendText("Checking the matcher on received view: ")
//            description.appendText("with expectedStatus=$expectedText")
//        }
//
//        override fun matchesSafely(webView: View): Boolean {
//            val webViewBody: String = runBlocking {
//                suspendCoroutine<String> { cont ->
//                    webView.evaluateJavascript(
//                        "(function() { return document.documentElement.innerText; })();"
//                    ) {
//                        cont.resume(it)
//                    }
//                }
//            }
//
//            webView.backgroundTintList?.defaultColor
//
//
//            val expected = "\"" + expectedText + "\""
//            return expected == webViewBody
//        }
//
//        suspend fun fetchWebViewContent(webView: WebView): String = suspendCoroutine { cont ->
//            webView.evaluateJavascript(
//                "(function() { return document.documentElement.innerText; })();"
//            ) {
//                cont.resume(it)
//            }
//        }
//
//    }
//}