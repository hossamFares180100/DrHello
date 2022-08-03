package com.example.drhello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Toast;

import com.example.drhello.databinding.ActivitySplashScreenBinding;
import com.example.drhello.ui.login.SignIn;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class IntroScreenActivity extends AppCompatActivity {

    IntroViewPager introViewPagerAdapter;
    int position = 0;
    Animation btnAnim;
    final List<ScreenItem> mList = new ArrayList<>();
    private ActivitySplashScreenBinding activitySplashScreenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        activitySplashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        activitySplashScreenBinding.shimmer.startShimmerAnimation();
        // when this activity is about to be launch we need to check if its openened before or not

        if (restorePrefData()) {
            Intent SignInActivity = new Intent(getApplicationContext(), SignIn.class);
            startActivity(SignInActivity);
            finish();
        }


        mList.add(new ScreenItem( R.drawable.chatbotnew, R.drawable.chatbotsplash,"bot"));
        mList.add(new ScreenItem( R.drawable.hardwarenew, R.drawable.hardwaresplash,"hard"));
        mList.add(new ScreenItem( R.drawable.socialnew, R.drawable.social_media,"social"));
        mList.add(new ScreenItem( R.drawable.xraynew, R.drawable.medicalsplash,"ray"));

        introViewPagerAdapter = new IntroViewPager(this, mList,activitySplashScreenBinding);
        activitySplashScreenBinding.screenViewpager.setAdapter(introViewPagerAdapter);

        activitySplashScreenBinding.tabIndicator.setupWithViewPager(activitySplashScreenBinding.screenViewpager);

        activitySplashScreenBinding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread background = new Thread() {
                    public void run() {
                        try {
                         //   sleep(4 * 1000);
                            Intent signInActivity = new Intent(getApplicationContext(), SignIn.class);
                            savePrefsData();
                            startActivity(signInActivity);
                            finish();
                        } catch (Exception e) {
                            Toast.makeText(IntroScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                background.start();
            }
        });

        activitySplashScreenBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = activitySplashScreenBinding.screenViewpager.getCurrentItem();
                if (position < mList.size()-1) {
                    activitySplashScreenBinding.screenViewpager.setCurrentItem(position);
                    position++;
                } else if (position == mList.size() - 1) {
                    loaddLastScreen();
                }else{
                    activitySplashScreenBinding.btnNext.setVisibility(View.VISIBLE);
                    activitySplashScreenBinding.btnGetStarted.setVisibility(View.VISIBLE);
                    activitySplashScreenBinding.tvSkip.setVisibility(View.VISIBLE);
                    activitySplashScreenBinding.tabIndicator.setVisibility(View.VISIBLE);
                }
            }
        });

        // skip button click listener
        activitySplashScreenBinding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySplashScreenBinding.screenViewpager.setCurrentItem(mList.size());
            }
        });

        // tablayout add change listener
        activitySplashScreenBinding.tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }



    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        activitySplashScreenBinding.btnNext.setVisibility(View.INVISIBLE);
        activitySplashScreenBinding.btnGetStarted.setVisibility(View.VISIBLE);
        activitySplashScreenBinding.tvSkip.setVisibility(View.INVISIBLE);
        activitySplashScreenBinding.tabIndicator.setVisibility(View.INVISIBLE);
        activitySplashScreenBinding.btnGetStarted.setAnimation(btnAnim);
    }

}