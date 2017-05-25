package com.example.lmjin_000.pedarro.network;

import com.example.lmjin_000.pedarro.model.Bike;
import com.example.lmjin_000.pedarro.model.BikeTmap;
import com.example.lmjin_000.pedarro.model.Rent;
import com.example.lmjin_000.pedarro.model.Station;
import com.example.lmjin_000.pedarro.model.registerJ;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface NetworkService {

    /**
     * 다음api에서 제공해주는 API_KEY를 String 형으로 저장
     * GET 어노테이션과 메소드 구현
     * 서버에 요청할 디렉토리를 GET 어노테이션에 인자로 넣어줌
     * Call<받고자 하는 데이터 타입> (request에 추가될 사항들)
     * request에 추가될 사항들을 예로 들면
     * Body가 될 수도 있고(POST 방식의 경우)
     * Path가 될 수도 있고
     * QueryMap, Query가 될 수도 있고
     * Body, Path, Query가 다 들어갈 수도 있습니다.
     */

    @POST("/Tmap/{UserID}/{Arrivest}/{currentX}/{currentY}")
    Call<BikeTmap> getData(@Body BikeTmap bikeTmap, @Path("UserID") String UserID,
                           @Path("Arrivest") String arrivest,@Path("currentX") String currentX
                            ,@Path("currentY") String currentY);

    @GET("/Station/{id}")
    Call<Station> getStation(@Path("id") String id);

    @GET("/Station")
    Call<List<Station>> allStation();

    @GET("/Push/{registerID}")
    Call<String> pushGCM(@Path("registerID") String id);

    @POST("/Login/")
    Call<registerJ> post_register(@Body registerJ register);

    @GET("/Login/{loginId}/{pass}")
    Call <registerJ> getNameLogin( @Path("loginId") String loginId , @Path("pass") String pass );

    @POST("/Rent")
    Call<Rent> randomnum(@Body Rent rent);

    @GET("/Tmap/{id}/{del}")
    Call<BikeTmap> getBikeid(@Path("id") String id,@Path("del") int del);

    @POST("/Bike")
    Call<Bike> biketrouble(@Body Bike bike);

    @GET("/Bike/Arrive/{Stationid}")
    Call<List<BikeTmap>> arrive_info(@Path("Stationid") String stationid);

    @GET("/Bike/Recommend/{Stationid}")
    Call<List<Bike>> recommend_info(@Path("Stationid") String stationid);
}
