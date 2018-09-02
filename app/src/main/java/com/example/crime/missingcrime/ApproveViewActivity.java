package com.example.crime.missingcrime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.crime.missingcrime.Model.ReportModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ApproveViewActivity extends AppCompatActivity {
    private static final String MY_PERMISSIONS_REQUEST_READ_CONTACTS =null;
    NumberPicker singleNumber,dualNumber,dulexNumber;
    public ReportModel reportModel;
    Button booking;
    public TextView title, location,type, time;
    public ImageView logo;
    public FirebaseDatabase database;
    public FirebaseAuth mAuth;
    private FirebaseUser user;

    public static String profileId;
    public static String hotelID;
    public static String profileMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_view);
        textview();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String tmp = extras.getString("id");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        profileId = user.getUid();
        profileMail = user.getEmail();
        database = FirebaseDatabase.getInstance();
        DatabaseReference deleteDB = database.getReference().child("reports").child(tmp);
        final ImageView profilePicture = (ImageView) findViewById(R.id.imageViewPro);
        reportModel = new ReportModel();
        reportModel.setUser_id(tmp);
/*        name.setText(userModel.getFirstName());
        status.setText(userModel.getStatus());
        email.setText(userModel.getEmail());*/
        deleteDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getValue();
                    reportModel = dataSnapshot.getValue(ReportModel.class);
                    hotelID=reportModel.getId();
                    title.setText(reportModel.getTitle());
                    type.setText(reportModel.getType());
                    time.setText(reportModel.getTime());
                    location.setText(reportModel.getLocation());
                    Picasso.with(getApplicationContext())
                            .load(reportModel.getImage())
                            .resize(500, 300)
                            .centerCrop()
                            .into(profilePicture);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
                /*
                Map<String,String> map=dataSnapshot.getValue(Map.class);

                name.setText(map.get("firstName"));
                status.setText(map.get("status"));
                email.setText(map.get("email"));
*/


        });

    }
    public void textview() {
        title = (TextView) findViewById(R.id.textViewHVT);
        type = (TextView) findViewById(R.id.textViewType);
        location = (TextView) findViewById(R.id.textViewLocation);
        time = (TextView) findViewById(R.id.textViewTime);
    }
}
