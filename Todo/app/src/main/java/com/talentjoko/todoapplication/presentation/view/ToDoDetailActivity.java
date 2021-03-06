package com.talentjoko.todoapplication.presentation.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.talentjoko.todoapplication.R;
import com.talentjoko.todoapplication.presentation.common.BaseActivity;
import com.talentjoko.todoapplication.presentation.edit.EditToDoDetailActivity;

import static com.talentjoko.todoapplication.presentation.view.ToDoDetailFragment.ARG_TODO_KEY;


/**
 * An activity representing a single _Todo detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ToDoListActivity}.
 */
public class ToDoDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        String todoKey = getIntent().getStringExtra(ARG_TODO_KEY);
        if (todoKey == null) {
            throw new IllegalArgumentException("Must pass TODO_KEY");
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ToDoDetailActivity.this, EditToDoDetailActivity.class);
            intent.putExtra(ARG_TODO_KEY, getIntent().getStringExtra(ARG_TODO_KEY));
            startActivity(intent);
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(ARG_TODO_KEY, todoKey );
            ToDoDetailFragment fragment = new ToDoDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.todo_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
