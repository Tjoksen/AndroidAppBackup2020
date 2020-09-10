package com.talentjoko.rxjavawebinar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talentjoko.rxjavawebinar.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> dataSet;

    public MyAdapter(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_list_item,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setTitle(dataSet.get(position));
        holder.setRepoSubTitle(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
       public void setTitle(String title){
            TextView mRepoTitleTv=itemView.findViewById(R.id.repo_title);
            mRepoTitleTv.setText(title);
        }
        public void setRepoSubTitle(String subTitle){
            TextView mRepoSubTitleTv= itemView.findViewById(R.id.repo_sub_title);
            mRepoSubTitleTv.setText(subTitle);
        }
    }
}
