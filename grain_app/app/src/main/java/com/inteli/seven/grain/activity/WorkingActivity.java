package com.inteli.seven.grain.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.inteli.seven.grain.R;
import com.inteli.seven.grain.ServerUrl;

public class WorkingActivity extends BaseActivity {

    ProgressDialog pDialog;

    int box1Count = 0, box2Count = 0, box3Count = 0, box4Count = 0;

    TextView box1CountText, box2CountText, box3CountText, box4CountText;
    TextView box1gramText, box2gramText, box3gramText;
    ImageButton box1_add, box1_sub, box2_add, box2_sub, box3_add, box3_sub, box4_add, box4_sub;


    Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_working);

        box1_add = findViewById(R.id.box1_add);
        box1_sub = findViewById(R.id.box1_sub);
        box2_add = findViewById(R.id.box2_add);
        box2_sub = findViewById(R.id.box2_sub);
        box3_add = findViewById(R.id.box3_add);
        box3_sub = findViewById(R.id.box3_sub);
        box4_add = findViewById(R.id.box4_add);
        box4_sub = findViewById(R.id.box4_sub);

        box1CountText = findViewById(R.id.box1);
        box2CountText = findViewById(R.id.box2);
        box3CountText = findViewById(R.id.box3);
        box4CountText = findViewById(R.id.box4);

        box1gramText = findViewById(R.id.box1_1);
        box2gramText = findViewById(R.id.box2_1);
        box3gramText = findViewById(R.id.box3_1);

        start = findViewById(R.id.start);


        box1_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1Count < 8) {
                    box1Count++;
                    box1CountText.setText(box1Count + "");
                    box1gramText.setText(box1Count * 20 + "g");
                } else {
                    Toast.makeText(WorkingActivity.this, "잡곡은 최대 8번까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        box1_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box1Count > 0) {
                    box1Count--;
                    box1CountText.setText(box1Count + "");
                    box1gramText.setText(box1Count * 20 + "g");
                }

            }
        });


        box2_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box2Count < 4) {
                    box2Count++;
                    box2CountText.setText(box2Count + "");
                    box2gramText.setText(box2Count * 200 + "g");
                } else {
                    Toast.makeText(WorkingActivity.this, "쌀은 최대 4번까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        box2_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box2Count > 0) {
                    box2Count--;
                    box2CountText.setText(box2Count + "");
                    box2gramText.setText(box2Count * 200 + "g");
                }

            }
        });


        box3_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box3Count < 8) {
                    box3Count++;
                    box3CountText.setText(box3Count + "");
                    box3gramText.setText(box3Count * 20 + "g");
                } else {
                    Toast.makeText(WorkingActivity.this, "잡곡은 최대 8번까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        box3_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box3Count > 0) {
                    box3Count--;
                    box3CountText.setText(box3Count + "");
                    box3gramText.setText(box3Count * 20 + "g");
                }

            }
        });


        //세척 횟수
        box4_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box4Count < 5) {
                    box4Count++;
                    box4CountText.setText(box4Count + "");
                } else {
                    Toast.makeText(WorkingActivity.this, "세척은 최대 5번까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        box4_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (box4Count > 0) {
                    box4Count--;
                    box4CountText.setText(box4Count + "");
                }

            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int sum = box1Count + box2Count + box3Count + box4Count;
                if (sum > 0) {//잡곡 선택이 되어있으면.

                    AlertDialog.Builder builder = new AlertDialog.Builder(WorkingActivity.this);
                    builder.setTitle("실행 준비");
                    builder.setMessage("쌀씻기를 시작합니다.\n선택하신 사항은 다음과 같습니다." + "\n" +
                            "\n잡곡1:" + box1Count +
                            "\n잡곡2:" + box3Count +
                            "\n쌀:" + box2Count +
                            "\n씻기횟수:" + box4Count +
                            "\n\n진행하시려면 확인을 눌러주세요.");

                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendData(String.valueOf(box1Count) + String.valueOf(box2Count) + String.valueOf(box3Count) + String.valueOf(box4Count));
                            Intent intent = new Intent(getApplicationContext(), FinishActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    });
                    builder.setNegativeButton("진행취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dilalog = builder.create();
                    dilalog.show();


                } else {
                    Toast.makeText(WorkingActivity.this, "씻을 곡물을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void sendData(String data) {

        Log.d("url", ServerUrl.getStartWorkingUrl(data));

        //업로드중 다이얼로그 표시.
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setMessage("실행중입니다... 잠시만 기다려 주세요.");
        pDialog.show();


        //데이터 가져오기.
        StringRequest req = new StringRequest(Request.Method.GET, ServerUrl.getStartWorkingUrl(data), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    //success or fail check.
                    hidePDialog();
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

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
