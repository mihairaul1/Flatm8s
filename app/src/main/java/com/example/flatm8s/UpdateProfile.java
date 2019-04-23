package com.example.flatm8s;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateProfile extends AppCompatActivity {

    private EditText updateUserName, updateUserEmail, updateUserDOB, updateUserAge,
            updateUserUniversity, updateUserCourse, updateUserYear;
    private Button save;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private ImageView updateProfilePicture;

    private static int PICK_IMAGE = 123;
    Uri imagePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                updateProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update profile");

        /*Assigning each variable with the correspondent id
        declared in the XML file
        * */

        updateUserName = findViewById(R.id.etNameUpdate);
        updateUserEmail = findViewById(R.id.etEmailUpdate);
        updateUserAge = findViewById(R.id.etAgeUpdate);
        updateUserDOB = findViewById(R.id.etDOBUpdate);
        updateUserUniversity = findViewById(R.id.etUniversityUpdate);
        updateUserCourse = findViewById(R.id.etCourseUpdate);
        updateUserYear = findViewById(R.id.etYearUpdate);
        save = findViewById(R.id.btnSaveUpdate);
        updateProfilePicture = findViewById(R.id.ivProfileUpdate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        updateUserDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int cYear = calendar.get(Calendar.YEAR);
                int cMonth = calendar.get(Calendar.MONTH);
                int cDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), datePickerListener, cYear, cMonth, cDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        final DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                updateUserName.setText(userProfile.getUserName());
                updateUserEmail.setText(userProfile.getUserEmail());
                updateUserAge.setText(userProfile.getUserAge());
                updateUserDOB.setText(userProfile.getUserDOB());
                updateUserUniversity.setText(userProfile.getUserUniversity());
                updateUserCourse.setText(userProfile.getUserCourse());
                updateUserYear.setText(userProfile.getUserYear());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this, databaseError.getCode(), Toast.LENGTH_LONG).show();
            }
        });

        final StorageReference storageReference = firebaseStorage.getReference();

        storageReference.child(firebaseAuth.getUid()).child("Images/Profile Picture").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(updateProfilePicture);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateUserName.getText().toString();
                String email = updateUserEmail.getText().toString();
                String age = updateUserAge.getText().toString();
                String dob = updateUserDOB.getText().toString();
                String university = updateUserUniversity.getText().toString();
                String course = updateUserCourse.getText().toString();
                String year = updateUserYear.getText().toString();

                UserProfile userProfile = new UserProfile(name, email, dob, age, university, course, year);

                databaseReference.setValue(userProfile);

                StorageReference imageReference = storageReference.child(firebaseAuth.getUid()).child("Images").child("Profile Picture");
                UploadTask uploadTask = imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UpdateProfile.this, "Upload successfull!", Toast.LENGTH_SHORT).show();
                    }
                });

                finish();
            }
        });

        updateProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image!"), PICK_IMAGE);
            }
        });
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            String format = new SimpleDateFormat("dd MM YYYY").format(calendar.getTime());
            updateUserDOB.setText(format);
            updateUserAge.setText(Integer.toString(calculateAge(calendar.getTimeInMillis())));
        }
    };

    int calculateAge(long date){
        // Extracting the user input
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        // Getting today's date
        Calendar today = Calendar.getInstance();

        // Calculate age by substracting dob's year from the
        // current year
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        // Comparing the current day with the day from the dob
        // if date of birth day is greater than today's date age--
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age --;
        }

        return age;
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
