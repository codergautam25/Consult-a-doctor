package com.project.consultadoctor.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.consultadoctor.DashboardActivity;
import com.project.consultadoctor.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorRegisterFragment extends Fragment {
    private static final int REQUEST_CODE = 102;
    EditText Et_FullName, Et_Email, Et_Mobile, Et_Password, Et_conPassword, EtDoctorQualification, EtDoctorExperience, EtSpecialization;
    Button btn_Register;
    TextView mHaveAccountTv;
    CheckBox chkAddress;
    EditText etLat, etLng;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String email, password, qualification, experience, specialization, name, mobile;

    public DoctorRegisterFragment() {
        // Required empty public constructor
    }


    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
    }

    String latitude = null;
    String longitude = null;

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
                                    latitude = Double.toString(location.getLatitude());
                                    longitude = Double.toString(location.getLongitude());
                                    Toast.makeText(getActivity(),latitude+"  "+longitude  , Toast.LENGTH_SHORT).show();
                                   // lonTextView.setText(+"");
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(getActivity(), "Turn on location", Toast.LENGTH_LONG).show();
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

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = Double.toString(mLastLocation.getLatitude());
            longitude = Double.toString(mLastLocation.getLongitude());
            Toast.makeText(getActivity(),mLastLocation.getLatitude()+"   "+mLastLocation.getLongitude(),Toast.LENGTH_LONG).show();

        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                Objects.requireNonNull(getActivity()),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }else{
                chkAddress.setChecked(false);
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





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_doctor_register, container, false);

        //XML Views
        Et_FullName=(EditText)view.findViewById(R.id.Et_FullName);
        Et_Email=(EditText)view.findViewById(R.id.Et_Email);
        Et_Mobile=(EditText)view.findViewById(R.id.Et_Mobile);
        Et_Password=(EditText)view.findViewById(R.id.Et_Password);
        Et_conPassword=(EditText)view.findViewById(R.id.Et_conPassword);
        chkAddress =(CheckBox) view.findViewById(R.id.TxtAddress);
        EtDoctorQualification=(EditText)view.findViewById(R.id.EtDoctorQualification);
        EtDoctorExperience=(EditText)view.findViewById(R.id.EtDoctorExperience);
        btn_Register=(Button)view.findViewById(R.id.btn_Register);
        EtSpecialization=(EditText)view.findViewById(R.id.EtSpecialization);
        mHaveAccountTv=view.findViewById(R.id.have_accountTV);

        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering the user....");
        chkAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    getLastLocation();
                }else{
                    Toast.makeText(getActivity(), "Not Checked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=Et_Email.getText().toString().trim();
                password=Et_Password.getText().toString().trim();
                qualification=EtDoctorQualification.getText().toString().trim();
                experience=EtDoctorExperience.getText().toString().trim();
                specialization=EtSpecialization.getText().toString().trim();
                name=Et_FullName.getText().toString();
                mobile=Et_Mobile.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Et_Email.setError("Invalid Email");
                    Et_Email.setFocusable(true);
                }else  if (password.length()<6){
                    Et_Password.setError("Invalid Password");
                    Et_Password.setFocusable(true);
                }else if(TextUtils.isEmpty(qualification)){
                    EtDoctorQualification.setError("Please Input Your Qualification");
                    EtDoctorQualification.setFocusable(true);
                }else if (TextUtils.isEmpty(experience) || experience.equals(0)){
                    EtDoctorExperience.setError("You must have atleast one year Experience");
                    EtDoctorExperience.setFocusable(true);
                }
                else if(TextUtils.isEmpty(specialization)){
                    EtSpecialization.setError("Input Your Specialization");
                    EtSpecialization.setFocusable(true);
                }else if (TextUtils.isEmpty(name)){
                    Et_FullName.setError("Name Can't be empty");
                    Et_FullName.setFocusable(true);
                }
                else if (TextUtils.isEmpty(mobile) && mobile.length()!=10){
                    Et_Mobile.setError("Invalid Number");
                    Et_Mobile.setFocusable(true);
                }else if(!chkAddress.isChecked()){
                    chkAddress.setError("You must have to allow your location");
                    chkAddress.setFocusable(true);
                }
                else {
                    RegisterUser();
                }
            }
        });


        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return  view;
    }

    private void RegisterUser() {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String email=user.getEmail();
                    String uid=user.getUid();

                    HashMap<Object,String> hashMap=new HashMap<>();
                    hashMap.put("email",email);
                    hashMap.put("uid",uid);
                    hashMap.put("name",name);//user later
                    hashMap.put("onlineStatus","online");
                    hashMap.put("typingTo","noOne");
                    hashMap.put("phone",mobile);
                    hashMap.put("image","");
                    hashMap.put("cover","");
                    hashMap.put("userType","doctor"); //Additioned
                    hashMap.put("specialization",specialization);
                    hashMap.put("experience",experience);
                    hashMap.put("address", latitude+","+longitude);
                    hashMap.put("degree",qualification);

                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference DoctorReference=database.getReference("Users").child("Doctor");

                    DoctorReference.child(uid).setValue(hashMap);

                    Toast.makeText(getActivity(), "Registered Successfully...\n"+user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                    Objects.requireNonNull(getActivity()).startActivity(new Intent(getActivity(), DashboardActivity.class));
                   //finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

            }
        });


    }
}
