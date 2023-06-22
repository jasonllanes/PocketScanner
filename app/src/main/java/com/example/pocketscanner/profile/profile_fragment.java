package com.example.pocketscanner.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketscanner.R;
import com.example.pocketscanner.authentication.log_in;
import com.example.pocketscanner.database.firebase_crud;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class profile_fragment extends Fragment {

    Button btnYes, btnNo,btnEditProfile,btnAbout,btnLogout;
    TextView tvName,tvEmail;
    firebase_crud fc;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        fc = new firebase_crud();
        mAuth = FirebaseAuth.getInstance();

        tvName = v.findViewById(R.id.tvName);
        tvEmail = v.findViewById(R.id.tvEmail);

        fc.retrieveProfile(mAuth.getUid(),tvName,tvEmail);
        btnEditProfile = v.findViewById(R.id.btnEditProfile);
        btnAbout = v.findViewById(R.id.btnAbout);
        btnLogout = v. findViewById(R.id.btnLogOut);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(getContext());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.edit_name);
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
                                    Toast.makeText(getContext(), "Successfully sent! Please check your email.", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }else{
                                    Toast.makeText(getContext(), "Something went wrong! Please try again...", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }
                            }
                        });
                    }
                });
                builder.show();
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), about_app.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(getContext());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.setContentView(R.layout.log_out_pop);
                builder.setCancelable(true);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                btnYes = builder.findViewById(R.id.btnYes);
                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getContext(), log_in.class);
                        startActivity(i);

                        Toast.makeText(getActivity(), "Log out successfully", Toast.LENGTH_SHORT).show();



                    }
                });

                btnNo = builder.findViewById(R.id.btnNo);
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });




        return v;
    }
}