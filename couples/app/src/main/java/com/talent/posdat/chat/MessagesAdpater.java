package com.talent.posdat.chat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.talent.posdat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdpater extends RecyclerView.Adapter<MessagesAdpater.MessageViewHolder>
{
    private List<Messages> userMessageList;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    ProgressDialog pDialog;



    public MessagesAdpater(List<Messages> userMessageList) {
        this.userMessageList = userMessageList;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder
    {
        public TextView senderMessage, receiverMessage;
        public CircleImageView messageProfileImage;
        public ImageView messageSenderPicture, messageReceiverPicture;
        public VideoView messageSenderVideo, messageReceiverVideo;

        public MessageViewHolder(@NonNull View itemView)
        {
            super(itemView);

            senderMessage=itemView.findViewById(R.id.sender_message_text);
           receiverMessage=itemView.findViewById(R.id.receiver_message_text);
            messageProfileImage=itemView.findViewById(R.id.message_profile_image);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
            messageSenderVideo = itemView.findViewById(R.id.sender_message_video_view);
            messageReceiverVideo = itemView.findViewById(R.id.receiver_message_video_view);
        }
        }



    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_layout_of_users,parent,false);
                  mAuth=FirebaseAuth.getInstance();
                return new MessageViewHolder(view);
           }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position)
    {
        String messageSenderId=mAuth.getCurrentUser().getUid();
        Messages messages=userMessageList.get(position);
        String fromUserId = messages.getFrom();
        String fromMessageType=messages.getType();

        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("profileImage"))
                {
                    String image =dataSnapshot.child("profileImage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(holder.messageProfileImage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        holder.receiverMessage.setVisibility(View.GONE);
        holder.messageProfileImage.setVisibility(View.GONE);
        holder.senderMessage.setVisibility(View.GONE);
        holder.messageSenderPicture.setVisibility(View.GONE);
        holder.messageReceiverPicture.setVisibility(View.GONE);
        holder.messageReceiverVideo.setVisibility(View.GONE);
        holder.messageSenderVideo.setVisibility(View.GONE);





        if(fromMessageType.equals("text"))
        {
            holder.senderMessage.setVisibility(View.VISIBLE);
           holder.receiverMessage.setVisibility(View.INVISIBLE);
            holder.messageProfileImage.setVisibility(View.INVISIBLE);
            if(fromUserId.equals(messageSenderId))
            {
                holder.senderMessage.setBackgroundResource(R.drawable.sender_message_background);
                holder.senderMessage.setTextColor(Color.BLACK);
                holder.senderMessage.setGravity(Gravity.LEFT);
                holder.senderMessage.setText(messages.getMessage());
            }
            else
            {
               holder.senderMessage.setVisibility(View.INVISIBLE);
                holder.receiverMessage.setVisibility(View.VISIBLE);
                holder.messageProfileImage.setVisibility(View.VISIBLE);

                holder.receiverMessage.setBackgroundResource(R.drawable.receiver_message_background);
                holder.receiverMessage.setTextColor(Color.BLACK);
                holder.receiverMessage.setGravity(Gravity.LEFT);
                holder.receiverMessage.setText(messages.getMessage());


            }

        }
        else if(fromMessageType.equals("image"))
        {
            if(fromUserId.equals(messageSenderId))
            {
                holder.messageSenderPicture.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageSenderPicture);
            }
            else {
                holder.messageReceiverPicture.setVisibility(View.VISIBLE);
                holder.messageProfileImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).into(holder.messageReceiverPicture);
            }

        }
        else if(fromMessageType.equals("video"))
        {
            if(fromUserId.equals(messageSenderId))
            {
                holder.messageReceiverVideo.setVisibility(View.GONE);
                holder.messageSenderVideo.setVisibility(View.VISIBLE);


                // Create a progressbar
                pDialog = new ProgressDialog(holder.itemView.getContext());
                // Set progressbar title
                pDialog.setTitle("Receiving Video");
                // Set progressbar message
                pDialog.setMessage("Buffering...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                // Show progressbar
               // pDialog.show();

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(
                            holder.itemView.getContext());
                    mediacontroller.setAnchorView(holder.messageSenderVideo);
                    // Get the URL from String VideoURL
                    Uri video = Uri.parse(messages.getMessage());
                    holder.messageSenderVideo.setMediaController(mediacontroller);
                    holder.messageSenderVideo.setVideoURI(video);

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                holder.messageSenderVideo.requestFocus();
                holder.messageSenderVideo.seekTo(15);
               /* holder.messageSenderVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        //holder.messageSenderVideo.start();
                    }
                });*/

                holder.messageSenderVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.messageSenderVideo.start();
                    }
                });

            }
            else {
                holder.messageSenderVideo.setVisibility(View.GONE);
                holder.messageReceiverVideo.setVisibility(View.VISIBLE);

                // Create a progressbar
                pDialog = new ProgressDialog(holder.itemView.getContext());
                // Set progressbar title
                pDialog.setTitle("Receiving Video");
                // Set progressbar message
                pDialog.setMessage("Buffering...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                // Show progressbar
               // pDialog.show();

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(
                            holder.itemView.getContext());
                    mediacontroller.setAnchorView(holder.messageReceiverVideo);
                    // Get the URL from String VideoURL
                    Uri video = Uri.parse(messages.getMessage());
                    holder.messageReceiverVideo.setMediaController(mediacontroller);
                    holder.messageReceiverVideo.setVideoURI(video);

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                holder.messageReceiverVideo.requestFocus();
                holder.messageReceiverVideo.seekTo(15);
               /* holder.messageReceiverVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        holder.messageReceiverVideo.start();
                    }
                });*/

                holder.messageReceiverVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.messageReceiverVideo.start();
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }
}
