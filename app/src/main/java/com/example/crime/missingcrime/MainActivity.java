package com.example.crime.missingcrime;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        button();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            // User is signed in
        } else {
            Intent intentLogin= new Intent(this, SignInActivity.class );
            startActivity(intentLogin);
        }
    }
    void button(){

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
                Intent intentAddCrime = new Intent(this, AddReportActivity.class);
                startActivity(intentAddCrime);
                break;
            case R.id.menu_report_list:
                Intent intentReportList = new Intent(this, ReportsActivity.class);
                startActivity(intentReportList);
                break;

            case R.id.menu_logout:
                if(isServicesOK()){
                    signOut();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    void signOut()
    {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
