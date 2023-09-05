package com.example.cryptotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileDisplay extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView txtFirstName, txtLastName, txtEmail, txtFullName;
    Button btnLogout, btnUpdate;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);
        txtFullName = findViewById(R.id.profile_full_name);
        txtFirstName = findViewById(R.id.profile_first_name);
        txtLastName = findViewById(R.id.profile_last_name);
        txtEmail = findViewById(R.id.profile_email);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        txtFullName.setText(user.getDisplayName().toString());
        txtFirstName.setText(user.getDisplayName().toString().substring(0,user.getDisplayName().indexOf(" ")));
        txtLastName.setText(user.getDisplayName().toString().substring(user.getDisplayName().indexOf(" "), user.getDisplayName().length()-1));
        txtEmail.setText(user.getEmail());
        btnLogout = findViewById(R.id.btn_logout);
        btnUpdate = findViewById(R.id.btn_update);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(txtFirstName.getText().toString() + " " + txtLastName.getText().toString()).build();
                user.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileDisplay.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                user.updateEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileDisplay.this, "Email succesfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}