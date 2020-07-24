package com.example.tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.List;

public class testUser extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FusedLocationProviderClient fusedLocationProviderClient;

    ImageView user;
    TextView profname, profbusno, profbusype, proffrom, profto;
    Button start, finish, logout;

    LocationManager locationManager;
    boolean isGpsLocation;
    boolean isNetworkloc;
    ProgressDialog progressDialog;
    Location loc;
    double la, lo;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_user);


        progressDialog = new ProgressDialog(testUser.this);
        progressDialog.setMessage("uploading");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(testUser.this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("conductors").child(firebaseAuth.getCurrentUser().getUid());
        //DatabaseReference databaseReference = firebaseDatabase.getReference("conductors").child("Yq7zveh9MkdV21976IfLexkpZid2");
        user = (ImageView) findViewById(R.id.imageView3);
        profname = findViewById(R.id.name);
        profbusno = findViewById(R.id.busnum);
        profbusype = findViewById(R.id.bustyp);
        proffrom = findViewById(R.id.from);
        profto = findViewById(R.id.to);
        start = findViewById(R.id.strt);
        finish = findViewById(R.id.start);
        logout = (Button) findViewById(R.id.logout);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserClass helperClass = dataSnapshot.getValue(UserClass.class);
                profname.setText("Name :" + helperClass.getName());
                profbusno.setText("Bus No :" + helperClass.getBusno());
                profbusype.setText("Bus Type :" + helperClass.getBustype());
                proffrom.setText("From :" + helperClass.getFrom());
                profto.setText("To :" + helperClass.getTo());

                Log.d("Value", helperClass.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Logout", "it is working");
                Intent logot = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(logot);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fin", "  wrking");
                int num = 0;
                String n = "NotStarted";
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("conductors").child(userid);
                ref.child("status").setValue(num);
                ref.child("latitude").setValue(num);
                ref.child("longitude").setValue(num);
                ref.child("place").setValue(n);

            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("str", "wrkng");
                if (ActivityCompat.checkSelfPermission(testUser.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(testUser.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(testUser.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                        }
                    }
                });
                int num = 1;
                double lat, lon;
                lat = la;
                lon = lo;
                String city;
                try {

                    Geocoder geocoder = new Geocoder(testUser.this);
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(lat, lo, 1);
                    city = addresses.get(0).getLocality();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("conductors").child(userid);
                ref.child("status").setValue(num);
                ref.child("latitude").setValue(lat);
                ref.child("longitude").setValue(lon);
                ref.child("place").setValue(num);
            }
        });
    }
}