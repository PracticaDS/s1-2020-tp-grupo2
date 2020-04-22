package ar.edu.unq.pdes.myprivateblog

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.web.sugar.Web
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

fun onTitle() = Espresso.onView(ViewMatchers.withId(R.id.title))
fun onBody() = Espresso.onView(ViewMatchers.withId(R.id.body))

fun clickPickerColor(pickedColor: Int) =
    Espresso.onView(withTintColor(pickedColor)).perform(ViewActions.click())

fun goToCreatePost() = Espresso.onView(ViewMatchers.withId(R.id.create_new_post))
    .perform(ViewActions.click())
fun clickSaveBtn() = Espresso.onView(ViewMatchers.withId(R.id.btn_save))
    .perform(ViewActions.click())
fun clickEditBtn() = Espresso.onView(ViewMatchers.withId(R.id.btn_edit))
    .perform(ViewActions.click())
fun clickBackBtn() = Espresso.onView(ViewMatchers.withId(R.id.btn_back))
    .perform(ViewActions.click())
fun clickDeleteBtn() = Espresso.onView(ViewMatchers.withId(R.id.btn_delete))
    .perform(ViewActions.click())
fun clickCloseBtn() = Espresso.onView(ViewMatchers.withId(R.id.btn_close))
    .perform(ViewActions.click())

fun onTitle_type(text: String) = onTitle().perform(ViewActions.typeText(text))
fun onBody_type(text: String) = onBody().perform(ViewActions.typeText(text))

fun onBody_clearAndType(text: String) =
    onBody().perform(ViewActions.clearText()).perform(ViewActions.typeText(text))

fun onTitle_clearAndType(text: String) =
    onTitle().perform(ViewActions.clearText()).perform(ViewActions.typeText(text))

fun checkOnWebBody(bodyText: String) =
    Web.onWebView(ViewMatchers.withId(R.id.body))
        .check(withWebViewTextMatcher("<head></head><body>$bodyText</body>"))

fun checkOnPostList(matcher: Matcher<View?>) =
    Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
        .check(ViewAssertions.matches(matcher))

fun checkPostList_notHasText(text: String) =
    checkOnPostList(CoreMatchers.not(hasItem(ViewMatchers.hasDescendant(ViewMatchers.withText(text)))))

fun checkPostList_hasText(text: String, position: Int? = null) {
    if (position == null) {
        checkOnPostList(hasItem(ViewMatchers.hasDescendant(ViewMatchers.withText(text))))
    } else {
        checkOnPostList(atPosition(position,
            ViewMatchers.hasDescendant(ViewMatchers.withText(text))
        ))
    }
}

fun checkTitle_hasText(text: String) = onTitle().check(
    ViewAssertions.matches(
        ViewMatchers.withText(
            text
        )
    )
)

fun checkAmountPosts(amountPosts: Int) =
    Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
        .check(hasItemCount(amountPosts));