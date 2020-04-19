package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
    */

    @Test
    fun whenTappingOnNewPost_ShouldCreatePostAndShouldAddAnItemToTheList() {
        val amountPost = 0;
        val newTitle = "Nuevo post"
        val textBody = "Esta es una prueba"

        onView(withId(R.id.create_new_post))
            .perform(click())

        onView(withId(R.id.title))
            .perform(typeText(newTitle))

        onView(withId(R.id.body))
            .perform(typeText(textBody))

        onView(withId(R.id.btn_save))
            .perform(click())

        onView(withId(R.id.btn_back))
            .perform(click())

        onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
            .check(hasItemCount(amountPost + 1));
    }

}
