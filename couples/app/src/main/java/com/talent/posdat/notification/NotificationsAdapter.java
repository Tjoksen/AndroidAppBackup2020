package com.talent.posdat.notification;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talent.posdat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>
{
    private Context mContext;
    private List<Notification> mNotificationList;
    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public  class NotificationsViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView profileImage;
        private ImageView postImage;
        private TextView username,textcomment;


        public NotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.notification_profile_image);
            postImage=itemView.findViewById(R.id.notification_post_image);
            username=itemView.findViewById(R.id.notification_username);
            textcomment=itemView.findViewById(R.id.notification_comment);
        }
    }
}
