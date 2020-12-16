package com.inteli.seven.grain.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.inteli.seven.grain.Application;
import com.inteli.seven.grain.CheckFinishService;
import com.inteli.seven.grain.R;
import com.inteli.seven.grain.ServerUrl;

public class MainActivity extends BaseActivity {

    TextView box1, box2, box3, state;
    ImageButton cycle;
    Button start;

    boolean isBox1Ok = false;
    boolean isBox2Ok = false;
    boolean isBox3Ok = false;

    int mState = -1;


    @Override
    protected void onResume() {
        super.onResume();

        try {
            //잔량 설정.
            downloadGrain();
            //상태 설정.
            setState();
        } catch (Exception e) {

        }


        if (!isServiceRunningCheck()) {
            Toast.makeText(this, "반갑습니다!", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(getApplicationContext(), CheckFinishService.class);
            startService(intent);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        state = findViewById(R.id.state);
        cycle = findViewById(R.id.cycle);


        //시작 화면으로 이동,
        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isBox1Ok = true;
                isBox2Ok = true;
                isBox3Ok = true;

                if (isBox1Ok && isBox2Ok && isBox3Ok) { //모두 잔량 있으면.

                    if (mState == 0 || mState == 2) { //0일때만 동작가능.
                        Intent intent = new Intent(getApplicationContext(), WorkingActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MainActivity.this, "쌀을 씻는중입니다.\n새로고침 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "잔량이 부족하거나 서버와 접속이 안되고 있습니다.\n새로고침 후 이용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(MainActivity.this, "새로고침", Toast.LENGTH_SHORT).show();

                //잔량 설정.
                downloadGrain();
                //상태 설정.
                setState();
            }
        });


        //잔량 설정.
        downloadGrain();
        //상태 설정.
        setState();


    }


    private void downloadGrain() {

        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.GET, ServerUrl.getDownloadGrainUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {
                try {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String[] resp = s.split(",");
                            int boxData1 = Integer.parseInt(resp[0]);
                            int boxData2 = Integer.parseInt(resp[1]);
                            int boxData3 = Integer.parseInt(resp[2]);

                            isBox1Ok = calculateGrain(box1, boxData1);
                            isBox2Ok = calculateGrain(box2, boxData2);
                            isBox3Ok = calculateGrain(box3, boxData3);

                        }
                    });


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

    private boolean calculateGrain(TextView grainView, int grainData) {

        if (grainData < 0) {
            grainView.setText("센서오류!");
            grainView.setTextColor(Color.parseColor("#f53a1f"));

            return true;
        } else if (0 <= grainData && grainData < 5) {
            grainView.setText("가득");
            grainView.setTextColor(Color.parseColor("#36ca1a"));

            return true;
        } else if (5 <= grainData && grainData < 17) {
            grainView.setText("보통");
            grainView.setTextColor(Color.parseColor("#521c00"));

            return true;

        } else if (17 <= grainData) {
            grainView.setText("부족");
            grainView.setTextColor(Color.parseColor("#f53a1f"));

            return false;
        }

        return false;
    }


    private void setState() {

        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.GET, ServerUrl.getDownloadStateUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {
                try {

                    mState = Integer.parseInt(s);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (s.equals("0")) {
                                state.setText("대기중");
                            } else if (s.equals("1")) {
                                state.setText("쌀 씻는중");
                            } else if (s.equals("2")) {
                                state.setText("씻기 완료");
                            }

                        }
                    });


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


    public boolean isServiceRunningCheck() {
        ActivityManager manager = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.inteli.seven.grain.CheckFinishService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
