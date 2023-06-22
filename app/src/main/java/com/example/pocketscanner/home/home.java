package com.example.pocketscanner.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;



import com.example.pocketscanner.R;
import com.example.pocketscanner.databinding.ActivityHomeBinding;
import com.example.pocketscanner.identifier.scan_fragment;
import com.example.pocketscanner.profile.profile_fragment;


public class home extends AppCompatActivity {

    ActivityHomeBinding binding;
    boolean isConnected;
    LinearLayout tvConnection;
    Dialog builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        replaceFragment(new home_fragment());
        binding.btmNavBarView.setBackground(null);

        binding.btmNavBarView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home){
                replaceFragment(new home_fragment());
            }else if(item.getItemId() == R.id.scan){
                replaceFragment(new scan_fragment());
            }else if(item.getItemId() == R.id.profile){
                replaceFragment(new profile_fragment());
            }
            return true;
        });

    }
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}