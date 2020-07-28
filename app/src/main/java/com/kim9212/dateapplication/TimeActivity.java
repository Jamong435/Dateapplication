package com.kim9212.dateapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class TimeActivity extends AppCompatActivity {

    public TimePicker timePicker;

    int Pick_hour, Pick_min;
    Button click6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        click6 = findViewById(R.id.clcick6);
        timePicker = findViewById(R.id.time_picker);

        click6.setOnClickListener(listener);


    }

    public View.OnClickListener listener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {

            Pick_hour= timePicker.getHour();
            Pick_min= timePicker.getMinute();


            Intent intent = new Intent(TimeActivity.this, CheckActivity.class);
            intent.putExtra("time",Pick_hour+Pick_min);

            Log.i("Tag", String.valueOf(Pick_hour+Pick_min));
            startActivity(intent);

            String ampm;
            switch (v.getId())
            {
                case R.id.clcick6:
                {
                    if(timePicker.getHour()>11 && timePicker.getHour()<24){
                        ampm="오후";
                    }
                    else ampm= "오전";
                }
            }
        }



    };

}