package com.talent.posdat.search;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.profile.ProfileFragment;
import com.talent.posdat.profile.User;
import com.talent.posdat.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class SearchFragment extends Fragment
{
    private RecyclerView recyclerView;
    private List<User> mUsers;

    private  EditText searchBar;
    FirebaseAuth mAuth;
    private  DatabaseReference usersRef,followsRef;
    String currentUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       View view= inflater.inflate(R.layout.fragment_search, container, false);

       usersRef= FirebaseDatabase.getInstance().getReference("Users");
        followsRef=FirebaseDatabase.getInstance().getReference().child("Follow");
       mAuth=FirebaseAuth.getInstance();

       recyclerView=view.findViewById(R.id.search_recyclerview);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       searchBar=view.findViewById(R.id.search_bar);
       mUsers=new ArrayList<>();


       readUsers();
       searchBar.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
          //  searchUsers(s.toString().toLowerCase());
               searchPeopleAndFriends(s.toString().toLowerCase());
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        return view;
    }


    private void searchPeopleAndFriends(String seachQuery)
    {
        Toast.makeText(getContext(), "Searching...", Toast.LENGTH_SHORT).show();
//here retreive  the searchfullname variable stored in firebase and written well formatted full name
        // also consider searching first and last name  separately

        Query searchConnectionQuery= usersRef.orderByChild("username").startAt(seachQuery).endAt(seachQuery+"\uf8ff");



        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(searchConnectionQuery, User.class)
                        .build();

        final FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<User,UserViewHolder>
                (options)
        {

            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, final int position, @NonNull final User user) {

                final String postKey= getRef(position).getKey();

                userViewHolder.setFullname(user.getFullname());
                userViewHolder.setCountry(user.getCountry());
                userViewHolder.setProfileImage(user.getProfileImage());
                userViewHolder.setStatus(user.getStatus());


                isFollowing(postKey,userViewHolder.follow_button);
                if(mAuth.getCurrentUser()!=null) {
                    currentUserId = mAuth.getCurrentUser().getUid();

                    if(postKey.equals(currentUserId))
                    {
                        userViewHolder.follow_button.setText("Own Profile");
                        userViewHolder.follow_button.setEnabled(false);
                    }
                    userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor=getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                            editor.putString("profileid",postKey);
                            editor.apply();
                            Fragment fragment=new ProfileFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("receiverId",postKey);
                                fragment.setArguments(bundle);


                            ((HomeActivity)getActivity()).replaceFragments(fragment);



                        }
                    });

                    userViewHolder.follow_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(userViewHolder.follow_button.getText().toString().equals("follow"))
                            {
                                FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUserId)
                                        .child("following").child(postKey).setValue(true);
                                FirebaseDatabase.getInstance().getReference().child("Follow").child(postKey)
                                        .child("following").child(currentUserId).setValue(true);
                            }
                            else
                            {
                                FirebaseDatabase.getInstance().getReference().child("Follow").child(currentUserId)
                                        .child("following").child(postKey).removeValue();
                                FirebaseDatabase.getInstance().getReference().child("Follow").child(postKey)
                                        .child("following").child(currentUserId).removeValue();
                            }
                        }
                    });



                }


            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                UserViewHolder holder = new UserViewHolder(view);
                return holder;
            }
        };

        searchConnectionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user= snapshot.getValue(User.class);
                    mUsers.add(user);
                }
                firebaseRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();



    }


    private void readUsers()
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(searchBar.getText().toString().equals("")){
                    mUsers.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        User user =snapshot.getValue(User.class);
                        mUsers.add(user);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public class UserViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public Button follow_button;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            follow_button=mView.findViewById(R.id.search_button_follow);

        }


        public void setFullname(String fullname) {
            TextView fullName= mView.findViewById(R.id.all_users_profile_full_name);
            fullName.setText(fullname);
        }

        public void setProfileImage(String profileImage) {
            CircleImageView profImage= mView.findViewById(R.id.all_users_display_profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(profImage);
        }

        public void setCountry(String country) {
            TextView Country= mView.findViewById(R.id.all_users_profile_country);
            Country.setText(country);
        }
        public void setStatus(String status) {
            TextView statusTextview= mView.findViewById(R.id.all_users_profile_status);
            statusTextview.setText(status);
        }
    }

    public void isFollowing(final String userid, final Button followbtn)
    {
     DatabaseReference   followsRef2=FirebaseDatabase.getInstance().getReference().child("Follow");
        FirebaseAuth    mAuth2=FirebaseAuth.getInstance();
        if(mAuth2.getCurrentUser()!=null)
        {
            currentUserId=mAuth2.getCurrentUser().getUid();
            DatabaseReference reference=followsRef2.child(currentUserId).child("following");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(userid).exists())
                    {
                        followbtn.setText("following");
                    }
                    else
                    {
                        followbtn.setText("follow");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    }

}
