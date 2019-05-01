package com.example.flatm8s;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This Activity has been implemented in order to enable the
 * user to send Maintenance Enquiries regarding faults in
 * the flat.
 * All the text inputted in the fields of this activity is
 * automatically transferred to an email client. The user only
 * needs to press the send button.
 */

public class MaintenanceEnquiry extends AppCompatActivity {
    private EditText mailTo, mailSubject, mailMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_enquiry);

        getSupportActionBar().setTitle("Maintenance enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mailTo = findViewById(R.id.etTo);
        mailSubject = findViewById(R.id.etSubject);
        mailMessage = findViewById(R.id.etMessage);

        Button sendButton = findViewById(R.id.btnSend);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
                finish();
            }
        });
    }

    private void sendMail() {
        /* Creating a list that will store the emails to which we
         would like to send the email and we separate the addresses
         using commas which are then removed using the split method. */
        String destinationList = mailTo.getText().toString();
        String[] destinations = destinationList.split(",");
        String subject = mailSubject.getText().toString();
        String message = mailMessage.getText().toString();

        Intent emailClient = new Intent(Intent.ACTION_SEND);
        emailClient.putExtra(Intent.EXTRA_EMAIL, destinations);
        emailClient.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailClient.putExtra(Intent.EXTRA_TEXT, message);

        if(TextUtils.isEmpty(destinationList) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(message)){
            Toast.makeText(MaintenanceEnquiry.this, "Please fill in all the required fields!", Toast.LENGTH_SHORT).show();
        }else{
            emailClient.setType("message/rfc822");
            startActivity(Intent.createChooser(emailClient, "Choose an email client from below!"));
        }
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