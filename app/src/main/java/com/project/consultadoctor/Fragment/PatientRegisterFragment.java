package com.project.consultadoctor.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.consultadoctor.DashboardActivity;
import com.project.consultadoctor.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatientRegisterFragment extends Fragment {
    EditText Et_FullName,Et_Email,Et_Mobile,Et_Password,Et_conPassword,Et_Gender;
    Button btn_Register;
    TextView mHaveAccountTv,TxtAddress;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    String email,password,name,mobile,gender;

    public PatientRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_patient_register, container, false);

        //XML Views
        Et_FullName=(EditText)view.findViewById(R.id.Et_FullName);
        Et_Email=(EditText)view.findViewById(R.id.Et_Email);
        Et_Mobile=(EditText)view.findViewById(R.id.Et_Mobile);
        Et_Password=(EditText)view.findViewById(R.id.Et_Password);
        Et_conPassword=(EditText)view.findViewById(R.id.Et_conPassword);
        TxtAddress=(TextView) view.findViewById(R.id.TxtAddress);
        btn_Register=(Button)view.findViewById(R.id.btn_Register);
        Et_Gender=(EditText)view.findViewById(R.id.Et_Gender);
        mHaveAccountTv=view.findViewById(R.id.have_accountTV);


        mAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering the user....");
        TxtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get location
            }
        });



        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=Et_Email.getText().toString().trim();
                password=Et_Password.getText().toString().trim();
                name=Et_FullName.getText().toString();
                mobile=Et_Mobile.getText().toString().trim();
                gender=Et_Gender.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Et_Email.setError("Invalid Email");
                    Et_Email.setFocusable(true);
                }else  if (password.length()<6){
                    Et_Password.setError("Invalid Password");
                    Et_Password.setFocusable(true);
                }else if (TextUtils.isEmpty(name)){
                    Et_FullName.setError("Name Can't be empty");
                    Et_FullName.setFocusable(true);
                }
                else if (TextUtils.isEmpty(mobile) && mobile.length()!=10){
                    Et_Mobile.setError("Invalid Number");
                    Et_Mobile.setFocusable(true);
                }else if (TextUtils.isEmpty(gender) && (gender.toLowerCase().equals("male") || gender.toLowerCase().equals("female") || gender.toLowerCase().equals("transgender"))){
                    Et_Gender.setError("Gender Can't be empty");
                    Et_Gender.setFocusable(true);
                }else {
                    RegisterPatient();
                }
            }
        });



        return  view;
    }

    private void RegisterPatient() {

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
                    hashMap.put("userType","patient"); //Additioned
                    hashMap.put("address",TxtAddress.getText().toString());
                    hashMap.put("gender",gender);

                    FirebaseDatabase database=FirebaseDatabase.getInstance();
                    DatabaseReference patientReference =database.getReference("Users").child("Patient");

                    patientReference.child(uid).setValue(hashMap);

                    Toast.makeText(getActivity(), "Registered Successfully...\n"+user.getEmail(),
                            Toast.LENGTH_SHORT).show();

                    getActivity().startActivity(new Intent(getActivity(), DashboardActivity.class));
                    getActivity().finish();
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
