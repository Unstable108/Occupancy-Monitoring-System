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

public class Login extends AppCompatActivity {
private EditText edt_email,edt_Password;
private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        edt_email = findViewById(R.id.edt_email);
        edt_Password = findViewById(R.id.edt_Password);
        mAuth = FirebaseAuth.getInstance();



    }



    public void Signintbtn(View view) {
        if (edt_email.getText().toString().length()==0){

            Toast.makeText(this, "Enter Email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edt_Password.getText().toString().length()==0){

            Toast.makeText(this, "Enter Email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        if (edt_email.getText().toString().equals("100") && edt_Password.getText().toString().equals("100")){
            startActivity(new Intent(Login.this,StrengthActivity.class));
            finish();
        }
        else {

            LoginAccount();

        }

    }
    public void gotoSignup(View view) {
        startActivity(new Intent(Login.this,Signup.class));
        finish();
    }

    private void LoginAccount(){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(edt_email.getText().toString(),edt_Password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            CheckVerifyUser();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login.this, "Please Check Your Email and Password"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void CheckVerifyUser(){
        final FirebaseUser user = mAuth.getCurrentUser();

        if (!user.isEmailVerified()){

            Toast.makeText(this, "Please Verify Your Email", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            startActivity(new Intent(Login.this,MainActivity.class));
            //finish();

        }

    }
}