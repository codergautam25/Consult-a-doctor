package com.project.consultadoctor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.consultadoctor.Modal.ModalPayHistory;
import com.project.consultadoctor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PayHistoryAdapter extends  RecyclerView.Adapter<PayHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<ModalPayHistory> payHistories;
    Boolean isDoctor;

    public PayHistoryAdapter(Context context, ArrayList<ModalPayHistory> payHistories,Boolean isDoctor) {
        this.context = context;
        this.payHistories = payHistories;
        this.isDoctor = isDoctor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_payhistory,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        if(isDoctor){
            holder.txtSenderName.setText(payHistories.get(position).getDoctorName());
            holder.txtPaidToLabel.setText("Received Rs ".concat(payHistories.get(position).getAmount()).concat(" From"));
            holder.txtReceiverName.setText(payHistories.get(position).getPatientName());
            try {
                Picasso.get().load(payHistories.get(position).getDoctorImageUrl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.senderIv);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                Picasso.get().load(payHistories.get(position).getPatientImageUrl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.receiverIv);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            holder.txtSenderName.setText(payHistories.get(position).getPatientName());
            holder.txtPaidToLabel.setText("Received Rs ".concat(payHistories.get(position).getAmount()).concat(" From"));
            holder.txtReceiverName.setText(payHistories.get(position).getDoctorName());
            holder.txtPaidToLabel.setText("Paid Rs ".concat(payHistories.get(position).getAmount()).concat(" To"));
            try {
                Picasso.get().load(payHistories.get(position).getPatientImageUrl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.senderIv);
            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                Picasso.get().load(payHistories.get(position).getDoctorImageUrl()).placeholder(R.drawable.ic_person_black_24dp).into(holder.receiverIv);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public int getItemCount() {
        return payHistories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView senderIv,receiverIv;
        TextView txtSenderName,txtPaidToLabel,txtReceiverName;
        public MyViewHolder(@NonNull View v) {
            super(v);
            senderIv = (CircleImageView) v.findViewById(R.id.senderIv);
            receiverIv = (CircleImageView)v.findViewById(R.id.receiverIv);
            txtSenderName = (TextView)v.findViewById(R.id.txtSenderName);
            txtPaidToLabel = (TextView)v.findViewById(R.id.txtPaidToLabel);
            txtReceiverName = (TextView)v.findViewById(R.id.txtReceiverName);
        }
    }
}
