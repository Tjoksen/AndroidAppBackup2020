package com.talentjoko.leaveappjsc.ui.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.talentjoko.leaveappjsc.R;
import com.talentjoko.leaveappjsc.model.Todo;
import com.talentjoko.leaveappjsc.model.TodoDao;
import com.talentjoko.leaveappjsc.model.TodoDataBase;
import com.talentjoko.leaveappjsc.utils.HelperClass;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private Context mContext;
    View root;
    HelperClass helperClass;
    TodoDataBase todoDataBase;
    TodoDao todoDao;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        todoDataBase=TodoDataBase.getAppDatabase(mContext);
        todoDao=todoDataBase.todoDao();
        helperClass=new HelperClass(todoDataBase,todoDao);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_todolist, container, false);


       initialize();


       setupListViewListener();
Thread thread = new Thread(){
    @Override
    public void run() {
        for(Todo todo:todoDao.getAllTodos()){
            items.add(todo.getTask());
//            Toast.makeText(mContext, "All items retrieved", Toast.LENGTH_SHORT).show();
        }
    }
};
thread.start();



        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            addItemDialog(mContext);
        });

        return root;
    }

    void initialize(){
        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter(mContext,
                android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) root.findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);


    }





    // Attaches a long click listener to the listview
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                (adapter, item, pos, id) -> {
                    // Remove the item within array at position
                    items.remove(pos);

                    itemsAdapter.notifyDataSetChanged();

                    return true;
                });
    }



    private void addItemDialog(Context c) {
        final EditText taskEditText = new EditText(getActivity().getApplicationContext());
        Todo todo= new Todo();

        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new task")
                .setMessage("What do you want to do next?")
                .setView(taskEditText)
                .setPositiveButton("Add", (dialog1, which) -> {
                    String task = String.valueOf(taskEditText.getText());
                    todo.setTask(task);
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            todoDao.insert(todo);
                        }
                    };
                    thread.start();
                    //helperClass.insertTodo(todo);
                    itemsAdapter.add(task);



                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }





}