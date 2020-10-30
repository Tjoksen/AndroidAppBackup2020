package com.talentjoko.leaveappjsc.utils;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.talentjoko.leaveappjsc.R;

public  class Util {



    public  static  void sureCheckInDialog() {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_RIGHT_ICON);
//        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_menu_gallery);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.checkin_dialog);
        dialog.setTitle("Sure Checking In?");

        dialog.show();
        Button yes = (Button)dialog.findViewById(R.id.yesButton);
        Button no = (Button)dialog.findViewById(R.id.noButton);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (true) {
                    showCheckInActivity();

                } else {
                    dialog.dismiss();

                }

                Toast.makeText(getContext(),"Checking in ...",Toast.LENGTH_SHORT).show();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}
