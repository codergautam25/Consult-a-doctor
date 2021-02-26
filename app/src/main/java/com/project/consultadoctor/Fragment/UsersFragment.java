package com.project.consultadoctor.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.consultadoctor.Adapter.AdapterChat;
import com.project.consultadoctor.Adapter.DoctorAdapter;
import com.project.consultadoctor.Adapter.PatientAdapter;
import com.project.consultadoctor.ChatActivity;
import com.project.consultadoctor.Modal.DoctorModal;
import com.project.consultadoctor.Modal.ModelChat;
import com.project.consultadoctor.Modal.PatientModal;
import com.project.consultadoctor.Modal.ProfileModal;
import com.project.consultadoctor.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    private static final String SHARED_PREFS = "SHARED_PREFS";
    RecyclerView recyclerView;
    DoctorAdapter doctorAdapter;
    ArrayList<DoctorModal> doctorList;
    ArrayList<PatientModal> patientList;
    FirebaseAuth firebaseAuth;
    Boolean isDoctor;
    TextView txtNoPatient;
    private PatientAdapter patientAdapter;

    public ArrayList<String> doctorsMsg;

    SharedPreferences prefs;

    public UsersFragment(Boolean isDoctor) {
        this.isDoctor=isDoctor;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);


        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.doctorRecycler);
        txtNoPatient=view.findViewById(R.id.txtNoPatient);

        prefs = this.getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorList=new ArrayList<>();
        patientList=new ArrayList<>();
        doctorsMsg = new ArrayList<>();

        if (isDoctor) {
            getAllPatients();
        }else {
            getAllDoctors();
        }


        return  view;
    }
    private void lastMessages() {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    ArrayList<String> tempList = new ArrayList<>();
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelChat chat=ds.getValue(ModelChat.class);
                        if (chat.getReceiver().equals(firebaseAuth.getCurrentUser().getUid())){
                             tempList.add(chat.getSender());

                        }

                    }
                    //Set the values
                    try {
                        Set<String> set = new HashSet<String>(tempList);
                        prefs.edit().putStringSet("patient",set).apply();
                    }catch (Exception e)
                    {
                        Log.e("TAG",e.getMessage() + "");
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(),"getting some exceptions",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAllPatients() {
        lastMessages();
        Set<String> set =prefs.getStringSet("patient", null);
        if(set != null && set.size() >0){
            doctorsMsg=new ArrayList<>(set);
        }else {
            doctorsMsg = new ArrayList<>();
        }


        DatabaseReference patientRef= FirebaseDatabase.getInstance().getReference("Users").child("Patient");

        patientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                   PatientModal  patientModal=ds.getValue(PatientModal.class);
                    if (doctorsMsg.contains(patientModal.getUid())){
                        patientList.add(patientModal);
                    }
                }
                if (patientList.size()==0){
                    txtNoPatient.setVisibility(View.VISIBLE);
                }else{
                    txtNoPatient.setVisibility(View.INVISIBLE);
                }
                patientAdapter=new PatientAdapter(getActivity(), patientList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(patientAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getAllDoctors() {
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference doctorRef= FirebaseDatabase.getInstance().getReference("Users").child("Doctor");

        doctorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                doctorList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                  DoctorModal  doctorModal=ds.getValue(DoctorModal.class);
                    if (!doctorModal.getUid().equals(firebaseUser.getUid())){
                        doctorList.add(doctorModal);
                    }

                    doctorAdapter=new DoctorAdapter(getActivity(), doctorList);
                    recyclerView.setAdapter(doctorAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
