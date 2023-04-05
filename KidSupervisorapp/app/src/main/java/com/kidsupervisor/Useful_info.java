package com.kidsupervisor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
public class Useful_info  extends AppCompatDialogFragment {

    private TextView link_1,link_2,link_3;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_useful_info, null);

        link_1=view.findViewById(R.id.link1);
        link_2=view.findViewById(R.id.link2);
        link_3=view.findViewById(R.id.link3);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("INFORMATION")
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }

                });
        link_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.healthychildren.org/English/ages-stages/baby/sleep/Pages/A-Parents-Guide-to-Safe-Sleep.aspx"));
                startActivity(intent);
            }
        });
        link_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sleepfoundation.org/infants-and-sleep"));
                startActivity(intent);
            }
        });
        link_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mayoclinic.org/healthy-lifestyle/infant-and-toddler-health/in-depth/healthy-baby/art-20046051"));
                startActivity(intent);
            }
        });


        return builder.create();
    }
}
