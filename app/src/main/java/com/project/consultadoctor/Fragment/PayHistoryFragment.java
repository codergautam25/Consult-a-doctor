package com.project.consultadoctor.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.health.PackageHealthStats;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.consultadoctor.Adapter.PayHistoryAdapter;
import com.project.consultadoctor.Modal.ModalPayHistory;
import com.project.consultadoctor.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PayHistoryFragment extends Fragment {

    private final Boolean isDoctor;
    RecyclerView payHistoryRecycler;;

    public PayHistoryFragment(Boolean isDoctor) {
        this.isDoctor=isDoctor;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_pay_history, container, false);

        payHistoryRecycler=(RecyclerView)view.findViewById(R.id.payHistoryRecycler);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference payReference =database.getReference("Users").child("Payment");


        payReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ModalPayHistory> payHistoryList=new ArrayList<>();
                String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModalPayHistory payHistory=ds.getValue(ModalPayHistory.class);
                    if(isDoctor ){
                        if(currentUid.equals(payHistory.getReceiverUid())){
                            payHistoryList.add(payHistory);}
                    }else
                    {
                        if(currentUid.equals(payHistory.getSenderUid()))
                        payHistoryList.add(payHistory);
                    }
                }
                PayHistoryAdapter historyAdapter =new PayHistoryAdapter(getActivity(), payHistoryList,isDoctor);
                historyAdapter.notifyDataSetChanged();
                payHistoryRecycler.setAdapter(historyAdapter);
                payHistoryRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                payHistoryRecycler.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                payHistoryRecycler.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


      return view;
    }

}
