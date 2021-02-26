package com.project.consultadoctor.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.project.consultadoctor.R;

public class AddressAlertDialog extends DialogFragment {

    Context context;


    public AddressAlertDialog(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final EditText etLat, etLng;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogTheme);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_latlng_design, null);
        etLat = (EditText)view.findViewById(R.id.etLat);
        etLng = (EditText)view.findViewById(R.id.etLng);

        builder.setView(view).setPositiveButton(
                "Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String lng = etLng.getText().toString();
                        String lat = etLat.getText().toString();

                        if(!lat.equals("") && !lng.equals("")){

                        }

                    }
                }
        ).setNegativeButton(
                "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                }
        ).setNeutralButton("Fetch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });



        return builder.create();
    }

}
