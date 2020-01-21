package com.talent.posdat.post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.SettingsActivity;
import com.talent.posdat.comment.CommentActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPostsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView myPostsList;
    private DatabaseReference postsRef,likesRef;
    private String currentUserId;
    private FirebaseAuth mAuth;

    private Boolean likeChecker=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();

        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");

        myPostsList=findViewById(R.id.my_all_posts_list);
        myPostsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostsList.setLayoutManager(linearLayoutManager);

        mToolBar=findViewById(R.id.user_post_app_bar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Posts");


        displayMyAllPosts();

    }

    private void displayMyAllPosts() {
        Query sortPostsInDescendingOrder =  postsRef.orderByChild("uid")
                .startAt(currentUserId).endAt(currentUserId+"\uf8ff");

        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(sortPostsInDescendingOrder, Posts.class)
                        .build();

        FirebaseRecyclerAdapter<Posts,PostsViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                (options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final PostsViewHolder postsViewHolder, int position, @NonNull Posts posts)
            {

                final String postKey= getRef(position).getKey();

                postsViewHolder.setFullname(posts.getFullname());
                postsViewHolder.setDate(posts.getDate());
                postsViewHolder.setDescription(posts.getDescription());
                postsViewHolder.setTime(posts.getTime());
                postsViewHolder.setPostImage(posts.getPostImage());
                postsViewHolder.setUserProfileImage(posts.getUserProfileImage());

                postsViewHolder.setLikesButtonStatus(postKey);
                postsViewHolder.setCommentButtonStatus(postKey);
                postsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Click three dots for options",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        //sendUserToClickPost(postKey);
                    }
                });

                postsViewHolder.likeButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        likeChecker=true;
                        likesRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(likeChecker.equals(true)) {


                                    if (dataSnapshot.child(postKey).hasChild(currentUserId)) {
                                        likesRef.child(postKey).child(currentUserId).removeValue();
                                        likeChecker = false;
                                    } else {
                                        likesRef.child(postKey).child(currentUserId).setValue(true);
                                        likeChecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                      });
                    }
                });

                postsViewHolder.commentButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent commentsIntent= new Intent(MyPostsActivity.this, CommentActivity.class);
                        commentsIntent.putExtra("PostKey",postKey);
                        startActivity(commentsIntent);
                    }
                });

                postsViewHolder.numOfComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent commentsIntent= new Intent(MyPostsActivity.this,CommentActivity.class);
                        commentsIntent.putExtra("PostKey",postKey);
                        startActivity(commentsIntent);
                    }
                });
                postsViewHolder.buttonOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //creating Popup menu
                        PopupMenu popupMenu= new PopupMenu(getApplicationContext(),postsViewHolder.buttonOptions);
                        //inflating menu
                        popupMenu.inflate(R.menu.options_menu);
                        //adding click listener
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId())
                                {
                                    case R.id.nav_edit:
                                        sendUserToClickPost(postKey);
                                        break;
                                    case R.id.nav_delete:
                                        sendUserToClickPost(postKey);
                                        break;
                                    case R.id.nav_settings_post:
                                        openSettingsActivity();
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });

            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_post_layout, parent, false);
                PostsViewHolder holder = new PostsViewHolder(view);
                return holder;
            }
        };
        myPostsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    public  static  class PostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView ;
        ImageButton likeButton, commentButton;
        TextView numOfLikes,numOfComments,buttonOptions;
        int countLikes,countComments;


        String currentUserId;
        DatabaseReference likesRef,postRef;
        private FirebaseAuth mAuth;


        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            likeButton=mView.findViewById(R.id.dislike);
            commentButton=mView.findViewById(R.id.comment);
            numOfLikes=mView.findViewById(R.id.likes_number);
            numOfComments=mView.findViewById(R.id.comments_number);
            buttonOptions=mView.findViewById(R.id.textViewOptions);
            likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
            postRef= FirebaseDatabase.getInstance().getReference().child("Posts");
            mAuth=FirebaseAuth.getInstance();
            currentUserId=mAuth.getCurrentUser().getUid();


        }

        public void setFullname(String fullname) {
            TextView username= mView.findViewById(R.id.post_user_name);
            username.setText(fullname);
        }

        public void setDescription(String description) {
            TextView desc= mView.findViewById(R.id.post_description);
            desc.setText(description);
        }
        public void setTime(String time) {
            TextView postTime= mView.findViewById(R.id.post_time);
            postTime.setText(" @ "+time);
        }

        public void setDate(String date) {
            TextView postDate= mView.findViewById(R.id.post_date);
            postDate.setText(" "+date);
        }

        public void setUserProfileImage(String userProfileImage) {
            CircleImageView profImage= mView.findViewById(R.id.post_profile_image);
            Picasso.get().load(userProfileImage).placeholder(R.drawable.profile).into(profImage);
        }

        public void setPostImage(String postImage) {
            ImageView postImag= mView.findViewById(R.id.post_image);
            Picasso.get().load(postImage).into(postImag);
        }

        public void setLikesButtonStatus(final String postKey)
        {
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(postKey).hasChild(currentUserId)){
                        countLikes=(int)dataSnapshot.child(postKey).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                        numOfLikes.setText(Integer.toString(countLikes) + " likes");
                    }
                    else
                    {
                        countLikes=(int)dataSnapshot.child(postKey).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        numOfLikes.setText(Integer.toString(countLikes) + " likes");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setCommentButtonStatus(final String postKey)
        {
            final  DatabaseReference commentRef=postRef.child(postKey).child("comments");

            commentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        countComments=(int)dataSnapshot.getChildrenCount();
                        numOfComments.setText(Integer.toString(countComments) + " comments");
                    }
                    else
                    {
                        numOfComments.setText("");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendUserToClickPost(String postKey) {
        Intent clickPostIntent= new Intent(MyPostsActivity.this, ClickPostActivity.class);
        clickPostIntent.putExtra("PostKey",postKey);
        startActivity(clickPostIntent);
    }

    private void openSettingsActivity() {
        Intent settingsIntent = new Intent(MyPostsActivity.this, SettingsActivity.class);
        startActivity(settingsIntent);
    }
}
