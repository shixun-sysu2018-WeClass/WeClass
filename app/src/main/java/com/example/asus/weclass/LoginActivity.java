package com.example.asus.weclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS on 2018/6/10.
 */
import retrofit2.Response;

public class LoginActivity  extends Activity {
    private ServerService service;
    private int uid;
    private String pass = "";
    private int type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);


        final EditText username = (EditText) findViewById(R.id.username_text);
        final EditText password = (EditText) findViewById(R.id.password_text);
        final RadioButton is_manage = (RadioButton) findViewById(R.id.is_manager);
        final RadioButton is_student = (RadioButton) findViewById(R.id.is_student);
        final Button log_in = (Button) findViewById(R.id.login_but);



        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //利用账户获取到用户的用户名，uid，密码，类型（连接数据库）
                final String s1 = username.getText().toString();
                final String s2 = password.getText().toString();
                final int s3;
                if (is_manage.isChecked()) {
                    s3 = 0;
                } else {
                    s3 = 1;
                }
//                network_status = isNetworkAvailable(LoginActivity.this);
                if (username.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!isNetworkAvailable(LoginActivity.this)) {
                        Toast.makeText(LoginActivity.this, "无网络连接", Toast.LENGTH_SHORT).show();
                } else {
                    service = ServerFactory.createService("http://120.79.84.147:8000");
                    service.getUser(s1, s2, s3)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<LoginSerClass>>() {
                                @Override
                                public void onCompleted() {
                                    System.out.print("完成传输");

                                }

                                @Override
                                public void onError(Throwable e) {
//                                    Toast.makeText(LoginActivity.this, "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                                    if (e instanceof HttpException) {
                                        HttpException exception = (HttpException)e;
                                        int code = exception.response().code();
                                        if (code == 405) {
                                            String t = String.valueOf(code);
                                            Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                        } else if (code == 404) {
                                            String t = String.valueOf(code);
                                            Toast.makeText(LoginActivity.this, "找不到User", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onNext(List<LoginSerClass> github) {
                                    uid = github.get(0).getUId();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", s3);
                                    bundle.putInt("uid", uid);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                }




//                if (username.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
//                } else if (password.getText().toString().equals("")) {
//                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
//                } else if (is_manage.isChecked()) {
//                    //判断用户是否存在，以及账号密码是否正确
//                    if (type == 1) {
//                        Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
//                    } else if (password.getText().toString().equals(pass)) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("type", 0);
//                        bundle.putString("uid", uid);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(LoginActivity.this, "账号密码不匹配", Toast.LENGTH_SHORT).show();
//                    }
//                } else if (is_student.isChecked()) {
//                    //判断用户是否存在，以及账号密码是否正确
//                    if (type == 0) {
//                        Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
//                    } else if (password.getText().toString().equals(pass)) {
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putInt("type", 1);
//                        bundle.putString("uid", uid);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(LoginActivity.this, "账号密码不匹配", Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
        });




    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if(info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;

    }
}
