package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.graduationproject.util.SharedPreferencesUtil;

public class FuncAbsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Context context;

    private LinearLayout layout_point;
    private ImageView imageView;

    private Button btn_gotouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func_abs);
        context = this;

        if(!SharedPreferencesUtil.loadData(this)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        layout_point = (LinearLayout)findViewById(R.id.layout_point);
        createPoints(3);
        showPoint(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    btn_gotouse.setVisibility(View.VISIBLE);
                }else{
                    btn_gotouse.setVisibility(View.GONE);
                }
                showPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_gotouse = (Button)findViewById(R.id.btn_gotouse);
        btn_gotouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.saveData(context,false);
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createPoints(int count){
        for(int i=0;i<count;i++){
            imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.icon_circle_gray);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0,20,0);
            layout_point.addView(imageView,lp);
        }
    }

    private void showPoint(int number){
        int count = layout_point.getChildCount();
        for(int i=0;i<count;i++){
            if(i == number){
                ImageView img = (ImageView) layout_point.getChildAt(i);
                img.setImageResource(R.mipmap.icon_circle_wight);
            }else{
                ImageView img = (ImageView) layout_point.getChildAt(i);
                img.setImageResource(R.mipmap.icon_circle_gray);
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_func_abs, container, false);
            RelativeLayout layout_funcabs = (RelativeLayout)rootView.findViewById(R.id.layout_funcabs);
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                layout_funcabs.setBackgroundResource(R.mipmap.func_abs_login);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                layout_funcabs.setBackgroundResource(R.mipmap.func_abs_list);
            }else if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                layout_funcabs.setBackgroundResource(R.mipmap.func_abs_chart);
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
