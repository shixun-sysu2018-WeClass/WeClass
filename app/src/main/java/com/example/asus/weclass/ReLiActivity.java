package com.example.asus.weclass;

import android.app.Activity;

/**
 * Created by ASUS on 2018/6/10.
 */
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ReLiActivity extends AppCompatActivity {

    private ServerService service;
    private List<Map<String, Object>> listview = new ArrayList<>();
    private CommonAdapter commonadapter;
    private ListView listView;
    private List<Map<String, Object>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_list);

        final ImageButton back_but = (ImageButton) findViewById(R.id.back_but);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ReLiActivity.this, MainActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("uid", bundle1.getInt("uid"));
//                bundle.putInt("type", bundle1.getInt("type"));
//                intent.putExtras(bundle);
                finish();
            }
        });

        final Bundle bundle1 = this.getIntent().getExtras();
        final int uid = bundle1.getInt("uid");
        final int type = bundle1.getInt("type");

        final List<RequList> reqlist = new ArrayList<>();
        reqlist.add(new RequList("2018-04-30", "申请中", "2018-05-12", "B403",
                "第5-9节", "杨玉楠", "15331368", "13318707450", "申请教室"));
        reqlist.add(new RequList("2018-05-01", "申请中", "2018-05-13", "B303",
                "第4-6节", "杨小楠", "15331366", "13318707450", "申请教室"));
        reqlist.add(new RequList("2018-05-02", "申请中", "2018-05-14", "A402",
                "第4-9节", "杨大楠", "15331363", "13318707450", "申请教室"));
        reqlist.add(new RequList("2018-05-03", "申请中", "2018-05-15", "D103",
                "第5-7节", "杨啊楠", "15331362", "13318707450", "申请教室"));

        final String[] req_time = new String[12];
        final String[] status = new String[12];
        final String[] req_class_time = new String[12];
        final String[] classroom = new String[12];
        final String[] det_time = new String[12];
        final String[] req_name = new String[12];
        final String[] classID = new String[12];
        final String[] telnum = new String[12];
        final String[] detail = new String[12];
        for (int i = 0; i < 4; i++) {
            RequList temp = reqlist.get(i);
            req_time[i] = temp.getreq_time();
            status[i] = temp.getstatus();
            req_class_time[i] = temp.getreq_class_time();
            classroom[i] = temp.getclassroom();
            det_time[i] = temp.getdet_time();
            req_name[i] = temp.getreq_name();
            classID[i] = temp.getclassID();
            telnum[i] = temp.gettelnum();
            detail[i] = temp.getdetail();
        }


        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonadapter = new CommonAdapter<Map<String, Object>>(this,
                R.layout.reqlist_model, list) {
            @Override
            public void convert(MyViewHolder hodlder, Map<String, Object> t) {
                TextView reqtime = hodlder.getViews(R.id.req_time);
                reqtime.setText(t.get("req_time").toString());
                TextView status = hodlder.getViews(R.id.req_status);
                status.setText(t.get("status").toString());
                TextView req_name = hodlder.getViews(R.id.req_name);
                req_name.setText(t.get("req_name").toString());
                TextView req_class_time = hodlder.getViews(R.id.req_class_time);
                req_class_time.setText(t.get("req_class_time").toString());
                TextView classroom = hodlder.getViews(R.id.classroom);
                classroom.setText(t.get("classroom").toString());
            }
        };

//        service = ServerFactory.createService("http://120.79.84.147:8000");
//        service.getReqList("searchWaitingList")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<RequListSerClass>>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.print("完成传输");
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof HttpException) {
//                            HttpException exception = (HttpException) e;
//                            int code = exception.response().code();
//                            if (code == 404) {
//                                String t = String.valueOf(code);
//                                Toast.makeText(ReLiActivity.this, "无申请", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onNext(List<RequListSerClass> room) {
//                        for (RequListSerClass temp : room) {
//                            String s = "待审核";
//                            Map<String, Object> tmap = new LinkedHashMap<>();
//                            tmap.put("req_time", temp.getBookDate());
//                            tmap.put("status", s);
//                            tmap.put("req_name", temp.getStudentName());
//                            tmap.put("req_class_time", temp.getStartDate());
//                            tmap.put("classroom", temp.getCID__RoomName());
//                            tmap.put("aid", temp.getAID());
//                            tmap.put("start_time", temp.getStartTime());
//                            tmap.put("end_time", temp.getEndTime());
//                            tmap.put("usage", temp.getUsage());
//                            tmap.put("reason", temp.getReason());
//                            tmap.put("stu_num", temp.getStudentNumber());
//                            tmap.put("phon_num", temp.getPhoneNumber());
//                            tmap.put("uid", temp.getUID());
//                            list.add(tmap);
//                        }
//                        commonadapter.notifyDataSetChanged();
//                    }
//                });


        commonadapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent1 = new Intent(ReLiActivity.this, ReqDetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("req_time", list.get(position).get("req_time").toString());
                bundle.putString("status", "待审核");
                bundle.putString("req_class_time", list.get(position).get("req_class_time").toString());
                bundle.putString("classroom", list.get(position).get("classroom").toString());
                bundle.putString("req_name", list.get(position).get("req_name").toString());
                bundle.putInt("uid", uid);
                bundle.putInt("type", type);
                bundle.putString("detail", list.get(position).get("usage").toString());
                bundle.putString("phone", list.get(position).get("phon_num").toString());
                bundle.putString("stu_num", list.get(position).get("stu_num").toString());
                bundle.putString("app_uid", list.get(position).get("uid").toString());
                bundle.putString("aid", list.get(position).get("aid").toString());
                bundle.putString("start_time", list.get(position).get("start_time").toString());
                bundle.putString("end_time", list.get(position).get("end_time").toString());
                bundle.putInt("pos", position);

                intent1.putExtras(bundle);
                startActivityForResult(intent1, 1);
            }
        });

        recyclerView.setAdapter(commonadapter);




    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    list.clear();
                    service = ServerFactory.createService("http://120.79.84.147:8000");
                    service.getReqList("searchWaitingList")
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<RequListSerClass>>() {
                                @Override
                                public void onCompleted() {
                                    System.out.print("完成传输");

                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e instanceof HttpException) {
                                        HttpException exception = (HttpException) e;
                                        int code = exception.response().code();
                                        if (code == 404) {
                                            String t = String.valueOf(code);
                                            Toast.makeText(ReLiActivity.this, "找不到记录", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onNext(List<RequListSerClass> room) {
                                    for (RequListSerClass temp : room) {
                                        String s = "待审核";
                                        Map<String, Object> tmap = new LinkedHashMap<>();
                                        tmap.put("req_time", temp.getBookDate());
                                        tmap.put("status", s);
                                        tmap.put("req_name", temp.getStudentName());
                                        tmap.put("req_class_time", temp.getStartDate());
                                        tmap.put("classroom", temp.getCID__RoomName());
                                        tmap.put("aid", temp.getAID());
                                        tmap.put("start_time", temp.getStartTime());
                                        tmap.put("end_time", temp.getEndTime());
                                        tmap.put("usage", temp.getUsage());
                                        tmap.put("reason", temp.getReason());
                                        tmap.put("stu_num", temp.getStudentNumber());
                                        tmap.put("phon_num", temp.getPhoneNumber());
                                        tmap.put("uid", temp.getUID());
                                        list.add(tmap);
                                    }
                                    commonadapter.notifyDataSetChanged();
                                }
                            });
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(runnable);

    }

    private Runnable runnable = new Runnable() {
        public void run() {
            handler.sendEmptyMessage(1);
        }
    };

}
