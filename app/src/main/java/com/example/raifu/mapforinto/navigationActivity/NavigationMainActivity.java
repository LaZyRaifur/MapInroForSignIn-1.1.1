package com.example.raifu.mapforinto.navigationActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.raifu.mapforinto.NavigationOthersActivity.CircleTransform;
import com.example.raifu.mapforinto.R;
import com.example.raifu.mapforinto.navigationFragment.HomeFragment;
import com.example.raifu.mapforinto.navigationFragment.MovieFragment;
import com.example.raifu.mapforinto.navigationFragment.NotificationFragment;
import com.example.raifu.mapforinto.navigationFragment.PhotosFragment;
import com.example.raifu.mapforinto.navigationFragment.SettingsFragment;

import java.util.logging.Handler;

public class NavigationMainActivity extends AppCompatActivity {
    //urls to load navigation header background image
    //and profiles name
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    //tag used to attach the fragment
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIE = "movie";
    private static final String TAG_NOTIFICATION = "notification";
    private static final String TAG_SETTING = "setting";
    //index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    //toolbar title respected to select nav menu item
    private String[] activityTitles;

    //flag to load home fragment when users press back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private android.os.Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new android.os.Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        //navigation view handler
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        //load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //load nav menu header data
        loadNavHeader();

        //initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        //name,website
        txtName.setText("Raifur rahim");
        txtWebsite.setText("www.github.com");

        //loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        //loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        //showing dot next to notification label
        navigationView.getMenu().getItem(3).setActionView(R.layout.navigation_menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        //selecting appropriate nav menu item
        selectNavMenu();
        //set toolbar title
        setToolbarTitle();

        //if user select the current navigation menu again, don't do anything
        //just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            //show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                //update the main content by replacing fragment
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        //if mPendingRunnable is not null,then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //show or hide the fab button
        toggleFab();
        //closing drawer on item click
        drawer.closeDrawers();

        //refresh toolbar menu
        invalidateOptionsMenu();


    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                //home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                //photos
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;

            case 2:
                //movies fragment
                MovieFragment movieFragment = new MovieFragment();
                return movieFragment;

            case 3:
                //notification fragment
                NotificationFragment notificationFragment = new NotificationFragment();
                return notificationFragment;
            case 4:
                //settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();

        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }


    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }


    private void setUpNavigationView() {
        //setting navigation view item selected listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //this method will trigger on item click of navigation menu
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //replacing the main content with contentFragment which is inbox view
                    case R.id.nav_myProfile:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_AcceptRequest:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_MyRaisedRequest:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIE;
                        break;
                    case R.id.nav_DonationHistory:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATION;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTING;
                        break;

                    case R.id.nav_about_us:
                        //launching new intent instead of loading fragment
                        startActivity(new Intent(NavigationMainActivity.this, NavigationAboutUs.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy:
                        //launching new intent instead of loading fragment
                        startActivity(new Intent(NavigationMainActivity.this, NavigationPrivacy.class));
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;


                }

                //checking if the item is in checked state or not,if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout

        drawer.setDrawerListener(actionBarDrawerToggle);


        //calling sync state is necessary or else your hamburger icon wont show up

        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        //this code loads home fragment when back key is used
        //when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            //checking if users is on other navigation menu
            //rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;

            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu,this adds items to the action bar if it is present

        //show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main_navigation, menu);
        }

        //when fragment is notification ,load the menu created for notification
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notification_navigation, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_SHORT).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'

        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }


    //show or hide the tab
    private void toggleFab() {
        if (navItemIndex == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }


}
