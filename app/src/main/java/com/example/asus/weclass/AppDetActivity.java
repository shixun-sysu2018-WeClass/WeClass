package com.example.asus.weclass;

import android.app.Activity;

/**
 * Created by ASUS on 2018/6/10.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppDetActivity extends AppCompatActivity {
    private TextView txtShow;
    private Button buttonCancel;
    private ServerService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_detail);

        final Bundle bd = this.getIntent().getExtras();
        int status = bd.getInt("status_code");
//        final String uid = bd.getString("uid");
//        final int type = bd.getInt("type");

        txtShow = (TextView)findViewById(R.id.status);
        String toshow ;

        txtShow.setText(bd.getString("status"));



        txtShow = (TextView)findViewById(R.id.classroom);
        toshow = bd.getString("classroom");
        txtShow.setText(toshow);

        txtShow = (TextView)findViewById(R.id.book_date);
        toshow = bd.getString("BookDate").toString();
        txtShow.setText(toshow);

        txtShow = (TextView)findViewById(R.id.application_date);
        toshow = bd.getString("applicationDate").toString();
        txtShow.setText(toshow);

        txtShow = (TextView)findViewById(R.id.begin);
        toshow = bd.getString("begin").toString();
        txtShow.setText(toshow);

        txtShow = (TextView)findViewById(R.id.end);
        toshow = bd.getString("end").toString();
        txtShow.setText(toshow);

        txtShow = (TextView)findViewById(R.id.statement);
        toshow = bd.getString("statement").toString();
        txtShow.setText(toshow);

        final Button back_but = (Button) findViewById(R.id.back_but);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final RequestParams params = new RequestParams();
        params.add("AID",String.valueOf(bd.getInt("aid")));

        buttonCancel =(Button) findViewById(R.id.buttonCancel);
        if (bd.getString("status").equals("未通过")) {
            buttonCancel.setText("删除条目");
        }
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> params = new HashMap<>();
                params.put("AID", String.valueOf(bd.getInt("aid")));
                service = ServerFactory.createService("http://120.79.84.147:8000");
                service.cancelapp(params)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseClass1>() {
                            @Override
                            public void onCompleted() {
                                System.out.print("完成传输");
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof HttpException) {
                                    HttpException exception = (HttpException)e;
                                    int code = exception.response().code();
                                    if (code == 404) {
                                        Toast.makeText(AppDetActivity.this, "无相关记录", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onNext(ResponseClass1 github) {
//                                Toast.makeText(AppDetActivity.this, "成功取消预约", Toast.LENGTH_SHORT).show();
//                                Bundle bundle2 = new Bundle();
//                                bundle2.putInt("type", bd.getInt("type"));
//                                bundle2.putString("uid", bd.getString("uid"));
//                                Intent intent = new Intent(AppDetActivity.this, AppListActivity.class);
//                                intent.putExtras(bundle2);
//                                startActivity(intent);
                                Bundle bundle2 = new Bundle();
                                bundle2.putInt("pos", bd.getInt("pos"));
                                Intent intent2 = new Intent(AppDetActivity.this, MainActivity.class);
                                intent2.putExtras(bundle2);
                                setResult(2, intent2);
                                finish();

                            }
                        });
            }
        });

    }


}

