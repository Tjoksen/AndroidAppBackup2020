package com.talentjokonya.todoapplication.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.talentjokonya.todoapplication.data.model.User;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {
    private  List<User> users;
    private  Context mContext;
    DatabaseReference usersRef;

    public AdminAdapter(Context context, List<User> users) {
        this.mContext=context;
        this.users=users;
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_user_item,parent,false);
        MyViewHolder myViewHolder= new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapShot:dataSnapshot.getChildren()){
                        if(userSnapShot.child("profile").exists()){
                            User user = dataSnapshot.getValue(User.class);
//                            users.add(user);
                            String firstName = user.getFirstName();
                            String surname = user.getSurname();
                            String phoneNumber =user.getPhoneNumber();
                            String nationality = user.getNationality();
                            holder.lastName.setText(firstName);
                            holder.lastName.setText(surname);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }


    class  MyViewHolder extends  RecyclerView.ViewHolder{
        TextView firstName,lastName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName=itemView.findViewById(R.id.admin_first_name);
            lastName=itemView.findViewById(R.id.admin_second_name);
        }
    }
}
