package com.project.consultadoctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.project.consultadoctor.Fragment.DoctorRegisterFragment;
import com.project.consultadoctor.Fragment.LoginFragment;
import com.project.consultadoctor.Fragment.PatientRegisterFragment;
import com.project.consultadoctor.Fragment.RegisterOrLoginFragment;
import com.project.consultadoctor.Fragment.UserTypeFragment;

public class RegisterActivity extends AppCompatActivity implements UserTypeFragment.Callback,
        RegisterOrLoginFragment.MyOnClick
        , LoginFragment.MyCallback {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FragmentTransaction transaction_Rules=getSupportFragmentManager().beginTransaction();
        transaction_Rules.add(R.id.frame,new RegisterOrLoginFragment());//.addToBackStack("RorLFragment");
        transaction_Rules.commit();
    }

    @Override
    public void passUserType(String user) {
        if(user.equals("doctor")){
            FragmentTransaction doctor =getSupportFragmentManager().beginTransaction();
            doctor.replace(R.id.frame,new DoctorRegisterFragment()).addToBackStack("drr");
            doctor.commit();
        }else{
            FragmentTransaction patient=getSupportFragmentManager().beginTransaction();
            patient.replace(R.id.frame,new PatientRegisterFragment()).addToBackStack("ptr");
            patient.commit();
        }
    }

    @Override
    public void onClickedOnRegOrLogin(String registerOrLogin) {
        if(registerOrLogin.equals("register")){
            FragmentTransaction register=getSupportFragmentManager().beginTransaction();
            register.replace(R.id.frame,new UserTypeFragment()).addToBackStack("r");
            register.commit();

        }else if(registerOrLogin.equals("dashboard")){
            startActivity(new Intent(RegisterActivity.this,DashboardActivity.class));
            finish();
        }else{

            FragmentTransaction login=getSupportFragmentManager().beginTransaction();
            login.replace(R.id.frame,new LoginFragment()).addToBackStack("l");
            login.commit();

        }
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 1) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void changeFragment(String fName) {
        if(fName.equals("dashboard")){
            startActivity(new Intent(RegisterActivity.this,DashboardActivity.class));
            finish();
        }else{
            FragmentTransaction register=getSupportFragmentManager().beginTransaction();
            register.replace(R.id.frame,new UserTypeFragment()).addToBackStack("l");
            register.commit();
        }
    }
}
