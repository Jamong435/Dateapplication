package com.kim9212.dateapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class GoogleLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //    TextView tvName;
    //    TextView tvEmail;
    CircleImageView iv;

    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth auth; // 파이어 베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드


    private EditText et_id, et_pass;
    private Button btn_login1, btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
//        tvName= findViewById(R.id.tv_name);
//        tvEmail= findViewById(R.id.tv_email);
        iv = findViewById(R.id.iv);
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        btn_login1 = findViewById(R.id.btn_login1);
        btn_register = findViewById(R.id.btn_register);






        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        auth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화.

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼을 클릭했을 때 이곳을 수행.
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });





        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoogleLogin.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EditText에 현재 입력되어있는 값을 get(가져온다)해온다.
                final String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {


                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                Toast.makeText(GoogleLogin.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");
                                Intent intent = new Intent(GoogleLogin.this, MainActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPass", userPass);
                                startActivity(intent);

                            } else { // 로그인에 실패한 경우
                                if (userID == null) {
                                    return;
                                }
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(GoogleLogin.this);
                queue.add(loginRequest);
            }
        });

        Session.getCurrentSession().addCallback(sessionCallback);
    }
    //카카오 로그인 서버와 연결을 시도하는 세션작업의 결과를 듣는 리스너
    ISessionCallback sessionCallback = new ISessionCallback() {
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
    void requestUserInfo() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {

            }

            @Override
            public void onSuccess(MeV2Response result) {
                //사용자 계정 정보 객체
                UserAccount userAccount = result.getKakaoAccount();
                if (userAccount == null)

                    return;
                //1. 이메일 정보 , 닉네임( 보여지지는 않으나 가지고 있음)
                //tvEmail.setText( userAccount.getEmail() );
                //tvName.setText(nickName);


                //2. 기본 프로필 정보(닉네임, 이미지, 섬네일 이미지)
                Profile profile = userAccount.getProfile();

                if (profile == null) return;

                String nickName = profile.getNickname();
                String imgUrl = profile.getProfileImageUrl();
                //G에 저장하는 과정
                G.nickName = nickName;
                G.imgUrl = imgUrl;
                //sharedpreference로 내부저장소에 닉네임과 사진저장
                getSharedPreferences("name", MODE_PRIVATE).edit().putString("name", G.nickName).commit();
                getSharedPreferences("name", MODE_PRIVATE).edit().putString("picture", G.imgUrl).commit();
                Intent intent = new Intent(GoogleLogin.this, MainActivity.class);
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


    //        //keyhas받는 구문
//        String keyHash = getKeyHash(this);
//        Log.i("TAG", keyHash);

//    //카카오 키해시 리턴하는 메소드
//    public static String getKeyHash(final Context context) {
//        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
//        if (packageInfo == null)
//            return null;
//
//        for (Signature signature : packageInfo.signatures) {
//            try {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
//            } catch (NoSuchAlgorithmException e) {
//                Log.w("TAG", "Unable to get MessageDigest. signature=" + signature, e);
//            }
//        }
//        return null;
//    }

//    public void clickLogout(View view) {
//
//        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
//
//            @Override
//            public void onCompleteLogout() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(GoogleLogin.this, "로그아웃 완료", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                Session.getCurrentSession().removeCallback(sessionCallback);
//            }
//        });
//
//    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // 구글 로그인 인증을 요청 했을 때 결과 값을 되돌려 받는 곳.
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) { // 인증결과가 성공적이면..
                GoogleSignInAccount account = result.getSignInAccount(); // account 라는 데이터는 구글로그인 정보를 담고있습니다. (닉네임,프로필사진Url,이메일주소...등)
                resultLogin(account); // 로그인 결과 값 출력 수행하라는 메소드
            }
        }

    }

    private void resultLogin(final GoogleSignInAccount account) { //구글관련
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) { // 로그인이 성공했으면...
                            Toast.makeText(GoogleLogin.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("nickName",account.getDisplayName());
                            intent.putExtra("photoUrl",String.valueOf(account.getPhotoUrl())); // String.valueOf() 특정 자료형을 String 형태로 변환.
                            startActivity(intent);
                        } else { // 로그인이 실패했으면..
                            Toast.makeText(GoogleLogin.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void clicktextview(View view) {
        Intent intent= new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
