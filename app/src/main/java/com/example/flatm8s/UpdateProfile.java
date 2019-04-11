package com.example.flatm8s;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {

    private EditText updateUserName, updateUserEmail, updateUserAge, updateUserUniversity;
    private Button save;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        /*Assigning each variable with the correspondent id
        declared in the XML file
        * */

        updateUserName = findViewById(R.id.etNameUpdate);
        updateUserEmail = findViewById(R.id.etEmailUpdate);
        updateUserAge = findViewById(R.id.etAgeUpdate);
        updateUserUniversity = findViewById(R.id.etUniversityUpdate);
        save = findViewById(R.id.btnSaveUpdate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                updateUserName.setText(userProfile.getUserName());
                updateUserEmail.setText(userProfile.getUserEmail());
                updateUserAge.setText(userProfile.getUserAge());
                updateUserUniversity.setText(userProfile.getUserUniversity());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateUserName.getText().toString();
                String email = updateUserEmail.getText().toString();
                String age = updateUserAge.getText().toString();
                String university = updateUserUniversity.getText().toString();

                UserProfile userProfile = new UserProfile(name, email, age, university);

                databaseReference.setValue(userProfile);

                finish();
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
