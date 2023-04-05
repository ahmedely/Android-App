package com.kidsupervisor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
public class ReportBugDialog  extends AppCompatDialogFragment {

    private TextView text,subject;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_bug_dialog, null);

        text=view.findViewById(R.id.reports);
        subject=view.findViewById(R.id.subj);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle("SEND A BUG REPORT")
                .setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendMail();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
    public void sendMail(){
        String []emails = {"coen69311@gmail.com"};
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,emails);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT,text.getText().toString());

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose client"));
    }
}
