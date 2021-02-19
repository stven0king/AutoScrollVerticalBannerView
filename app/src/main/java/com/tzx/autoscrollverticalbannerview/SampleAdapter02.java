package com.tzx.autoscrollverticalbannerview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Description
 * Date 2021/2/19 3:08 下午
 * Created by Tanzhenxing
 */
public class SampleAdapter02 extends AutoScrollVerticalBannerView.Adapter<DataModel> {
    private List<DataModel> mDatas;

    public SampleAdapter02(List<DataModel> datas) {
        super(datas);
    }

    @Override
    public View getView(AutoScrollVerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_02,null);
    }

    @Override
    public void bindView(final View view, final DataModel data) {
        TextView tv = (TextView) view.findViewById(R.id.tv_02);
        tv.setText(data.title);
        //你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //比如打开url
                Toast.makeText(view.getContext(),data.url,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getShowBannerCount() {
        return 3;
    }

    @Override
    public int getScrollBannerCount() {
        return 2;
    }
}
