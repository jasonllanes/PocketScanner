package com.example.pocketscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.pocketscanner.authentication.log_in;
import com.example.pocketscanner.database.firebase_crud;
import com.example.pocketscanner.home.home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash_activity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    ImageView ivLogo;
    FirebaseAuth mAuth;

    firebase_crud fc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivLogo = findViewById(R.id.ivLogo);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        ivLogo.startAnimation(animation);


        mAuth = FirebaseAuth.getInstance();
        fc = new firebase_crud();

        FirebaseUser user = mAuth.getCurrentUser();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(user != null) {
                    Intent intent = new Intent(splash_activity.this, home.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(splash_activity.this, log_in.class);
                    startActivity(intent);
                    finish();
                }
                }
            }, SPLASH_DISPLAY_LENGTH);




    }
}