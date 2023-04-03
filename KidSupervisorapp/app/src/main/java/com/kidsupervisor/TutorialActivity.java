package com.kidsupervisor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {

    Button skip, next,back;
    ConstraintLayout constraintLayout;
    short counter=0;
    Pref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        pref = new Pref(this);
        pref.setUserStatus(false);

        skip=findViewById(R.id.skipBtn);
        next=findViewById(R.id.nextBtn);
        back=findViewById(R.id.backBtn);
        constraintLayout=findViewById(R.id.tutLayout);

        //go to mainActivity
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TutorialActivity.this, MainActivity.class));
                finish();
            }
        });
        //go to next page
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter==0){
                    constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this,R.drawable.slide2));
                    counter++;
                }
                else if(counter==1) {
                    constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.slide3));
                    counter++;
                }
                else if(counter==2){
                constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.slide4));
                counter++;
                }
                else{
                    startActivity(new Intent(TutorialActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
        //previous page
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(counter>0) {
                    if (counter == 3) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.slide3));
                        counter--;
                    } else if (counter == 2) {
                        constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.slide2));
                        counter--;
                    } else {
                        constraintLayout.setBackground(ContextCompat.getDrawable(TutorialActivity.this, R.drawable.slide1));
                        counter--;
                    }
                }
            }
        });
    }

}