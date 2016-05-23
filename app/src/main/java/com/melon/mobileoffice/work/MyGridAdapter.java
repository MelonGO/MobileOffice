package com.melon.mobileoffice.work;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melon.mobileoffice.R;

public class MyGridAdapter extends BaseAdapter {
    private Context mContext;

    public String[] img_text = {"签到", "公告", "客户", "员工", "项目", "会议",};
    public int[] imgs = {R.drawable.app_assign, R.drawable.app_financial,
            R.drawable.app_transfer, R.drawable.app_donate,
            R.drawable.app_exchange, R.drawable.app_appcenter};

    public MyGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return img_text.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
        iv.setBackgroundResource(imgs[position]);

        tv.setText(img_text[position]);
        return convertView;
    }

}
