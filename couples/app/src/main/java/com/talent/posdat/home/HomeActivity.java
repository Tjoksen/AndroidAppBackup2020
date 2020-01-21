package com.talent.posdat.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.talent.posdat.ImageSliderActivity;
import com.talent.posdat.R;
import com.talent.posdat.chat.ChatActivity;
import com.talent.posdat.chat.ChatFragment;
import com.talent.posdat.notification.NotificationFragment;
import com.talent.posdat.profile.ProfileFragment;
import com.talent.posdat.search.SearchFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    public  static FragmentManager fragmentManager;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private CircleImageView navProfileImage;
    private TextView navProfileUserName;
    String currentUserId;


    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentUserId=getIntent().getStringExtra("receiverId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager=getSupportFragmentManager();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if(findViewById(R.id.fragment_container_home)!=null){

            if(savedInstanceState!=null){
             return;
            }

            Fragment fragment=new  HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("receiverId",currentUserId);
            fragment.setArguments(bundle);

            fragmentManager.beginTransaction().replace(R.id.fragment_container_home,
                   fragment ).commit();
        }



        //toolbar
        mToolbar=(Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout= findViewById(R.id.drawable_layout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle= new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView=findViewById(R.id.navigation_view);


        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);

        navProfileImage=navView.findViewById(R.id.nav_profile_image);
        navProfileUserName=navView.findViewById(R.id.nav_user_full_name);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                UserMenuSelector(menuItem);
                return false;
            }
        });

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return  true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem menuItem) {
        switch(menuItem.getItemId())
        {

            case R.id.nav_post:

                break;
            case R.id.nav_profile:

                break;

            case R.id.nav_home:

                break;
            case R.id.nav_find_friends:

                break;

            case R.id.nav_friends:
                break;

            case R.id.nav_messages:

                break;
            case R.id.nav_settings:

                break;

            case R.id.nav_logout:

                break;

        }
    }

    public  static  void  replaceFragments(Fragment fragment)
    {
        if(fragment!=null)
        {

           fragmentManager.beginTransaction().replace(R.id.fragment_container_home,
                    fragment).addToBackStack(null).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
        {
            switch(menuItem.getItemId())
            {

                case R.id.nav_bottom_home:
                    Fragment fragment = new HomeFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("receiverId",currentUserId);
                    fragment.setArguments(bundle);
                    selectedFragment=fragment;
                    break;

                case R.id.nav_bottom_search:
                    selectedFragment=new SearchFragment();
                    break;

                case R.id.nav_bottom_add:
                    startActivity(new Intent(HomeActivity.this, ChatActivity.class));
                    break;
                case R.id.nav_bottom_favourite:
                    startActivity(new Intent(HomeActivity.this, ImageSliderActivity.class));
                    break;

                case R.id.nav_bottom_profile:
                    SharedPreferences.Editor  editor= getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                    editor.putString("profileid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    editor.apply();

                    Fragment fragmentProf = new ProfileFragment();
                    Bundle bundleProf = new Bundle();
                    bundleProf.putString("receiverId",currentUserId);
                    fragmentProf.setArguments(bundleProf);
                    selectedFragment=fragmentProf;
                    break;


            }

            if(selectedFragment!=null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_home,
                        selectedFragment).commit();

            }
            return true;
        }
    };
}
