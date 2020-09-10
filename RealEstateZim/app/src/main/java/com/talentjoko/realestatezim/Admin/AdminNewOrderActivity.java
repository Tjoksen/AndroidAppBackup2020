package com.talentjoko.realestatezim.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.talentjoko.realestatezim.Model.AdminOrders;
import com.talentjoko.realestatezim.R;


public class AdminNewOrderActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);
        init();

    }

    private void init() {
        ordersRef = FirebaseDatabase.getInstance().getReference().child("AdminOrders");
        orderList=findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecylerInit();
    }

    private void firebaseRecylerInit() {
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef,AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter= new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int i, @NonNull final AdminOrders adminOrders) {
                holder.userName.setText("Name: " + adminOrders.getName());
                holder.userPhoneNumber.setText("Phone: " + adminOrders.getPhone());
                holder.userTotalPrice.setText("Total Amount: " + adminOrders.getTotalAmount());
                holder.userDateTime.setText("Order at : " + adminOrders.getDate()+"Time"+adminOrders.getTime());
                holder.userShippingAddress.setText("Shipping Address: " + adminOrders.getAddress() +adminOrders.getCity());
                holder.showOrdersBtn.setOnClickListener(v -> {
                    String uID=getRef(i).getKey();
                    Intent intent =new Intent(AdminNewOrderActivity.this,AdminUserProductsActivity.class);
                    intent.putExtra("uid",uID);
                    startActivity(intent);
                });
                holder.itemView.setOnClickListener(v -> {
                    CharSequence options1[]=new CharSequence[]
                            {
                                    "Yes",
                                    "No"
                            };
                    AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                    builder.setTitle("Have you shipped this order products?");

                    builder.setItems(options1, (dialog, which) -> {
                        if(which==0){
                            String uID=getRef(i).getKey();
                            removeOrder(uID);
                        }
                        else {
                            finish();
                        }
                    });
                    builder.show();
                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return  new AdminOrdersViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrdersViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        private Button showOrdersBtn;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.order_user_name);
            userPhoneNumber=itemView.findViewById(R.id.order_phone_number);
            userTotalPrice=itemView.findViewById(R.id.order_total_price);
            userDateTime=itemView.findViewById(R.id.order_date_time);
            userShippingAddress=itemView.findViewById(R.id.order_address_city);
            showOrdersBtn=itemView.findViewById(R.id.show_all_pdtcs_btn);
        }
    }

    private void removeOrder(String uID) {
        ordersRef.child(uID).removeValue();
    }

}
