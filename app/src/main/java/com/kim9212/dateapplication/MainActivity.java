package com.kim9212.dateapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView iv;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title=findViewById(R.id.title);
        iv=findViewById(R.id.iv);

        //카카오로 로그인했을시
        Glide.with(MainActivity.this).load(G.imgUrl).into(iv);
        title.setText(G.nickName+"님오늘은뭐해요?");
        Intent intent= getIntent();
        String userID= intent.getStringExtra("userID");


        //구글로 로그인했을시
        String nickname= intent.getStringExtra("nickname");
        String photourl= intent.getStringExtra("photoUrl");
        title.setText(nickname);
        Glide.with(this).load(photourl).into(iv);


        //닷홈으로 로그인했을시
        if (G.nickName==null){
            title.setText(userID+"님오늘은뭐해요?");
        }

    }

    public void startnewplan(View view) {
        Intent intent= new Intent(this,NewPlanActivity.class);
        startActivity(intent);
    }
    public void startsaveddata(View view) {
        Intent intent= new Intent(this,ListActivity.class);
        startActivity(intent);
    }



}