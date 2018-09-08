package com.example.crime.missingcrime;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText email,password;
    private Button signUp, signIn;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView forgot;
    private ProgressDialog pBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pBar=new ProgressDialog(this);
        button();
        textview();
        edittext();
        isInternetConnection();
    }
    void button()
    {
        signUp=(Button)findViewById(R.id.buttonLSignup);
        signIn=(Button)findViewById(R.id.buttonLLogin);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignUp=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(goSignUp);
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });

    }
    void textview()
    {
        forgot=(TextView) findViewById(R.id.textViewForgorPassword);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
    void edittext(){
        email=(EditText)findViewById(R.id.editTextLEmail);
        password=(EditText)findViewById(R.id.editTextLPass);

    }
    void signin(){
        String sEmail=email.getText().toString().trim();
        String sPassword=password.getText().toString().trim();
        Toast.makeText(this, sEmail+sPassword, Toast.LENGTH_SHORT).show();
        if(TextUtils.isEmpty(sEmail))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(sPassword))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }
        pBar.setMessage("Signin user...");
        pBar.show();
        mAuth.signInWithEmailAndPassword(sEmail,sPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            Intent signinD=new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(signinD);
                            pBar.dismiss();

                        }
                        else {
                            Toast.makeText(SignInActivity.this,"Sorry, Please try again",Toast.LENGTH_LONG).show();
                            pBar.dismiss();
                        }
                    }
                });



    }
    public void isInternetConnection()
    {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
        else {
            connected = false;
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
}
