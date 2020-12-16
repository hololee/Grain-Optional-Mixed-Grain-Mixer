package com.inteli.seven.grain;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inteli.seven.grain.activity.SplashActivity;

import java.util.HashMap;

public class CheckFinishService extends Service {


    Notification notiEx;
    int state = -1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("upadteState", "start");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notiEx = new Notification.Builder(CheckFinishService.this, "aa")
                    .setContentTitle("GRAIN")
                    .setContentText("grain 알림 서비스가 실행중입니다.")
                    .setSmallIcon(R.drawable.set)
                    .build();
        } else {
            notiEx = new Notification.Builder(CheckFinishService.this)
                    .setContentTitle("GRAIN")
                    .setContentText("grain 알림 서비스가 실행중입니다.")
                    .setSmallIcon(R.drawable.set)
                    .build();
        }
        startForeground(1201, notiEx);


        //인터넷 연결 확인.
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (conManager != null) {
            info = conManager.getActiveNetworkInfo();
        }

        //연결 되어있다면.
        if (info != null && info.isConnected()) {

            new Thread(new Runnable() {
                @Override
                public void run() {


                    while (true) {


                        Log.d("state", "current : " + getState());
                        if (state == 2) {//작업 완료시에만.

                            Intent intent = new Intent(getApplicationContext(), Alertctivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);

                            Log.d("upadteState", "success");
                            //상태 0으로 변경.
                            updateState();

                        }

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                }
            }).start();


        }


        return super.onStartCommand(intent, flags, startId);
    }


    private int getState() {


        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.GET, ServerUrl.getDownloadStateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {
                try {

                    state = Integer.parseInt(s);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        Application.getInstance().addToRequestQueue(req);

        return state;
    }


    private void updateState() {


        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.GET, ServerUrl.getUpdateStateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    //success or fail check.
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
            }
        });

        Application.getInstance().addToRequestQueue(req);


    }

}
