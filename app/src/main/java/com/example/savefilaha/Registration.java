package com.example.savefilaha;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regUsername, regEmail,  regPassword;
    Button regBtn, regToLoginBtn;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);



        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        regName =findViewById(R.id.username);
        regBtn = findViewById(R.id.register);


        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        //Save data in FireBase on button click

    }//onCreate Method End


    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("الرجاء ملأ الخانة");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("شكل البريد الإلكتروني غير صحيح ");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            regPassword.setError("الرجاء ملأ الخانة");
            return false;
        } else if (!val.matches(passwordVal)) {
            regPassword.setError("كلمة السر يجب أن تتوفر على أربع حروف على الأقل دون مساحة فارغة");
            return false;
        }
        else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    //This function will execute when user click on Register Button
    public void registerUser(View view) {

        if( !validatePassword() |  !validateEmail() ){
            return;
        }
        else{

            String email = regEmail.getEditText().getText().toString();
            String password = regPassword.getEditText().getText().toString();
            String name =regName.getEditText().getText().toString();
            User user = new User(email, password, name);
            reference.child(name).setValue(user);
            Toast.makeText(this,"أضيف الحساب بنجاح", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Registration.this,Home.class);
            startActivity(intent);
            finish();
        }

    }
    public void log(View view){

        Intent intent=new Intent(Registration.this,Login.class);

        startActivity(intent);

    }
}