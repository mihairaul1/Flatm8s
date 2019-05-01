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
 * Class used in order to create the Consumables Activity which creates
 * new consumables fields that are added to the list-view and pushed onto
 * the database.
 * The user can also remove the Consumables added, action which also
 * removes them from the database.
 */

public class ConsumablesActivity extends AppCompatActivity {

    ArrayList<String> itemsList = new ArrayList<>();
    ArrayList<String> keysList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private EditText newConsumables;
    private Button addConsumables, deleteConsumables;
    private ListView consumablesListView;
    private Boolean itemSelected = false;
    private int selectedPosition = 0;

    String consumablesDescription;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Consumables");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumables);

        getSupportActionBar().setTitle("Consumables");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newConsumables = findViewById(R.id.etNewConsumables);
        addConsumables = findViewById(R.id.btnAddConsumables);
        deleteConsumables = findViewById(R.id.btnDeleteConsumables);
        consumablesListView = findViewById(R.id.lvConsumables);
        deleteConsumables.setEnabled(false);

        itemsList = new ArrayList<>();

        adapter = new ArrayAdapter<>(ConsumablesActivity.this, android.R.layout.simple_list_item_single_choice, itemsList);
        consumablesListView.setAdapter(adapter);
        consumablesListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        consumablesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                itemSelected = true;
                deleteConsumables.setEnabled(true);
            }
        });

        addChildEventListener();

        addConsumables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    pushConsumables(newConsumables);
                }
            }
        });

        deleteConsumables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeConsumables(consumablesListView);
                deleteConsumables.setEnabled(false);
            }
        });
    }

    private void addChildEventListener() {
        ChildEventListener childListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                adapter.add((String) dataSnapshot.child("consumablesDescription").getValue());

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


    public void pushConsumables(View view){
        String item = newConsumables.getText().toString();
        String key = databaseReference.push().getKey();

        newConsumables.setText("");
        databaseReference.child(key).child("consumablesDescription").setValue(item);

        adapter.notifyDataSetChanged();
    }

    public void removeConsumables(View view){
        consumablesListView.setItemChecked(selectedPosition, false);
        databaseReference.child(keysList.get(selectedPosition)).removeValue();
    }

    private Boolean validate(){
        Boolean result = false;

        consumablesDescription = newConsumables.getText().toString();

        // Checking if the user has introduced anything in the task description field
        if(consumablesDescription.isEmpty()){
            // if nothing has been put in the task description box, throw the following toast
            Toast.makeText(ConsumablesActivity.this, "Please enter a consumable!", Toast.LENGTH_SHORT).show();
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
