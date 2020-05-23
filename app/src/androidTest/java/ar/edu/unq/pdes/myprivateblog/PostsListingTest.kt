package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
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

//    @Before
//    fun setUp() {
//        val testingComponent: ApplicationComponent = DaggerApplicationComponent.builder()
//            .authModule(FakeAuthModule())
//            .build()
//    }

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

    @Test
    fun whenTappingOnNewPost_ShouldCreatePostAndShouldAddAnItemToTheEndOfTheList() {
        val amountPost = 0;
        val postTitle1 = "Post 1"
        val postTextBody1 = "This is the body of post 1"

        val postTitle2 = "Post 2"
        val postTextBody2 = "This is the body of post 2"

        //create post 1
        goToCreatePost()
        onTitle_type(postTitle1)
        onBody_type(postTextBody1)
        clickSaveBtn()
        clickBackBtn()

        //create post 2
        goToCreatePost()
        onTitle_type(postTitle2)
        onBody_type(postTextBody2)
        clickSaveBtn()
        clickBackBtn()

        checkAmountPosts(2)
        checkPostList_hasText(postTitle1, 0)
        checkPostList_hasText(postTitle2, 1)
    }

    @Test
    fun whenTheAppStarts_ShouldShowTheBottonToCreateNewPostAndAnImage() {
        onView(withId(R.id.text_empty_list))
            .check(matches(isDisplayed()))
        onView(withId(R.id.create_new_post))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenCreatingPost_shouldButtonsMustBeVisible() {

        goToCreatePost()
        onView(withId(R.id.btn_save)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_close)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.body)).check(matches(isDisplayed()));
        onView(withId(R.id.color_picker)).check(matches(isDisplayed()));
        onView(withId(R.id.formatting_toolbar)).check(matches(isDisplayed()));
    }

    @Test
    fun whenCreatingPost_shouldNavigateToPostDetailButtonsMustBeVisible() {
        val postTitle = "post1"
        val bodyText = "This is the body"
        val pickedColor = Color.parseColor("#b39ddb")

        goToCreatePost()
        onTitle_type(postTitle)
        onBody_type(bodyText)
        clickPickerColor(pickedColor)
        clickSaveBtn()

        onView(withId(R.id.btn_back)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_edit)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_delete)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.title)).check(matches(isDisplayed()));
        onView(withId(R.id.body)).check(matches(isDisplayed()));

    }

}
