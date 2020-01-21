package com.talent.posdat.profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.friends.FriendsActivity;
import com.talent.posdat.friends.FriendsFragment;
import com.talent.posdat.home.HomeActivity;
import com.talent.posdat.post.MyPostsActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private TextView userProfileStatus, userName, fullname, country, userDOB, userGender, userRelationStatus;
    private CircleImageView userProfileImage;

    //Firebase
    private DatabaseReference profileUserRef,friendsRef,postsRef;
    private FirebaseAuth mAuth;
    private Button myPosts,myFriends;

    private  int countPosts=0;
    private String currentUserId,receiverId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
      View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
           receiverId=bundle.getString("receiverId");
        }
        initialise(view);

        mAuth=FirebaseAuth.getInstance();;
       // currentUserId=mAuth.getCurrentUser().getUid();
        currentUserId=receiverId;
        profileUserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        friendsRef=FirebaseDatabase.getInstance().getReference().child("Friends");
        postsRef=FirebaseDatabase.getInstance().getReference().child("Posts");

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myuserName = dataSnapshot.child("username").getValue().toString();
                    String myfullname = dataSnapshot.child("fullname").getValue().toString();
                    String mycountry = dataSnapshot.child("country").getValue().toString();
                    String myuserDOB = dataSnapshot.child("dob").getValue().toString();
                    String myuserGender = dataSnapshot.child("gender").getValue().toString();
                    String myuserRelationStatus = dataSnapshot.child("relationshipstatus").getValue().toString();
                    String myProfileStatus = dataSnapshot.child("status").getValue().toString();

                    if(dataSnapshot.hasChild("profileImage"))
                    {
                        String myprofilePic = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(myprofilePic).placeholder(R.drawable.profile).into(userProfileImage);
                    }

                    userProfileStatus.setText(myProfileStatus);
                    userName.setText("@ "+ myuserName);
                    fullname.setText(myfullname);
                    country.setText("Country:"+mycountry);
                    userDOB.setText("D.O.B :"+myuserDOB);
                    userGender.setText("Gender:"+myuserGender);
                    userRelationStatus.setText("Relationship:"+myuserRelationStatus);
                }

                myFriends.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendUserToMyFriends();
                    }
                });

                myPosts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendUserToMyPosts();
                    }
                });


                postsRef.orderByChild("uid")
                        .startAt(currentUserId).endAt(currentUserId+"\uf8ff")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.exists())
                                {
                                    countPosts=(int)dataSnapshot.getChildrenCount();
                                    myPosts.setText(""+countPosts+ " Posts");
                                }
                                else
                                {
                                    myPosts.setText("No Posts");

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                friendsRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            int numOfFriends = (int)dataSnapshot.getChildrenCount();
                            myFriends.setText(""+ numOfFriends+" Friends");
                        }
                        else
                        {
                            myFriends.setText("No Friends");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        return view;
    }
    private void initialise(View view)
    {
        userProfileStatus = view.findViewById(R.id.my_profile_status);
        userName = view.findViewById(R.id.my_profile_username);
        fullname = view.findViewById(R.id.my_profile_full_name);
        country = view.findViewById(R.id.my_country);
        userDOB = view.findViewById(R.id.my_dob);
        userGender = view.findViewById(R.id.my_gender);
        userRelationStatus = view.findViewById(R.id.my_relationship_status);
        userProfileImage = view.findViewById(R.id.my_profile_pic);


        myFriends=view.findViewById(R.id.my_friends_button);
        myPosts=view.findViewById(R.id.my_post_button);



    }

    private void sendUserToMyFriends()
    {
        Fragment fragment=new FriendsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("receiverId",receiverId);
        fragment.setArguments(bundle);

        HomeActivity.replaceFragments(fragment);
    }

    private void sendUserToMyPosts()
    {

        Intent myPostIntent = new Intent(getContext(), MyPostsActivity.class);
        startActivity(myPostIntent);
    }


}
