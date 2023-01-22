package com.example.savefilaha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Forum extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference database;
    Adab myAdapter;
    FloatingActionButton fab;
    ArrayList<User> list;
    ImageView rImage;
    ImageView imageView;
    String x;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_forum);

        Intent intent = getIntent();
        if (intent.hasExtra("name")){
            String va = intent.getStringExtra("name");
            x=va;

        }
        else{
            x="";
        }




        FirebaseStorage storage = FirebaseStorage.getInstance();

// Read the URL of the image from the Realtime Database
//        DatabaseReference imageRef = FirebaseDatabase.getInstance().getReference("users").child("image").child("image1");
//        imageRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get the image url
//                String imageUrl = dataSnapshot.getValue().toString();
//
//
//                // Use Glide to load the image into the ImageView
//
//                if (imageRef != null) {
//                    Glide.with(Forum.this)
//
//                            .load(imageUrl)
//                            .override(200, 200)
//                            .into(imageView);
//                } else {
//                    // Handle the error of imageRef being null
//                }
//
//
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Handle error
//            }
//        });
        fab= findViewById(R.id.fab);
        recyclerView = findViewById(R.id.userList);
        database = FirebaseDatabase.getInstance().getReference("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new Adab(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String imageUrl = dataSnapshot.child("image").getValue().toString();
                    String text = dataSnapshot.child("text").getValue().toString();


                    User user = dataSnapshot.getValue(User.class);
                    user.setImage(imageUrl);
                    user.setText(text);
                    user.setKey(dataSnapshot.getKey());
                    list.add(user);



                }
                myAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void AddC(View view){

        Intent intent=new Intent(Forum.this,Add.class);

        startActivity(intent);
        finish();

    }



}