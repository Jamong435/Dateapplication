package com.kim9212.dateapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NewPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);
    }

    public void clickbtn(View view) {
        Intent intent= new Intent(this,MapActivity.class);
        startActivity(intent);
    }
}