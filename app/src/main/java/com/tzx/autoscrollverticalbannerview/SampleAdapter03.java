package com.tzx.autoscrollverticalbannerview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Description
 * Date 2021/2/19 3:08 下午
 * Created by Tanzhenxing
 */
public class SampleAdapter03 extends AutoScrollVerticalBannerView.Adapter<DataModel> {
    private List<DataModel> mDatas;

    public SampleAdapter03(List<DataModel> datas) {
        super(datas);
    }

    @Override
    public View getView(AutoScrollVerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_03,null);
    }

    @Override
    public void bindView(final View view, final DataModel data) {
        TextView tv = (TextView) view.findViewById(R.id.title);
        tv.setText(data.title);

        TextView tag = (TextView) view.findViewById(R.id.tag);
        tag.setText(data.url);

    }

    @Override
    public int getScrollBannerCount() {
        return 2;
    }

    @Override
    public int getShowBannerCount() {
        return 2;
    }
}
