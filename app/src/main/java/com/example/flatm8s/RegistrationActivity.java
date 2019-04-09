package com.example.flatm8s;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

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
    }

    private Boolean validate(){
        Boolean result = false;

        // Creating variables containing the registered details
        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        // Checking if all the fields have been filled in
        if (name.isEmpty() || password.isEmpty() || email.isEmpty()){
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
}
