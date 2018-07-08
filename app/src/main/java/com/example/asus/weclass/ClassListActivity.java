package com.example.asus.weclass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ASUS on 2018/6/10.
 */

public class ClassListActivity extends Activity {
    private ServerService service;
    private List<Map<String, Object>> listview = new ArrayList<>();
    private SimpleAdapter simpleAdapter;
    private ListView listView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom_list);


        Bundle bundle1 = this.getIntent().getExtras();
        final int uid = bundle1.getInt("uid");
        final int type = bundle1.getInt("type");
        listView = (ListView) findViewById(R.id.list_view);
        Map<String, Object> title1 = new LinkedHashMap<>();
        title1.put("class_name", "教室号");
        title1.put("capacity", "容量");
        title1.put("cid", ", ");
        listview.add(title1);


        service = ServerFactory.createService("http://120.79.84.147:8000");
        service.getClass("searchRoomList")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ClassListSerClass>>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("完成传输");

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException)e;
                            int code = exception.response().code();
                            if (code == 204) {
                                Toast.makeText(ClassListActivity.this, "找不到记录", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onNext(List<ClassListSerClass> room) {
                        for (ClassListSerClass temp : room) {
                            Map<String, Object> title2 = new LinkedHashMap<>();
                            title2.put("class_name", temp.getRoomName());
                            title2.put("capacity", temp.getCapacity());
                            title2.put("cid", temp.getCID());
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

        simpleAdapter = new SimpleAdapter(this, listview, R.layout.classroom_model,
                new String[] {"class_name","capacity"}, new int[] {R.id.room_num, R.id.capacity});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                if (pos != 0) {
                    Intent intent = new Intent(ClassListActivity.this, RoomDetActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("class_name",listview.get(pos).get("class_name").toString());
                    bundle.putInt("cid",Integer.parseInt(listview.get(pos).get("cid").toString()));
                    bundle.putInt("uid",uid);
                    bundle.putInt("type", type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

    }
}
