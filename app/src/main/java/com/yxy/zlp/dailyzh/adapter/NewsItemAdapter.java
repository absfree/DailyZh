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

import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.util.imageLoader.FreeImageLoader;
import com.yxy.zlp.dailyzhi.R;

import java.util.List;

public class NewsItemAdapter extends BaseAdapter {
    private List<Story> mStories;
    private Context mContext;
    private SharedPreferences mSP;

    private FreeImageLoader mFreeImageLoader;

    public NewsItemAdapter(Context context, List<Story> items) {
        mContext = context;
        mStories = items;
        mFreeImageLoader = FreeImageLoader.getInstance(mContext);
        mSP = PreferenceManager.getDefaultSharedPreferences(mContext);
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
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.theme_news_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.titleIV = (ImageView) convertView.findViewById(R.id.titleIV);
            viewHolder.themeTopic = (TextView) convertView.findViewById(R.id.theme_topic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String newsRead = mSP.getString("newsRead", "");
        if (newsRead.contains(mStories.get(position).getId() + "")) {
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.read_textcolor));
        } else {
            viewHolder.title.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }
        Story story = mStories.get(position);
        if (position == 0) {
            ((FrameLayout) viewHolder.themeTopic.getParent()).setBackgroundColor(Color.TRANSPARENT);
            viewHolder.themeTopic.setVisibility(View.VISIBLE);
            viewHolder.themeTopic.setText(story.getTitle());
            viewHolder.title.setVisibility(View.GONE);
            viewHolder.titleIV.setVisibility(View.GONE);
        } else {
            viewHolder.themeTopic.setVisibility(View.GONE);
            viewHolder.title.setVisibility(View.VISIBLE);
            viewHolder.title.setText(story.getTitle());
            if (story.getImages() != null) {
                viewHolder.titleIV.setVisibility(View.VISIBLE);
                mFreeImageLoader.displayImage(story.getImages().get(0), viewHolder.titleIV);
            }
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView themeTopic;
        TextView title;
        ImageView titleIV;
    }

}
