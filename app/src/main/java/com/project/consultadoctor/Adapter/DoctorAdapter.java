package com.project.consultadoctor.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.project.consultadoctor.ChatActivity;
import com.project.consultadoctor.Modal.DoctorModal;
import com.project.consultadoctor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    private static final int PERMISSION_ID = 101;
    Context context;
    ArrayList<DoctorModal> doctorsList;

    String latitude = null;
    String longitude = null;


    public DoctorAdapter(Context context, ArrayList<DoctorModal> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final String hisImage=doctorsList.get(position).getImage();
        final String  hisName=doctorsList.get(position).getName();


        final String hisUID=doctorsList.get(position).getUid();
        holder.nameTv.setText(hisName);
        holder.txtDoctorSpecialization.setText(doctorsList.get(position).getSpecialization());
        holder.txtDoctorDegree.setText(doctorsList.get(position).getDegree());
        holder.txtDistance.setText(doctorsList.get(position).getAddress());

        try {
            Picasso.get().load(hisImage).placeholder(R.drawable.ic_person_black_24dp).into(holder.avatarIv);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.txtChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("hisUid",hisUID);
                intent.putExtra("hisImage",hisImage);
                intent.putExtra("hisName",hisName);
                intent.putExtra("isDoctor",true);
                intent.putExtra("phone",doctorsList.get(position).getPhone());
                context.startActivity(intent);
            }
        });


        holder.txtGetClinicDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("Clinic Distance","The Doctor's Clinic is 2.5 KM from here!!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    void showDialog(String title,String msg){
        Dialog dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        dialog.show();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder {
        CircleImageView avatarIv;
        TextView nameTv,txtDoctorSpecialization,txtDoctorDegree,txtDistance,txtGetClinicDistance,txtChat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv=(CircleImageView)itemView.findViewById(R.id.avatarIv);
            nameTv=(TextView)itemView.findViewById(R.id.nameTv);
            txtDoctorSpecialization=(TextView)itemView.findViewById(R.id.txtDoctorSpecialization);
            txtDoctorDegree=(TextView)itemView.findViewById(R.id.txtDoctorDegree);
            txtDistance=(TextView)itemView.findViewById(R.id.txtDistance);
            txtGetClinicDistance = (TextView)itemView.findViewById(R.id.txtGetClinicDistance);
            txtChat = (TextView)itemView.findViewById(R.id.txtChat);
        }
    }
}
