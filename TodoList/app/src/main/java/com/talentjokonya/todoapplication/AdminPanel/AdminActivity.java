package com.talentjokonya.todoapplication.AdminPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.talentjokonya.todoapplication.data.db.Todo;
import com.talentjokonya.todoapplication.view.ui.auth.LoginActivity;


import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private GridLayoutManager gridLayoutManager;
    @BindView(R.id.admin_recycler)
    RecyclerView todoListRecycler;
    private DatabaseReference fTodoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            fTodoDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid());
        }
        setUpRecyclerView();
        updateUI();
        loadData();

    }

    private void setUpRecyclerView() {
        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        todoListRecycler.setHasFixedSize(true);
        todoListRecycler.setLayoutManager(gridLayoutManager);
       // todoListRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadData() {
        Query query = fTodoDatabase.orderByValue();
        FirebaseRecyclerOptions<Todo> options =
                new FirebaseRecyclerOptions.Builder<Todo>()
                        .setQuery(query,  Todo.class)
                        .build();
        FirebaseRecyclerAdapter<Todo, TodoViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Todo, TodoViewHolder>(
                options) {
            @NonNull
            @Override
            public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            protected void onBindViewHolder(@NonNull TodoViewHolder viewHolder, int position, @NonNull Todo model) {
                final String noteId = getRef(position).getKey();

                fTodoDatabase.child("profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("firstName") && dataSnapshot.hasChild("surname")) {
                            String firstName = dataSnapshot.child("firstName").getValue().toString();
                            String surname = dataSnapshot.child("surname").getValue().toString();

                            viewHolder.itemView.setOnClickListener(view -> {
                                Intent intent = new Intent(AdminActivity.this, NewTodoActivity.class);
                                intent.putExtra("noteId", noteId);
                                startActivity(intent);
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        };
        todoListRecycler.setAdapter(firebaseRecyclerAdapter);
    }

    private void updateUI(){

        if (fAuth.getCurrentUser() != null){
            Log.i("AdminActivity", "fAuth != null");
        } else {
            Intent startIntent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(startIntent);
            finish();
            Log.i("AdminActivity", "fAuth == null");
        }

    }
    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
