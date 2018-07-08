package com.example.asus.weclass;

/**
 * Created by ASUS on 2018/6/13.
 */


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



        import rx.Subscriber;
        import rx.schedulers.Schedulers;
        import rx.android.schedulers.AndroidSchedulers;
/**
 * Created by weihuajian on 16/6/12.
 */
public class CourseDao {

    private static List<CourseModel> courseModels[] = new ArrayList[7];
    private List<Map<String, Object>> list = new ArrayList<>();
    //private CardAdapter<Map<String, Object>> cardAdapter;
    private ServerService service;

    public static void setCourseModels(List<jsonModel> jsonModels, String roomname) throws ParseException {
        SimpleDateFormat simpleformat=new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar_now = Calendar.getInstance();
        Calendar calendar_temp = calendar_now;
        String date_now = simpleformat.format(calendar_now.getTime());
        String date_temp = date_now;
        for (int i = 0; i < courseModels.length; i++) {
            courseModels[i] = new ArrayList<>();
        }
        List<CourseModel> models_1 = new ArrayList<>();
        //jsonModels.clear();
        int j = 0;
        int count = 0;
        for (int i = 0; i < jsonModels.size(); i++) {
            String date = jsonModels.get(i).getDate();
            while (!date.equals(date_temp)) {
                j++;
                Date date1 = simpleformat.parse(date_temp);
                calendar_temp.setTime(date1);
                calendar_temp.add(Calendar.DATE, 1);
                date_temp = simpleformat.format(calendar_temp.getTime());
                if (!models_1.isEmpty()) {
                    courseModels[j-1].addAll(models_1);
                    count++;
                }
                models_1.clear();
                if (j > 6) break;
            }

            String time = jsonModels.get(i).getTime();
            int int_time = Integer.parseInt(time);
            String usage = jsonModels.get(i).getUsage();
            int id = i;
            int oid = Integer.parseInt(jsonModels.get(i).getOID());// jsonModels.get(i).getOID();
            int week = j;
            int courseFlag = (int) (Math.random() * 10);
            models_1.add(new CourseModel(id, usage, int_time, oid, week, roomname, courseFlag));
        }
        if (!models_1.isEmpty()) {
            courseModels[j].addAll(models_1);
            count++;
        }
        Log.e("count", count+"");
        List<CourseModel> models_2 = new ArrayList<>();
//        models_2.add(new CourseModel(0, "C语言", 1, 2, 1, "A401", (int) (Math.random() * 10)));
//        models_2.add(new CourseModel(1, "Ruby", 3, 3, 1, "A453", (int) (Math.random() * 10)));
//        models_2.add(new CourseModel(1, "PHP", 6, 3, 1, "A483", (int) (Math.random() * 10)));
//        courseModels[0].addAll(models_2);




//            private int id; //课程ID
//            private String courseName;
//            private int section; //从第几节课开始
//            private int oid; //跨几节课
//            private int week; //周几
//            private String classRoom; //教室
//            private int courseFlag; //课程背景颜色


    }
    //List<jsonModel>
    public static List<CourseModel>[] getCourseData() {



//
//        courseModels[1].addAll(models_1);
        return courseModels;

    }
}
