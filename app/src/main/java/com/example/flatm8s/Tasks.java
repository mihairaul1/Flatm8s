package com.example.flatm8s;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tasks extends AppCompatActivity {

    ArrayList<String> itemList;

    ArrayAdapter<String> adapter;

    EditText newTask;
    Button addTask;
    ListView tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        getSupportActionBar().setTitle("Tasks");

        newTask = findViewById(R.id.etNewTask);
        addTask = findViewById(R.id.btnAddTask);
        tasks = findViewById(R.id.lvTasks);

        itemList = new ArrayList<>();

        adapter = new ArrayAdapter<>(Tasks.this, android.R.layout.simple_list_item_multiple_choice, itemList);

        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.add(newTask.getText().toString());
                newTask.setText("");
                adapter.notifyDataSetChanged();
            }
        };

        tasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                SparseBooleanArray positionChecker = tasks.getCheckedItemPositions();

                int count = tasks.getCount();

                for(int item = count-1; item >= 0; item--){
                    if(positionChecker.get(item)){
                        adapter.remove(itemList.get(item));
                    }
                }

                positionChecker.clear();
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        addTask.setOnClickListener(addListener);

        tasks.setAdapter(adapter);
    }
}
