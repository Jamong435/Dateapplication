package com.kim9212.dateapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    String baseUrl = "http://www.kobis.or.kr";
    String API_KEY = "b2fcdd211f6519d04f4f4586e36a746c";
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        recyclerView=findViewById(R.id.rv_recyclerview);

        //Retrofit 객체생성
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface= retrofit.create(RetrofitInterface.class);


        //현재 시간 구해오기
        long now= System.currentTimeMillis();
        Date mDate= new Date((now)+(1000*60*60*24*-1));
        SimpleDateFormat simpleDate= new SimpleDateFormat("yyyyMMdd");
        String gettime=simpleDate.format(mDate);

        retrofitInterface.getBoxOffice(API_KEY,""+gettime).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                Map<String,Object> boxOfficeResult= (Map<String, Object>) response.body().get("boxOfficeResult");
                ArrayList<Map<String, Object>> jsonList = (ArrayList) boxOfficeResult.get("dailyBoxOfficeList");
                mAdapter=new MovieAdapter(jsonList);
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {

            }
        });




    }// onCreate()..



}// MainActivity class..
