package com.talentjoko.todoapplication.presentation.list;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.talentjoko.todoapplication.R;
import com.talentjoko.todoapplication.domain.Repository;
import com.talentjoko.todoapplication.domain.entities.ToDo;
import com.talentjoko.todoapplication.presentation.view.ToDoDetailActivity;
import com.talentjoko.todoapplication.presentation.view.ToDoDetailFragment;

public class SimpleItemRecyclerViewAdapter extends FirebaseRecyclerAdapter<ToDo, SimpleItemRecyclerViewAdapter.ViewHolder> {

    Repository repository;

    public SimpleItemRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<ToDo> options, Repository repository) {
        super(options);
        this.repository = repository;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ToDo model) {
        final DatabaseReference todoRef = getRef(position);
        final String todoKey = todoRef.getKey();

        holder.title.setText(model.title);
        holder.description.setText(model.description);
        if(model.completed){
            holder.checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
        } else {
            holder.checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }

        holder.itemView.setTag(model);
        holder.itemView.setOnClickListener(view -> {

            Context context = view.getContext();
            Intent intent = new Intent(context, ToDoDetailActivity.class);
            intent.putExtra(ToDoDetailFragment.ARG_TODO_KEY, todoKey);

            context.startActivity(intent);
        });

        holder.checkBox.setOnClickListener(view -> {
            model.completed = !model.completed;
            repository.editTodo(todoKey, model.title, model.description, model.completed);
            notifyDataSetChanged();
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView description;
        final ImageView checkBox;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}