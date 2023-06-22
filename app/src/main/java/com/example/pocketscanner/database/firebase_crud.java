package com.example.pocketscanner.database;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pocketscanner.authentication.log_in;
import com.example.pocketscanner.data.UserDetails;
import com.example.pocketscanner.home.home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class firebase_crud {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();


    public void retrieveProfile(String id, TextView name, TextView email){
        DatabaseReference profileReference = database.getReference("Users/" + id);
        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                name.setText(snapshot.child("name").getValue(String.class));
                email.setText(snapshot.child("email").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateProfile(Context context,String id,String fullname){
        DatabaseReference userRef = database.getReference("Users");

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", fullname);

        userRef.child(id).updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Successfully Edited", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void logIn(Activity activity, Context context, String email, String password){
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Welcome to PocketScanner!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, home.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
                    context.startActivity(i);
                    activity.finish();
                }else{
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signUp(Activity activity, Context context,String name, String email,String password){
        DatabaseReference accountRefGeneral = database.getReference("Users");

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserDetails userDetails = new UserDetails(mAuth.getUid(),name, email,password,"");
                    accountRefGeneral.child(mAuth.getUid()).setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(context, "Successfully created an account.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, home.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
                            context.startActivity(i);
                            activity.finish();
                        }
                    });
                }else{
                    Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void logOut(Activity activity, Context context){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(context, log_in.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); ;
        context.startActivity(i);
        activity.finish();
    }

}
