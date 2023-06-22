package com.example.pocketscanner.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketscanner.R;
import com.example.pocketscanner.database.firebase_crud;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class log_in extends AppCompatActivity{

     TextView tvSignUp,tvForgotPassword;
    EditText etEmail,etPassword;
    Button btnLogin;

    ImageView ivLogo;
    firebase_crud ff;

    public log_in() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        tvSignUp = findViewById(R.id.tvSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        ivLogo = findViewById(R.id.ivLogo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.splash_anim);
        ivLogo.startAnimation(animation);

        ff = new firebase_crud();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if(etEmail.getText().toString().equalsIgnoreCase("admin@gmail.com") && etPassword.getText().toString().equalsIgnoreCase("admin123")){
//                    Intent i = new Intent(log_in.this, admin_home.class);
//                    startActivity(i);
                }
                if(etEmail.getText().toString().isEmpty()){
                    etEmail.setError("Please enter your email");
                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Empty Password");
                }else{
                    etEmail.setText("");
                    etPassword.setText("");
                    ff.logIn(log_in.this, getApplicationContext(),email,password);

                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail.setText("");
                etPassword.setText("");
                Intent i = new Intent(log_in.this,sign_up.class);
                startActivity(i);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(log_in.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.forgot_password_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                EditText etEmail = builder.findViewById(R.id.etEmail);
                Button btnSend = builder.findViewById(R.id.btnSend);
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String send_email = etEmail.getText().toString().trim();
                        FirebaseAuth.getInstance().sendPasswordResetEmail(send_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    UserRecords userRecord = FirebaseAuth.getInstance()..get(send_email);
//// See the UserRecord reference doc for the contents of userRecord.
//                                    System.out.println("Successfully fetched user data: " + userRecord);
                                    Toast.makeText(log_in.this, "Successfully sent! Please check your email.", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }else{
                                    Toast.makeText(log_in.this, "Something went wrong! Please try again...", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }
                            }
                        });
                    }
                });
                builder.show();
            }
        });


    }
}