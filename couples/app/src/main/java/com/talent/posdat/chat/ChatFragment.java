package com.talent.posdat.chat;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;
import com.talent.posdat.friends.FindFriends;
import com.talent.posdat.friends.Friends;
import com.talent.posdat.home.HomeActivity;
import com.talent.posdat.home.HorizontalPicActivity;
import com.talent.posdat.profile.ProfileFragment;
import com.talent.posdat.story.StoryAdapter;
import com.talent.posdat.story.Story;
import com.talent.posdat.videocall.LoginActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment {


    private EditText messageText;
    private ImageButton sendMessageBtn,attachImageBtn,audioRecord;
    private RecyclerView privateChatList;
    private Context mContext=getContext();

    private  final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessagesAdpater messagesAdpater;

    private  String messageReciverId,messageSenderId, messageRecieverName;
    private TextView receiverName,userLastSeen;
    private CircleImageView receiverProfileImage;
    private  String saveCurrentTime,saveCurrentDate,currentUserfullname,postRandomName;

    private Uri fileUri;
    private StorageTask uploadTask;
    private String checker="",myUrl="";
    private ProgressDialog progressDialog;

    private MediaRecorder mRecorder;

    //firebase
    private DatabaseReference rootRef,usersRef;
    private FirebaseAuth mAuth;
    private String mFileName;
    private static final String LOG_TAG="Record Log";

    //PREVIOUS CHAT FRAGMENT
    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story>  storyList;
    private List<String> followingList;
    private String receiverId,currentUserId;

    private  FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            messageReciverId=bundle.getString("visitUserKey");
            messageRecieverName=  bundle.getString("fullname");
        }
        rootRef= FirebaseDatabase.getInstance().getReference();
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth=FirebaseAuth.getInstance();
        messageSenderId=mAuth.getCurrentUser().getUid();

        progressDialog= new ProgressDialog(getContext());



        Random random= new Random();
        int audionum=random.nextInt();

        mFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName+="recording"+audionum+".3gp";


        initialiseFields();
       // displayRecieverInfo();
       sendMessageBtn.setVisibility(View.VISIBLE);

        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMessage();

            }
        });



        attachImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence []
                        {
                                "Send Image ",
                                "Send Video ",
                                "PDF File ",
                                "Audio File"
                        };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Select File");

                //added offline from here
                View view = LayoutInflater.from(mContext).inflate(R.layout.alerdialog_layout, null);
                GridView listView = view.findViewById(R.id.list_view);
                listView.setNumColumns(2);
                int icons[] = {R.drawable.ic_imageplay,R.drawable.ic_videoplay,R.drawable.ic_pdf,R.drawable.ic_audioplay};

                // ArrayAdapter arrayAdapter = new ArrayAdapter(ChatActivity.this, R.layout.item_dialog, R.id.tv1, options){};
                listView.setAdapter(new IconTrayAdapter(options,icons));
                //to here
                builder.setView(view);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position)
                        {
                            case 0:
                                checker="image";
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent,"Select Image"),438);
                                Toast.makeText(mContext, "Send Image", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                checker="video";
                                Intent intentVideo = new Intent();
                                intentVideo.setAction(Intent.ACTION_GET_CONTENT);
                                intentVideo.setType("video/*");
                                startActivityForResult(intentVideo.createChooser(intentVideo,"Select Video"),438);
                                Toast.makeText(mContext, "Send Video", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(mContext, "Send PDF", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                recordAndUpload();

                                Toast.makeText(mContext, "Send Audio", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

                AlertDialog dialog=builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp=dialog.getWindow().getAttributes();
                wmlp.gravity= Gravity.BOTTOM;
                wmlp.x=10;
                wmlp.y=10;
                builder.show();
            }
        });
        fetchMessages();

    }

    private void recordAndUpload()
    {

    }

    private void startRecording()
    {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG,"prepare() failed");

        }
        mRecorder.start();
    }

    private void stopRecording()
    {
        mRecorder.stop();
        mRecorder.release();
        mRecorder=null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

        }

        if(requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {

            progressDialog.setTitle("Sending File");
            progressDialog.setMessage("Please wait while uploading your File");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.show();

            fileUri=data.getData();

            if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");
                final String message_sender_ref="Messages/" + messageSenderId + "/" +messageReciverId;
                final String message_reciever_ref="Messages/" +messageReciverId + "/" +messageSenderId ;

                //push will create a unique random key
                DatabaseReference user_message_key=rootRef.child("Messages").child(messageSenderId).child(messageReciverId).push();
                final String message_push_id =user_message_key.getKey();

                final StorageReference filePath = storageReference.child(message_push_id+"."+"jpg");

                uploadTask=filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw  task.getException();
                        }


                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageImageBody= new HashMap();
                            messageImageBody.put("message",myUrl);
                            messageImageBody.put("name",fileUri.getLastPathSegment());
                            messageImageBody.put("time",saveCurrentTime);
                            messageImageBody.put("date",saveCurrentDate);
                            messageImageBody.put("type",checker);
                            messageImageBody.put("from",messageSenderId);


                            Map messageBodyDetails = new HashMap() ;
                            messageBodyDetails.put(message_sender_ref+ "/"+message_push_id,messageImageBody);
                            messageBodyDetails.put(message_reciever_ref+ "/"+message_push_id,messageImageBody);

                            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(mContext, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                                        messageText.setText("");
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        String errorMsg=task.getException().getMessage();
                                        Toast.makeText(mContext, "Error occured : " + errorMsg, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        messageText.setText("");
                                    }

                                }
                            });

                        }
                    }
                });

            }
            else  if(checker.equals("pdf"))
            {
               /* Intent intent = new Intent(this, AlbumSelectActivity.class);
                //set limit on number of images that can be selected, default is 10
                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 6);
                startActivityForResult(intent, Constants.REQUEST_CODE);*/
            }
            else  if(checker.equals("audio"))
            {

            }
            else  if(checker.equals("video"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Video Files");
                final String message_sender_ref="Messages/" + messageSenderId + "/" +messageReciverId;
                final String message_reciever_ref="Messages/" +messageReciverId + "/" +messageSenderId ;

                //push will create a unique random key
                DatabaseReference user_message_key=rootRef.child("Messages").child(messageSenderId).child(messageReciverId).push();
                final String message_push_id =user_message_key.getKey();
                final StorageReference filePath = storageReference.child(message_push_id+"."+"mp4");

                uploadTask=filePath.putFile(fileUri);
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0 *taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded "+progress + "%...");
                    }
                }).continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if(!task.isSuccessful())
                        {
                            throw  task.getException();
                        }


                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageImageBody= new  HashMap();
                            messageImageBody.put("message",myUrl);
                            messageImageBody.put("name",fileUri.getLastPathSegment());
                            messageImageBody.put("time",saveCurrentTime);
                            messageImageBody.put("date",saveCurrentDate);
                            messageImageBody.put("type",checker);
                            messageImageBody.put("from",messageSenderId);


                            Map messageBodyDetails = new HashMap() ;
                            messageBodyDetails.put(message_sender_ref+ "/"+message_push_id,messageImageBody);
                            messageBodyDetails.put(message_reciever_ref+ "/"+message_push_id,messageImageBody);

                            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(mContext, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                                        messageText.setText("");
                                        progressDialog.dismiss();
                                    }
                                    else
                                    {
                                        String errorMsg=task.getException().getMessage();
                                        Toast.makeText(mContext, "Error occured : " + errorMsg, Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        messageText.setText("");
                                    }

                                }
                            });

                        }
                    }
                });


            }
            else {

                Toast.makeText(getContext(), "Error :Try again" , Toast.LENGTH_SHORT).show();
            }

        }


    }




    private void videoCallinitiate()
    {

        rootRef.child("Users").child(messageSenderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentUserfullname= dataSnapshot.child("fullname").getValue().toString();
                    Intent videoColIntent = new Intent(mContext, LoginActivity.class);
                    videoColIntent.putExtra("receiverId",messageRecieverName);
                    videoColIntent.putExtra("myId",currentUserfullname);
                    startActivity(videoColIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void fetchMessages()
    {
        rootRef.child("Messages").child(messageSenderId).child(messageReciverId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if(dataSnapshot.exists())
                {
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesList.add(messages);
                    messagesAdpater.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage()
    {

        updateUsersStatus("online");


        String message= messageText.getText().toString();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(getContext(), "Please type something...!", Toast.LENGTH_SHORT).show();
        }
        else {
            String message_sender_ref="Messages/" + messageSenderId + "/" +messageReciverId;
            String message_reciever_ref="Messages/" +messageReciverId + "/" +messageSenderId ;

            //push will create a unique random key
            DatabaseReference user_message_key=rootRef.child("Messages").child(messageSenderId).child(messageReciverId).push();
            String message_push_id =user_message_key.getKey();

            Calendar calForDate=Calendar.getInstance();
            SimpleDateFormat currentDate= new SimpleDateFormat("dd-MMM-yyyy");
            saveCurrentDate=currentDate.format(calForDate.getTime());

            Calendar calForTime=Calendar.getInstance();
            SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm aa");
            saveCurrentTime=currentTime.format(calForTime.getTime());

            postRandomName=saveCurrentDate+saveCurrentTime;

            Map messageTextBody= new  HashMap();
            messageTextBody.put("message",message);
            messageTextBody.put("time",saveCurrentTime);
            messageTextBody.put("date",saveCurrentDate);
            messageTextBody.put("type","text");
            messageTextBody.put("from",messageSenderId);

                /*Tree structure will be  Messages(root)
                1.then message_sender_ref(senderId then ReceiverId )
                 then message_push_id(message unique ) to avoid message replacement then message details will be stored under that
                2. then we also need a receiver node to store the copy of the message separately in a similar manner

                    */

            Map messageBodyDetails = new HashMap() ;
            messageBodyDetails.put(message_sender_ref+ "/"+message_push_id,messageTextBody);
            messageBodyDetails.put(message_reciever_ref+ "/"+message_push_id,messageTextBody);

            rootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(mContext, "Message Sent Successfully", Toast.LENGTH_SHORT).show();
                        messageText.setText("");
                    }
                    else
                    {
                        String errorMsg=task.getException().getMessage();
                        Toast.makeText(mContext, "Error occured : " + errorMsg, Toast.LENGTH_SHORT).show();
                        messageText.setText("");
                    }



                }
            });

        }
    }



    private void updateUsersStatus(String status)
    {
        String saveCurrentDate,saveCurrentTime;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM-dd-yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        Calendar calForTime=Calendar.getInstance();
        SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm a");
        saveCurrentTime=currentTime.format(calForTime.getTime());

        Map currentStateMap = new HashMap();
        currentStateMap.put("time",saveCurrentTime);
        currentStateMap.put("date",saveCurrentDate);
        currentStateMap.put("type",status);
        usersRef.child(messageSenderId).child("userState")
                .updateChildren(currentStateMap);
    }

    private void displayRecieverInfo()
    {
        receiverName.setText(messageRecieverName);
        rootRef.child("Users").child(messageReciverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {


                    if (dataSnapshot.hasChild("profileImage")) {
                        final String profileImage = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(receiverProfileImage);
                    }
                    final  String type= dataSnapshot.child("userState").child("type").getValue().toString();
                    final  String lastDate= dataSnapshot.child("userState").child("date").getValue().toString();
                    final  String lastTime= dataSnapshot.child("userState").child("time").getValue().toString();
                    if(type.equals("online"))
                    {
                        userLastSeen.setText("online");
                    }
                    else
                    {

                        userLastSeen.setText("last seen:" + lastTime +" " +lastDate);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initialiseFields()
    {


    }


    class IconTrayAdapter extends BaseAdapter {

        CharSequence [] title;
        int [] icon;

        IconTrayAdapter()
        {
            title=null;
            icon=null;
        }

        public IconTrayAdapter(CharSequence[] title, int[] icon) {
            this.title = title;
            this.icon = icon;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View row;
            row=inflater.inflate(R.layout.item_dialog,parent,false);
            TextView titleview;
            ImageView iconview;
            titleview=row.findViewById(R.id.tv1);
            iconview=row.findViewById(R.id.iv1);
            titleview.setText(title[position]);
            iconview.setImageResource(icon[position]);


            return row;
        }
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =inflater.inflate(R.layout.fragment_chat, container, false);
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            receiverId=bundle.getString("receiverId");
        }
        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getUid();
        recyclerView_story=view.findViewById(R.id.stories_recyclerview);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView_story.setLayoutManager(linearLayoutManager);
        storyList= new ArrayList<>();
        storyAdapter= new StoryAdapter(getContext(),storyList);
        recyclerView_story.setAdapter(storyAdapter);
        checkFollowing();
        return view;
    }

    public void checkFollowing()
    {
        followingList= new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    followingList.add(snapshot.getKey());
                }
              //  readStory();
                displayFriendsProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void displayFriendsProfile()
    {
      DatabaseReference  friendsRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserId);

        FirebaseRecyclerOptions<Friends> options =
                new FirebaseRecyclerOptions.Builder<Friends>()
                        .setQuery(friendsRef, Friends.class)
                        .build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>
                (options)
        {

            @Override
            protected void onBindViewHolder(@NonNull final FriendsViewHolder friendsViewHolder, final int position, @NonNull final Friends friends)
            {
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
                                                Fragment fragment=new ChatFragment();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("receiverId",friendIds);
                                                fragment.setArguments(bundle);


                                                ((HomeActivity)getActivity()).replaceFragments(fragment);
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

                friendsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Log.d(TAG,"onclick  : clicked on an item" );
                        //sendUserToChat(postKey);

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
        recyclerView_story.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        //   initRecyclerView();



    }




    public static class FriendsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        Button follow;
        ImageButton onlineStatusView;
        CircleImageView profImage;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            onlineStatusView=mView.findViewById(R.id.all_users_online_status);
           profImage= mView.findViewById(R.id.all_users_display_profile_image);
           follow=mView.findViewById(R.id.search_button_follow);
           follow.setVisibility(View.GONE);

        }

        public void setDate(String date) {
            TextView dateoffriends= mView.findViewById(R.id.all_users_profile_country);
            dateoffriends.setText(date);
            dateoffriends.setVisibility(View.GONE);

        }

        public void setFullname(String fullname) {
            TextView fullName= mView.findViewById(R.id.all_users_profile_full_name);
            fullName.setText(fullname);
        }

        public void setProfileImage(String profileImage) {

            if(profileImage!=null){
                 Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(profImage);
            }

        }

        public void setStatus(String status) {
            TextView statusTextview= mView.findViewById(R.id.all_users_profile_status);
              statusTextview.setText(status);
           statusTextview.setVisibility(View.GONE);
        }

    }


    public void readStory()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent=System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("","", FirebaseAuth.getInstance().getCurrentUser().getUid(),0,0));

                for(String id:followingList)
                {
                    int countStory=0;
                    Story story=null;
                    for(DataSnapshot snapshot:dataSnapshot.child(id).getChildren())
                    {
                        story=snapshot.getValue(Story.class);
                        if(timecurrent>story.getTimestart() && timecurrent<story.getTimeend())
                        {
                            countStory++;
                        }
                    }

                    if(countStory>0)
                    {
                        storyList.add(story);
                    }
                }

                storyAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
