package com.talentjoko.todoapplication.presentation.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.talentjoko.todoapplication.R;
import com.talentjoko.todoapplication.TodoApplication;
import com.talentjoko.todoapplication.domain.entities.ToDo;
import com.talentjoko.todoapplication.presentation.common.BaseActivity;

import javax.inject.Inject;

import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.COMPLETED;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.ERROR;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.LOADING;
import static com.talentjoko.todoapplication.presentation.view.GetToDoViewState.SUCCESS;


/**
 * A fragment representing a single ToDo detail screen.
 * This fragment is either contained in a {@link ToDoListActivity}
 * in two-pane mode (on tablets) or a {@link ToDoDetailActivity}
 * on handsets.
 */
public class ToDoDetailFragment extends Fragment {

    /**
     * The fragment argument representing the TODO_KEY that this fragment
     * represents.
     */
    public static final String ARG_TODO_KEY = "TODO_KEY";
    private static final String TAG = ToDoDetailFragment.class.getSimpleName();

//    private Query todoReference;
    private String todoKey;
    private View rootView;
//    private ValueEventListener todoListener;


    @Inject
    public ToDoDetailVMFactory toDoDetailVMFactory;
    public ToDoDetailViewModel toDoDetailViewModel;

    private TextView description;
    private ImageView checkBox;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToDoDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TodoApplication) getActivity().getApplication()).applicationComponent.inject(this);

        if (getArguments() != null && getArguments().containsKey(ARG_TODO_KEY)) {
            todoKey = getArguments().getString(ARG_TODO_KEY);
//            todoReference = RepositoryImpl.INSTANCE().getQueryForSingleUserTodo(todoKey);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.todo_detail, container, false);
        description = rootView.findViewById(R.id.description);
        checkBox = rootView.findViewById(R.id.checkBox_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toDoDetailViewModel = ViewModelProviders.of(this, toDoDetailVMFactory).get(ToDoDetailViewModel.class);
        toDoDetailViewModel.getTodoLiveData.observe(getActivity(), this::handleViewState);
    }


    @Override
    public void onStart() {
        super.onStart();
        toDoDetailViewModel.getToDo(todoKey);
    }


    private void handleViewState(GetToDoViewState getToDoViewState) {
        int state = getToDoViewState.state;
        switch (state) {
            case LOADING:

                break;
            case SUCCESS:
                setTodo(getToDoViewState.todo);
                break;
            case ERROR:
                if (getActivity() != null) {
                    ((BaseActivity) getActivity()).showErrorDialog(getToDoViewState.throwable);
                }
                break;
            case COMPLETED:
                break;
        }
    }

    private void setTodo(ToDo todo) {
        if (todo != null && isAdded()) {
            Activity activity = ToDoDetailFragment.this.getActivity();
            if (activity != null) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(todo.title);
                }
                if (rootView != null) {
                    description.setText(todo.description);
                    if (todo.completed) {
                        checkBox.setImageResource(R.drawable.ic_check_box_black_24dp);
                    } else {
                        checkBox.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                    }
                }
            }
        }
    }

}
