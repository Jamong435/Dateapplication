package com.kim9212.dateapplication;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.android.material.navigation.NavigationView;
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

    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout= findViewById(R.id.layout_drawer);
        navigationView= findViewById(R.id.nav);
        title=findViewById(R.id.title);
        iv=findViewById(R.id.iv);

        Intent intent= getIntent();

        //카카오로 로그인했을시
        Glide.with(MainActivity.this).load(G.imgUrl).into(iv);
        title.setText(G.nickName+"님오늘은뭐해요?");

        //닷홈으로 로그인했을시
        String userID= intent.getStringExtra("userID");
        if (G.nickName==null){
            title.setText(userID+"님오늘은뭐해요?");
        }



//        //구글로 로그인했을시 //TO DO: "구글에서도 로그인했을떄도 ID값을 가져와서 쓰고싶으나 계속 NULL값을 받아옴. 허나 로그인은됨.


//        String nick= intent.getStringExtra("nickname");
//        String photourl= intent.getStringExtra("photoUrl");
//        title.setText(nick);
//        Glide.with(this).load(photourl).into(iv);



//        //구글에서 로그인했는데 혹시 null값일떄
//        if(nick==null){
//            title.setText("출발해볼까요?");
//        }

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