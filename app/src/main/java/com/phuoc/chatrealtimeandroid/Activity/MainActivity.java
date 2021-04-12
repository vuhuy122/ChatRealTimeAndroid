
package com.phuoc.chatrealtimeandroid.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.phuoc.chatrealtimeandroid.R;
import com.phuoc.chatrealtimeandroid.User;

public class MainActivity extends AppCompatActivity {
    private TextView txtLogin, txtSignUp , txtForget;
    private Button btnSubmit;
    private TextInputLayout txtConfirm,txtusername;
    private TextInputEditText  edtEmail, edtPass, edtConfirm, edtUsername;
    private boolean isSignUp = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ChatRealtimeAndroid);
        setContentView(R.layout.activity_main);
        txtLogin = findViewById(R.id.txtLogin);
        txtSignUp = findViewById(R.id.txtSignUp);
        txtConfirm = findViewById(R.id.txtconfirm);
        txtusername = findViewById(R.id.txtusername);
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtuser);
        edtPass = findViewById(R.id.edtPass);
        edtConfirm = findViewById(R.id.edtConfirm);

        btnSubmit = findViewById(R.id.btnSubmit);
        txtForget = findViewById(R.id.txtForget);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent i =new Intent(MainActivity.this, FriendActivity.class);
            startActivity(i);
            finish();
            //nếu đã đăng nhập thì khong cần đăng nhập lại
        }
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignUp = false;
                txtLogin.setBackground(getResources().getDrawable(R.drawable.text_selected));
                txtLogin.setTextColor(getResources().getColor(R.color.white_card));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txtSignUp.setElevation(0);
                    txtLogin.setElevation(4);
                }
                txtSignUp.setBackground(getResources().getDrawable(R.drawable.text_unselect));
                txtSignUp.setTextColor(getResources().getColor(R.color.red));
                btnSubmit.setText("Log in");
                txtForget.setVisibility(View.VISIBLE);
                txtConfirm.setVisibility(View.GONE);
                txtusername.setVisibility(View.GONE);

            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSignUp = true;
                txtSignUp.setBackground(getResources().getDrawable(R.drawable.text_selected));
                txtSignUp.setTextColor(getResources().getColor(R.color.white_card));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    txtLogin.setElevation(0);
                    txtSignUp.setElevation(4);
                }
                txtLogin.setBackground(getResources().getDrawable(R.drawable.text_unselect));
                txtLogin.setTextColor(getResources().getColor(R.color.red));
                btnSubmit.setText("Sign Up");
                txtForget.setVisibility(View.GONE);
                txtConfirm.setVisibility(View.VISIBLE);
                txtusername.setVisibility(View.VISIBLE);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtEmail.getText().toString().isEmpty() || edtPass.getText().toString().isEmpty()){
                    if (isSignUp && edtConfirm.getText().toString().isEmpty()){
                        Toast.makeText(MainActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (isSignUp){
                    handlerSignUp();
                }
                else {
                    handlerLogin();
                }
            }
        });
    }
    public void handlerSignUp(){
        if ( edtPass.getText().toString().equals(edtConfirm.getText().toString()) == false && edtUsername.getText().toString().isEmpty()) {

            Toast.makeText(MainActivity.this, "Please check your info" , Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(edtUsername.getText().toString(),edtEmail.getText().toString(),""));
                        Intent i =new Intent(MainActivity.this, FriendActivity.class);
                        startActivity(i);
                        Toast.makeText(MainActivity.this, "Sign up successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }
    public void handlerLogin(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Log in successfully", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(MainActivity.this, FriendActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}