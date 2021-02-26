package com.project.consultadoctor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.project.consultadoctor.ChatActivity;
import com.project.consultadoctor.Modal.PatientModal;
import com.project.consultadoctor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder>{

    Context context;
    ArrayList<PatientModal> patientList;

    public PatientAdapter(Context context, ArrayList<PatientModal> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_patient,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final String hisUID=patientList.get(position).getUid();
        final String  hisName=patientList.get(position).getName();
        holder.nameTv.setText(hisName);
        holder.mobile.setText(patientList.get(position).getPhone());
        holder.gender.setText(patientList.get(position).getGender());
        final String  hisImage=patientList.get(position).getImage();
        try {
            Picasso.get().load(hisImage).placeholder(R.drawable.patient_logo).into(holder.avatarIv);
        }catch (Exception e){
        e.printStackTrace();
        }


        holder.txtChatPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                intent.putExtra("hisImage",hisImage);
                intent.putExtra("hisName",hisName);
                intent.putExtra("isDoctor",false);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView avatarIv;
        TextView nameTv,mobile,gender,txtChatPatient;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            avatarIv=(CircleImageView)itemView.findViewById(R.id.avatarIv);
            nameTv=(TextView)itemView.findViewById(R.id.nameTv);
            mobile=(TextView)itemView.findViewById(R.id.mobile);
            gender=(TextView)itemView.findViewById(R.id.gender);
            txtChatPatient = (TextView) itemView.findViewById(R.id.txtChatPatient);
        }
    }
}
