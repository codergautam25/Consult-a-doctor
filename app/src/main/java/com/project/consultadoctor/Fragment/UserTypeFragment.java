package com.project.consultadoctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.project.consultadoctor.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserTypeFragment extends Fragment {
    CircleImageView imgDoctor,imgPatient;
    DrawerLayout drawerLayout;


    public UserTypeFragment() {
        // Required empty public constructor
    }

    Callback mListener = null;

    public interface Callback{
        void passUserType(String user);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (Callback) context;
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
        View view= inflater.inflate(R.layout.fragment_user_type, container, false);
        imgDoctor = (CircleImageView)view.findViewById(R.id.imgDoctor);
        imgPatient = (CircleImageView)view.findViewById(R.id.imgPatient);

        imgDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.passUserType("doctor");
            }
        });

        imgPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.passUserType("patient");
            }
        });

        return  view;

    }

}
