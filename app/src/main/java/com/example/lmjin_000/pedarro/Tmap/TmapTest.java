package com.example.lmjin_000.pedarro.Tmap;

/**
 * Created by lmjin_000 on 2016-02-23.
 */
public class TmapTest {
    /*
    public void TaskStart(){
        tmap = new Tmap();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Intent intent = getIntent();
                    BikeTmap bikeTmap = (BikeTmap)intent.getParcelableExtra("BikeTmap");

                    tmap.execute(bikeTmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void TaskEnd(){

    }
    public class Tmap extends AsyncTask<BikeTmap, BikeTmap, BikeTmap>
    {
        private BikeTmap bikeTmap2 = new BikeTmap();
        private BikeTmap last_bikeTmap = new BikeTmap();

        @Override
        protected BikeTmap doInBackground(BikeTmap... bikeTmaps) {
            while (isCancelled() == false) {

                //현재위치 받은뒤 서버로부터 값 가져오기 입력 값  : 현재 위치 ( model 새로 만들어야 할 듯 )
                //일단 임시로 현재 위치 좌표값 +해서 계산하겠음
                bikeTmap2 = bikeTmaps[0];

                Log.i("test", "사용자 이름 : " + bikeTmap2.getUserID());
                Log.i("test", "도착 정거장 : " + bikeTmap2.getArrivest());
                Log.i("test", "tmap - x : " + longitude);
                Log.i("test", "tmap - y : " + latitude);

                String temptX = Double.toString(longitude);
                String temptY = Double.toString(latitude);

                Call<BikeTmap> tmapCall = networkService.getData(bikeTmap2, bikeTmap2.getUserID(), bikeTmap2.getArrivest(),temptX,temptY);
                tmapCall.enqueue(new Callback<BikeTmap>() {
                    @Override
                    public void onResponse(Response<BikeTmap> response, Retrofit retrofit) {
                        if (response.isSuccess()) {
                            last_bikeTmap = response.body();
                        } else {
                            int statusCode = response.code();
                            Log.i("test", "아직 대여에 성공하지 못하였습니다." + statusCode);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i("test", "서버 onFailure 에러내용 : " + t.getMessage());
                    }
                });

                try {
                    Thread.sleep(60000);//60초마다
                } catch (InterruptedException ex) {}

                publishProgress(last_bikeTmap);
            }
            return last_bikeTmap;
        }

        @Override
        protected void onPreExecute(){
            // ip, port 연결 , network 연결
            ApplicationController application = ApplicationController.getInstance();
            application.buildNetworkService("52.36.42.94", 3000);
            networkService = ApplicationController.getInstance().getNetworkService();

        }

        @Override
        protected void onProgressUpdate(BikeTmap... result) {

        }

        @Override
        protected void onPostExecute(BikeTmap result) {
            //클라 화면에 글씨 띄우기
            Log.i("Arrive","도착예정정보 끝");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("Arrive", "도착예정정보 끝");
        }

    }
    */
}
