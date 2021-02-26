package com.project.consultadoctor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.consultadoctor.Adapter.AdapterChat;
import com.project.consultadoctor.Modal.ModelChat;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CircleImageView profileIv;
    TextView nameTv,userStatusTv,txtCallDoctor;
    EditText messageEt;
    CircleImageView sendBtn,callCirImg;

    FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseDatabase;
     DatabaseReference userDbRef;

    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    ArrayList<ModelChat> chatList;
    AdapterChat adapterChat;

    String hisUid;
    String myUid;
    String hisImage;
    String hisName;
    private boolean isDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        isDoctor = getIntent().getBooleanExtra("isDoctor",false);


        recyclerView = findViewById(R.id.chat_recyclerView);
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatus);
        messageEt = findViewById(R.id.input_message);
        sendBtn = findViewById(R.id.send_message_btn);
        callCirImg = (CircleImageView)findViewById(R.id.callCirImg);
        txtCallDoctor = (TextView)findViewById(R.id.txtCallDoctor);

        if(!isDoctor){
            callCirImg.setVisibility(View.INVISIBLE);
            txtCallDoctor.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        hisUid = intent.getStringExtra("hisUid");
        hisImage = intent.getStringExtra("hisImage");
        hisName = intent.getStringExtra("hisName");
        nameTv.setText(hisName);
        try {
            Picasso.get().load(hisImage).placeholder(R.drawable.ic_person_black_24dp).into(profileIv);
        }catch (Exception e){
           e.printStackTrace();
        }
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();





      /* if (isDoctor){
           userDbRef = firebaseDatabase.getReference("Users").child("Doctor");
           Query userQuery=userDbRef.orderByChild("uid").equalTo(hisUid);
           // Get user picture and Name
           userQuery.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //check until required info is received
                   for (DataSnapshot ds:dataSnapshot.getChildren()) {
                       String typingStatus = "" + ds.child("typingTo").getValue();

                       //check typing status
                       if (typingStatus.equals(myUid)) {
                           userStatusTv.setText("Typing...");
                       } else {
                           //get value of online status
                           String onlineStatus = "" + ds.child("onlineStatus").getValue();
                           if (onlineStatus.equals("online")) {
                               userStatusTv.setText(onlineStatus);
                           } else {
                               //show proper date and time
                               Calendar calendar = Calendar.getInstance();

                               SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                               String saveCurrentDate = currentDate.format(calendar.getTime());

                               SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                               String saveCurrentTime = currentTime.format(calendar.getTime());
                               userStatusTv.setText("Last seen at: " + saveCurrentDate + "  " + saveCurrentTime);
                           }
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }else
       { userDbRef = firebaseDatabase.getReference("Users").child("Patient");
           Query userQuery=userDbRef.orderByChild("uid").equalTo(hisUid);
           // Get user picture and Name
           userQuery.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   //check until required info is received
                   for (DataSnapshot ds:dataSnapshot.getChildren()) {
                       String typingStatus = "" + ds.child("typingTo").getValue();

                       //check typing status
                       if (typingStatus.equals(myUid)) {
                           userStatusTv.setText("Typing...");
                       } else {
                           //get value of online status
                           String onlineStatus = "" + ds.child("onlineStatus").getValue();
                           if (onlineStatus.equals("online")) {
                               userStatusTv.setText(onlineStatus);
                           } else {
                               //show proper date and time
                               Calendar calendar = Calendar.getInstance();

                               SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                               String saveCurrentDate = currentDate.format(calendar.getTime());

                               SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
                               String saveCurrentTime = currentTime.format(calendar.getTime());
                               userStatusTv.setText("Last seen at: " + saveCurrentDate + "  " + saveCurrentTime);
                           }
                       }
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });
       }*/

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEt.getText().toString().trim();
                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "cannot send the Empty message", Toast.LENGTH_SHORT).show();

                } else {
                    sendMessage(message);

                }
            }
        });

        callCirImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getIntent().getStringExtra("phone")));
                startActivity(intent);
            }
        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    checkTypingStatus("onOne");
                } else {
                    checkTypingStatus(hisUid);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        readMessages();

        seenMessages();
    }

    private void seenMessages() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHashMap=new HashMap<>();
                        hasSeenHashMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList=new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                try {
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        ModelChat chat=ds.getValue(ModelChat.class);
                        if (chat.getReceiver().equals(myUid) && chat.getSender().equals(hisUid) ||
                                chat.getReceiver().equals(hisUid) && chat.getSender().equals(myUid)){
                            chatList.add(chat);
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(ChatActivity.this,"getting some exceptions",Toast.LENGTH_LONG).show();
                }

                //Adapter
                adapterChat = new AdapterChat(ChatActivity.this,chatList);
                adapterChat.notifyDataSetChanged();

                recyclerView.setAdapter(adapterChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




    private void sendMessage(String message) {
        DatabaseReference databaseReference=firebaseDatabase.getInstance().getReference();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("date",saveCurrentDate);
        hashMap.put("isSeen",false);
        databaseReference.child("Chats").push().setValue(hashMap);

        messageEt.setText(" ");
    }

    private  void checkUserStatus(){
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if (user!=null){
            //user already signed in
            //set Email of logged in user
            //mPeofileTv.setText(user.getEmail());
            myUid=user.getUid();  //Currently signed in User Uid
        }else {
            startActivity(new Intent(ChatActivity.this, DashboardActivity.class));
            finish();
        }
    }

    private  void checkOnlineStatus(String status){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String ,Object>hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);

        dbRef.updateChildren(hashMap);
    }

    private  void checkTypingStatus(String typing){
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String ,Object>hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);

        dbRef.updateChildren(hashMap);
    }


    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onResume() {
        checkOnlineStatus("online");
        super.onResume();
    }

}
