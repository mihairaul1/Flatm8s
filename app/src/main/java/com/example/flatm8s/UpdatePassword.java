package com.example.flatm8s;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This Activity lets the user update his password by pressing the
 * Change Password button within the Profile Activity.
 * The new password gets uploaded into Firebase Authentication
 * therefore the user is able to use it straight after pressing
 * the update button.
 */

public class UpdatePassword extends AppCompatActivity {

    private EditText newPassword;
    private Button update;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        getSupportActionBar().setTitle("Update password");

        newPassword = findViewById(R.id.etNewPassword);
        update = findViewById(R.id.btnNewPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newUserPassword = newPassword.getText().toString();

                firebaseUser.updatePassword(newUserPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(UpdatePassword.this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            Toast.makeText(UpdatePassword.this, "Failed to update your password!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
