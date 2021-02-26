package com.project.consultadoctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.project.consultadoctor.R;
import com.project.consultadoctor.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterOrLoginFragment extends Fragment {

    Button mRegisterBtn,mLoginBtn;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth mAtuth = FirebaseAuth.getInstance();
        if(mAtuth.getCurrentUser()
                !=null){
            mListener.onClickedOnRegOrLogin("dashboard");
        }
    }

    public RegisterOrLoginFragment() {
        // Required empty public constructor
    }

    MyOnClick mListener = null;

    public interface MyOnClick{
        void onClickedOnRegOrLogin(String registerOrLogin);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MyOnClick) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_register_or_login, container, false);


        mLoginBtn=view.findViewById(R.id.login_btn);
        mRegisterBtn=view.findViewById(R.id.register_btn);

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mListener.onClickedOnRegOrLogin("register");
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickedOnRegOrLogin("login");
            }
        });
       return view;
    }


}
