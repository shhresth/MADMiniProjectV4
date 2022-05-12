package com.example.madminiprojectv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Edits;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    DatabaseReference reference;
    EditText e1;
    ListView l1;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Button button;
    EditText ee;
String name;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    button = findViewById(R.id.button1);
        e1=(EditText) findViewById(R.id.editText);
        l1=(ListView)findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        request_name();
        l1.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference().getRoot();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> myset = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();
                while (i.hasNext()){
                    myset.add(((DataSnapshot)i.next()).getKey());
                }
                list.clear();
                list.addAll(myset);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Please Check your Network Connectivity!", Toast.LENGTH_LONG).show();
            }
        });

        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(MainActivity.this,ChatroomActivity.class);
                intent.putExtra("room_name",((TextView)view).getText().toString());
                intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });

    }
public void request_name(){
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
    alertDialog.setTitle("Enter Your Name Here");
    ee = new EditText(this);
    alertDialog.setView(ee);
    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            name= ee.getText().toString();
        }
    });
    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.cancel();
            request_name();
        }
    });
    alertDialog.show();
}

    public void create_chatroom(android.view.View view) {
        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put(e1.getText().toString(),"Chatroom");
        reference.updateChildren(hashMap);

        list.add(e1.getText().toString());
        adapter.notifyDataSetChanged();
    }
}