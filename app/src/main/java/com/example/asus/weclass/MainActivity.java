package com.example.asus.weclass;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Bundle bundle1 = this.getIntent().getExtras();
        if (bundle1 != null) {
            final int type = bundle1.getInt("type");
            final int uid = bundle1.getInt("uid");
            final Button stu_view_class = (Button) findViewById(R.id.stu_view_class);
            final Button stu_view_appoint = (Button) findViewById(R.id.stu_view_appoint);
            final Button stu_passchange = (Button) findViewById(R.id.stu_passchange);
            final Button man_view_class = (Button) findViewById(R.id.man_view_class);
            final Button man_view_appoint = (Button) findViewById(R.id.man_view_appoint);
            final Button man_passchange = (Button) findViewById(R.id.man_passchange);


            if (type == 0) {
                stu_view_class.setVisibility(View.GONE);
                stu_view_appoint.setVisibility(View.GONE);
                stu_passchange.setVisibility(View.GONE);
                man_view_class.setVisibility(View.VISIBLE);
                man_view_appoint.setVisibility(View.VISIBLE);
                man_passchange.setVisibility(View.VISIBLE);
            } else {
                stu_view_class.setVisibility(View.VISIBLE);
                stu_view_appoint.setVisibility(View.VISIBLE);
                stu_passchange.setVisibility(View.VISIBLE);
                man_view_class.setVisibility(View.GONE);
                man_view_appoint.setVisibility(View.GONE);
                man_passchange.setVisibility(View.GONE);
            }


            final Bundle bundle2 = new Bundle();
            bundle2.putInt("type", type);
            bundle2.putInt("uid", uid);

            stu_view_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, ClassListActivity.class);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });

            stu_view_appoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AppListActivity.class);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });

            stu_passchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PassActivity.class);
                    intent.putExtras(bundle2);
                    startActivityForResult(intent, 1);
                }
            });

            man_view_class.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ClassListActivity.class);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });

            man_view_appoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ReLiActivity.class);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });

            man_passchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, PassActivity.class);
                    intent.putExtras(bundle2);
                    startActivityForResult(intent, 1);
                }
            });
        }

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.main_layout);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof Button) {
                view.getBackground().setAlpha(35);
            }
        }
    }
}
