package com.talentjoko.leaveappjsc.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.talentjoko.leaveappjsc.MainActivity;
import com.talentjoko.leaveappjsc.R;
import com.talentjoko.leaveappjsc.ui.check_in_out.CheckInOutFragment;
import com.talentjoko.leaveappjsc.ui.todolist.TodoListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardFragment extends Fragment {

  @BindView(R.id.todolist)
  CircleImageView todoBtn ;
  @BindView(R.id.leavedays)
  CircleImageView leaveBtn;
  @BindView(R.id.events)
  CircleImageView eventsBtn;
  @BindView(R.id.checkinout)
  CircleImageView checkInOutBtn;
  @BindView(R.id.notices)
  CircleImageView noticesBtn;
  @BindView(R.id.vacancy)
  CircleImageView vacancyBtn;
  @BindView(R.id.memo)
  CircleImageView memoBtn;
  @BindView(R.id.more)
  CircleImageView moreBtn;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this,root);

        todoBtn.setOnClickListener(view -> {
            TodoListFragment todoFrag= new TodoListFragment();
            ((MainActivity)getActivity()).replaceFragment(R.id.nav_todo_list);
            Toast.makeText(getActivity().getApplicationContext(), "Todo List", Toast.LENGTH_SHORT).show();
        });
        checkInOutBtn.setOnClickListener(view -> {
            CheckInOutFragment checkinoutFrag= new CheckInOutFragment();
            ((MainActivity)getActivity()).replaceFragment(R.id.nav_check_in_out);
            Toast.makeText(getActivity().getApplicationContext(), "Check IN OUT", Toast.LENGTH_SHORT).show();
        });

        return root;

      }




}