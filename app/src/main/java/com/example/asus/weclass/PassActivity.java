package com.example.asus.weclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS on 2018/6/10.
 */

public class PassActivity extends Activity {
    private ServerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_change);


        final Bundle bundle1 = this.getIntent().getExtras();
        final int uid = bundle1.getInt("uid");
        final int type = bundle1.getInt("type");
        final EditText new_pass = (EditText) findViewById(R.id.new_pass);
        final EditText con_pass = (EditText) findViewById(R.id.confirm_pass);
        final EditText old_pass = (EditText) findViewById(R.id.old_pass);
        final Button changeBut = (Button) findViewById(R.id.change_but);
        final Button cancelBut = (Button) findViewById(R.id.cancel_but);


        changeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (old_pass.getText().toString().equals("")||con_pass.getText().toString().equals("")) {
                    Toast.makeText(PassActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (new_pass.getText().toString().equals("")) {
                    Toast.makeText(PassActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (con_pass.getText().toString().equals("")) {
                    Toast.makeText(PassActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                }
                else if (new_pass.getText().toString().equals(con_pass.getText().toString())) {
                    //change data
                    //post
                    Map<String, Object> params = new HashMap<>();
                    params.put("UID", uid);
                    params.put("Password", old_pass.getText().toString());
                    params.put("NewPassword", new_pass.getText().toString());
                    service = ServerFactory.createService("http://120.79.84.147:8000");
                    service.postPass(params)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ResponseClass>() {
                                @Override
                                public void onCompleted() {
                                    System.out.print("完成传输");
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e instanceof HttpException) {
                                        HttpException exception = (HttpException)e;
                                        int code = exception.response().code();
                                        if (code == 400) {
                                            Toast.makeText(PassActivity.this, "新密码不能和原密码相同", Toast.LENGTH_SHORT).show();
                                        } else if (code == 404) {
                                            Toast.makeText(PassActivity.this, "找不到UID", Toast.LENGTH_SHORT).show();
                                        } else if (code == 405) {
                                            Toast.makeText(PassActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onNext(ResponseClass github) {
//                                    Bundle bundle2 = new Bundle();
//                                    bundle2.putInt("type", type);
//                                    bundle2.putInt("uid", bundle1.getInt("uid"));
//                                    Intent intent = new Intent(PassActivity.this, MainActivity.class);
//                                    intent.putExtras(bundle2);
//                                    startActivity(intent);
                                    finish();
                                }
                            });
                } else {
                    Toast.makeText(PassActivity.this, "密码确认错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
