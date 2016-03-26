package com.ten.dmitry.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseRouteActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    public static final String SELECTED_ROUTE = "ChooseRouteActivityRoute";
    private ArrayList<String> routeNames;
    protected int numItems;
    ChooseRouteAdapter crAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route2);
        routeNames = new ArrayList<>();
        routeNames.add("Washroom");
        routeNames.add("Sink");
        numItems = routeNames.size();
        // TODO: Extract possible route names from the saved data in the admin section

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        crAdapter =
                new ChooseRouteAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(crAdapter);
    }

    public class ChooseRouteAdapter extends FragmentPagerAdapter{

        public ChooseRouteAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ChooseRouteFragment();
            Bundle args = new Bundle();
            args.putString(ChooseRouteFragment.ARG_OBJECT, routeNames.get(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return numItems;
        }
    }

    // Instances of this class are fragments representing a single
// object in our collection.
    public static class ChooseRouteFragment extends Fragment {
        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            View rootView = inflater.inflate(
                    R.layout.activity_choose_route2, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(args.getString(ARG_OBJECT));
            return rootView;
        }
    }
}

