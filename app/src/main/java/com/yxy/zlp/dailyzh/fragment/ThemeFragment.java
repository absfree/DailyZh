package com.yxy.zlp.dailyzh.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yxy.zlp.dailyzh.activity.MainActivity;
import com.yxy.zlp.dailyzh.activity.NewsContentActivity;
import com.yxy.zlp.dailyzh.adapter.NewsItemAdapter;
import com.yxy.zlp.dailyzh.model.Story;
import com.yxy.zlp.dailyzh.model.ThemeNews;
import com.yxy.zlp.dailyzh.util.httpUtil.AsyncHttpUtils;
import com.yxy.zlp.dailyzh.util.Constants;
import com.yxy.zlp.dailyzh.util.httpUtil.HttpUtils;
import com.yxy.zlp.dailyzh.util.httpUtil.ResponseHandler;
import com.yxy.zlp.dailyzh.util.imageLoader.FreeImageLoader;
import com.yxy.zlp.dailyzhi.R;

@SuppressLint("ValidFragment")
public class ThemeFragment extends BaseFragment {
    private ListView mNewsList;
    private ImageView titleIV;
    private TextView titleTV;
    private String themeId;
    private String themeTitle;
    private ThemeNews news;
    private NewsItemAdapter mAdapter;
    private FreeImageLoader mFreeImageLoader;
    private SharedPreferences mSP;

    public ThemeFragment(String id, String title) {
        themeId = id;
        themeTitle = title;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSP = PreferenceManager.getDefaultSharedPreferences(mActivity);
        ((MainActivity) mActivity).setToolbarTitle(themeTitle);
        View view = inflater.inflate(R.layout.theme_content, container, false);
        mFreeImageLoader = FreeImageLoader.getInstance(mActivity);
        mNewsList = (ListView) view.findViewById(R.id.news_list);
        View listHeader = LayoutInflater.from(mActivity).inflate(
                R.layout.theme_content_header, mNewsList, false);
        titleIV = (ImageView) listHeader.findViewById(R.id.title_img);
        titleTV = (TextView) listHeader.findViewById(R.id.title);
        mNewsList.addHeaderView(listHeader);
        mNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Story currentStory = (Story) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, NewsContentActivity.class);
                intent.putExtra(MainFragment.CURRENT_STORY, currentStory);

                String newsRead = mSP.getString("newsRead", "");
                if (!newsRead.contains(currentStory.getId() + "")) {
                    newsRead += currentStory.getId();
                }
                mSP.edit().putString("newsRead", newsRead);
                TextView newsTitle = (TextView) view.findViewById(R.id.title);
                newsTitle.setTextColor(getResources().getColor(R.color.read_textcolor));
                startActivity(intent);
            }
        });
        mNewsList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (mNewsList != null && mNewsList.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((MainActivity) mActivity).setRefreshEnable(enable);
                }
            }
        });
        return view;
    }

    @Override
    protected void initContent() {
        super.initContent();
        if (HttpUtils.isOnline(mActivity)) {
            AsyncHttpUtils.get(Constants.THEME + themeId, new ResponseHandler() {
                @Override
                public void onSuccess(byte[] result) {
                    String jsonString = new String(result);
                    parseThemeJson(jsonString);
                }

                @Override
                public void onFailure() {

                }
            });
        } else {
            Toast.makeText(mActivity, R.string.offline, Toast.LENGTH_SHORT);
        }
    }

    private void parseThemeJson(String jsonString) {
        Gson gson = new Gson();
        news = gson.fromJson(jsonString, ThemeNews.class);
        titleTV.setText(news.getDescription());
        mFreeImageLoader.displayImage(news.getImage(), titleIV);
        mAdapter = new NewsItemAdapter(mActivity, news.getStories());
        mNewsList.setAdapter(mAdapter);
    }

}
