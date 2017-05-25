package com.example.lmjin_000.pedarro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.lmjin_000.pedarro.component.ApplicationController;
import com.example.lmjin_000.pedarro.model.Station;
import com.example.lmjin_000.pedarro.network.NetworkService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lmjin_000 on 2015-12-08.
 */
public class BikeStation_View extends Activity {
    TextView title, station, all_bike, rent_bike, park_bike, soon_bike, soon_bike_minute;
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bikestation);

        init();
        //서버 설정
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.36.42.94", 3000);
        initNetworkService();

        Intent intent = getIntent();
        getStationInfo(intent.getStringExtra("Station_name"));
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font_L.otf");
        title.setTypeface(tf);
    }

    private void initNetworkService() {
        // TODO: 13. ApplicationConoller 객체를 이용하여 NetworkService 가져오기
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void init() {
        title = (TextView)findViewById(R.id.title);
        station = (TextView)findViewById(R.id.station_name);
        all_bike = (TextView)findViewById(R.id.all_bike);
        rent_bike = (TextView)findViewById(R.id.rent_bike);
        park_bike = (TextView)findViewById(R.id.park_bike);
        soon_bike = (TextView)findViewById(R.id.soon_bike);
        soon_bike_minute = (TextView)findViewById(R.id.soon_bike_minute);
    }

    public void getStationInfo(final String station_name) {
        Call<Station> stationCall = networkService.getStation(station_name);//NetworkService에 등록해둔거 호출
        stationCall.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(Response<Station> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Station station_temp = response.body();
                    station.setText(station_temp.getName());
                    all_bike.setText("총 거치대 : "+station_temp.getAll());
                    rent_bike.setText("대여 가능 : "+Integer.toString(station_temp.getPark()));
                    park_bike.setText("반납 가능 : "+Integer.toString(station_temp.getAll() - station_temp.getPark()));
                } else {
                    int statusCode = response.code();
                    Log.i("MyTag", "응답코드 : " + statusCode);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("MyTag", "서버 onFailure 에러내용 : " + t.getMessage());
            }
        });
    }
}
