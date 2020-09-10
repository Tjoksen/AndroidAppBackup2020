package com.talentjoko.realestatezim.Admin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.talentjoko.realestatezim.HomeActivity;
import com.talentjoko.realestatezim.MainActivity;
import com.talentjoko.realestatezim.R;

public class AdminCategoryActivity extends AppCompatActivity
{
    private ImageView tShirts, sportsTShirts, femaleDresses, sweathers;
    private ImageView glasses, hatsCaps, walletsBagsPurses, shoes;
    private ImageView headPhonesHandFree, Laptops, watches, mobilePhones;

    private Button LogoutBtn,CheckOrdersBtn,mantainProductsBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        permissions();
        LogoutBtn=findViewById(R.id.admin_logout__btn);
        CheckOrdersBtn=findViewById(R.id.check_orders_btn);
        mantainProductsBtn=findViewById(R.id.mantain_btn);
        tShirts = findViewById(R.id.t_shirts);
        sportsTShirts = findViewById(R.id.sports_t_shirts);
        femaleDresses = findViewById(R.id.female_dresses);
        sweathers = findViewById(R.id.sweathers);

        glasses = findViewById(R.id.glasses);
        hatsCaps = findViewById(R.id.hats_caps);
        walletsBagsPurses = findViewById(R.id.purses_bags_wallets);
        shoes = findViewById(R.id.shoes);

        headPhonesHandFree = findViewById(R.id.headphones_handfree);
        Laptops = findViewById(R.id.laptop_pc);
        watches = findViewById(R.id.watches);
        mobilePhones = findViewById(R.id.mobilephones);

        mantainProductsBtn.setOnClickListener(v -> {
            Intent intent= new Intent(AdminCategoryActivity.this, HomeActivity.class);
           intent.putExtra("Admin","Admin");
            startActivity(intent);

        });

        LogoutBtn.setOnClickListener(v -> {
            Intent intent= new Intent(AdminCategoryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        CheckOrdersBtn.setOnClickListener(v -> {
            Intent intent= new Intent(AdminCategoryActivity.this,AdminNewOrderActivity.class);
            startActivity(intent);

        });

        tShirts.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "tShirts");
            startActivity(intent);
        });


        sportsTShirts.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Sports tShirts");
            startActivity(intent);
        });


        femaleDresses.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Female Dresses");
            startActivity(intent);
        });


        sweathers.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Sweathers");
            startActivity(intent);
        });


        glasses.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Glasses");
            startActivity(intent);
        });


        hatsCaps.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Hats Caps");
            startActivity(intent);
        });



        walletsBagsPurses.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Wallets Bags Purses");
            startActivity(intent);
        });


        shoes.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Shoes");
            startActivity(intent);
        });



        headPhonesHandFree.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "HeadPhones HandFree");
            startActivity(intent);
        });


        Laptops.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Laptops");
            startActivity(intent);
        });


        watches.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Watches");
            startActivity(intent);
        });


        mobilePhones.setOnClickListener(view -> {
            Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
            intent.putExtra("category", "Mobile Phones");
            startActivity(intent);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }

        }
    }
    public void permissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(AdminCategoryActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();

        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("This permission is required for the app to function perfectly")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AdminCategoryActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }

    }
}
