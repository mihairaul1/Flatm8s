package com.example.flatm8s;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword, userAge, userUniversity;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilePic;
    String name, email, age, password, university;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        // Getting an instance of FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Checking if everything has been filled in
                if (validate()){
                    //Upload data to database
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                sendEmailVerification();
                            }else{
                                Toast.makeText(RegistrationActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            }
        });
    }

    private void setupUIViews(){
        userName = findViewById(R.id.etUserName);
        userEmail = findViewById(R.id.etUserEmail);
        userPassword = findViewById(R.id.etUserPassword);
        regButton = findViewById(R.id.btnRegister);
        userLogin = findViewById(R.id.tvUserLogin);
        userAge = findViewById(R.id.etAge);
        userUniversity = findViewById(R.id.etUniversity);
        userProfilePic = findViewById(R.id.ivProfile);
    }

    private Boolean validate(){
        Boolean result = false;

        // Creating variables containing the registered details
        name = userName.getText().toString();
        password = userPassword.getText().toString();
        email = userEmail.getText().toString();
        age = userAge.getText().toString();
        university = userUniversity.getText().toString();

        // Checking if all the fields have been filled in
        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty() || university.isEmpty()){
            // If empty, display error message
            Toast.makeText(this, "Please enter all the details!", Toast.LENGTH_LONG).show();
        }else{
            result = true;
        }

        return  result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserInfo();
                        Toast.makeText(RegistrationActivity.this, "Successfully registered! Verification mail sent.", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    }else{
                        Toast.makeText(RegistrationActivity.this, "Verification mail has NOT been sent!", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    /* Function that send the data from the registration form to
       the database */

    private void sendUserInfo(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile = new UserProfile(name, email, age, university);

        myRef.setValue(userProfile);
    }
}
