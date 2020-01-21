package com.talent.posdat.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talent.posdat.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalViewAdapter  extends  RecyclerView.Adapter<HorizontalViewAdapter.ViewHolder>{
    private  static  final  String TAG="HorizontalViewAdapter";

    //vars
    private ArrayList<String> mCountries = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPropicUrls= new ArrayList<>();
    private Context mContext;

    public HorizontalViewAdapter(Context mContext,ArrayList<String> mCountries, ArrayList<String> mNames, ArrayList<String> mPropicUrls) {
        this.mCountries = mCountries;
        this.mNames = mNames;
        this.mPropicUrls = mPropicUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder : called");
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_home_display,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder : called");

        Glide.with(mContext)
                .asBitmap()
                .load(mPropicUrls.get(position))
                .placeholder(R.drawable.profile)
                .into(holder.profImage);
        holder.fullName.setText(mNames.get(position));
        holder.country.setText(mCountries.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onclick  : clicked on an item" +mNames.get(position));
                Toast.makeText(mContext, "Clicked " + mNames.get(position), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullName;
        CircleImageView profImage;
        TextView country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profImage =itemView.findViewById(R.id.users_home_profile_image);
            country = itemView.findViewById(R.id.users_home_age_city_nation);
            fullName=itemView.findViewById(R.id.users_home_fullname);
        }

    }

}
