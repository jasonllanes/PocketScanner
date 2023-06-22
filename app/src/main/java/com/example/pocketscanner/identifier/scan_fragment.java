package com.example.pocketscanner.identifier;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.pocketscanner.R;
import com.example.pocketscanner.ml.ModelUnquant;
import com.google.firebase.auth.FirebaseAuth;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class scan_fragment extends Fragment {
    int imageSize = 224;
    int camera;
    Bitmap image;
    LinearLayout layout;
    FirebaseAuth mAuth;
    Button btnScan,btnHistory,btnOkay;
    TextView tvResult,tvDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan_fragment, container, false);

        btnScan = v.findViewById(R.id.btnScan);
//        btnHistory = v.findViewById(R.id.btnHistory);



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),start_scanning.class);
                startActivity(i);
            }
        });


//        btnHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        return v;
    }
}