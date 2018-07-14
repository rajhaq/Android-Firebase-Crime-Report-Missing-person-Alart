package com.example.crime.missingcrime;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText email;
    private Button reset, signIn;
    private ProgressDialog pBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pBar=new ProgressDialog(this);
        button();
        edittext();
    }
    void button()
    {
        reset=findViewById(R.id.buttonSend);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendresetemail();
            }
        });

    }
    void edittext()
    {

    }
    void sendresetemail()
    {
        String sEmail=email.getText().toString().trim();
        mAuth.sendPasswordResetEmail(sEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Email Sent, check it", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
