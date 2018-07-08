package com.example.asus.weclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS on 2018/6/10.
 */

public class AppActivity extends Activity {
    private ServerService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application);


        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            final String room = bundle.getString("class_name");
            final int uid = bundle.getInt("uid");
            final EditText stu_num = (EditText) findViewById(R.id.stu_num_text);
            final TextView room_num = (TextView) findViewById(R.id.room_num_text);
            final EditText app_date = (EditText) findViewById(R.id.app_date_text);
            final EditText start_time = (EditText) findViewById(R.id.start_time_text);
            final EditText endtime = (EditText) findViewById(R.id.end_time_text);
            final EditText reason = (EditText) findViewById(R.id.reason_text);
            final EditText stu_name = (EditText) findViewById(R.id.stu_name_text);
            final EditText stu_phone = (EditText) findViewById(R.id.phone_text);
            final Button cancel_but = (Button) findViewById(R.id.cancel_but);
            final Button confirm_but = (Button) findViewById(R.id.confirm_but);

            room_num.setText(room);
            cancel_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            confirm_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(System.currentTimeMillis());
                    String s1 = simpleDateFormat.format(date);

                    if (stu_num.getText().toString().equals("") || stu_name.getText().toString().equals("") ||
                            app_date.getText().toString().equals("") ||start_time.getText().toString().equals("") ||
                            endtime.getText().toString().equals("") ||reason.getText().toString().equals("") ||
                            stu_phone.getText().toString().equals("") ) {
                        Toast.makeText(AppActivity.this, "请输入完整的信息", Toast.LENGTH_SHORT).show();
                    }else if (Integer.parseInt(start_time.getText().toString()) < 0 || Integer.parseInt(start_time.getText().toString()) > 12 ||
                            Integer.parseInt(endtime.getText().toString()) < 0 || Integer.parseInt(endtime.getText().toString()) > 12) {
                        Toast.makeText(AppActivity.this, "请输入正确的课时", Toast.LENGTH_SHORT).show();
                    } else if (stu_phone.getText().toString().length() > 0 && stu_phone.getText().toString().length() != 11) {
                        Toast.makeText(AppActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    }
                    else  {
                        Map<String, Object> params = new HashMap<>();
                        params.put("UID", uid);
                        params.put("RoomName", room_num.getText().toString());
                        params.put("Usage", reason.getText().toString());
                        params.put("StartTime", start_time.getText().toString());
                        params.put("EndTime", endtime.getText().toString());
                        params.put("StartDate", app_date.getText().toString());
                        params.put("BookDate", s1);
                        params.put("StudentNumber", stu_num.getText().toString());
                        params.put("StudentName", stu_name.getText().toString());
                        params.put("PhoneNumber", stu_phone.getText().toString());
                        service = ServerFactory.createService("http://120.79.84.147:8000");
                        service.newapp(params)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<ResponseClass2>() {
                                    @Override
                                    public void onCompleted() {
                                        System.out.print("完成传输");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (e instanceof HttpException) {
                                            HttpException exception = (HttpException) e;
                                            int code = exception.response().code();
                                            if (code == 400) {
                                                Toast.makeText(AppActivity.this, "时间不合理", Toast.LENGTH_SHORT).show();
                                            }
                                            if (code == 403) {
                                                Toast.makeText(AppActivity.this, "被占用", Toast.LENGTH_SHORT).show();
                                            }
                                            if (code == 404) {
                                                Toast.makeText(AppActivity.this, "找不到用户或教室", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onNext(ResponseClass2 github) {
//                                    Toast.makeText(AppActivity.this, "成功添加", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                    }
                }
            });
        }



    }
}
