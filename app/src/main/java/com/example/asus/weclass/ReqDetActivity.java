package com.example.asus.weclass;

import android.app.Activity;

/**
 * Created by ASUS on 2018/6/10.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReqDetActivity extends AppCompatActivity {
    private ServerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_detail);

        final Bundle bundle1 = this.getIntent().getExtras();

            final String req_time1 = bundle1.getString("req_time");
            final String req_name1 = bundle1.getString("req_name");
            final String status1 = bundle1.getString("status");
            final String req_class_time1 = bundle1.getString("req_class_time");
            final String det_time1 = bundle1.getString("det_time");
            final String classroom1 = bundle1.getString("classroom");
            final String classID1 = bundle1.getString("classID");
            final String telnum1 = bundle1.getString("phone");
            final String detail1 = bundle1.getString("detail");

            TextView req_time = (TextView) findViewById(R.id.req_time);
            TextView req_name = (TextView) findViewById(R.id.req_name);
            TextView status = (TextView) findViewById(R.id.status);
            TextView det_class_Time = (TextView) findViewById(R.id.det_class_time);
            TextView det_time = (TextView) findViewById(R.id.det_time);
            TextView classroom = (TextView) findViewById(R.id.classroom);
            TextView classID = (TextView) findViewById(R.id.classID);
            TextView telnum = (TextView) findViewById(R.id.telnum);
            TextView detail = (TextView) findViewById(R.id.detail);

            req_time.setText(req_time1);
            req_name.setText(req_name1);
            status.setText(status1);
            det_class_Time.setText(req_class_time1);
            det_time.setText(det_time1);
            classroom.setText(classroom1);
            classID.setText(classID1);
            telnum.setText(telnum1);
            detail.setText(detail1);
            final ImageView backbut = (ImageView) findViewById((R.id.back));
            backbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            final Button permitBut = (Button) findViewById(R.id.permit);
            permitBut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final AlertDialog.Builder alerDialog = new AlertDialog.Builder(ReqDetActivity.this);

                    alerDialog.setTitle("是否确认通过").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Map<String, Object> params = new HashMap<>();
                            params.put("AID", bundle1.getString("aid"));
                            params.put("NewStatus", 1);
                            service = ServerFactory.createService("http://120.79.84.147:8000");
                            service.postStatus(params)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<ReqDetSerClass>() {
                                        @Override
                                        public void onCompleted() {
                                            System.out.print("完成传输");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (e instanceof HttpException) {
                                                HttpException exception = (HttpException) e;
                                                int code = exception.response().code();
                                                if (code == 403) {
                                                    Toast.makeText(ReqDetActivity.this, "该课室已经被申请", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNext(ReqDetSerClass reqDetSerClass) {
                                        Toast.makeText(ReqDetActivity.this, "成功通过", Toast.LENGTH_SHORT).show();
//                                        Bundle bundle2 = new Bundle();
//                                        Intent intent = new Intent(ReqDetActivity.this, ReLiActivity.class);
//                                        intent.putExtras(bundle2);
//                                        startActivity(intent);
//                                            Bundle bundle2 = new Bundle();
//                                            bundle2.putInt("pos", bundle1.getInt("pos"));
//                                            Intent intent2 = new Intent(ReqDetActivity.this, ReLiActivity.class);
//                                            intent2.putExtras(bundle2);
//                                            setResult(2, intent2);
                                            finish();
                                        }
                                    });
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
            });

            final Button rejectbut = (Button) findViewById(R.id.reject);
            rejectbut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final LayoutInflater factor = LayoutInflater.from(ReqDetActivity.this);
                    final View view_in = factor.inflate(R.layout.dialog, null);
                    final AlertDialog.Builder alerDialog = new AlertDialog.Builder(ReqDetActivity.this);
                    alerDialog.setTitle("").setView(view_in).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText editReason = (EditText) view_in.findViewById(R.id.editreason_text);
                            String Reason = editReason.getText().toString();
                            Map<String, Object> params = new HashMap<>();
                            params.put("AID", bundle1.getString("aid"));
                            params.put("NewStatus", 2);
                            params.put("Reason", Reason);
                            service = ServerFactory.createService("http://120.79.84.147:8000");
                            service.postStatus(params)
                                    .subscribeOn(Schedulers.newThread())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<ReqDetSerClass>() {
                                        @Override
                                        public void onCompleted() {
                                            System.out.print("完成传输");
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (e instanceof HttpException) {
                                                HttpException exception = (HttpException) e;
                                                int code = exception.response().code();
                                                if (code == 403) {
                                                    Toast.makeText(ReqDetActivity.this, "拒绝原因不能为空", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onNext(ReqDetSerClass reqDetSerClass) {
//                                        Bundle bundle2 = new Bundle();
//                                        bundle2.putInt("type", bundle1.getInt("type"));
//                                        bundle2.putString("uid", bundle1.getString("uid"));
//                                        Intent intent = new Intent(ReqDetActivity.this, ReLiActivity.class);
//                                        intent.putExtras(bundle2);
//                                        startActivity(intent);

//                                            Bundle bundle3 = new Bundle();
//                                            bundle3.putInt("pos", bundle1.getInt("pos"));
//                                            Intent intent2 = new Intent(ReqDetActivity.this, ReLiActivity.class);
//                                            intent2.putExtras(bundle3);
//                                            setResult(2, intent2);
                                            finish();
                                        }
                                    });

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
            });
        }
}
