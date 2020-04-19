package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.CoreMatchers.not
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

    private fun onTitle() = onView(withId(R.id.title))
    private fun onBody() = onView(withId(R.id.body))

    private fun clickPickerColor(pickedColor: Int) =
        onView(withTintColor(pickedColor)).perform(click())

    private fun goToCreatePost() = onView(withId(R.id.create_new_post)).perform(click())
    private fun clickSaveBtn() = onView(withId(R.id.btn_save)).perform(click())
    private fun clickEditBtn() = onView(withId(R.id.btn_edit)).perform(click())
    private fun clickBackBtn() = onView(withId(R.id.btn_back)).perform(click())
    private fun clickDeleteBtn() = onView(withId(R.id.btn_delete)).perform(click())
    private fun clickCloseBtn() = onView(withId(R.id.btn_close)).perform(click())

    private fun onTitle_type(text: String) = onTitle().perform(typeText(text))
    private fun onBody_type(text: String) = onBody().perform(typeText(text))

    private fun onBody_clearAndType(text: String) =
        onBody().perform(clearText()).perform(typeText(text))

    private fun onTitle_clearAndType(text: String) =
        onTitle().perform(clearText()).perform(typeText(text))

    private fun checkOnWebBody(bodyText: String) =
        onWebView(withId(R.id.body))
            .check(withWebViewTextMatcher("<head></head><body>$bodyText</body>"))

    private fun checkOnPostList(matcher: Matcher<View?>) =
        onView(withId(R.id.posts_list_recyclerview)).check(matches(matcher))

    private fun checkPostList_notHasText(text: String) =
        checkOnPostList(not(hasItem(hasDescendant(withText(text)))))

    private fun checkPostList_hasText(text: String, position: Int? = null) {
        if (position == null) {
            checkOnPostList(hasItem(hasDescendant(withText(text))))
        } else {
            checkOnPostList(atPosition(position, hasDescendant(withText(text))))
        }
    }

    private fun checkTitle_hasText(text: String) = onTitle().check(matches(withText(text)))

    private fun checkAmountPosts(amountPosts: Int) =
        onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
            .check(hasItemCount(amountPosts));

    @Test
    fun whenTappingOnNewPost_postCreationScreenShouldOpen() {
        goToCreatePost()
        onTitle().check(matches(withHint(R.string.hint_post_title)))
    }

    @Test
    fun whenCreatingPost_shouldNavigateToPostDetail() {
        val postTitle = "post1"
        val bodyText = "This is the body"
        val pickedColor = Color.parseColor("#b39ddb")

        goToCreatePost()
        onTitle_type(postTitle)
        onBody_type(bodyText)
        clickPickerColor(pickedColor)
        clickSaveBtn()

        checkTitle_hasText(postTitle)
        checkOnWebBody(bodyText)
    }

    /*
    @Test //Texto en negrita
    fun whenTappingOnBoldButton_textBodyShouldInBoldStyle() {

        onView(withId(R.id.create_new_post))
            .perform(click())

        onView(withId(R.id.format_bar_button_bold))
            .perform(click())

        val bodyText = "post1"

        onView(withId(R.id.body))
            .perform(typeText(bodyText))

        onView(withId(R.id.body))
            .check(matches(withBoldStyle(R.id.body)));
    }

    @Test
    fun whenEditingPost_shouldModifyThePost() {
        val postTitleToEdit = "postToEdit"
        val postTitleEdited = "postEdited"
        val bodyTextToEdit = "This is the body to edit"
        val bodyTextEdited = "This is the body edited"

        goToCreatePost()
        onTitle_type(postTitleToEdit)
        onBody_type(bodyTextToEdit)
        clickSaveBtn()
        clickEditBtn()
        onTitle_clearAndType(postTitleEdited)
        onBody_clearAndType(bodyTextEdited)
        clickSaveBtn()

        checkTitle_hasText(postTitleEdited)
        checkOnWebBody(bodyTextEdited)

        clickBackBtn()
        checkPostList_hasText(postTitleEdited)
    }

    @Test
    fun whenDeletingPost_shouldBeRemoved() {
        val postTitle = "post"
        val bodyText = "This is the body"

        goToCreatePost()
        onTitle_type(postTitle)
        onBody_type(bodyText)
        clickSaveBtn()
        clickDeleteBtn()

        checkPostList_notHasText(postTitle)
    }
    */

    @Test
    fun whenTappingOnNewPost_ShouldCreatePostAndShouldAddAnItemToTheList() {
        val postTitle = "Nuevo post"
        val bodyText = "Esta es una prueba"

        goToCreatePost()
        onTitle_type(postTitle)
        onBody_type(bodyText)
        clickSaveBtn()
        clickBackBtn()
        checkPostList_hasText(postTitle, 0)
        checkAmountPosts(1)
    }

    @Test
    fun whenTappingOnNewPostAndClosingWithoutSaving_theNumberOfItemsOnTheListPostsShouldNotChange() {
        val postTitle = "Mi nuevo post"
        val bodyText = "Blah blah"

        goToCreatePost()
        onTitle_type(postTitle)
        onBody_type(bodyText)
        clickCloseBtn()
        checkPostList_notHasText(postTitle)
        checkAmountPosts(0)
    }
}
