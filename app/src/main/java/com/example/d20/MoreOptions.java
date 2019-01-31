package com.example.d20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Activity that manages the fragments used to create custom dice.
 */
public class MoreOptions extends AppCompatActivity {

    private ViewPager viewPager;
    private SectionsPagerAdapter pagerAdapter;
    static String listReturn = "customDice";
    static String numDiceReturn = "numDice";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_options);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    /**
     * Saves the last open tab when closed.
     */
    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences("moreOptionsPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int tab = viewPager.getCurrentItem();
        editor.putInt("tab", tab);
        editor.apply();
    }

    /**
     * Reopens to the last open tab when started.
     */
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("moreOptionsPref", Context.MODE_PRIVATE);
        int tab = pref.getInt("tab", 0);
        viewPager.setCurrentItem(tab);
    }

    /**
     * Handles calls to return results when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        sendResults();
        super.onBackPressed();
    }

    /**
     * Handles calls to return results when the up button is pressed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        sendResults();
        return true;
    }

    /**
     * Makes the call to the open tab to get the custom dice, sets the results, and calls finish()
     * to close activity.
     */
    void sendResults() {
        Intent resultIntent = new Intent();
        switch (viewPager.getCurrentItem()) {
            case 0:
                //If first tab.
                MainFragment mainFrag = (MainFragment) pagerAdapter.instantiateItem(viewPager, 0);
                mainFrag.buildResults(resultIntent);
                break;
            case 1:
                //If second tab.
                StringListFragment stringListFrag
                        = (StringListFragment) pagerAdapter.instantiateItem(viewPager, 1);
                stringListFrag.buildResults(resultIntent);
                break;
            default:
                String[] defaultList = {"Default", "List"};
                resultIntent.putExtra("customDice", defaultList);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Pager adapter used fot the tabs.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainFragment();
                case 1:
                    return new StringListFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {return 2;}
    }
}
