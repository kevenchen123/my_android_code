package com.keven.camera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import com.keven.R;
import com.keven.utils.DisplayUtils;
import com.keven.utils.LoadLocalImageUtil;


public class MyGridViewAdpter extends BaseAdapter {
    public static final int ITEM_WIDTH = DisplayUtils.dpToPx(90), ITEM_HEIGHT = DisplayUtils.dpToPx(120);
    private Context context;
    private List<Image> list;

    public MyGridViewAdpter(Context context, List<Image> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (null == list) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            viewHolder.parent = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.grid_item,viewGroup,false);
            viewHolder.imageView = viewHolder.parent.findViewById(R.id.icon);
            viewHolder.parent.setTag(viewHolder);
            view = viewHolder.parent;
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        setCommonItem(viewHolder, i);
        return view;
    }

    private void setCommonItem(ViewHolder viewHolder, int pos) {
        final Image image = list.get(pos);
        viewHolder.parent.setOnClickListener(null);
        LoadLocalImageUtil.getInstance(context).displayFromSDCard(image.getUrl(), viewHolder.imageView);
    }

    public void notifyDataSetChanged(Image image) {
        list.add(image);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        RelativeLayout parent;
        ImageView imageView;
    }
}