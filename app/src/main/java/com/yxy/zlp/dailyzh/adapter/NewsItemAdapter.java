package com.yxy.zlp.dailyzh.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzhi.R;

import java.util.List;

public class NewsItemAdapter extends BaseAdapter {
    private List<Story> mStories;
    private Context mContext;
    private ImageLoader mImageloader;
    private DisplayImageOptions options;
    private SharedPreferences mSP;

    public NewsItemAdapter(Context context, List<Story> items) {
        mContext = context;
        mStories = items;
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.theme_news_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.titleIV = (ImageView) convertView.findViewById(R.id.titleIV);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String readSeq = mSP.getString("newsRead", "");
        if (readSeq.contains(mStories.get(position).getId() + "")) {
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.read_textcolor));
        } else {
            viewHolder.title.setTextColor(mContext.getResources().getColor(android.R.color.black));
        }

        Story story = mStories.get(position);
        viewHolder.title.setText(story.getTitle());
        if (story.getImages() != null) {
            viewHolder.titleIV.setVisibility(View.VISIBLE);
            mImageloader.displayImage(story.getImages().get(0), viewHolder.titleIV, options);
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class ViewHolder {
        TextView title;
        ImageView titleIV;
    }

}
