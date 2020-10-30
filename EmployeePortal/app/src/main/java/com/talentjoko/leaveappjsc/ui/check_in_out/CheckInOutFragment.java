package com.talentjoko.leaveappjsc.ui.check_in_out;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.talentjoko.leaveappjsc.R;
import com.talentjoko.leaveappjsc.utils.HelperClass;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CheckInOutFragment extends Fragment {

    private Context mContext;
    Button checkInOut;
    View root;








    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_check_in_out, container, false);

        checkInOut = root.findViewById(R.id.btn_checkinout);
        checkInOut.setOnClickListener(view -> sureCheckInDialog());
//display current checkin time
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

// textView is the TextView view that should display it
       // textView.setText(currentDateTimeString);



        return root;
    }



    public   void sureCheckInDialog() {
        final Dialog dialog = new Dialog(mContext);

        dialog.requestWindowFeature(Window.FEATURE_RIGHT_ICON);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.checkin_dialog);
        dialog.setTitle("Sure Checking In?");

        dialog.show();
        Button yes = (Button)dialog.findViewById(R.id.yesButton);
        Button no = (Button)dialog.findViewById(R.id.noButton);

        yes.setOnClickListener(view -> {
            if (true) {

                Toast.makeText(mContext,"Checking in ...",Toast.LENGTH_SHORT).show();
                checkInOut.setText("CHECK-OUT");
                dialog.dismiss();
            } else {
                dialog.dismiss();

            }


        });

        no.setOnClickListener(view -> {
            checkInOut.setText("CHECK-IN");
            dialog.dismiss();
        });
    }



}