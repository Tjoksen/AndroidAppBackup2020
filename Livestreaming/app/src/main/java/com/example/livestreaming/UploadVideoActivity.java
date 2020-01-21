package com.example.livestreaming;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Random;

public class UploadVideoActivity extends AppCompatActivity {

    private VideoView videoUploadView;
   private Button buttonUpload,buttonChoose,backButton;
   private  static  final int GALLERY_PICK=100;

   private Uri filePath;
   private  String myUrl,videoKey;

   DatabaseReference retrieveRef= FirebaseDatabase.getInstance().getReference().child("Videos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        buttonUpload=findViewById(R.id.buttonUpload);
        backButton=findViewById(R.id.backToMain);
        buttonChoose=findViewById(R.id.buttonChoose);
        videoUploadView=findViewById(R.id.uploadVideoView);

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showFileChooser();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentToVideoView();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadVideo();
            }
        });
    }

    public  void showFileChooser()
    {
        //ContentValues content = new  Co
        Intent videoIntent = new Intent();
        videoIntent.setAction(Intent.ACTION_GET_CONTENT);
        videoIntent.setType("video/mp4");
        startActivityForResult(Intent.createChooser(videoIntent,"Select Video"),GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null & data.getData() != null) {
            filePath = data.getData();


            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        UploadVideoActivity.this);
                mediacontroller.setAnchorView(videoUploadView);
                // Get the URL from String VideoURL
                Uri video = filePath;
                videoUploadView.setMediaController(mediacontroller);
                videoUploadView.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoUploadView.requestFocus();
            videoUploadView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    //.dismiss();
                    videoUploadView.start();

                }

            });
        }
    }
    private void uploadVideo()
    {
        if(filePath !=null)
        {
            final ProgressDialog progressDialog= new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();


            final StorageReference videoRef= FirebaseStorage.getInstance().getReference().child("videos");


            videoRef.putFile(filePath).
                   addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 *taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded "+progress + "%...");
                }
            }).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return videoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();


                        Random random = new Random();
                       int randomNum= random.nextInt() *10;



                        Toast.makeText(UploadVideoActivity.this, "Video Uploaded", Toast.LENGTH_SHORT).show();
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("filePath",myUrl);
                        

                        retrieveRef.child("video"+ randomNum).updateChildren(userMap);


                        progressDialog.dismiss();

                        //startActivity(new Intent(UploadVideoActivity.this, VideoViewActivity.class));

                    }

                    else {
                        Toast.makeText(UploadVideoActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
            else {
                
        }


    }
public void  sentToVideoView()
{
   Intent intentVideoView = new Intent(UploadVideoActivity.this,VideoViewActivity.class);
   intentVideoView.putExtra("videoKey",myUrl);
   startActivity(intentVideoView);

}

}
