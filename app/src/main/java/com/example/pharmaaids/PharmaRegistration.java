package com.example.pharmaaids;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PharmaRegistration extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    private String lat,lon;

    private EditText emailEditText,passwordEditText,unameEditText,phoneEditText,confirmPasswordEditText,locationEditText;
    private Button button;

    private ImageView imageView;

    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private static final int REQUEST_CODE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_registration);

        emailEditText = (EditText)findViewById(R.id.PharmaRegistrationEmailEditTextId);
        passwordEditText = (EditText)findViewById(R.id.PharmaRegistrationPasswordEditTextId);
        confirmPasswordEditText = (EditText)findViewById(R.id.PharmaRegistrationConfirmPasswordEditTextId);
        unameEditText = (EditText)findViewById(R.id.PharmaRegistrationNameEditTextId);
        phoneEditText = (EditText)findViewById(R.id.PharmaRegistrationPhoneEditTextId);
        locationEditText = (EditText)findViewById(R.id.PharmaRegistrationLocationEditTextId);
        button = (Button)findViewById(R.id.PharmaRegistrationButtonId);
       progressDialog = new ProgressDialog(PharmaRegistration.this);
       imageView = (ImageView)findViewById(R.id.PharmaRegistrationImageViewId);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //////////////////////////////////////
        getLastLocation();

        storageReference = FirebaseStorage.getInstance().getReference("Pharmacy");

        firebaseAuth = FirebaseAuth.getInstance();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                progressDialog.show();
                String name = unameEditText.getText().toString();
                String mail = emailEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
                String repwd = confirmPasswordEditText.getText().toString();
                String phn = phoneEditText.getText().toString();
                String location = locationEditText.getText().toString();
                if(mail.isEmpty()){
                    emailEditText.setError("Enter an email address....");
                    emailEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    emailEditText.setError("Enter a valid email address....");
                    emailEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                //checking the validity of the phone
                if(phn.length()<11 || phn.length()>11)
                {
                    phoneEditText.setError("Phone Number Must be 11 Characters....");
                    phoneEditText.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
                if(phn.length()==11)
                {
                    if(phn.charAt(0)!='0')
                    {
                        progressDialog.dismiss();
                        phoneEditText.setError("Invalid number...1st Character must be '0'....");
                        phoneEditText.requestFocus();
                        return;
                    }
                    if(phn.charAt(1)!='1')
                    {
                        progressDialog.dismiss();
                        phoneEditText.setError("Phone Number Must be start with 01.....");
                        phoneEditText.requestFocus();
                        return;
                    }
                    if(phn.charAt(2)=='0' || phn.charAt(2)=='1' || phn.charAt(2)=='2')
                    {
                        progressDialog.dismiss();
                        phoneEditText.setError("Invalid number...3rd Character must be '3'/'4'/'5'/'6'/'7'/'8'/'9' ");
                        phoneEditText.requestFocus();
                        return;
                    }

                }



                //checking the validity of the password
                if(pwd.isEmpty())
                {
                    progressDialog.dismiss();
                    passwordEditText.setError("Enter a password.....");
                    passwordEditText.requestFocus();
                    return;
                }

                if(pwd.length()<6)
                {
                    progressDialog.dismiss();
                    passwordEditText.setError("Minimum length of password should be 6....");
                    passwordEditText.requestFocus();
                    return;
                }

                if(!pwd.equals(repwd))
                {
                    progressDialog.dismiss();
                    Toast.makeText(PharmaRegistration.this,"Check your Password please...",Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(phn.charAt(2)!='3' || phn.charAt(2)!='5' || phn.charAt(2)!='6'||
//                        phn.charAt(2)!='7' || phn.charAt(2)!='8' || phn.charAt(2)!='9')
//                {
//                    progressDialog.dismiss();
//                    Toast.makeText(PharmaRegistration.this,"Please enter a valid phone number...",Toast.LENGTH_SHORT).show();
//                    return;
//                }

                register(name,mail,pwd,phn,location);


            }
        });

    }

    ///////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    lat = (location.getLatitude()+"");
                                    lon = (location.getLongitude()+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = (mLastLocation.getLatitude()+"");
            lon = (mLastLocation.getLongitude()+"");
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }















    ///////////////////////////////////////////////////////////////


    private void register(final String name, final String email, String password, final String phn, final String location)
    {

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            final String userId = firebaseUser.getUid();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Pharmacy").child(userId);

                            StorageReference reference = storageReference.child(userId+"."+getExtension(imageUri));
                            reference.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content


                                            Toast.makeText(getApplicationContext(),"" +
                                                    "Image is stored successfully",Toast.LENGTH_SHORT).show();

                                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                                            while (!uriTask.isSuccessful());
                                            Uri display = uriTask.getResult();

                                            final double latitude = Double.parseDouble(lat);
                                            double longitude = Double.parseDouble(lon);


                                            Pharma pharma = new Pharma(userId,name,email,phn,display.toString(),location,latitude,longitude);



                            databaseReference.setValue(pharma).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Registration is successful,Confirm your email from your gmail",Toast.LENGTH_SHORT).show();
                                        emailEditText.setText("");
                                        passwordEditText.setText("");
                                        confirmPasswordEditText.setText("");
                                        unameEditText.setText("");
                                        phoneEditText.setText("");
                                        locationEditText.setText("");
                                        button.setVisibility(View.VISIBLE);
                                        progressDialog.dismiss();


                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Registration is unsuccessful",Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),"" +
                                                    "Image is not stored",Toast.LENGTH_SHORT).show();
                                        }
                                    });




                        }
                        else
                        {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(getApplicationContext(), "Already registered with this email", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Problem in registration...", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
    }

    public String getExtension(Uri imageUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver
                .getType(imageUri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            Picasso.with(this).load(imageUri).into(imageView);
        }
    }
}