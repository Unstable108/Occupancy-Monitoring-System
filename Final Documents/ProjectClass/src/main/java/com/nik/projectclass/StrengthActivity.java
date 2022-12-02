package com.nik.projectclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

public class StrengthActivity extends AppCompatActivity {
private TextView Str,str2;
private String a,b,p;
 private    String post;
    private DatabaseReference mreference;
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength);
        getSupportActionBar().hide();
        Str = findViewById(R.id.str);
        str2 = findViewById(R.id.str2);
        mreference = FirebaseDatabase.getInstance().getReference();



        data();

    }
    private void data(){
        mreference.child("Data1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                post = snapshot.getValue().toString();
                Str.setText("Room1: "+post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mreference.child("Data2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String post2 = snapshot.getValue().toString();
                str2.setText("Room2: "+post2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void pop(){
        int aq = Integer.parseInt(post);
        int b = aq+1;
        p = String.valueOf(b);

        HashMap<String,Object> map = new HashMap<>();
        map.put("Data"+a,p);

        FirebaseDatabase.getInstance().getReference()
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(StrengthActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            data();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StrengthActivity.this, "Check Your Internet", Toast.LENGTH_SHORT).show();
                e.getStackTrace();
            }
        });
    }
}