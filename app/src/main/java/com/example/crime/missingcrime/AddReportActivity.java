package com.example.crime.missingcrime;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crime.missingcrime.Model.ReportModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddReportActivity extends AppCompatActivity {
    private EditText title,location,type,time;
    private Button add;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pBar;
    public static String userId;
    private StorageReference mStorage;
    private FirebaseStorage storage;
    private ImageButton imageUpload;
    public String image;
    ImageView report;
    private static final int GALLERY_INTENT=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        database=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pBar=new ProgressDialog(this);
        userId = user.getUid();
        report=(ImageView)findViewById(R.id.imageView3) ;
        storage = FirebaseStorage.getInstance();
        imageUpload=(ImageButton)findViewById(R.id.imageButton);
        mStorage= storage.getReference();
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);

            }
        });
        button();
        edittext();
    }
    void button()
    {
        add=(Button)findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setMessage("Adding report...");
                pBar.show();
                addReport();
                Intent goSignUp=new Intent(AddReportActivity.this,MainActivity.class);
                startActivity(goSignUp);
            }
        });

    }
    void addReport()
    {
        final DatabaseReference firebase = database.getReference().child("reports").push();
        String pushId = firebase.getKey();
        ReportModel report = new ReportModel();
        report.setId(pushId);
        report.setTitle(title.getText().toString());
        report.setLocation(location.getText().toString());
        report.setTime(time.getText().toString());
        report.setType(type.getText().toString());
        report.setImage(image);
        report.setUser_id(userId);
        firebase.setValue(report);
        pBar.dismiss();

    }

    void edittext(){
        title=(EditText)findViewById(R.id.editTextTitle);
        location=(EditText)findViewById(R.id.editTextLocation);
        type=(EditText)findViewById(R.id.editTextType);
        time=(EditText)findViewById(R.id.editTextTime);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){
            pBar.setMessage("Uploading...");
            pBar.show();
            Uri uri=data.getData();
            StorageReference filePath = mStorage.child("report").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddReportActivity.this,"Upload done",Toast.LENGTH_SHORT).show();
                    pBar.dismiss();
                    Uri  dUri=taskSnapshot.getDownloadUrl();
                    image=dUri.toString();
                    Picasso.with(getApplicationContext())
                            .load(dUri.toString())
                            .resize(450, 300)
                            .centerCrop()
                            .into(report);
                }
            });
        }
    }


}
