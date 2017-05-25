package com.example.lmjin_000.pedarro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lmjin_000.pedarro.Tmap.RentNum;
import com.example.lmjin_000.pedarro.component.ApplicationController;
import com.example.lmjin_000.pedarro.model.BikeTmap;
import com.example.lmjin_000.pedarro.model.Station;
import com.example.lmjin_000.pedarro.network.NetworkService;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by lmjin_000 on 2015-12-08.
 */
public class BikeRent extends ActionBarActivity
        implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private AutoCompleteTextView search;
    private Button btn_rent;
    private ImageView btn_search;
    private TextView stop_station;
    private NetworkService networkService;

    //자동완성
    private List<String> keywords = new ArrayList<String>();
    //도착예정정보 저장
    private BikeTmap biketmap = new BikeTmap();
    LocationManager manager;
    private MapView mMapView;
    private MapPOIItem mDefaultMarker;
    private MapPoint DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.315978, 126.838493);

    MapLayout mapLayout;

    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent);

        init();

        mapLayout = new MapLayout(this);
        mMapView = mapLayout.getMapView();

        mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapView.setMapType(MapView.MapType.Standard);
        mMapView.setPOIItemEventListener(this);
        mMapView.setCurrentLocationEventListener(this);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapLayout);

        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        //서버 설정
        ApplicationController application = ApplicationController.getInstance();
        application.buildNetworkService("52.36.42.94", 3000);
        initNetworkService();

        Map_init();

        //자동완성성
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, keywords);
        search.setAdapter(adapter);

        //검색하였을 때
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_keyword = search.getText().toString();
                Call<Station> stationCall = networkService.getStation(search_keyword);//NetworkService에 등록해둔거 호출
                stationCall.enqueue(new Callback<Station>() {
                    @Override
                    public void onResponse(Response<Station> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            Station station_temp = response.body();
                            MapPOIItem poiltem = mMapView.findPOIItemByName(station_temp.getName())[0];
                            mMapView.selectPOIItem(poiltem, false);
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
        });
        //대여신청 하였을때
        btn_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(biketmap.getArrivest() != null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                        String context = Context.LOCATION_SERVICE;
                        manager = (LocationManager)getSystemService(context);
                        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                                    alertCheckGPS();
                        }else {
                            Successdialog();
                        }
                }else{
                    Errordialog("도착지를 설정해주세요");
                }
            }
        });
    }

    private void Errordialog(String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(Message)
                .setCancelable(false)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertCheckGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String context = Context.LOCATION_SERVICE;
        manager = (LocationManager)getSystemService(context);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            builder.setMessage("대여를 위해서 반드시 GPS를 켜주셔야합니다.\n" +
                    "확인을 누를 시 gps설정화면으로 이동합니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveConfigGPS();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // GPS 설정화면으로 이동
    private void moveConfigGPS() {
        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
    }

    private void Successdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("도착지 : "+biketmap.getArrivest()+"\n정보가 일치합니까?")
                .setCancelable(false)
                .setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setting = getSharedPreferences("setting", 0);
                                biketmap.setUserID(setting.getString("ID",""));

                                Intent intent = new Intent(getApplicationContext(), RentNum.class);
                                intent.putExtra("BikeTmap", biketmap);

                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }
    private void initNetworkService() {
        // TODO: 13. ApplicationConoller 객체를 이용하여 NetworkService 가져오기
        networkService = ApplicationController.getInstance().getNetworkService();
    }

    private void init() {
        btn_rent = (Button)findViewById(R.id.btn_rent);
        search = (AutoCompleteTextView)findViewById(R.id.editTextQuery);
        btn_search = (ImageView)findViewById(R.id.buttonSearch);
        stop_station = (TextView)findViewById(R.id.stop_staiton);
    }
    //자동완성
    private void Map_init(){

        Call<List<Station>> stationCall = networkService.allStation();//NetworkService에 등록해둔거 호출
        stationCall.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Response<List<Station>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    List<Station> station_temp = response.body();

                    for (Station station : station_temp) {
                        DEFAULT_MARKER_POINT = MapPoint.mapPointWithGeoCoord(Double.parseDouble(station.getLat()), Double.parseDouble(station.getHard()));
                        createDefaultMarker(mMapView, station.getName());
                        keywords.add(station.getName());
                    }

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

    private void createDefaultMarker(MapView mapView, String name) {
        mDefaultMarker = new MapPOIItem();
        mDefaultMarker.setItemName(name);
        mDefaultMarker.setTag(0);
        mDefaultMarker.setMapPoint(DEFAULT_MARKER_POINT);
        mDefaultMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        mDefaultMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

        mapView.addPOIItem(mDefaultMarker);
    }

    //커스텀 벌룬
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            ((TextView) mCalloutBalloon.findViewById(R.id.title)).setText(poiItem.getItemName());
            ((TextView) mCalloutBalloon.findViewById(R.id.bike))
                    .setText("도착지로 설정하기");

            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(37.315978,126.838493), 2, true);

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        biketmap.setArrivest(mapPOIItem.getItemName());
        stop_station.setText("도착지 : " + mapPOIItem.getItemName());
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }
}
