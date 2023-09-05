package com.example.cryptotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Registration extends AppCompatActivity {

    TextInputEditText editFirstName, editLastName, editEmail, editPassword;
    Button btnRegister;
    //MaterialToolbar topAppBar;
    TextView loginText;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), StartUpScreen.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        editFirstName = findViewById(R.id.first_name);
        editLastName = findViewById(R.id.last_name);
        editEmail = findViewById(R.id.email);
        editPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btn_register);
        loginText = findViewById(R.id.login_now);


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName, lastName, email, password;
                firstName = editFirstName.getText().toString();
                lastName = editLastName.getText().toString();
                email = editEmail.getText().toString();
                password = editPassword.getText().toString();


                if(TextUtils.isEmpty(firstName)){
                    Toast.makeText(Registration.this, "First name must be provided!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lastName)){
                    Toast.makeText(Registration.this, "Last name must be provided!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Registration.this, "E-mail must be provided!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!email.matches(emailPattern)){
                    Toast.makeText(Registration.this, "E-mail format invalid!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Registration.this, "Password must be provided!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<5){
                    Toast.makeText(Registration.this, "Password must be longer than 5 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Registration.this, "Account successfully created!",
                                            Toast.LENGTH_SHORT).show();
                                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(firstName + " " + lastName).build();
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    firebaseUser.updateProfile(userProfileChangeRequest);
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Registration.this, "Authentication failed!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }

    private Boolean validateFirstName(){
        String val = editFirstName.getText().toString();
        if(val.isEmpty()){
            editFirstName.setError("First name can't be empty!");
            return false;
        } else{
            editFirstName.setError(null);
            return true;
        }
    }
    private Boolean validateLastName(){
        String val = editLastName.getText().toString();
        if(val.isEmpty()){
            editLastName.setError("First name can't be empty!");
            return false;
        } else{
            editLastName.setError(null);
            return true;
        }
    }
    private Boolean validateEmail(){
        String val = editEmail.getText().toString();
        if(val.isEmpty()){
            editFirstName.setError("First name can't be empty!");
            return false;
        } else{
            editFirstName.setError(null);
            return true;
        }
    }
}