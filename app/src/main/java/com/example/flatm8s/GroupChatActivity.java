package com.example.flatm8s;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flatm8s.Adapters.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * In this Activity, the group chat between all the flat occupants
 * has been implemented.
 * The user is able to send messages and delete his/ her messages.
 * The messages that are sent by the user appear under a brighter
 * color than the rest of the messages.
 */

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    MessageAdapter messageAdapter;
    UserProfile userProfile;
    List<Message> messages;

    RecyclerView rvMessage;
    EditText chatMessage;
    ImageButton sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        getSupportActionBar().setTitle("Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

    }

    private void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userProfile = new UserProfile();

        rvMessage = findViewById(R.id.rvChat);
        chatMessage = findViewById(R.id.etChatMessage);
        sendMessage = findViewById(R.id.btnSendMessage);
        sendMessage.setOnClickListener(this);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(chatMessage.getText().toString())){
            Message message = new Message(chatMessage.getText().toString(), userProfile.getUserName());
            chatMessage.setText("");
            databaseReference.push().setValue(message);
        }else{
            Toast.makeText(GroupChatActivity.this, "Empty message can't be sent!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        userProfile.setUserEmail(currentUser.getEmail());

        firebaseDatabase.getReference("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(UserProfile.class);
                AllMethods.name = userProfile.getUserName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference = firebaseDatabase.getReference("Messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                displayMessages(messages);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<>();

                for(Message m: messages){
                    if(m.getKey().equals(message.getKey())){
                        newMessages.add(message);
                    }else{
                        newMessages.add(m);
                    }
                }

                messages = newMessages;

                displayMessages(messages);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);

                message.setKey(dataSnapshot.getKey());

                List<Message> newMessages = new ArrayList<Message>();

                for(Message m:messages){
                    if(!m.getKey().equals(message.getKey())){
                        newMessages.add(m);
                    }
                }
                messages = newMessages;
                displayMessages(messages);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    private void displayMessages(final List<Message> messages) {
        LinearLayoutManager manager = new LinearLayoutManager(GroupChatActivity.this);
        manager.setStackFromEnd(true);
        rvMessage.setLayoutManager(manager);
        messageAdapter = new MessageAdapter(GroupChatActivity.this, messages, databaseReference);
        rvMessage.setAdapter(messageAdapter);

        rvMessage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {

                if(bottom<oldBottom){
                    rvMessage.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rvMessage.smoothScrollToPosition(rvMessage.getAdapter().getItemCount()-1);
                        }
                    }, 100);
                }

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
