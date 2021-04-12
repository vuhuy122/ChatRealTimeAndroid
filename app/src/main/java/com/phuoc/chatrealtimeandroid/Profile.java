package com.phuoc.chatrealtimeandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.phuoc.chatrealtimeandroid.Activity.MainActivity;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    private Button btnLogOut, btnUpload;
    private ImageView imgProfile;
    private TextView txtUsername;
    private Uri imgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnUpload = findViewById(R.id.btnUploadIMG);
        imgProfile = findViewById(R.id.imgProfile);
        txtUsername = findViewById(R.id.txtUserEmail);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
            }
        });
        txtUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(Intent.ACTION_PICK);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent,1);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadPhoto();
            }
        });

    }

    private void upLoadPhoto() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading...");
        dialog.show();
        FirebaseStorage.getInstance().getReference("image/" + UUID.randomUUID().toString()).putFile(imgPath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                           if (task.isSuccessful()){
                               updateProfilePic(task.getResult().toString());
                           }
                        }
                    });
                    Toast.makeText(Profile.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Profile.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                 dialog.setMessage("Uploaded " + (int)progress+ "%");
            }
        });
    }

    private void updateProfilePic(String Url) {
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/imgProfile").setValue(Url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
           imgPath = data.getData();
           getImageInImageView();
        }
    }

    private void getImageInImageView() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imgProfile.setImageBitmap(bitmap);
    }
}