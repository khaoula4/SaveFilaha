package com.example.savefilaha;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.savefilaha.ml.LiteModelAiyVisionClassifierInsectsV13;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class Detection extends AppCompatActivity {

    Button pick,take,identify;
    ImageView BirdPic,circle;
    TextView TVresult;
    private Bitmap img;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                assert data != null;
                img = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(img.getWidth(), img.getHeight());
                img = ThumbnailUtils.extractThumbnail(img, dimension, dimension);
                BirdPic.setImageBitmap(img);
                circle.setVisibility(View.INVISIBLE);
                TVresult.setText(R.string.identify_bird);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        pick = findViewById(R.id.picgallery_btn);
        take = findViewById(R.id.takepic_btn);
        identify=findViewById(R.id.identify_btn);
        BirdPic =findViewById(R.id.imageView5);
        TVresult =findViewById(R.id.textView2);
        circle = findViewById(R.id.imageView4);


        take.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });











        @SuppressLint({"ResourceAsColor", "SetTextI18n"}) ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            BirdPic.setImageURI(data.getData());
                            circle.setVisibility(View.INVISIBLE);
                            TVresult.setText("Click to Identify Bird Specie");
                            //TVresult.setTextColor(getResources().getColor(R.color.black));


                        }

                        Uri uri = null;
                        if (data != null) {
                            uri = data.getData();
                        }
                        try {
                            img = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        pick.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);


        });


        identify.setOnClickListener(v -> {



            try {

                LiteModelAiyVisionClassifierInsectsV13 model = LiteModelAiyVisionClassifierInsectsV13.newInstance(getApplicationContext());

                // Creates inputs for reference.
                TensorImage image = TensorImage.fromBitmap(img);

                // Runs model inference and gets result.
                LiteModelAiyVisionClassifierInsectsV13.Outputs outputs = model.process(image);
                List<Category> probability = outputs.getProbabilityAsCategoryList();

                // Releases model resources if no longer used.
                model.close();

                probability.sort(Comparator.comparing(Category::getScore).reversed());

                TVresult.setText(probability.get(0).getLabel());


            } catch (IOException e) {
                // TODO Handle the exception
            }

        });


    }



}