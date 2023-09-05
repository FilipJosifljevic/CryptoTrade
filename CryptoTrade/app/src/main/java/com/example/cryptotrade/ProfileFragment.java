package com.example.cryptotrade;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    TextView txtFirstName, txtLastName, txtEmail, txtFullName, txtUserName;
    Button btnLogout, btnUpdate;
    ImageView profileImageView;
    FloatingActionButton fab;
    private ActivityResultLauncher<String> imagePickerLauncher;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 100;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        txtFullName = view.findViewById(R.id.profile_full_name);
        txtUserName = view.findViewById(R.id.profile_user_name);
        txtFirstName = view.findViewById(R.id.profile_first_name);
        txtLastName = view.findViewById(R.id.profile_last_name);
        txtEmail = view.findViewById(R.id.profile_email);
        profileImageView = view.findViewById(R.id.profile_img);
        fab = view.findViewById(R.id.fab_camera);

        fab.setOnClickListener(v -> launchImagePicker());


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        txtFullName.setText(user.getDisplayName().toString());
        txtUserName.setText(user.getEmail().toString().substring(0,user.getEmail().toString().indexOf("@")));
        txtFirstName.setText(user.getDisplayName().toString().substring(0,user.getDisplayName().indexOf(" ")));
        txtLastName.setText(user.getDisplayName().toString().substring(user.getDisplayName().indexOf(" "), user.getDisplayName().length()));
        txtEmail.setText(user.getEmail());

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri ->{
            if(uri!=null){
                profileImageView.setImageURI(uri);
                UserProfileChangeRequest userImageChangeRequest = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri).build();
                auth.getCurrentUser().updateProfile(userImageChangeRequest);
            }
        });

        String imageUri = auth.getCurrentUser().getPhotoUrl().toString();
        if( imageUri!= null){
            Picasso.get()
                    .load(auth.getCurrentUser().getPhotoUrl())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profileImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully from offline cache
                        }

                        @Override
                        public void onError(Exception e) {
                            // Try again with online caching if image not found in offline cache
                            Picasso.get()
                                    .load(imageUri)
                                    .into(profileImageView);
                        }
                    });

        } else {
            profileImageView.setImageResource(R.drawable.def_prof);
        }
        btnLogout = view.findViewById(R.id.btn_logout);
        btnUpdate = view.findViewById(R.id.btn_update);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                            Toast.makeText(getActivity(), "Profile updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                user.updateEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Email succesfully updated!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return view;
    }

    private void launchImagePicker(){
        imagePickerLauncher.launch("image/*");
    }

}