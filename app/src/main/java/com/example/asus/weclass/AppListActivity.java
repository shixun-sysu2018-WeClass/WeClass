package com.example.asus.weclass;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.AsyncHttpResponseHandler;
//import com.loopj.android.http.RequestParams;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AppListActivity extends AppCompatActivity {
    private ServerService service;
    private List<Map<String, Object>> listview = new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_list);

        final Bundle bundle1 = this.getIntent().getExtras();
        final int uid = bundle1.getInt("uid");
        final int type = bundle1.getInt("type");
        ListView listView;
        listView = (ListView) findViewById(R.id.list_one);
//        Map<String, Object> title1 = new LinkedHashMap<>();
//        title1.put("status", "状态");
//        title1.put("room_name", "教室");
//        title1.put("application_date", "申请日期 ");
//        listview.add(title1);


        service = ServerFactory.createService("http://120.79.84.147:8000");
        service.getAppList(String.valueOf(uid))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AppListSerClass>>() {
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
                                Toast.makeText(AppListActivity.this, "无相关记录", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onNext(List<AppListSerClass> room) {
                        for (AppListSerClass temp : room) {
                            String s = "";
                            if (temp.getStatus() == 0) {
                                s = "待审核";
                            } else if (temp.getStatus() == 1) {
                                s = "已通过";
                            } else if (temp.getStatus() == 2) {
                                s = "未通过";
                            }
                            Map<String, Object> title2 = new LinkedHashMap<>();
                            title2.put("status", s);
                            title2.put("room_name", temp.getCID__RoomName());
                            title2.put("book_date", temp.getBookDate());
                            title2.put("application_date", temp.getStartDate());
                            title2.put("start_time", temp.getStartTime());
                            title2.put("end_time", temp.getEndTime());
                            title2.put("application_date", temp.getStartDate());
                            title2.put("statement", temp.getUsage());
                            title2.put("aid", temp.getAID());
                            listview.add(title2);

                        }
                        simpleAdapter.notifyDataSetChanged();

                    }
                });


        final ImageButton back_but = (ImageButton) findViewById(R.id.back_but);
        back_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        simpleAdapter = new SimpleAdapter(this, listview, R.layout.applist_model,
                new String[] {"status","room_name", "application_date"}, new int[] {R.id.status, R.id.classroom, R.id.application_time});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                if (pos != 0) {
                    Intent intent = new Intent(AppListActivity.this, AppDetActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("aid",Integer.parseInt(listview.get(pos).get("aid").toString()));
                    bundle.putString("classroom",listview.get(pos).get("room_name").toString());
                    bundle.putString("BookDate",listview.get(pos).get("book_date").toString());
                    bundle.putString("applicationDate",listview.get(pos).get("application_date").toString());
                    bundle.putString("begin",listview.get(pos).get("start_time").toString());
                    bundle.putString("end",listview.get(pos).get("end_time").toString());
                    bundle.putString("statement",listview.get(pos).get("statement").toString());
                    bundle.putInt("uid",uid);
                    bundle.putInt("type", type);
                    bundle.putInt("pos", pos);
                    bundle.putString("status", listview.get(pos).get("status").toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
//                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent t) {
        super.onActivityResult(requestcode, resultcode, t);
        if(requestcode == 1) {
            if(resultcode == RESULT_CANCELED) {
                return;
            }
            Bundle bundle3 = t.getExtras();
            if (bundle3 != null) {
                int pos = bundle3.getInt("pos");
                listview.remove(pos);
                simpleAdapter.notifyDataSetChanged();
            }

        }
    }



}
