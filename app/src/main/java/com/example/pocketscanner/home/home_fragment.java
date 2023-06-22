package com.example.pocketscanner.home;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.pocketscanner.R;

import com.example.pocketscanner.data.NewsData;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class home_fragment extends Fragment {
    FirebaseListAdapter listAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth;
    StorageReference storageReference;
    ListView lvContributions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_fragment, container, false);

        lvContributions = v.findViewById(R.id.lvContributions);

        Query query = FirebaseDatabase.getInstance().getReference().child( "TechNews");
        FirebaseListOptions<NewsData> o = new FirebaseListOptions.Builder<NewsData>()
                .setLayout(R.layout.news_item)
                .setQuery(query,NewsData.class)
                .build();
        listAdapter = new FirebaseListAdapter(o) {
            @Override
            protected void populateView(View v, Object model, int position) {
                String img_link;
                TextView name = v.findViewById(R.id.tvName);
                TextView date_released = v.findViewById(R.id.tvDate);
                ImageView item = v.findViewById(R.id.ivItem);

                NewsData newsData = (NewsData) model;
                name.setText(((NewsData) model).getName());
                date_released.setText(((NewsData) model).getDate_released());
                img_link = ((NewsData) model).getImg_link();



                //set the proper link when saving already the contribution for this to work
                storageReference = FirebaseStorage.getInstance().getReference("TechNewsImages/").child("news_1.png");
                Glide.with(home_fragment.this).load(img_link).into(item);

                lvContributions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView name = view.findViewById(R.id.tvName);
                        Toast.makeText(getActivity(), name.getText().toString(), Toast.LENGTH_SHORT).show();
//                        TextView tvID = view.findViewById(R.id.tvID);
//                        String contribution_id = tvID.getText().toString();
//                        Intent intent = new Intent(activity, edit_contribution.class);
//                        intent.putExtra("id",id);
//                        intent.putExtra("contribution_id",contribution_id);
//                        intent.putExtra("barangay",barangay);
//                        activity.startActivity(intent);

                    }
                });

            }
        };
        lvContributions.setAdapter(listAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        listAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        listAdapter.stopListening();
    }
}