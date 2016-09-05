package com.yxy.zlp.dailyzh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.util.Constant;
import com.yxy.zlp.dailyzhi.R;

import java.util.ArrayList;
import java.util.List;

public class MainNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<Story> mStories;

    private SharedPreferences mSP;

    public MainNewsAdapter(Context context) {
        mContext = context;
        mSP = PreferenceManager.getDefaultSharedPreferences(mContext);
        mStories = new ArrayList<>();
    }

    private class ViewHolder {
        TextView newsTopic;
        TextView newsTitle;
        ImageView newsTitleIV;
    }


    @Override
    public int getCount() {
        return mStories.size();
    }

    @Override
    public Object getItem(int position) {
        return mStories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_news_item,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.newsTopic = (TextView) convertView.findViewById(R.id.news_topic);
            viewHolder.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
            viewHolder.newsTitleIV = (ImageView) convertView.findViewById(R.id.news_title_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String newsRead = mSP.getString("newsRead", "");
        if (newsRead.contains(mStories.get(position).getId() + "")) {
            viewHolder.newsTitle.setTextColor(mContext.getResources().getColor(R.color.read_textcolor));
        } else {
            viewHolder.newsTitle.setTextColor(Color.BLACK);
        }
        Story story = mStories.get(position);
        if (story.getType() == Constant.TOPIC) {
            ((FrameLayout) viewHolder.newsTopic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            viewHolder.newsTopic.setVisibility(View.VISIBLE);
            viewHolder.newsTopic.setText(story.getTitle());
            viewHolder.newsTitle.setVisibility(View.GONE);
            viewHolder.newsTitleIV.setVisibility(View.GONE);
        } else {
            ((FrameLayout) viewHolder.newsTopic.getParent()).setBackgroundResource(R.drawable.main_selector);
            viewHolder.newsTopic.setVisibility(View.GONE);
            viewHolder.newsTitle.setVisibility(View.VISIBLE);
            viewHolder.newsTitle.setText(story.getTitle());
            viewHolder.newsTitleIV.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(story.getImages().get(0)).into(viewHolder.newsTitleIV);
        }
        return convertView;
    }

    public void addList(List<Story> stories) {
        this.mStories.addAll(stories);
        notifyDataSetChanged();
    }
}
