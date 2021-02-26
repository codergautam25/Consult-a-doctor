package com.project.consultadoctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.consultadoctor.Modal.ModelChat;
import com.project.consultadoctor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterChat extends RecyclerView.Adapter<AdapterChat.myViewHolder> {

    private static  final  int MSG_TYPE_LEFT=0;
    private static  final  int MSG_TYPE_RIGHT=1;

    Context context;
    ArrayList<ModelChat> chatList;


    FirebaseUser fUser; // creating a new activity

    public AdapterChat(Context context, ArrayList<ModelChat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return new myViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return new myViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, final int position) {
        holder.messageTv.setText(chatList.get(position).getMessage());
        String time=chatList.get(position).getTime();
        String date=chatList.get(position).getDate();
        holder.timeTv.setText(date+time);

        try{
            Picasso.get().load(chatList.get(position).getImageUrl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.profileIv);
        }catch (Exception e){
            e.printStackTrace();
        }


       /* holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete message?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });*/
        if (position==chatList.size()-1){
            if (chatList.get(position).isSeen()){
                holder.isSeenTv.setText("Seen");
            }else {
                holder.isSeenTv.setText("Delivered");
            }
        }else {
            holder.isSeenTv.setVisibility(View.GONE);
        }

    }

  /*  private void deleteMessage(int position) {
        final String myUID=FirebaseAuth.getInstance().getCurrentUser().getUid();

        String msgTime=chatList.get(position).getTime();

        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Chats");
        Query query=dbRef.orderByChild("time").equalTo(msgTime);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()){

                    if (ds.child("sender").getValue().equals(myUID)){
                       // ds.getRef().removeValue();

                        //2)set the value of message  "This message was deleted.."
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("message","This message was deleted..");
                        ds.getRef().updateChildren(hashMap);

                        Toast.makeText(context, "message deleted..", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"You can delete only your messages..",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }

    class  myViewHolder extends  RecyclerView.ViewHolder {

        ImageView profileIv;
        TextView messageTv,timeTv,isSeenTv;
        LinearLayout messageLayout;   //for click listener to show delete

       public myViewHolder(@NonNull View itemView) {
           super(itemView);

           profileIv=itemView.findViewById(R.id.profileIv);
           messageTv=itemView.findViewById(R.id.messageTv);
           timeTv=itemView.findViewById(R.id.timeTv);
           isSeenTv=itemView.findViewById(R.id.isSeenTv);
           messageLayout=itemView.findViewById(R.id.messageLayout);
       }
   }


}
