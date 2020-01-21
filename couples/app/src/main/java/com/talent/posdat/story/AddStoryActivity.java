package com.talent.posdat.story;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.talent.posdat.R;
import com.talent.posdat.home.HomeActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class AddStoryActivity extends AppCompatActivity {

    private Uri imageUri;
    private String myUrl="";
    private StorageTask storageTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        storageReference= FirebaseStorage.getInstance().getReference().child("story");

        CropImage.activity()
                .setAspectRatio(9,16)
                .start(AddStoryActivity.this);

    }

    private  String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void publishStory()
    {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Posting");
        pd.setMessage("Updating your Story");
        pd.show();

        if(imageUri !=null)
        {
            final StorageReference imageReference=storageReference
                    .child(System.currentTimeMillis() + "." +getFileExtension(imageUri));

            storageTask=imageReference.putFile(imageUri);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Task<Uri>  then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUri=task.getResult();
                        myUrl=downloadUri.toString();

                        String myId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Story").child(myId);
                        String storyId=reference.push().getKey();
                        long timeend=System.currentTimeMillis() + 86400000;// 1 day long

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("imageUrl",myUrl);
                        hashMap.put("timestart", ServerValue.TIMESTAMP);//time of the server in Firebase
                        hashMap.put("timeend",timeend);
                        hashMap.put("storyid",storyId);
                        hashMap.put("userid",myId);

                        reference.child(storyId).setValue(hashMap);
                        pd.dismiss();
                        finish();
                    }else {

                        Toast.makeText(AddStoryActivity.this, "Failed :" +   task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddStoryActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(this, "No Image Selected...", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri=result.getUri();
            publishStory();
        }else {
            Toast.makeText(this, "Something has gone wrrong..Try Again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddStoryActivity.this, HomeActivity.class));
            finish();
        }
    }
}
