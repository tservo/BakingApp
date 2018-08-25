package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.utilities.JsonUtilities;

import static android.support.test.espresso.Espresso.onView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.bakingapp.RecipeDetailFragment.ARG_RECIPE;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class SelectRecipeStepDetailsFromRecipeDetailActivityTest {
    /**
     *  In here, we will attempt to get to the recipe step details from the recipe details
     *  fragment. We need to prime the RecipeDetailActivity with a test Recipe first.
     */

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
        new ActivityTestRule<>(RecipeDetailActivity.class, false, false);

    private Recipe mRecipe;

    @Before
    public void setUpActivity() {
        if (null == mRecipe) {
            Context context = InstrumentationRegistry.getTargetContext();
            mRecipe = JsonUtilities.retrieveTestRecipe(context);
        }

        // start the recipe detail activity with this recipe intent.
        Intent intent = new Intent();
        intent.putExtra(ARG_RECIPE,mRecipe);
        mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testPassesToRecipeStepDetail() {
        int stepPosition = 2;
        onView(withId(R.id.recipe_step_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(stepPosition, click()));

        onView(allOf( withId(R.id.recipe_step_detail_description)
                ,withParent(withId(R.id.recipe_step_description_container)),
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
                .check(matches(withText(mRecipe.getStep(stepPosition).getDescription())));
    }

}
