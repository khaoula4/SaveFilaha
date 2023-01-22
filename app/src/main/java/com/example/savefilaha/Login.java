package com.example.savefilaha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button login_btn;

    TextInputLayout email, password, name;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //This Line will hide the status bar from the screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_login);


        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login);

    }

    public void loginUser(View view) {
        //Validate Login Info
        if (!validateEmail() | !validatePassword()) {
            return;
        }
        else {
            isUser();
        }
    }
    public void isUser(){

        final String userEnteredname = name.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("name").equalTo(userEnteredname);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    password.setError(null);
                    password.setErrorEnabled(false);


                    String nameFromDB = snapshot.child(userEnteredname).child("name").getValue(String.class);

                    if( nameFromDB.equals(userEnteredname)){



                        Intent intent = new Intent(Login.this,Forum.class);
                        intent.putExtra("name", userEnteredname);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        name.setError("لا يوجد حساب بهذا الاسم");
                        name.requestFocus();
                    }
                }
                else {

                    password.setError("كلمة السر خاطئة");
                    password.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    private Boolean validateEmail() {
        String val = name.getEditText().getText().toString();
        if (val.isEmpty()) {
            name.setError("الرجاء ملأ الخانة");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("الرجاء ملأ الخانة");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void reg(View view){

        Intent intent=new Intent(Login.this,Registration.class);

        startActivity(intent);

    }


}