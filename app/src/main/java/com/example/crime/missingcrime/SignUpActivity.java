package com.example.crime.missingcrime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crime.missingcrime.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity {

    private EditText name,email,password;
    private Button signUp, signIn;
    private ProgressDialog pBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pBar=new ProgressDialog(this);
        button();
        edittext();
    }
    void button(){

        signUp =(Button)findViewById(R.id.buttonSignUp);
        signIn =(Button)findViewById(R.id.buttonSignIn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin= new Intent(SignUpActivity.this, SignInActivity.class );
                startActivity(intentLogin);

            }
        });

    }
    void edittext(){
        name=(EditText)findViewById(R.id.editTextName);
        email=(EditText)findViewById(R.id.editTextEmail);
        password=(EditText)findViewById(R.id.editTextPassword);

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            Intent intentMain= new Intent(this, MainActivity.class );
            startActivity(intentMain);
        } else {

        }
    }
    public void signup(){
        String sEmail=email.getText().toString().trim();
        final String sName=name.getText().toString().trim();
        String sPassword=password.getText().toString().trim();
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
        if(TextUtils.isEmpty(sName))
        {
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_SHORT).show();
            return;
        }
        pBar.setTitle("User Signing up...");
        pBar.show();
        mAuth.createUserWithEmailAndPassword(sEmail,sPassword)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(SignUpActivity.this,"Successfully Done",Toast.LENGTH_LONG).show();

//                            checkForUserAndSignuUp(mAuth.getCurrentUser());
                            // dataSet(mAuth.getCurrentUser(),editName.getText().toString());
                            dataSet(mAuth.getCurrentUser(),name.getText().toString());
                            Intent signup=new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(signup);
                            pBar.dismiss();
                        }
                        else {
                            Toast.makeText(SignUpActivity.this,"Sorry, Please try again",Toast.LENGTH_LONG).show();
                            pBar.dismiss();
                        }
                    }
                });

    }
    private void sendEmailVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {
                        }
                        // [END_EXCLUDE]
                    }               });
        // [END send_email_verification]
    }
    private void dataSet(FirebaseUser currentUser, String Name)
    {
        final DatabaseReference firebase = database.getReference().child("users").child(currentUser.getUid());
        UserModel userModel = new UserModel();
        userModel.setEmail(currentUser.getEmail());
        userModel.setName(Name);
        userModel.setId(currentUser.getUid());
        firebase.setValue(userModel);

    }

}
