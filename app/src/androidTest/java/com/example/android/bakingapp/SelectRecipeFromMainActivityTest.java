package com.example.android.bakingapp;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SelectRecipeFromMainActivityTest {
    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested, MenuActivity in this case, will be launched
     * before each test that's annotated with @Test and before methods annotated with @Before.
     *
     * The activity will be terminated after the test and methods annotated with @After are
     * complete. This rule allows you to directly access the activity during the test.
     */
    // need to get the simpleidlingresource call done at the right time.
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;


    // completed (6) Registers any resource that needs to be synchronized with Espresso before
    // the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getSimpleIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

    }

    @Test
    public void displayRecyclerViewTest() {
      onView(withId(R.id.recipe_card_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void selectAndGoToDetailActivityTest() {
        onView(withId(R.id.recipe_card_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));

        onView(withId(R.id.ingredients_list_view)).check(matches(isDisplayed()));


    }

    // completed (8) Unregister resources when not needed to avoid malfunction
    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
