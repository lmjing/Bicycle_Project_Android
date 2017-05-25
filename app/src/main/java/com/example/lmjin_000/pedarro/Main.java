package com.example.lmjin_000.pedarro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lmjin_000.pedarro.GCM.gcmActivity;
import com.example.lmjin_000.pedarro.Login_v2.Login;
import com.example.lmjin_000.pedarro.component.ApplicationController;
import com.example.lmjin_000.pedarro.model.BikeTmap;
import com.example.lmjin_000.pedarro.network.NetworkService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lmjin_000 on 2015-12-03.
 */
public class Main extends AppCompatActivity {

    TextView one,two,using;
    Button menu1,menu2,menu3,menu4;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private NetworkService networkService;

    @Override
    protected void onResume() {
        super.onResume();
        Bikeid();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BikeRent.class);
                startActivity(intent);
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BikeStationMap.class);
                startActivity(intent);
            }
        });
        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), gcmActivity.class);
                startActivity(intent);
            }
        });
        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("Auto_Login_enabled", false);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void init() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font_L.otf");
        using = (TextView)findViewById(R.id.using);
        one = (TextView)findViewById(R.id.one);
        two = (TextView)findViewById(R.id.two);
        menu1 = (Button)findViewById(R.id.menu1);
        menu2 = (Button)findViewById(R.id.menu2);
        menu3 = (Button)findViewById(R.id.menu3);
        menu4 = (Button)findViewById(R.id.menu4);
        one.setTypeface(tf);
        two.setTypeface(tf);
        /*
        menu1.setTypeface(tf);
        menu2.setTypeface(tf);
        menu3.setTypeface(tf);
        menu4.setTypeface(tf);
        */
    }

    public void Bikeid() {
        //서버 접속
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.36.42.94", 3000);
        networkService = ApplicationController.getInstance().getNetworkService();

        setting = getSharedPreferences("setting", 0);

        Call<BikeTmap> gcmCall = networkService.getBikeid(setting.getString("ID",""),0);//NetworkService에 등록해둔거 호출
        gcmCall.enqueue(new Callback<BikeTmap>() {
            @Override
            public void onResponse(Response<BikeTmap> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    BikeTmap result = response.body();
                    if (result.getStartst() != null) {
                        using.setText("대여 완료");
                        Log.i("메인", "대여완료");
                    } else {
                        using.setText(setting.getString("USING",""));
                        Log.i("메인", "대여 신청중 : "+setting.getString("USING",""));
                    }
                } else {
                    int statusCode = response.code();
                    Log.i("메인", "응답코드 : " + statusCode);
                    using.setText("환영합니다");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("메인", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }
}
