package com.huabing.githubanalyse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huabing.githubanalyse.Bean.PopSelect;
import com.huabing.githubanalyse.R;

import java.util.List;

/**
 * Created by 30781 on 2017/7/2.
 */

public class PopSelectAdapter extends ArrayAdapter<PopSelect>{
    private int resourceId;
    public PopSelectAdapter(Context context, int textViewResourceId, List<PopSelect> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        PopSelect popSelect=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView ivImage=(ImageView)view.findViewById(R.id.item_iv_image);
        TextView tvName=(TextView)view.findViewById(R.id.item_tv_name);
        ivImage.setImageResource(popSelect.getImageId());
        tvName.setText(popSelect.getName());
        return view;
    }
}
