package com.example.flatm8s;

import android.app.ProgressDialog;
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

public class LoginActivity extends AppCompatActivity {

    // Declaring all the variables that are needed
    private EditText Name;
    private EditText Password;
    private TextView Info, forgotPassword;
    private Button Login;
    private int counter = 5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assigning id's to all the variables
        Name = findViewById(R.id.etUserEmail);
        Password = findViewById(R.id.etUserPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);
        userRegistration = findViewById(R.id.tvRegister);
        forgotPassword = findViewById(R.id.tvForgotPassword);

        Info.setText("Number of attempts remaining: 5");

        firebaseAuth = firebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        /* Checking if a user has already logged in and if so
           automatically send him into the app without having
           to log in again */
        if (user != null){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });


    }

    // validate will try and login the user
    private void validate(String userName, String userPassword){

        progressDialog.setMessage("Verification in progress.");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("Number of attempts remaining: "+ counter);
                    if (counter == 0){
                        Login.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }else{
            Toast.makeText(this, "Verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
