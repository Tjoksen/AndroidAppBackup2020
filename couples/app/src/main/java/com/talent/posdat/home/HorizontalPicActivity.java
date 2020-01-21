package com.talent.posdat.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.chat.ChatActivity;
import com.talent.posdat.friends.FindFriends;
import com.talent.posdat.friends.FindFriendsActivity;
import com.talent.posdat.profile.PersonProfileActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalPicActivity extends AppCompatActivity {
    private  static  final  String TAG="HorizontalPicActivity";
    //vars
    private ArrayList<String> mCountries = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPropicUrls= new ArrayList<>();
    private RecyclerView friendsList;
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference usersRef;

    FirebaseRecyclerAdapter<FindFriends, ViewHolder> firebaseRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_pic);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        friendsList=findViewById(R.id.recyclerview_horizontal_profiles);
        friendsList.setLayoutManager(layoutManager);

        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        displayUsersProfile();

    }


    private void displayUsersProfile()
    {


        FirebaseRecyclerOptions<FindFriends> options =
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(usersRef, FindFriends.class)
                        .build();

         firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<FindFriends, ViewHolder>
                (options)
        {

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position, @NonNull FindFriends friends) {

                final String postKey= getRef(position).getKey();
                Log.d(TAG,"onBindViewHolder : called");

                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(friends.getProfileImage())
                        .placeholder(R.drawable.profile)
                        .into(viewHolder.profImage);
                viewHolder.fullName.setText(friends.getFullname());
                viewHolder.country.setText(friends.getCountry());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"onclick  : clicked on an item" );
                        sendUserToChat(postKey);

                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d(TAG,"onCreateViewHolder : called");
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_home_display,parent,false);


                return new ViewHolder(view);
            }
        };
        friendsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
     //   initRecyclerView();



    }

private void initRecyclerView()
{
    Log.d(TAG,"initRecyclerView : init recyclerview");
    LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

    friendsList=findViewById(R.id.recyclerview_horizontal_profiles);
    friendsList.setLayoutManager(layoutManager);


}


    public class  ViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullName;
        CircleImageView profImage;
        TextView country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profImage =itemView.findViewById(R.id.users_home_profile_image);
            country = itemView.findViewById(R.id.users_home_age_city_nation);
            fullName=itemView.findViewById(R.id.users_home_fullname);
        }

    }

    private void sendUserToChat(String visitUserKey)
    {
        Intent personProfileIntent = new Intent(HorizontalPicActivity.this, ChatActivity.class);
        personProfileIntent.putExtra("visitUserKey",visitUserKey);
        startActivity(personProfileIntent);

    }
}
