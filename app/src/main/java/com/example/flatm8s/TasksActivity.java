package com.example.flatm8s;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Class used in order to create the Tasks Activity which creates
 * new task fields that are added to the list-view and pushed onto
 * the database.
 * The user can also complete the Tasks added, action which also
 * removes them from the database.
 */

public class TasksActivity extends AppCompatActivity {

    ArrayList<String> itemsList = new ArrayList<>();
    ArrayList<String> keysList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private EditText newTask;
    private Button addTask, taskCompleted;
    private ListView tasksListView;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

    String taskDescription;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Tasks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        getSupportActionBar().setTitle("Tasks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newTask = findViewById(R.id.etNewTask);
        addTask = findViewById(R.id.btnAddTask);
        taskCompleted = findViewById(R.id.btnTaskCompleted);
        tasksListView = findViewById(R.id.lvTasks);
        taskCompleted.setEnabled(false);

        itemsList = new ArrayList<>();

        adapter = new ArrayAdapter<>(TasksActivity.this, android.R.layout.simple_list_item_single_choice, itemsList);
        tasksListView.setAdapter(adapter);
        tasksListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                itemSelected = true;
                taskCompleted.setEnabled(true);
            }
        });

        addChildEventListener();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    addTask(newTask);
                }
            }
        });

        taskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeTask(tasksListView);
                taskCompleted.setEnabled(false);
            }
        });

    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.add((String) dataSnapshot.child("taskDescription").getValue());

                keysList.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keysList.indexOf(key);

                if(index != -1){
                    itemsList.remove(index);
                    keysList.remove(index);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childListener);
    }

    public void addTask(View view){
        String item = newTask.getText().toString();
        String key = databaseReference.push().getKey();

        newTask.setText("");
        databaseReference.child(key).child("taskDescription").setValue(item);

        adapter.notifyDataSetChanged();
    }

    public void completeTask(View view){
        tasksListView.setItemChecked(selectedPosition, false);
        databaseReference.child(keysList.get(selectedPosition)).removeValue();
    }

    private Boolean validate(){
        Boolean result = false;

        taskDescription = newTask.getText().toString();

        // Checking if the user has introduced anything in the task description field
        if(taskDescription.isEmpty()){
            // if nothing has been put in the task description box, throw the following toast
            Toast.makeText(TasksActivity.this, "Please enter a task!", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }

        return result;
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
