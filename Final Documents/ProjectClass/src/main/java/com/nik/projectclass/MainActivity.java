package com.nik.projectclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
private TextView textc;
    private ValueEventListener Vlistener;
    private ValueEventListener Vlistener2;
    private DatabaseReference mreference;
    private DatabaseReference reference;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> spinnerList;
    private DatabaseReference reference2;
    private ArrayAdapter<String> adapter2;
    private ArrayList<String> spinnerList2;
    private String a,b,Str,post;
    private Spinner spinner,spinner2;
    private Button ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textc = findViewById(R.id.textc);
        ok = findViewById(R.id.ok);
        spinner = findViewById(R.id.spinner);
        spinner2 = findViewById(R.id.spinner2);
        mreference = FirebaseDatabase.getInstance().getReference();

        reference = FirebaseDatabase.getInstance().getReference().child("Building");
        spinnerList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerList);
        spinner.setAdapter(adapter);

        reference2 = FirebaseDatabase.getInstance().getReference().child("Rooms");
        spinnerList2 = new ArrayList<>();
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerList2);
        spinner2.setAdapter(adapter2);

        GetRooms();
        GetRelationShip();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String j =  parent.getItemAtPosition(position).toString();
                MainActivity.this.a = j;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String n =  parent.getItemAtPosition(position).toString();
                MainActivity.this.b = n;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    private void GetRelationShip(){
        spinnerList.clear();
        Vlistener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()) {

                    spinnerList.add(item.getValue().toString());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                error.getMessage();
            }
        });
    }

    private void GetRooms(){
        spinnerList2.clear();
        Vlistener2 = reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item:snapshot.getChildren()) {

                    spinnerList2.add(item.getValue().toString());

                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                error.getMessage();
            }
        });


    }

    public void Submit(View view) {
        //Toast.makeText(this, ""+a, Toast.LENGTH_SHORT).show();
        data();
        if (post!=null) {
            int aq = Integer.parseInt(post);
            int b = aq + 1;
            String p = String.valueOf(b);
            //Toast.makeText(this, "" + p, Toast.LENGTH_SHORT).show();


            HashMap<String, Object> map = new HashMap<>();
            map.put("Data" + a, p);

            FirebaseDatabase.getInstance().getReference().updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                 ok.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, "Enter Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void  data(){
        mreference.child("Data"+a).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void Logout(View view) {
          data();
        if (post!=null) {

            int aq = Integer.parseInt(post);
            int b = aq - 1;
            String p = String.valueOf(b);

            HashMap<String, Object> map = new HashMap<>();
            map.put("Data" + a, p);

            FirebaseDatabase.getInstance().getReference()
                    .updateChildren(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, Login.class));
                                finish();
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Check Your Internet", Toast.LENGTH_SHORT).show();
                    e.getStackTrace();
                }
            });
        }
    }
}