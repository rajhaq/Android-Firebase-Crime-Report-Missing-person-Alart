package com.example.crime.missingcrime;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crime.missingcrime.Model.UserModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public UserModel userModel;
    Button send;
    TextView textView;
    public FirebaseDatabase database;
    CardView add,list,myReports,emergency,sendSMS;
    private FusedLocationProviderClient client;
    private LocationManager locationManager;
    private com.google.android.gms.location.LocationListener listener;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
        textView = findViewById(R.id.location);
        database = FirebaseDatabase.getInstance();
        client = LocationServices.getFusedLocationProviderClient(this);
        cardView();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            // User is signed in
            userModel = new UserModel();
            DatabaseReference userDB = database.getReference().child("users").child(user.getUid());
            userModel.setId(user.getUid());
            userDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        dataSnapshot.getValue();
                        userModel = dataSnapshot.getValue(UserModel.class);
                        textView.setText(userModel.getName());
                        Toast.makeText(MainActivity.this, userModel.getName()+user.getUid(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        } else {
            Intent intentLogin= new Intent(this, SignInActivity.class );
            startActivity(intentLogin);
        }
    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Toast.makeText(this, "isServicesOK: Google Play Services is working", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Toast.makeText(this, "isServicesOK: an error occured but we can fix it", Toast.LENGTH_SHORT).show();
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent intentMapView = new Intent(this, MainActivity.class);
                startActivity(intentMapView);
                break;

            case R.id.menu_add_crime:
                addReport();
                break;
            case R.id.menu_my_report_list:
                myReport();
                break;
            case R.id.menu_report_list:
                reportList();
                break;
            case R.id.menu_approve:
                Intent intentApprove = new Intent(this, ApproveActivity.class);
                startActivity(intentApprove);
                break;
            case R.id.menu_contacts:
                emergencyContact();
                break;
            case R.id.menu_logout:
                if(isServicesOK()){
                    signOut();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    public void cardView()
    {
        add=(CardView)findViewById(R.id.addReport);
        list=(CardView)findViewById(R.id.reportList);
        myReports=(CardView)findViewById(R.id.myReports);
        emergency=(CardView)findViewById(R.id.emergencyContacts);
        sendSMS=(CardView)findViewById(R.id.sendSMS);
//        on click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReport();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportList();
            }
        });
        myReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myReport();
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emergencyContact();
            }
        });
        sendSMS.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

                    return false;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {

                        if(location!= null){

                            final float lat= (float) location.getLatitude();
                            final float lang= (float) location.getLongitude();
                            textView.setText("Lattetude: "+lat+"Longitude:"+lang);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String url="https://maps.google.com/?q="+lat+","+lang;
                                    Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( url ) );
                                    startActivity( browse );
                                }
                            });
//                            Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                            String messageToSend = "Help Me!!"+"\n https://maps.google.com/?q="+lat+","+lang;
                            String number = "01739216256";

                            Toast.makeText(MainActivity.this, "SMS send : "+messageToSend , Toast.LENGTH_SHORT).show();
                            SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);                        }

                    }
                });

                return false;
            }
        });

    }

    void signOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
        return;
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {

// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS))
            {

                Toast.makeText(getBaseContext(),
                        "App requires permission to send SMS",
                        Toast.LENGTH_SHORT).show();

            }

            else
            {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);

            }
        }    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    public void addReport()
    {
        Intent intentAddCrime = new Intent(this, AddReportActivity.class);
        startActivity(intentAddCrime);
    }
    public void reportList()
    {
        Intent intentReportList = new Intent(this, ReportsActivity.class);
        startActivity(intentReportList);
    }
    public void myReport()
    {
        Intent myReports = new Intent(this, MyReportActivity.class);
        startActivity(myReports);
    }
    public void emergencyContact()
    {
        Intent intentContacts = new Intent(this, ContactsActvity.class);
        startActivity(intentContacts);
    }
}
