package com.talent.posdat.friends;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.chat.ChatFragment;
import com.talent.posdat.home.HomeActivity;
import com.talent.posdat.profile.ProfileFragment;


import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsFragment extends Fragment {


    private RecyclerView friendsList;

    //Firebase for authentcating the user
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    //FirebaseUser firebaseUser;
    private DatabaseReference friendsRef,usersRef;
    private String receiverId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        friendsList =view.findViewById(R.id.all_users_post_list);
        friendsList.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(),2);
        friendsList.setLayoutManager(gridLayoutManager);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            receiverId=bundle.getString("receiverId");
        }

         usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        friendsRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(receiverId);

      //  firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        mAuth=FirebaseAuth.getInstance();
        displayAllFriends();
    }





    private void displayAllFriends()
    {
        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(friendsRef, Friends.class)
                        .build();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (options)
        {

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder friendsViewHolder, final  int position, @NonNull final Friends friends) {

                final String friendIds= getRef(position).getKey();
                usersRef.child(friendIds).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            final  String fullname=dataSnapshot.child("fullname").getValue().toString();
                            final  String status=dataSnapshot.child("status").getValue().toString();

                            if(dataSnapshot.hasChild("profileImage"))
                            {
                                final  String profileImage=dataSnapshot.child("profileImage").getValue().toString();
                                friendsViewHolder.setProfileImage(profileImage);

                            }
                            if(dataSnapshot.hasChild("userState"))
                            {
                                final  String type=dataSnapshot.child("userState").child("type").getValue().toString();
                                if(type.equals("online"))
                                {
                                    friendsViewHolder.onlineStatusView.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    friendsViewHolder.onlineStatusView.setVisibility(View.INVISIBLE);
                                }


                            }


                            friendsViewHolder.setFullname(fullname);
                            friendsViewHolder.setStatus(status);

                            friendsViewHolder.setDate(friends.getDate());

                            friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    CharSequence options[] = new CharSequence[]
                                            {
                                                    fullname +"'s profile",
                                                    "Inbox Message "

                                            };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Select Option");
                                    builder.setItems(options, new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            if(which==0)
                                            {

                                                Fragment fragment=new ProfileFragment();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("receiverId",friendIds);
                                                fragment.setArguments(bundle);


                                                ((HomeActivity)getActivity()).replaceFragments(fragment);
                                            }
                                            if(which==1)
                                            {
                                            //Todo sent user to corresponding chat frag

                                                    Fragment fragmentPrivateChat = new ChatFragment();
                                                    Bundle privateChatBundle = new Bundle();
                                                    privateChatBundle.putString("visitUserKey",friendIds);
                                                    privateChatBundle.putString("fullname",fullname);
                                                    fragmentPrivateChat.setArguments(privateChatBundle);
                                                ((HomeActivity)getActivity()).replaceFragments(fragmentPrivateChat);
                                            }
                                        }
                                    });
                                    builder.show();

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout, parent, false);
                FriendsViewHolder holder = new FriendsViewHolder(view);
                return holder;
            }
        };
       friendsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton onlineStatusView;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            Button follow;
            onlineStatusView=mView.findViewById(R.id.all_users_online_status);
            follow=mView.findViewById(R.id.search_button_follow);
            follow.setVisibility(View.GONE);
        }

        public void setDate(String date) {
            TextView dateoffriends= mView.findViewById(R.id.all_users_profile_country);
            dateoffriends.setText(date);

        }

        public void setFullname(String fullname) {
            TextView fullName= mView.findViewById(R.id.all_users_profile_full_name);
            fullName.setText(fullname);
        }

        public void setProfileImage(String profileImage) {
            CircleImageView profImage= mView.findViewById(R.id.all_users_display_profile_image);
            Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(profImage);
        }

        public void setStatus(String status) {
            TextView statusTextview= mView.findViewById(R.id.all_users_profile_status);
            statusTextview.setText(status);
        }

    }

}
