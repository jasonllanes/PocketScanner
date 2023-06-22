package com.example.pocketscanner.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pocketscanner.R;
import com.example.pocketscanner.database.firebase_crud;

public class sign_up extends AppCompatActivity {

    EditText etName,etEmail,etPassword,etConfirmPassword;
    Button btnSignUp;
    TextView tvLogIn;
    firebase_crud fc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fc = new firebase_crud();

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tvLogIn = findViewById(R.id.tvLogIn);

        btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirm = etConfirmPassword.getText().toString();
                if(etEmail.getText().toString().isEmpty()){
                    etEmail.setError("Please enter your email");
                }else if(password.length() < 6){
                    etPassword.setError("Password should be longer than 6 characters");
                }else if(!password.equalsIgnoreCase(confirm)){
                    etPassword.setError("Not Matched");
                    etConfirmPassword.setError("Not Matched");
                }else if(etPassword.getText().toString().isEmpty()){
                    etPassword.setError("Empty Password");
                }else{
                    fc.signUp(sign_up.this, getApplicationContext(),name,email,password);
                    etEmail.setText("");
                    etPassword.setText("");
                    etConfirmPassword.setText("");
                }
            }
        });

        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(sign_up.this, log_in.class);
                startActivity(i);
                finish();
            }
        });


    }
}