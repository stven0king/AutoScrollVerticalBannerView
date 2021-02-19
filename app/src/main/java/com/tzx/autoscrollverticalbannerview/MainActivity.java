package com.tzx.autoscrollverticalbannerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<DataModel> datas01 = new ArrayList<>();
        datas01.add(new DataModel("白日依山尽","--->白日依山尽"));
        datas01.add(new DataModel("黄河入海流","--->黄河入海流"));
        datas01.add(new DataModel("欲穷千里目","--->欲穷千里目"));
        datas01.add(new DataModel("更上一层楼","--->更上一层楼"));

        final SampleAdapter01 adapter01 = new SampleAdapter01(datas01);
        final AutoScrollVerticalBannerView banner01 = (AutoScrollVerticalBannerView) findViewById(R.id.banner_01);
        banner01.setAdapter(adapter01);
        banner01.setGap(2000);
        banner01.setAnimDuration(1000);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner01.start();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                banner01.stop();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DataModel> newData = new ArrayList<>();
                newData.add(new DataModel("锄禾日当午","--->锄禾日当午"));
                newData.add(new DataModel("汗滴禾下土","--->汗滴禾下土"));
                newData.add(new DataModel("谁知盘中餐","--->谁知盘中餐"));
                newData.add(new DataModel("粒粒皆辛苦","--->粒粒皆辛苦"));
                adapter01.setData(newData);
            }
        });


        //-----------------------
        List<DataModel> datas02 = new ArrayList<>();
        datas02.add(new DataModel("A Life was so beautiful","--->Life was so beautiful,"));
        datas02.add(new DataModel("B From morning to evening","--->From morning to evening"));
        datas02.add(new DataModel("C We enjoyed the road to school","--->We enjoyed the road to school,"));
        datas02.add(new DataModel("D Birds chirped and Peace lingered","--->Birds chirped and Peace lingered"));
        final SampleAdapter02 adapter02 = new SampleAdapter02(datas02);
        final AutoScrollVerticalBannerView banner02 = (AutoScrollVerticalBannerView) findViewById(R.id.banner_02);
        banner02.setAdapter(adapter02);
        banner02.start();

        //------------------------

        List<DataModel> datas03 = new ArrayList<>();
        datas03.add(new DataModel("Life is so insecure","最新"));
        datas03.add(new DataModel("From afternoon to night","最火爆"));
        datas03.add(new DataModel("We fear the road to school","hot"));
        datas03.add(new DataModel("There may be destructive bombs,Peace has demolished","new"));
        final SampleAdapter03 adapter03 = new SampleAdapter03(datas03);
        final AutoScrollVerticalBannerView banner03 = (AutoScrollVerticalBannerView) findViewById(R.id.banner_03);
        banner03.setAdapter(adapter03);
        banner03.start();

    }

}