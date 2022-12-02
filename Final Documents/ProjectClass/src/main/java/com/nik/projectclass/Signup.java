package com.nik.projectclass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Signup extends AppCompatActivity {
    private EditText edt_email,edt_Password,edt_Name;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        edt_email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        edt_Name = findViewById(R.id.edt_Name);


    }

    public void Login(View view) {
        if (edt_Name.getText().toString().length()==0){
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            return;
        } if (edt_email.getText().toString().length()==0){
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return;
        } if (edt_Password.getText().toString().length()==0){
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            SignupAccount();
        }
    }
    private void SignupAccount() {
       // final String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(edt_email.getText().toString(), edt_Password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            EmailVerification();
                            final HashMap<String, Object> map = new HashMap<>();
                            map.put("Name",edt_Name.getText().toString());
                            map.put("Email",edt_email.getText().toString());
                            map.put("Password",edt_Password.getText().toString());


                            FirebaseDatabase.getInstance().getReference()
                                    .child("New Users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .push()
                                    .setValue(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Signup.this, "Create Account Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Signup.this,Login.class));
                                                finish();
                                            }
                                            else{
                                                // load.setVisibility(View.INVISIBLE);
                                                Toast.makeText(Signup.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //  load.setVisibility(View.INVISIBLE);
                Toast.makeText(Signup.this, "Please Retry Error: "+e.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void EmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Signup.this,"Please Verifiy Your Email Then Login ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this,Login.class));
                    finish();
                } else {
                    Toast.makeText(Signup.this, "Send Email Verification Falied Try After Some Time", Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Signup.this, ""+e.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}