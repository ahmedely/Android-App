package com.kidsupervisor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class KidInfoActivity extends AppCompatActivity {

    public KidInfoActivity(){

    }


    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new Pref(this);

        if (!pref.getBoolean("Switch")) {
            setTheme(R.style.lighttheme);
        } else {
            setTheme(R.style.darktheme);
        }
        setContentView(R.layout.activity_kid_info);

        Button saveBut, editBut, deleteBut;

        saveBut = (Button) findViewById(R.id.buttonSave);
        editBut = (Button) findViewById(R.id.buttonEdit);
        deleteBut = (Button) findViewById(R.id.buttonDelete);

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        editBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}

