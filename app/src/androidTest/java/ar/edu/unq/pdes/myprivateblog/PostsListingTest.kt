package ar.edu.unq.pdes.myprivateblog

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
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

        onView(withId(R.id.body))
            .perform(typeText("This is the body"))

        onView(withId(R.id.btn_save)).perform(click())

        onView(withId(R.id.title))
            .check(matches(withText(postTitle)))

    }


}
