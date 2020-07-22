package com.kim9212.dateapplication;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity  {

    private EditText et,et1;
    private WebView wv;
    private Button bt;

    @Override
    protected void onStart() {
        super.onStart();
        wv.loadUrl("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%EC%98%A4%EB%8A%98%EB%AD%90%ED%95%98%EC%A7%80");
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        webSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        et1 = findViewById(R.id.et1);
        et = findViewById(R.id.et);
        wv = findViewById(R.id.wv);
        bt = findViewById(R.id.bt);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

//                Toast.makeText(MainActivity.this, "로딩 끝", Toast.LENGTH_SHORT).show();

            }
        });
        et.setOnKeyListener(new View.OnKeyListener() { //enter먹게하는거
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    Toast.makeText(MapActivity.this, et.getText(), Toast.LENGTH_SHORT).show();

                    String address = et.getText().toString();
                    if (!address.startsWith("https://")) {
                        address = "https://" + address;
                    }
                    wv.loadUrl(address);
                    return true;
                }
                return false;
            }
        });
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    bt.callOnClick();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }
    public void loadsite(View view) {
        String address = et.getText().toString();
        if (!address.startsWith("https://")) {
            address = "https://" + address;
        }
        wv.loadUrl(address);
    }
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                if (wv.canGoBack()) {
                    wv.goBack();
                }
                return true;
            case R.id.action_forward:
                if (wv.canGoForward()) {
                    wv.goForward();
                }
                return true;
            case R.id.action_refresh:
                wv.reload();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void gopick_btn(View view) {

        try {
            String s = et1.getText().toString();

            Uri uri = Uri.parse("kakaomap://search?q=" + s + "맛집" + "&p=37.537229,127.005515");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }catch ( Exception e){
            throw e;
        }
    }

    public void saved_btn(View view) {
    }
}
