package com.talent.posdat.home;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.talent.posdat.R;
import com.talent.posdat.chat.ChatFragment;
import com.talent.posdat.friends.FriendsFragment;
import com.talent.posdat.notification.NotificationFragment;
import com.talent.posdat.search.SearchFragment;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton floatingActionButton ;
    private String receiverId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            receiverId=bundle.getString("receiverId");
        }
        TabLayout tabLayout=view.findViewById(R.id.frag_home_tablayout);
        ViewPager viewPager=view.findViewById(R.id.home_view_pager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        floatingActionButton=view.findViewById(R.id.fab);

        Fragment friendsFragment = new FriendsFragment();
        Bundle bundleFriends = new Bundle();
        bundleFriends.putString("receiverId",receiverId);
        friendsFragment.setArguments(bundleFriends);

        viewPagerAdapter.addFragment(friendsFragment,"MY FRIENDS");
        viewPagerAdapter.addFragment(new NotificationFragment(),"MATCHES YOU");
        viewPagerAdapter.addFragment(new SearchFragment(),"MESSAGED YOU");

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
          /*  case R.id.frag_home_tablayout: //name of any button that leads to another fragment
            HomeActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container_home,new ChatFragment());
            break;
            */
        }
    }


    public class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> fragmentTitleList;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.fragmentTitleList = new ArrayList<>();
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public  void addFragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            fragmentTitleList.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
