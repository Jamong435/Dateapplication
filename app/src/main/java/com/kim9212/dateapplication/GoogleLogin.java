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
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class GoogleLogin extends AppCompatActivity {

    TextView tvName;
    TextView tvEmail;
    CircleImageView iv;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        tvName= findViewById(R.id.tv_name);
        tvEmail= findViewById(R.id.tv_email);
        iv= findViewById(R.id.iv);


        String keyHash= getKeyHash(this);
        Log.i("TAG", keyHash);

        Session.getCurrentSession().addCallback(sessionCallback);
    }

    //카카오 로그인 서버와 연결을 시도하는 세션작업의 결과를 듣는 리스너
    ISessionCallback sessionCallback= new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            Toast.makeText(GoogleLogin.this, "로그인 세션연결 성공", Toast.LENGTH_SHORT).show();

            //로그인 된 사용자의 정보들 얻어오기
            requestUserInfo();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Toast.makeText(GoogleLogin.this, "로그인 실패", Toast.LENGTH_SHORT).show();

        }
    };

    //로그인 사용자정보 받기
    void requestUserInfo(){
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }
            @Override
            public void onSuccess(MeV2Response result) {
                //사용자 계정 정보 객체
                UserAccount userAccount = result.getKakaoAccount();
                if(userAccount==null)

                    return;
                //1. 이메일 정보
                tvEmail.setText( userAccount.getEmail() );

                //2. 기본 프로필 정보(닉네임, 이미지, 섬네일 이미지)
                Profile profile= userAccount.getProfile();

                if(profile==null) return;

                String nickName= profile.getNickname();
                String imgUrl= profile.getProfileImageUrl();
                tvName.setText(nickName);



                //G에 저장하는 과정
                G.nickName=nickName;
                G.imgUrl=imgUrl;

                //sharedpreference로 내부저장소에 닉네임과 사진저장
                getSharedPreferences("name",MODE_PRIVATE).edit().putString("name",G.nickName).commit();
                getSharedPreferences("name",MODE_PRIVATE).edit().putString("picture",G.imgUrl).commit();



                Intent intent= new Intent(GoogleLogin.this,MainActivity.class);
                startActivity(intent);



            }
        });

    }


    //앱이 종료될때
    @Override
    protected void onDestroy() {
        super.onDestroy();

        //세션연결 종료
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    //카카오 키해시 리턴하는 메소드
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    public void clickLogout(View view) {

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GoogleLogin.this, "로그아웃 완료", Toast.LENGTH_SHORT).show();
                    }
                });



                Session.getCurrentSession().removeCallback(sessionCallback);
            }
        });

    }
}
