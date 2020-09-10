package com.talentjoko.employeeclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talentjoko.employeeclient.entities.EmployeeEntity;

import java.util.List;

public class EmployeeesAdapter extends RecyclerView.Adapter<EmployeeesAdapter.MyViewHolder> {
 private List<EmployeeEntity> employeeEntities;
 private Context mContext;

    public EmployeeesAdapter(List<EmployeeEntity> employeeEntities, Context mContext) {
        this.employeeEntities = employeeEntities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.idTextView.setText(String.valueOf(employeeEntities.get(position).getId()));
        holder.fnameTextView.setText(String.valueOf(employeeEntities.get(position).getFirstName()));
        holder.snameTextView.setText(String.valueOf(employeeEntities.get(position).getLastName()));
        holder.emailTextView.setText(String.valueOf(employeeEntities.get(position).getEmail()));

        holder.itemView.setTag(employeeEntities.get(position));
      //  holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return employeeEntities.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView emailTextView;
        TextView idTextView;
        TextView fnameTextView;
        TextView snameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            idTextView = itemView.findViewById(R.id.idtxtview);
            fnameTextView = itemView.findViewById(R.id.firstNametextView);
            snameTextView = itemView.findViewById(R.id.tsurnametextView);
            emailTextView = itemView.findViewById(R.id.emailtextView);

        }
    }
}
