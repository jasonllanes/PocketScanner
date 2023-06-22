package com.example.pocketscanner.identifier;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketscanner.R;
import com.example.pocketscanner.ml.ModelUnquant;
import com.google.firebase.auth.FirebaseAuth;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class start_scanning extends AppCompatActivity {

    int imageSize = 224;
    int camera;
    Bitmap image;
    LinearLayout layout;
    FirebaseAuth mAuth;
    Button btnScan,btnHistory,btnOkay;
    TextView tvResult,tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_scanning);

        btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    camera = 1;
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);


                } else {
                    // no camera on this device
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(camera == 1){
                if(requestCode == 3){

                    image = (Bitmap) data.getExtras().get("data");
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    Toast.makeText(this, "Determining image...", Toast.LENGTH_SHORT).show();
                    classifyPlastic(image);
                    camera = 0;
                }else{
                    Uri dat = data.getData();
                    image = null;
                    try {
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyPlastic(image);
                    camera = 0;
                }
            }else if(camera == 2) {
                if(requestCode == 4) {

                    image = (Bitmap) data.getExtras().get("data");
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);

                    camera = 0;
                } else {
                    Uri dat = data.getData();
                    image = null;
                    try {
                        image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    camera = 0;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void classifyPlastic(Bitmap image){
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Mouse","Keyboard","Monitor","Power Supply","Graphics Card","Random Access Memory (RAM)","Hard Disk Drive (HDD)","Solid State Drive (SSD)","CMOS Battery","CPU Fan","Motherboard","Flash Drive","Automatic Voltage Regulator (AVR)","Central Processing Unit (CPU)","Laptop"};
            Dialog builder = new Dialog(start_scanning.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.setContentView(R.layout.result_pop);
            builder.setCancelable(true);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            tvResult = builder.findViewById(R.id.tvResult);
            tvDescription = builder.findViewById(R.id.tvDescription);
            btnOkay = builder.findViewById(R.id.btnOkay);
            btnOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            tvResult.setText(classes[maxPos]);
            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }

            tvDescription.setText("It is a " + tvResult.getText().toString() + "." + "\n\nConfidence Level:\n" + s);



            builder.show();
//            result.setText(classes[maxPos]);

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

}