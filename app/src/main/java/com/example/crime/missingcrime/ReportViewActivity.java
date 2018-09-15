package com.example.crime.missingcrime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.crime.missingcrime.Model.ReportModel;
import com.example.crime.missingcrime.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ReportViewActivity extends AppCompatActivity {
    private static final String MY_PERMISSIONS_REQUEST_READ_CONTACTS =null;
    public ReportModel reportModel;
    Button booking;
    public TextView title, location,type, time,posted,postedByName;
    public ImageView logo;
    public FirebaseDatabase database;
    public FirebaseAuth mAuth;
    private FirebaseUser user;
    public UserModel userModel;
    public static String profileId,hotelID, profileMail,postedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    posted.setText(reportModel.getPostTime());
                    title.setText(reportModel.getTitle());
                    type.setText(reportModel.getType());
                    time.setText(reportModel.getTime());
                    location.setText(reportModel.getLocation());
                    postedByName.setText("Posted by: "+ getName(reportModel.getUser_id()));
                    Picasso.with(getApplicationContext())
                            .load(reportModel.getImage())
                            .resize(500, 360)
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
        posted=(TextView)findViewById(R.id.textViewDate);
        postedByName=(TextView)findViewById(R.id.textViewPostedBy);
    }
    public String getName(String id) {
            userModel = new UserModel();
            DatabaseReference userDB = database.getReference().child("users").child(id);
            userModel.setId(id);
            userDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataSnapshot.getValue();
                        userModel = dataSnapshot.getValue(UserModel.class);
                        postedBy=userModel.getName();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            return postedBy;


    }
}
