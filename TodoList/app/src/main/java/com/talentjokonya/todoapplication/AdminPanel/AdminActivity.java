package com.talentjokonya.todoapplication.AdminPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {
DatabaseReference usersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users");
        retrieveUsers();
    }

    private void retrieveUsers() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot userSnapShot:dataSnapshot.getChildren()){
                       if(userSnapShot.child("profile").exists()){
                           String firstName = dataSnapshot.child("firstName").getValue().toString();
                           String surname = dataSnapshot.child("surname").getValue().toString();
                           String phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString();
                           String nationality = dataSnapshot.child("nationality").getValue().toString();

                       }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
