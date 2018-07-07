package com.example.asus.weclass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.RecyclerView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import junit.framework.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.cjj.Util.dip2px;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by ASUS on 2018/6/10.
 */

public class RoomDetActivity extends Activity {
    @BindView(R.id.weekNames)
    LinearLayout weekNames;

    @BindView(R.id.sections)
    LinearLayout sections;

    @BindView(R.id.mFreshLayout)
    MaterialRefreshLayout mFreshLayout;

    @BindViews({R.id.weekPanel_1, R.id.weekPanel_2, R.id.weekPanel_3, R.id.weekPanel_4,
            R.id.weekPanel_5, R.id.weekPanel_6, R.id.weekPanel_7})
    List<LinearLayout> mWeekViews;

    private int itemHeight;
    private int maxSection = 13;
    private ListView listView;
    private List<Map<String, Object>> listview = new ArrayList<>();
    //private CardAdapter<Map<String, Object>> cardAdapter;
    private ServerService service;
    private int oid;
    private int cid;
    private String className;
    private int type;
    private int uid;
//    final FloatingActionButton fltbut = (FloatingActionButton) findViewById(R.id.float_but);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_detail);
        ButterKnife.bind(this);

        Bundle bundle0 = this.getIntent().getExtras();
        cid = bundle0.getInt("cid");
        className = bundle0.getString("class_name");
        uid = bundle0.getInt("uid");
        type = bundle0.getInt("type");

//        if(type == 1) {
//            fltbut.setVisibility(View.INVISIBLE);
//        }

        waiting_apply();

        itemHeight = getResources().getDimensionPixelSize(R.dimen.sectionHeight);
        initWeekNameView();
        try {
            initWeekCourse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initSectionView();

        try {
            initWeekCourseView();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setRefreshListener();

    }





    /**
     * 初始化课程表
     */
    private void  initWeekCourse() throws ParseException {
        service = ServerFactory.createService("http://120.79.84.147:8000");
        List<jsonModel> jsonModels = new ArrayList<>();
        CourseDao.setCourseModels(jsonModels, className);
        service.getCourses(String.valueOf(cid))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<jsonModel>>() {
                    @Override
                    public void onCompleted() {
                        System.out.print("完成传输");
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException)e;
                            int code = exception.code();
                            if (code == 200) {
                                Toast.makeText(RoomDetActivity.this, "请求成功",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RoomDetActivity.this,"请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RoomDetActivity.this,"不是exception", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("Github-Demo", e.getMessage());

                    }

                    @Override
                    public void onNext(List<jsonModel> jsonModels) {
                        try {
                            CourseDao.setCourseModels(jsonModels, "A101");
                            mFreshLayout.setLoadMore(false);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        clearChildView();
                        try {
                            initWeekCourseView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mFreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mFreshLayout.finishRefreshing();
                            }
                        }, 500);

                    }

                });


    }


    private void initWeekCourseView() throws ParseException {

        List<jsonModel> jsonModels = new ArrayList<>();
        for (int i = 0; i < mWeekViews.size(); i++) {
            initWeekPanel(mWeekViews.get(i), CourseDao.getCourseData()[i]);
        }
    }


    /**
     * 下拉刷新
     */
    private void setRefreshListener() {
        mFreshLayout.setLoadMore(false);
        mFreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                clearChildView();
                try {
                    initWeekCourse();
                    initWeekCourseView();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mFreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFreshLayout.finishRefreshing();
                    }
                }, 500);
            }

        });
    }

    /**
     * 顶部周一到周日的布局
     **/
    private void initWeekNameView() {
        for (int i = 0; i < mWeekViews.size() + 1; i++) {
            TextView tvWeekName = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            if (i != 0) {
                lp.weight = 1;

                String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};


                Date today = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(today);
                calendar.add(calendar.DATE, i-1);
                int month = calendar.get(calendar.MONTH)+1;
                int day = calendar.get(Calendar.DATE);
                int w = calendar.get(Calendar.DAY_OF_WEEK)-1;
                if (w < 0)
                    w = 0;
                tvWeekName.setText(weekDays[w]+ '\n'+ day + "日");

                if (i == 1) {
                    tvWeekName.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    tvWeekName.setTextColor(Color.parseColor("#4A4A4A"));
                }
            } else {
                lp.weight = 0.8f;
                tvWeekName.setText(getMonth() + "月");
            }
            tvWeekName.setGravity(Gravity.CENTER_HORIZONTAL);
            tvWeekName.setLayoutParams(lp);
            weekNames.addView(tvWeekName);
        }
    }

    /**
     * 左边节次布局，设定每天最多12节课
     */
    private void initSectionView() {
        for (int i = 1; i <= maxSection; i++) {
            TextView tvSection = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.sectionHeight));
            lp.gravity = Gravity.CENTER;
            tvSection.setGravity(Gravity.CENTER);
            tvSection.setText(String.valueOf(i));
            tvSection.setLayoutParams(lp);
            sections.addView(tvSection);
        }
    }



    /**
     * 当前月份
     */
    public int getMonth() {
        int w = Calendar.getInstance().get(Calendar.MONTH) + 1;
        return w;
    }

    /**
     * 每次刷新前清除每个LinearLayout上的课程view
     */
    private void clearChildView() {
        for (int i = 0; i < mWeekViews.size(); i++) {
            if (mWeekViews.get(i) != null)
                if (mWeekViews.get(i).getChildCount() > 0)
                    mWeekViews.get(i).removeAllViews();
        }
    }


    public void initWeekPanel(LinearLayout ll, List<CourseModel> data) {

        if (ll == null || data == null || data.size() < 1)
            return;
        CourseModel firstCourse = data.get(0);
        for (int i = 0; i < data.size(); i++) {
            final CourseModel courseModel = data.get(i);
            oid = courseModel.getOid();

            if (courseModel.getCourseName().equals("null")) continue;

            if (courseModel.getSection() == 0)
                return;
            FrameLayout frameLayout = new FrameLayout(this);

            final CornerTextView tv = new CornerTextView(this,
                    ColorUtils.getCourseBgColor(courseModel.getCourseFlag()),
                    dip2px(this, 3));
            LinearLayout.LayoutParams frameLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    itemHeight);
            LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            if (i == 0) {
                frameLp.setMargins(0, (courseModel.getSection() - 1) * itemHeight, 0, 0);
            } else {
                frameLp.setMargins(0, (courseModel.getSection() - (firstCourse.getSection() + 1)) * itemHeight, 0, 0);
            }
            tv.setLayoutParams(tvLp);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(12);
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setText(courseModel.getCourseName());

            frameLayout.setLayoutParams(frameLp);
            frameLayout.addView(tv);
            frameLayout.setPadding(2, 2, 2, 2);
            ll.addView(frameLayout);
            firstCourse = courseModel;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (type == 0) {
                        LayoutInflater factor = LayoutInflater.from(RoomDetActivity.this);
                        final View view_in = factor.inflate(R.layout.layoutdialog, null);
                        final AlertDialog.Builder alerDialog = new AlertDialog.Builder(RoomDetActivity.this);
                        final TextView roomname = (TextView) view_in.findViewById(R.id.edit_roomname);
                        final EditText coursename = (EditText) view_in.findViewById(R.id.edit_coursename);
                        roomname.setText(courseModel.getClassRoom());
                        coursename.setText(courseModel.getCourseName());


                        alerDialog.setTitle("请输入修改信息").setView(view_in).setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String new_course = ((EditText) view_in.findViewById(R.id.edit_coursename)).getText().toString();
                                courseModel.setCourseName(new_course);
                                tv.setText(new_course);
                                Map<String, Object> params = new HashMap<>();
                                params.put("OID", oid);
                                params.put("newUsage", courseModel.getCourseName());
                                params.put("type", 2);
                                service = ServerFactory.createService("http://120.79.84.147:8000");
                                service.modifyCourse(params)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<ResponseClass3>() {
                                            @Override
                                            public void onCompleted() {
                                                System.out.print("完成传输");
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                                if (e instanceof HttpException) {
                                                    HttpException exception = (HttpException) e;
                                                    int code = exception.code();
                                                    if (code == 200) {
                                                        Toast.makeText(RoomDetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(RoomDetActivity.this, "找不到该使用", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                Log.e("Github-Demo", e.getMessage());

                                            }

                                            @Override
                                            public void onNext(ResponseClass3 responseClass) {

                                            }

                                        });


                                //与数据库对接
                            }
                        }).setNeutralButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("OID", oid);
                                params.put("newUsage", "null");
                                params.put("type", 1);
                                service = ServerFactory.createService("http://120.79.84.147:8000");
                                service.modifyCourse(params)
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<ResponseClass3>() {
                                            @Override
                                            public void onCompleted() {
                                                System.out.print("完成传输");
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                                if (e instanceof HttpException) {
                                                    HttpException exception = (HttpException) e;
                                                    int code = exception.code();
                                                    if (code == 200) {
                                                        Toast.makeText(RoomDetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(RoomDetActivity.this, "找不到该使用", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(RoomDetActivity.this, "不是exception", Toast.LENGTH_SHORT).show();
                                                }
                                                Log.e("Github-Demo", e.getMessage());

                                            }

                                            @Override
                                            public void onNext(ResponseClass3 responseClass) {

                                                courseModel.setCourseName("null");
                                                mFreshLayout.setLoadMore(false);
                                                clearChildView();
                                                try {
                                                    initWeekCourse();
                                                    initWeekCourseView();
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                                mFreshLayout.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mFreshLayout.finishRefreshing();
                                                    }
                                                }, 500);
                                            }

                                        });


                                try {
                                    initWeekCourse();
                                    initWeekCourseView();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

//                    final Button modifyBut = (Button) findViewById(R.id.action_modify_bar);
//                    final Button removeBut = (Button) findViewById(R.id.action_remove_bar);
//                    final Button cancelBut = (Button) findViewById(R.id.action_cancel_bar);

//                    modifyBut.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            final AlertDialog.Builder alerDialog = new AlertDialog.Builder(MainActivity.this);
//                            alerDialog.setTitle("是否确认修改").setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    courseModel.setCourseName(coursename.getText().toString());
//                                    //与数据库对接
//                                }
//                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                }
//                            }).show();
//                        }
//                    });
//
//                    removeBut.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            final AlertDialog.Builder alerDialog = new AlertDialog.Builder(MainActivity.this);
//                            alerDialog.setTitle("是否确认删除").setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    courseModel.setCourseName(coursename.getText().toString());
//                                    //与数据库对接
//                                }
//                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                }
//                            }).show();
//                        }
//                    });
//
//                    cancelBut.setOnClickListener(new View.OnClickListener() {
//                        public void onClick(View v) {
//                            //关闭弹窗
//                        }
//                    });

                        showToast(courseModel.getCourseName());
                    }
                }
            });
        }
    }

    /**
     * Toast
     */
    private void showToast(String msg) {
        if (TextUtils.isEmpty(msg))
            return;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 数字转换中文
     */
    public static String intToZH(int i) {
        String[] zh = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"", "十", "百", "千", "万", "十", "百", "千", "亿", "十"};

        String str = "";
        StringBuffer sb = new StringBuffer(String.valueOf(i));
        sb = sb.reverse();
        int r = 0;
        int l = 0;
        for (int j = 0; j < sb.length(); j++) {
            r = Integer.valueOf(sb.substring(j, j + 1));
            if (j != 0)
                l = Integer.valueOf(sb.substring(j - 1, j));
            if (j == 0) {
                if (r != 0 || sb.length() == 1)
                    str = zh[r];
                continue;
            }
            if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
                if (r != 0)
                    str = zh[r] + unit[j] + str;
                else if (l != 0)
                    str = zh[r] + str;
                continue;
            }
            if (j == 4 || j == 8) {
                str = unit[j] + str;
                if ((l != 0 && r == 0) || r != 0)
                    str = zh[r] + str;
                continue;
            }
        }
        if (str.equals("七"))
            str = "日";
        return str;
    }
    private void waiting_apply() {
        final FloatingActionButton fltbut = (FloatingActionButton) findViewById(R.id.float_but);
        if (type == 0) {
            fltbut.setVisibility(View.INVISIBLE);
        }
        fltbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(RoomDetActivity.this, AppActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("uid", uid);
                bundle.putString("class_name", className);
                intent1.putExtras(bundle);
                startActivityForResult(intent1, 1);
            }
        });
    }
}
