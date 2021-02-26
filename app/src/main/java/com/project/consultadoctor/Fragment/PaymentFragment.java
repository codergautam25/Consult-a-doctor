package com.project.consultadoctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.consultadoctor.Modal.DoctorModal;
import com.project.consultadoctor.Modal.PatientModal;
import com.project.consultadoctor.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    EditText amount, note, name, upivirtualid,receiverMobile;
    Button send;
    String TAG ="main";
    final int UPI_PAYMENT = 0;
    TextView txtPayHistory;

    FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

    FirebaseDatabase database;
    SharedPreferences prefs;
    private static final String SHARED_PREFS_FILE = "SHARED_PREFS_FILE";

    MyPayCallback mListener = null;


    public interface MyPayCallback{
        void payCall();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (MyPayCallback) context;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_payment, container, false);

         database=FirebaseDatabase.getInstance();

        prefs = this.getActivity().getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);



        send = (Button)view.findViewById(R.id.send);
        amount = (EditText)view.findViewById(R.id.amount_et);
        note = (EditText)view.findViewById(R.id.note);
        name = (EditText)view.findViewById(R.id.name);
        upivirtualid =(EditText)view.findViewById(R.id.upi_id);
        txtPayHistory=(TextView)view.findViewById(R.id.txtPayHistory);
        receiverMobile=(EditText)view.findViewById(R.id.receiverMobile);

        txtPayHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.payCall();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
                if (TextUtils.isEmpty(name.getText().toString().trim())){
                    Toast.makeText(getActivity()," Name is invalid", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(upivirtualid.getText().toString().trim())){
                    Toast.makeText(getActivity()," UPI ID is invalid", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(note.getText().toString().trim())){
                    Toast.makeText(getActivity()," Note is invalid", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(amount.getText().toString().trim())){
                    Toast.makeText(getActivity()," Amount is invalid", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(receiverMobile.getText().toString().trim()) && receiverMobile.length() !=10){
                    Toast.makeText(getActivity()," Mobile is invalid", Toast.LENGTH_SHORT).show();
                }else{
                    payUsingUpi(name.getText().toString(), upivirtualid.getText().toString(),
                            note.getText().toString(), amount.getText().toString());
                }
            }
        });

        return  view;
    }

    private void payUsingUpi(String name,String upiId, String note, String amount) {

        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri;
        uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);
        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");
        // check if intent resolves
        if(null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(getContext(),"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(getContext())) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(getActivity(), "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successfull: "+approvalRefNo);
                addTransactionToDatabase();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(getActivity(), "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

              //  addTransactionToDatabase();
            }
            else {
                Toast.makeText(getActivity(), "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
              //
                //  addTransactionToDatabase();
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(getActivity(), "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void addTransactionToDatabase() {
        final String receiver_mobile = receiverMobile.getText().toString();


           DatabaseReference doctorReference=database.getReference("Users").child("Doctor");
           doctorReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   String receiverUid = null;
                   for (DataSnapshot ds:dataSnapshot.getChildren()){
                       DoctorModal doctorModal=ds.getValue(DoctorModal.class);
                       if (Objects.requireNonNull(doctorModal).getPhone().equals(receiver_mobile)){
                           receiverUid = doctorModal.getUid();
                           String doctorImageUrl=doctorModal.getImage();
                        //   String doctorName=doctorModal.getName();
                           setTansactionValue(receiverUid,receiver_mobile,doctorImageUrl);
                            break;
                       }
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });    }

    private void setTansactionValue(final String receiverUid, final String receiver_mobile, final String doctorImageUrl) {

        final String doctorName = name.getText().toString();
        final String paid_amount = amount.getText().toString();
        final String senderUid = ""+user.getUid();
       // String patientName = user.getDisplayName();
       // String patientImageUrl= String.valueOf(user.getPhotoUrl());

            DatabaseReference patientReference=database.getReference("Users").child("Patient");
            patientReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String patientName, patientImageUrl;
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        PatientModal patientModal=ds.getValue(PatientModal.class);
                        if (patientModal.getUid().equals(user.getUid())){

                            patientName=patientModal.getName();
                            patientImageUrl=patientModal.getImage();

                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("senderUid",senderUid);
                            hashMap.put("doctorName", doctorName);  //receiver Name
                            hashMap.put("amount",paid_amount);
                            hashMap.put("phone",receiver_mobile);
                            hashMap.put("doctorImageUrl",doctorImageUrl);
                            hashMap.put("receiverUid", receiverUid);
                            hashMap.put("patientName",patientName);
                            hashMap.put("patientImageUrl",patientImageUrl);


                            DatabaseReference payReference =database.getReference("Users").child("Payment");

                            payReference.child(senderUid).setValue(hashMap);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }



    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

}
